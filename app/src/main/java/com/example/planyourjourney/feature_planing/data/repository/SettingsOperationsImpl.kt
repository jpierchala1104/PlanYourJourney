package com.example.planyourjourney.feature_planing.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.planyourjourney.feature_planing.data.serializer.SettingsSerializer
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.repository.SettingsOperations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.settingsDataStore by dataStore("settings.json", SettingsSerializer)
val Context.settingsPreferencesDataStore by preferencesDataStore(name = "settings")
class SettingsOperationsImpl(context: Context) : SettingsOperations {

    private val dataStore = context.settingsDataStore
    private val preferencesDataStore = context.settingsPreferencesDataStore
    override suspend fun saveSettings(settings: Settings) {
        dataStore.updateData {
            it.copy(
                language = settings.language,
                weatherUnits = settings.weatherUnits,
                weatherVariables = settings.weatherVariables,
                widgetLocation = settings.widgetLocation
            )
        }
        preferencesDataStore.edit { prefs ->
            prefs[stringPreferencesKey("settings")] = Json.encodeToString(settings)
        }
    }
    override fun readSettingsState(): Flow<Settings> {
        return dataStore.data
    }

    override suspend fun readSettingsInWidget(): Settings {
        val prefs = preferencesDataStore.data.first()
        val json = prefs[stringPreferencesKey("settings")]
        try {
            if (json != null)
            {
                return Json.decodeFromString(json)
            }
        } catch (ex: Exception){
            Log.e("SettingsOperations", "Error in Decoding from Json - $ex")
        }
        return Settings()
    }
}