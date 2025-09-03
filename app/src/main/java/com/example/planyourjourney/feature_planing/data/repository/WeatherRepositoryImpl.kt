package com.example.planyourjourney.feature_planing.data.repository

import com.example.planyourjourney.feature_planing.data.local.WeatherDatabase
import com.example.planyourjourney.feature_planing.data.mapper.toHourlyWeatherEntity
import com.example.planyourjourney.feature_planing.data.mapper.toLocation
import com.example.planyourjourney.feature_planing.data.mapper.toLocationEntity
import com.example.planyourjourney.feature_planing.data.mapper.toLocationWeather
import com.example.planyourjourney.feature_planing.data.remote.OpenMeteoAPI
import com.example.planyourjourney.feature_planing.domain.model.HourlyWeather
import com.example.planyourjourney.feature_planing.domain.model.Location
import com.example.planyourjourney.feature_planing.domain.model.LocationWeather
import com.example.planyourjourney.feature_planing.domain.model.Settings
import com.example.planyourjourney.feature_planing.domain.model.WeatherUnits
import com.example.planyourjourney.feature_planing.domain.repository.SettingsOperations
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository
import com.example.planyourjourney.feature_planing.domain.repository.WidgetPreloadOperations
import com.example.planyourjourney.feature_planing.domain.util.APIErrorResult
import com.example.planyourjourney.feature_planing.domain.util.APIFetchResult
import com.example.planyourjourney.feature_planing.domain.util.PrecipitationUnits
import com.example.planyourjourney.feature_planing.domain.util.Resource
import com.example.planyourjourney.feature_planing.domain.util.TemperatureUnits
import com.example.planyourjourney.feature_planing.domain.util.WindSpeedUnits
import com.example.planyourjourney.feature_planing.domain.util.celsiusToFahrenheit
import com.example.planyourjourney.feature_planing.domain.util.fahrenheitToCelsius
import com.example.planyourjourney.feature_planing.domain.util.inchesToMillimeters
import com.example.planyourjourney.feature_planing.domain.util.kilometersPerHourToKnots
import com.example.planyourjourney.feature_planing.domain.util.kilometersPerHourToMetersPerSecond
import com.example.planyourjourney.feature_planing.domain.util.kilometersPerHourToMilesPerHour
import com.example.planyourjourney.feature_planing.domain.util.knotsToKilometersPerHour
import com.example.planyourjourney.feature_planing.domain.util.knotsToMetersPerSecond
import com.example.planyourjourney.feature_planing.domain.util.knotsToMilesPerHour
import com.example.planyourjourney.feature_planing.domain.util.metersPerSecondToKilometersPerHour
import com.example.planyourjourney.feature_planing.domain.util.metersPerSecondToKnots
import com.example.planyourjourney.feature_planing.domain.util.metersPerSecondToMilesPerHour
import com.example.planyourjourney.feature_planing.domain.util.milesPerHourToKilometersPerHour
import com.example.planyourjourney.feature_planing.domain.util.milesPerHourToKnots
import com.example.planyourjourney.feature_planing.domain.util.milesPerHourToMetersPerSecond
import com.example.planyourjourney.feature_planing.domain.util.millimetersToInches
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenMeteoAPI,
    db: WeatherDatabase,
    private val settingsDataStore: SettingsOperations,
    private val widgetPreloadDataStore: WidgetPreloadOperations
) : WeatherRepository {

    private val dao = db.dao

    // TODO: function to refresh all the weather??
    //  figure out how to fetch from API without spamming it,

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

    override suspend fun fetchWeatherFromAPI(
        location: Location,
        weatherUnits: WeatherUnits
    ): Flow<APIFetchResult> {
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
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(APIFetchResult.Error(APIErrorResult.HttpExceptionError))
                emit(APIFetchResult.Loading(false))
                return@flow
            } catch (e: IOException) {
                e.printStackTrace()
                emit(APIFetchResult.Error(APIErrorResult.IOExceptionError))
                emit(APIFetchResult.Loading(false))
                return@flow
            }
            val locationId = dao.getLocationId(
                latitude = location.coordinates.latitude,
                longitude = location.coordinates.longitude
            )
            dao.clearHourlyWeathersAtLocation(locationId!!)
            dao.insertHourlyWeathers(remoteLocationWeather.hourlyWeatherList.map { hourlyWeather ->
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

    override suspend fun deleteHourlyWeathersWithDate(date: String) {
        dao.deleteHourlyWeathersWithDate(date)
    }

    override suspend fun saveSettings(settings: Settings) {
        settingsDataStore.saveSettings(settings)
    }

    override suspend fun getSettings(): Flow<Settings> {
        return settingsDataStore.readSettingsState()
    }

    override suspend fun preloadWidgetData(locationWeather: LocationWeather) {
        widgetPreloadDataStore.saveData(locationWeather)
    }

    override suspend fun updateUnits(settings: Settings, oldUnits: WeatherUnits) {
        val weatherEntities = dao.getHourlyWeathers()
        var convertedWeatherEntities = weatherEntities
        if (oldUnits.temperatureUnits == settings.weatherUnits.temperatureUnits) {
            //do nothing
        } else {
            if (oldUnits.temperatureUnits == TemperatureUnits.CELSIUS) {
                convertedWeatherEntities = convertedWeatherEntities.map {
                    it.copy(temperature2m = it.temperature2m.celsiusToFahrenheit())
                }
            } else if (oldUnits.temperatureUnits == TemperatureUnits.FAHRENHEIT) {
                convertedWeatherEntities = convertedWeatherEntities.map {
                    it.copy(temperature2m = it.temperature2m.fahrenheitToCelsius())
                }
            }
        }
        if (oldUnits.precipitationUnits == settings.weatherUnits.precipitationUnits) {
            //do nothing
        } else {
            if (oldUnits.precipitationUnits == PrecipitationUnits.MILLIMETERS) {
                convertedWeatherEntities = convertedWeatherEntities.map {
                    it.copy(
                        precipitation = it.precipitation.millimetersToInches(),
                        rain = it.rain.millimetersToInches(),
                        snowfall = it.snowfall.millimetersToInches()
                    )
                }
            } else if (oldUnits.precipitationUnits == PrecipitationUnits.INCH) {
                convertedWeatherEntities = convertedWeatherEntities.map {
                    it.copy(
                        precipitation = it.precipitation.inchesToMillimeters(),
                        rain = it.rain.inchesToMillimeters(),
                        snowfall = it.snowfall.inchesToMillimeters()
                    )
                }
            }
        }
        if (oldUnits.windSpeedUnits == settings.weatherUnits.windSpeedUnits) {
            //do nothing
        } else {
            when (oldUnits.windSpeedUnits) {
                WindSpeedUnits.KILOMETERS_PER_HOUR -> {
                    when (settings.weatherUnits.windSpeedUnits) {
                        WindSpeedUnits.METERS_PER_SECOND -> {
                            convertedWeatherEntities = convertedWeatherEntities.map {
                                it.copy(
                                    windSpeed10m = it.windSpeed10m.kilometersPerHourToMetersPerSecond()
                                )
                            }
                        }

                        WindSpeedUnits.MILES_PER_HOUR -> {
                            convertedWeatherEntities = convertedWeatherEntities.map {
                                it.copy(
                                    windSpeed10m = it.windSpeed10m.kilometersPerHourToMilesPerHour()
                                )
                            }
                        }

                        WindSpeedUnits.KNOTS -> {
                            convertedWeatherEntities = convertedWeatherEntities.map {
                                it.copy(
                                    windSpeed10m = it.windSpeed10m.kilometersPerHourToKnots()
                                )
                            }
                        }

                        else -> {
                            // Do Nothing
                        }
                    }
                }

                WindSpeedUnits.METERS_PER_SECOND -> {
                    when (settings.weatherUnits.windSpeedUnits) {
                        WindSpeedUnits.KILOMETERS_PER_HOUR -> {
                            convertedWeatherEntities = convertedWeatherEntities.map {
                                it.copy(
                                    windSpeed10m = it.windSpeed10m.metersPerSecondToKilometersPerHour()
                                )
                            }
                        }

                        WindSpeedUnits.MILES_PER_HOUR -> {
                            convertedWeatherEntities = convertedWeatherEntities.map {
                                it.copy(
                                    windSpeed10m = it.windSpeed10m.metersPerSecondToMilesPerHour()
                                )
                            }
                        }

                        WindSpeedUnits.KNOTS -> {
                            convertedWeatherEntities = convertedWeatherEntities.map {
                                it.copy(
                                    windSpeed10m = it.windSpeed10m.metersPerSecondToKnots()
                                )
                            }
                        }

                        else -> {
                            // Do Nothing
                        }
                    }
                }

                WindSpeedUnits.MILES_PER_HOUR -> {
                    when (settings.weatherUnits.windSpeedUnits) {
                        WindSpeedUnits.KILOMETERS_PER_HOUR -> {
                            convertedWeatherEntities = convertedWeatherEntities.map {
                                it.copy(
                                    windSpeed10m = it.windSpeed10m.milesPerHourToKilometersPerHour()
                                )
                            }
                        }

                        WindSpeedUnits.METERS_PER_SECOND -> {
                            convertedWeatherEntities = convertedWeatherEntities.map {
                                it.copy(
                                    windSpeed10m = it.windSpeed10m.milesPerHourToMetersPerSecond()
                                )
                            }
                        }

                        WindSpeedUnits.KNOTS -> {
                            convertedWeatherEntities = convertedWeatherEntities.map {
                                it.copy(
                                    windSpeed10m = it.windSpeed10m.milesPerHourToKnots()
                                )
                            }
                        }

                        else -> {
                            // Do Nothing
                        }
                    }
                }

                WindSpeedUnits.KNOTS -> {
                    when (settings.weatherUnits.windSpeedUnits) {
                        WindSpeedUnits.KILOMETERS_PER_HOUR -> {
                            convertedWeatherEntities = convertedWeatherEntities.map {
                                it.copy(
                                    windSpeed10m = it.windSpeed10m.knotsToKilometersPerHour()
                                )
                            }
                        }

                        WindSpeedUnits.METERS_PER_SECOND -> {
                            convertedWeatherEntities = convertedWeatherEntities.map {
                                it.copy(
                                    windSpeed10m = it.windSpeed10m.knotsToMetersPerSecond()
                                )
                            }
                        }

                        WindSpeedUnits.MILES_PER_HOUR -> {
                            convertedWeatherEntities = convertedWeatherEntities.map {
                                it.copy(
                                    windSpeed10m = it.windSpeed10m.knotsToMilesPerHour()
                                )
                            }
                        }

                        else -> {
                            // Do Nothing
                        }
                    }
                }
            }
        }
        if (convertedWeatherEntities == weatherEntities)
            return
        dao.insertHourlyWeathers(convertedWeatherEntities)
    }
}