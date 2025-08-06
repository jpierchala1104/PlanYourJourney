package com.example.planyourjourney.feature_planing.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.repository.WidgetPreloadOperations
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.widgetPreloadPreferencesDataStore by preferencesDataStore(name = "widget_preload_data")
class WidgetPreloadOperationsImpl(context: Context) : WidgetPreloadOperations {

    private val preferencesDataStore = context.widgetPreloadPreferencesDataStore
    override suspend fun saveData(locationWeather: LocationWeather) {
        preferencesDataStore.edit { prefs ->
            prefs[stringPreferencesKey("preloaded_data")] = Json.encodeToString(locationWeather)
        }
    }

    override suspend fun readData(): LocationWeather {
        val prefs = preferencesDataStore.data.first()
        val json = prefs[stringPreferencesKey("preloaded_data")]
        try {
            if (json != null) {
                return Json.decodeFromString(json)
            }
        } catch (ex: Exception){
            Log.e("WidgetPreloadOperations", "Error in Decoding from Json - $ex")
        }
        return LocationWeather(location = Location(), hourlyWeatherList = emptyList())
    }
}