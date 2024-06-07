package com.example.planyourjourney.feature_planing.data.repository

import android.content.Context
import androidx.datastore.dataStore
import com.example.planyourjourney.feature_planing.data.serializer.SettingsSerializer
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.repository.SettingsOperations
import kotlinx.coroutines.flow.Flow

val Context.dataStore by dataStore("settings.json", SettingsSerializer)
class SettingsOperationsImpl(context: Context) : SettingsOperations {

    private val dataStore = context.dataStore
    override suspend fun saveSettings(settings: Settings) {
        dataStore.updateData {
            it.copy(
                language = settings.language,
                weatherUnits = settings.weatherUnits,
                weatherVariables = settings.weatherVariables
            )
        }
    }
    override fun readSettingsState(): Flow<Settings> {
        return dataStore.data
    }
}