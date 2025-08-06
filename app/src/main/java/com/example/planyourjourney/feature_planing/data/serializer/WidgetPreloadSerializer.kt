package com.example.planyourjourney.feature_planing.data.serializer

import androidx.datastore.core.Serializer
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object WidgetPreloadSerializer : Serializer<LocationWeather> {
    override val defaultValue: LocationWeather
        get() = LocationWeather(
            location = Location(),
            hourlyWeatherList = emptyList()
        )

    override suspend fun readFrom(input: InputStream): LocationWeather {
        return try {
            Json.decodeFromString(
                deserializer = LocationWeather.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: LocationWeather, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = LocationWeather.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }

}