package com.example.planyourjourney.ui.planing

import android.app.Application
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.planyourjourney.services.ChartService
import com.example.planyourjourney.services.dataClasses.Coordinates
import com.example.planyourjourney.services.dataClasses.WeatherJson
import com.example.planyourjourney.services.WeatherRepository
import com.example.planyourjourney.services.dataClasses.Language
import com.example.planyourjourney.services.dataClasses.SearchInputType
import com.example.planyourjourney.services.dataClasses.Settings
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import kotlinx.coroutines.launch

class PlaningViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application
    private val repository = WeatherRepository()

    val settings = Settings(
        // language = Language. -> English and Polski (for polish)
        language = Language.English,
        // searchInputType = SearchInputType. -> LocationName, LatitudeAndLongitude and GoogleMaps
        searchInputType = SearchInputType.LocationName
    )

    private val _weather = MutableLiveData<WeatherJson>()
    val weather: LiveData<WeatherJson> = _weather

    val coordinates = Coordinates(0.0, 0.0)
    private val geocoder = Geocoder(context)
    private val geocodeListener = GeocodeListener { coords ->
        coordinates.latitude = coords.first().latitude
        coordinates.longitude = coords.first().longitude
    }

    // TODO: change to event and state

    private val chartService = ChartService()

    fun getWeather(location: Coordinates) {
        viewModelScope.launch {
            try {
                val weatherAtLocation = repository.getWeather(location)
                _weather.value = weatherAtLocation
            } catch (e: Exception) {
                Log.e("Repo", e.toString())
            }
        }
    }

    fun getChartEntryModelProducer(
        chartDoubleValues: ArrayList<Double> = arrayListOf(),
        chartIntValues: ArrayList<Int> = arrayListOf()
    ): CartesianChartModelProducer {
        return chartService.getCartesianChartModelProducer(
            chartSize = _weather.value!!.hourly!!.time.size,
            chartDoubleValues = chartDoubleValues,
            chartIntValues = chartIntValues)
    }

    fun getBottomAxisFormatter(): AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
        return chartService.getBottomAxisFormatter(_weather.value!!.hourly!!.time)
    }

    fun getStartAxisFormatter(unit: String): AxisValueFormatter<AxisPosition.Vertical.Start>{
        return chartService.getStartAxisFormatter(unit)
    }

    fun getMarkerLabelFormatter(unit: String = ""): MarkerLabelFormatter{
        return chartService.getMarkerLabelFormatter(_weather.value!!.hourly!!.time, unit)
    }

    fun getCoordinatesFromLocationName(locationName: String) {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            geocoder.getFromLocationName(locationName, 1, geocodeListener)
        } else {
            val coords = geocoder.getFromLocationName(locationName, 1)
            if (!coords.isNullOrEmpty()) {
                coordinates.latitude = coords.first().latitude
                coordinates.longitude = coords.first().longitude
            } else {
                Log.e("Geocoder", "Geocoder didn't find location")
            }
        }
    }
}