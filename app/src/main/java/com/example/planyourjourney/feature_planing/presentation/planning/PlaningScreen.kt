package com.example.planyourjourney.feature_planing.presentation.planning

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.planyourjourney.R
import com.example.planyourjourney.feature_planing.presentation.util.DecimalFormatter
import com.example.planyourjourney.feature_planing.presentation.util.WeatherUnits
import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.util.SearchInputType
import com.example.planyourjourney.core.presentation.AppToolbar
import com.example.planyourjourney.core.presentation.rememberMarker
import com.example.planyourjourney.feature_planing.domain.util.OutputType
import com.example.planyourjourney.feature_planing.presentation.planning.components.ChartSection
import com.example.planyourjourney.feature_planing.presentation.planning.components.CoordinatesInputSection
import com.example.planyourjourney.feature_planing.presentation.planning.components.LocationNameInputSection
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.chart.layout.fullWidth
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.chart.zoom.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.component.shape.shader.color
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed

@Composable
fun PlaningScreen(
    navController: NavController,
    viewModel: PlaningViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val weatherCoordinates = viewModel.weatherCoordinates.value
    val weatherLocationName = viewModel.weatherLocationName.value
    val scope = rememberCoroutineScope()
    val decimalFormatter = DecimalFormatter()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppToolbar(
                modifier = Modifier.wrapContentHeight(),
                title = stringResource(R.string.app_name)
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.clickable {

                    }
                )
            }
        }
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//
//                },
//                containerColor = Color.White
//            ) {
//                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
//            }
//        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
//            LaunchedEffect(key1 = context){
//                viewModel.uiEvents.collect{event ->
//
//                }
//            }
            Column(
                Modifier.padding(10.dp)
            ) {
                when (viewModel.state.value.settings.searchInputType) {
                    SearchInputType.LatitudeAndLongitude -> CoordinatesInputSection(
                        coordinates = weatherCoordinates,
                        onValueChange = {
                            viewModel.onEvent(PlaningEvent.CoordinatesChanged(it))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        decimalFormatter = decimalFormatter
                    )

                    SearchInputType.LocationName -> LocationNameInputSection(
                        locationName = weatherLocationName,
                        onValueChange = {
                            viewModel.onEvent(PlaningEvent.LocationNameChanged(it))
                        }
                    )
                    //SearchInputType.Map -> Map(viewModel, Modifier.fillMaxSize())
                }

                Button(onClick = {
                    viewModel.onEvent(PlaningEvent.GetWeather)
                }) {
                    Text(text = "Check weather at location")
                }
            }
            HorizontalDivider(
                Modifier
                    .fillMaxWidth()
                    .size(20.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.wrapContentSize().size(56.dp),
                        strokeWidth = 6.dp,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                } else {
                    if (!state.isWeatherLoaded || state.hourlyWeatherList.isEmpty()) {
                        Text(
                            text = "Weather not loaded"
                        )
                    } else {
                        when (state.settings.outputType) {
                            OutputType.Chart -> ChartSection(
                                chartStateList = state.chartStateList
                            )

                            OutputType.Card -> TODO()
                        }
                    }
                }
            }
        }
    }
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

//@Composable
//fun TemperatureChart(
//    modifier: Modifier = Modifier,
//    viewModel: PlaningViewModel
//) {
//    val scrollState = rememberVicoScrollState()
//    val zoomState = rememberVicoZoomState()
//    val marker = rememberMarker(
//        markerLabelFormatter = viewModel.getMarkerLabelFormatter(WeatherUnits.DEGREES_CELSIUS.units)
//    )
//    CartesianChartHost(
//        scrollState = scrollState,
//        zoomState = zoomState,
//        modifier = modifier.wrapContentHeight(),
//        chart = rememberCartesianChart(
//            rememberLineCartesianLayer(
//                spacing = 8.dp,
//                lines = listOf(rememberLineSpec(DynamicShaders.color(MaterialTheme.colorScheme.primary)))
//            ),
//            startAxis = rememberStartAxis(
//                itemPlacer = remember { AxisItemPlacer.Vertical.count(count = { 6 }) },
//                valueFormatter = viewModel.getStartAxisFormatter(WeatherUnits.DEGREES_CELSIUS.units)
//            ),
//            bottomAxis = rememberBottomAxis(
//                valueFormatter = viewModel.getBottomAxisFormatter(),
//
//                itemPlacer = AxisItemPlacer.Horizontal.default(
//                    spacing = 6,
//                    addExtremeLabelPadding = true
//                ),
//                label = rememberAxisLabelComponent(
//                    lineCount = 2
//                ),
//
//                ),
//            fadingEdges = rememberFadingEdges()
//        ),
//        modelProducer = viewModel.getChartEntryModelProducer(),
//        horizontalLayout = HorizontalLayout.fullWidth(),
//        marker = marker
//    )
//}

@Composable
fun WeatherCard(
    modifier: Modifier,
    viewModel: PlaningViewModel
) {
//    val indexes = viewModel.weather.value!!.withIndex().filter { hourlyWeather ->
//        hourlyWeather.value.time.hour == 0 ||
//                hourlyWeather.value.time.hour == 8 ||
//                hourlyWeather.value.time.hour == 12 ||
//                hourlyWeather.value.time.hour == 18
//    }.map { it.index }

    //val temperature
    Column(
        modifier = modifier
    ) {
        //Icon() TODO: Icon or image for different weather - rainy, sunny, cloudy
        // TODO: Fields for Date and Time, temperature, precipitation_probability, rain, snowfall, cloud %, wind_speed_10m
        Text(
            text = "text"
        )
    }
}