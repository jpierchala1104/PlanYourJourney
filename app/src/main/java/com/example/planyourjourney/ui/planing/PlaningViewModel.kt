package com.example.planyourjourney.ui.planing

import android.app.Application
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.planyourjourney.services.dataClasses.Coordinates
import com.example.planyourjourney.services.dataClasses.WeatherJson
import com.example.planyourjourney.services.WeatherRepository
import com.example.planyourjourney.services.dataClasses.Language
import com.example.planyourjourney.services.dataClasses.SearchInputType
import com.example.planyourjourney.services.dataClasses.Settings
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.diff.ExtraStore
import com.patrykandpatrick.vico.core.entry.diff.MutableExtraStore
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

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

    val chartEntryModelProducer = ChartEntryModelProducer()
    lateinit var bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>

    fun getWeather(location: Coordinates) {
        viewModelScope.launch {
            try {
                val weatherAtLocation = repository.getWeather(location)
                _weather.value = weatherAtLocation
                prepareChart()
                prepareBottomAxisFormatter()
            } catch (e: Exception) {
                Log.e("Repo", e.toString())
            }
        }
    }

    private fun prepareChart() {
        val dataPoints = arrayListOf<FloatEntry>()
        for (i in 0 until _weather.value!!.hourly!!.time.size) {
            dataPoints
                .add(
                    FloatEntry(
                        x = i.toFloat(),
                        y = _weather.value!!.hourly!!.temperature2m[i].toFloat()
                    )
                )
        }
        chartEntryModelProducer.setEntries(listOf(dataPoints))
    }

    private fun prepareBottomAxisFormatter() {
        bottomAxisValueFormatter = AxisValueFormatter { x, _ ->
            val beforeT = _weather.value!!.hourly!!.time[x.toInt()].substringBefore("T")
            val date = LocalDate.parse(beforeT)
            val afterT = _weather.value!!.hourly!!.time[x.toInt()].substringAfter("T")
            if(afterT == "00:00")"${date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)}\n$afterT"
            else "\n$afterT"
        }
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