package com.example.planyourjourney.feature_planing.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [HourlyWeatherEntity::class, LocationEntity::class],
    version = 1
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val dao: WeatherDao
}