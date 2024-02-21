package com.example.planyourjourney.services.dataClasses

import com.example.planyourjourney.services.dataClasses.Hourly
import com.example.planyourjourney.services.dataClasses.HourlyUnits
import com.google.gson.annotations.SerializedName

data class WeatherJson(
    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("longitude") var longitude: Double? = null,
    @SerializedName("generationtime_ms") var generationtimeMs: Double? = null,
    @SerializedName("utc_offset_seconds") var utcOffsetSeconds: Int? = null,
    @SerializedName("timezone") var timezone: String? = null,
    @SerializedName("timezone_abbreviation") var timezoneAbbreviation: String? = null,
    @SerializedName("elevation") var elevation: Int? = null,
    @SerializedName("hourly_units") var hourlyUnits: HourlyUnits? = HourlyUnits(),
    @SerializedName("hourly") var hourly: Hourly? = Hourly()
)