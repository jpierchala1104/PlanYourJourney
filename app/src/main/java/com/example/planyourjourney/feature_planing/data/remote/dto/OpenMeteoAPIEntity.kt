package com.example.planyourjourney.feature_planing.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OpenMeteoAPIEntity(
    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("longitude") var longitude: Double? = null,
    @SerializedName("generationtime_ms") var generationtimeMs: Double? = null,
    @SerializedName("utc_offset_seconds") var utcOffsetSeconds: Int? = null,
    @SerializedName("timezone") var timezone: String? = null,
    @SerializedName("timezone_abbreviation") var timezoneAbbreviation: String? = null,
    @SerializedName("elevation") var elevation: Int? = null,
    @SerializedName("hourly_units") var hourlyUnits: OpenMeteoAPIHourlyUnitsEntity? = OpenMeteoAPIHourlyUnitsEntity(),
    @SerializedName("hourly") var hourly: OpenMeteoAPIHourlyEntity? = OpenMeteoAPIHourlyEntity()
)