package com.example.planyourjourney.ui.planing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.planyourjourney.R
import com.example.planyourjourney.services.DecimalFormatter
import com.example.planyourjourney.services.dataClasses.SearchInputType
import com.example.planyourjourney.services.dataClasses.WeatherJson
import com.example.planyourjourney.ui.components.AppToolbar
import com.example.planyourjourney.ui.components.rememberMarker
import com.example.planyourjourney.ui.theme.PlanYourJourneyTheme
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.layout.fullWidth
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout

@Composable
fun PlaningScreen(
    viewModel: PlaningViewModel,
    decimalFormatter: DecimalFormatter
) {
    val weather by viewModel.weather.observeAsState(WeatherJson())

    PlanYourJourneyTheme {
        // A surface container using the 'background' color from the theme
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AppToolbar(
                modifier = Modifier.wrapContentHeight(),
                title = stringResource(R.string.app_name)
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Column(
                Modifier.padding(10.dp)
            ) {
                when (viewModel.settings.searchInputType) {
                    SearchInputType.LatitudeAndLongitude -> LatAndLongTextField(
                        Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp),
                        viewModel,
                        decimalFormatter
                    )

                    SearchInputType.LocationName -> LocationNameTextField(
                        Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp),
                        viewModel
                    )
                    //SearchInputType.Map -> Map(viewModel, Modifier.fillMaxSize())
                }

                Button(onClick = { viewModel.getWeather(viewModel.coordinates) }) {
                    Text(text = "Check weather at location")
                }
            }
            HorizontalDivider(
                Modifier
                    .fillMaxWidth()
                    .size(30.dp)
            )
            Column(
                Modifier.padding(5.dp)
            ) {
                if (weather.latitude == null && weather.hourly?.time.isNullOrEmpty()) {
                    Text(text = "Weather status")
                } else {
                    TemperatureChart(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun LatAndLongTextField(
    modifier: Modifier = Modifier,
    viewModel: PlaningViewModel,
    decimalFormatter: DecimalFormatter
) {
    LatitudeTextField(modifier, viewModel, decimalFormatter)
    LongitudeTextField(modifier, viewModel, decimalFormatter)
}

@Composable
fun LatitudeTextField(
    modifier: Modifier = Modifier,
    viewModel: PlaningViewModel,
    decimalFormatter: DecimalFormatter
) {
    var latitude by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = modifier,
        value = latitude,
        onValueChange = {
            latitude = decimalFormatter.checkLatitudeRange(decimalFormatter.cleanup(it))
            if (latitude != "" && latitude != ".")
                viewModel.coordinates.latitude = latitude.toDouble()
            else
                viewModel.coordinates.latitude = 0.0
        },
        label = { Text(text = "Latitude") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
fun LongitudeTextField(
    modifier: Modifier = Modifier,
    viewModel: PlaningViewModel,
    decimalFormatter: DecimalFormatter
) {
    var longitude by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = modifier,
        value = longitude,
        onValueChange = {
            longitude = decimalFormatter.checkLongitudeRange(decimalFormatter.cleanup(it))
            if (longitude != "" && longitude != ".")
                viewModel.coordinates.longitude = longitude.toDouble()
            else
                viewModel.coordinates.longitude = 0.0
        },
        label = { Text(text = "Longitude") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
fun LocationNameTextField(
    modifier: Modifier = Modifier,
    viewModel: PlaningViewModel
) {
    var locationName by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = modifier,
        value = locationName,
        onValueChange = {
            locationName = it
            viewModel.getCoordinatesFromLocationName(locationName)
        },
        label = { Text(text = "Location Name") }
    )
}

//@Composable
//fun Map(
//    viewModel: PlaningViewModel,
//    modifier: Modifier = Modifier
//) {
//    val cameraPositionState = rememberCameraPositionState{
//        position = CameraPosition.fromLatLngZoom(viewModel.coordinates.toLatLng(), 10f)
//    }
//    GoogleMap(
//        modifier = modifier,
//        cameraPositionState = cameraPositionState
//    ){
//        Marker(
//            state = MarkerState(position = viewModel.coordinates.toLatLng()),
//            title = "Marker on Location",
//            snippet = "Marker on Location"
//        )
//    }
//}

@Composable
fun TemperatureChart(
    modifier: Modifier = Modifier,
    viewModel: PlaningViewModel
) {
    val temperatureValues = viewModel.weather.value!!.hourly!!.temperature2m
    val scrollState = rememberChartScrollState()
    val marker = rememberMarker()
    Chart(
        modifier = modifier.wrapContentHeight(),
        chart = lineChart(spacing = 56.dp),
        chartModelProducer = viewModel.getChartEntryModelProducer(temperatureValues),
        chartScrollState = scrollState,
        startAxis = rememberStartAxis(
            itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = 6)
        ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = viewModel.getBottomAxisFormatter(),

            itemPlacer = AxisItemPlacer.Horizontal.default(
                addExtremeLabelPadding = true
            ),
            label = textComponent(
                lineCount = 2
            )
        ),
        horizontalLayout = HorizontalLayout.fullWidth(),
        marker = marker,
        isZoomEnabled = true
    )
}

@Composable
fun WeatherCard(
    modifier: Modifier,
    viewModel: PlaningViewModel
) {
    viewModel.weather.value!!.hourly!!.time
    Column(
        modifier = modifier
    ){
        //Icon() TODO: Icon or image for different weather - rainy, sunny, cloudy
        // TODO: Fields for Date and Time, temperature, precipitation_probability, rain, snowfall, cloud %, wind_speed_10m
        Text(
            text = "text"
        )
    }
}