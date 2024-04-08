package com.example.planyourjourney.feature_planing.data.repository

import com.example.planyourjourney.feature_planing.data.local.WeatherDatabase
import com.example.planyourjourney.feature_planing.data.mapper.toHourlyWeatherEntity
import com.example.planyourjourney.feature_planing.data.mapper.toLocation
import com.example.planyourjourney.feature_planing.data.mapper.toLocationEntity
import com.example.planyourjourney.feature_planing.data.remote.OpenMeteoAPI
import com.example.planyourjourney.feature_planing.data.mapper.toLocationWeather
import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository
import com.example.planyourjourney.feature_planing.domain.util.APIFetchResult
import com.example.planyourjourney.feature_planing.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenMeteoAPI, db: WeatherDatabase
) : WeatherRepository {

    private val dao = db.dao

    override suspend fun getWeatherAtLocation(
        location: Location
    ): Flow<Resource<LocationWeather>> {
        return flow {
            emit(Resource.Loading(true))
            val localLocationWeather = dao.getHourlyWeatherAtLocation(
                location.locationId!!
            )
            //localLocationWeather.let {
            emit(Resource.Success(data = localLocationWeather.toLocationWeather()))
            emit(Resource.Loading(false))
            //}
        }
    }

    override suspend fun fetchWeatherFromAPI(location: Location, weatherUnits: WeatherUnits) : Flow<APIFetchResult> {
        return flow {
            emit(APIFetchResult.Loading())
            val remoteLocationWeather = try {
                api.getWeather(
                    latitude = location.coordinates.latitude,
                    longitude = location.coordinates.longitude,
                    temperatureUnit = weatherUnits.temperatureUnits.queryUnits,
                    precipitationUnit = weatherUnits.precipitationUnits.queryUnits,
                    windSpeedUnit = weatherUnits.windSpeedUnits.queryUnits
                ).toLocationWeather()
            } catch (e: IOException) {
                e.printStackTrace()
                emit(APIFetchResult.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(APIFetchResult.Error("Couldn't load data"))
                null
            }
            val locationId = dao.getLocationId(
                latitude = location.coordinates.latitude,
                longitude = location.coordinates.longitude
            )

            dao.insertHourlyWeathers(remoteLocationWeather!!.hourlyWeatherList.map { hourlyWeather ->
                // TODO: If the id isn't saved the problem is here!!!
                hourlyWeather.toHourlyWeatherEntity().copy(locationWeatherId = locationId)
            })
            emit(APIFetchResult.Success())
            emit(APIFetchResult.Loading(false))
        }
    }

    override suspend fun getLocationsWithWeather(): Flow<Resource<List<LocationWeather>>> {
        return flow {
            emit(Resource.Loading(true))
            val localLocationWeather = dao.getLocationsWithHourlyWeather()
//            localLocationWeather.let {
            emit(Resource.Success(data = localLocationWeather.map { it.toLocationWeather() }))
            emit(Resource.Loading(false))
//            }
        }
    }

    override suspend fun getLocations(): Flow<Resource<List<Location>>> {
        return flow {
            emit(Resource.Loading(true))
            val localLocations = dao.getLocations()
//            localLocations.let {
            emit(Resource.Success(data = localLocations.map { it.toLocation() }))
            emit(Resource.Loading(false))
//            }
        }
    }

    override suspend fun getLocationId(location: Location): Int? {
        return dao.getLocationId(location.coordinates.latitude, location.coordinates.longitude)
    }

    override suspend fun insertLocation(location: Location) {
        dao.insertLocations(listOf(location.toLocationEntity()))
    }

    override suspend fun deleteLocation(location: Location) {
        dao.clearLocation(location.locationId!!)
    }

    override suspend fun deleteLocations() {
        dao.clearLocations()
    }

    override suspend fun insertHourlyWeathers(hourlyWeather: List<HourlyWeather>) {
        dao.insertHourlyWeathers(hourlyWeather.map { it.toHourlyWeatherEntity() })
    }

    override suspend fun deleteHourlyWeathers() {
        dao.clearHourlyWeathers()
    }

    override suspend fun deleteHourlyWeathersAtLocation(locationId: Int) {
        dao.clearHourlyWeathersAtLocation(locationId)
    }
}