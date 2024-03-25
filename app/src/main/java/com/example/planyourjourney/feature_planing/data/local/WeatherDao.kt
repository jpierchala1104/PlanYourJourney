package com.example.planyourjourney.feature_planing.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface WeatherDao {
    // HourlyWeather Table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyWeathers(
        hourlyWeatherEntities: List<HourlyWeatherEntity>
    )

    @Query("DELETE FROM hourlyweatherentity")
    suspend fun clearHourlyWeathers()

    @Query("DELETE FROM hourlyweatherentity WHERE locationWeatherId=:locationId")
    suspend fun clearHourlyWeathersAtLocation(locationId: Int)

    // Location Table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(
        locationEntity: List<LocationEntity>
    )

    @Query("DELETE FROM locationentity")
    suspend fun clearLocations()

    @Query("DELETE FROM locationentity WHERE locationId=:locationId")
    suspend fun clearLocation(locationId: Int)

    @Transaction
    @Query("SELECT * FROM locationentity WHERE locationId=:locationId")
    suspend fun getHourlyWeatherAtLocation(locationId: Int): List<LocationWithHourlyWeather>

    @Transaction
    @Query("SELECT * FROM locationentity")
    suspend fun getLocationsWithHourlyWeather(): List<LocationWithHourlyWeather>
}