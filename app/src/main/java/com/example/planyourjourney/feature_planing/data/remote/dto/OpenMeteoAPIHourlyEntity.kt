package com.example.planyourjourney.feature_planing.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OpenMeteoAPIHourlyEntity(
    @SerializedName("time") var time: ArrayList<String> = arrayListOf(),
    @SerializedName("temperature_2m") var temperature2m: ArrayList<Double> = arrayListOf(),
    @SerializedName("relative_humidity_2m") var relativeHumidity2m: ArrayList<Int> = arrayListOf(),
    @SerializedName("precipitation_probability") var precipitationProbability: ArrayList<Int> = arrayListOf(),
    @SerializedName("precipitation") var precipitation: ArrayList<Double> = arrayListOf(),
    @SerializedName("rain") var rain: ArrayList<Double> = arrayListOf(),
    @SerializedName("snowfall") var snowfall: ArrayList<Double> = arrayListOf(),
    @SerializedName("cloud_cover") var cloudCover: ArrayList<Int> = arrayListOf(),
    @SerializedName("wind_speed_10m") var windSpeed10m: ArrayList<Double> = arrayListOf()
)