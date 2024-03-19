package com.example.planyourjourney.feature_planing.data.repository

import com.example.planyourjourney.feature_planing.data.data_source.OpenMeteoAPI
import com.example.planyourjourney.feature_planing.data.mapper.toHourlyWeatherList
import com.example.planyourjourney.feature_planing.domain.model.Coordinates
import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository
import com.example.planyourjourney.feature_planing.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenMeteoAPI
) : WeatherRepository {
    override suspend fun getWeather(location: Coordinates): Flow<Resource<List<HourlyWeather>>> {
        return flow {
            emit(Resource.Loading(true))
            val hourlyWeatherList = try {
                api.getWeather(location.latitude, location.longitude).toHourlyWeatherList()
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            hourlyWeatherList?.let {weather ->
                emit(Resource.Success(weather))
                emit(Resource.Loading(false))
            }
        }
    }
}