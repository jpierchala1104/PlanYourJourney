package com.example.planyourjourney.feature_planing.data.local

import androidx.room.AutoMigration
import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = [HourlyWeatherEntity::class, LocationEntity::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ]
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val dao: WeatherDao
}