package com.example.planyourjourney.feature_planing.data.mapper

import com.example.planyourjourney.feature_planing.data.local.HourlyWeatherEntity
import com.example.planyourjourney.feature_planing.data.local.LocationEntity
import com.example.planyourjourney.feature_planing.data.local.LocationWithHourlyWeather
import com.example.planyourjourney.feature_planing.data.remote.dto.OpenMeteoAPIEntity
import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import java.time.LocalDateTime

fun OpenMeteoAPIEntity.toHourlyWeatherList(): List<HourlyWeather> {
    var hourlyWeatherList: List<HourlyWeather> = listOf()
    for (i in 0 until hourly!!.time.size) {
        hourlyWeatherList = hourlyWeatherList + HourlyWeather(
            time = LocalDateTime.parse(hourly!!.time[i]),
            temperature2m = hourly!!.temperature2m[i],
            relativeHumidity2m = hourly!!.relativeHumidity2m[i],
            precipitationProbability = hourly!!.precipitationProbability[i],
            precipitation = hourly!!.precipitation[i],
            rain = hourly!!.rain[i],
            snowfall = hourly!!.snowfall[i],
            cloudCover = hourly!!.cloudCover[i],
            windSpeed10m = hourly!!.windSpeed10m[i]
        )
    }
    return hourlyWeatherList
}

fun OpenMeteoAPIEntity.toLocationWeather(): LocationWeather {
    return LocationWeather(
        location = Location(
            locationId = null,
            locationName = null,
            coordinates = Coordinates(latitude!!, longitude!!)
        ), hourlyWeatherList = this.toHourlyWeatherList()
    )
}

fun HourlyWeatherEntity.toHourlyWeather(): HourlyWeather {
    return HourlyWeather(
        time = LocalDateTime.parse(time),
        temperature2m = temperature2m,
        relativeHumidity2m = relativeHumidity2m,
        precipitationProbability = precipitationProbability,
        precipitation = precipitation,
        rain = rain,
        snowfall = snowfall,
        cloudCover = cloudCover,
        windSpeed10m = windSpeed10m
    )
}

fun HourlyWeather.toHourlyWeatherEntity(): HourlyWeatherEntity {
    return HourlyWeatherEntity(
        time = time.toString(),
        temperature2m = temperature2m,
        relativeHumidity2m = relativeHumidity2m,
        precipitationProbability = precipitationProbability,
        precipitation = precipitation,
        rain = rain,
        snowfall = snowfall,
        cloudCover = cloudCover,
        windSpeed10m = windSpeed10m
    )
}

fun LocationWithHourlyWeather.toLocationWeather(): LocationWeather {
    return LocationWeather(location = location.toLocation(),
        hourlyWeatherList = hourlyWeather.map { it.toHourlyWeather() })
}

fun LocationWeather.toLocationWithHourlyWeather(): LocationWithHourlyWeather {
    return LocationWithHourlyWeather(location = location.toLocationEntity(),
        hourlyWeather = hourlyWeatherList.map { it.toHourlyWeatherEntity() })
}

fun LocationEntity.toLocation(): Location {
    return Location(
        locationId = locationId,
        locationName = locationName,
        coordinates = Coordinates(latitude, longitude)
    )
}

fun Location.toLocationEntity(): LocationEntity {
    return LocationEntity(
        locationId = locationId,
        locationName = locationName,
        latitude = coordinates.latitude,
        longitude = coordinates.longitude
    )
}