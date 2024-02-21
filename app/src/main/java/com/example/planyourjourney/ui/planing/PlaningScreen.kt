package com.example.planyourjourney.ui.planing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.planyourjourney.services.DecimalFormatter
import com.example.planyourjourney.services.dataClasses.SearchInputType
import com.example.planyourjourney.services.dataClasses.WeatherJson
import com.example.planyourjourney.ui.components.rememberMarker
import com.example.planyourjourney.ui.theme.PlanYourJourneyTheme
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer

@Composable
fun PlaningScreen(
    viewModel: PlaningViewModel,
    decimalFormatter: DecimalFormatter
) {
    val weather by viewModel.weather.observeAsState(WeatherJson())

    PlanYourJourneyTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                when (viewModel.settings.searchInputType) {
                    SearchInputType.LatitudeAndLongitude -> LatAndLongTextField(
                        decimalFormatter,
                        viewModel
                    )

                    SearchInputType.LocationName -> LocationNameTextField(viewModel)
                    //SearchInputType.Map -> Map(viewModel, Modifier.fillMaxSize())
                }

                Button(onClick = { viewModel.getWeather(viewModel.coordinates) }) {
                    Text(text = "Check weather at location")
                }
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth()
                        .size(30.dp))
                if (weather.latitude == null) {
                    Text(text = "Weather status")
                } else {
                    WeatherChart(viewModel)
//                    Row {
//                        LazyColumn() {
//                            items(1) {
//                                Row {
//                                    Text(text = "Time")
//                                    Text(text = "Temperature")
//                                }
//                            }
//                            items(weather.hourly!!.time.size) { time ->
//                                Row {
//                                    Text(text = weather.hourly!!.time[time])
//                                    Spacer(modifier = Modifier.size(5.dp))
//                                    Text(text = "${weather.hourly!!.temperature2m[time]}°C")
//                                }
//                            }
//                        }
//                    }
                }
            }
        }
    }
}

@Composable
fun LatAndLongTextField(decimalFormatter: DecimalFormatter, viewModel: PlaningViewModel) {
    LatitudeTextField(decimalFormatter, viewModel)
    LongitudeTextField(decimalFormatter, viewModel)
}

@Composable
fun LatitudeTextField(
    decimalFormatter: DecimalFormatter,
    viewModel: PlaningViewModel,
    modifier: Modifier = Modifier
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
    decimalFormatter: DecimalFormatter,
    viewModel: PlaningViewModel,
    modifier: Modifier = Modifier
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
    viewModel: PlaningViewModel,
    modifier: Modifier = Modifier
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
fun WeatherChart(
    viewModel: PlaningViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberChartScrollState()
    val marker = rememberMarker()
    Chart(
        modifier = modifier.wrapContentHeight(),
        chart = lineChart(spacing = 56.dp),
        chartModelProducer = viewModel.chartEntryModelProducer,
        chartScrollState = scrollState,
        startAxis = rememberStartAxis(
            itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = 6)
        ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = viewModel.bottomAxisValueFormatter,

            itemPlacer = AxisItemPlacer.Horizontal.default(
                addExtremeLabelPadding = true
                ),
            //labelRotationDegrees = -60f,
            label = textComponent(
                lineCount = 2
            )
        ),
        marker = marker,
        isZoomEnabled = true,
    )
}