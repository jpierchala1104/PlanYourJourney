package com.example.planyourjourney.di

import android.app.Application
import androidx.room.Room
import com.example.planyourjourney.feature_planing.data.local.WeatherDatabase
import com.example.planyourjourney.feature_planing.data.remote.OpenMeteoAPI
import com.example.planyourjourney.feature_planing.data.repository.WeatherRepositoryImpl
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository
import com.example.planyourjourney.feature_planing.domain.use_case.DeleteLocationUseCase
import com.example.planyourjourney.feature_planing.domain.use_case.DeleteWeatherAtLocationUseCase
import com.example.planyourjourney.feature_planing.domain.use_case.FetchWeatherAtLocationUseCase
import com.example.planyourjourney.feature_planing.domain.use_case.GetLocationsUseCase
import com.example.planyourjourney.feature_planing.domain.use_case.GetLocationsWithWeatherUseCase
import com.example.planyourjourney.feature_planing.domain.use_case.GetWeatherAtLocationUseCase
import com.example.planyourjourney.feature_planing.domain.use_case.InsertLocationUseCase
import com.example.planyourjourney.feature_planing.domain.use_case.PlaningUseCases
import com.example.planyourjourney.feature_planing.domain.use_case.WeatherDetailsUseCases
import com.example.planyourjourney.feature_planing.domain.use_case.WeatherUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideOpenMeteoAPI(): OpenMeteoAPI {
        return Retrofit.Builder()
            .client(
                OkHttpClient()
                    .newBuilder()
                    .addInterceptor { chain ->
                        val request = chain.request()
                        println("Outgoing request to ${request.url()}")
                        chain.proceed(request)
                    }
                    .build())
            .baseUrl(OpenMeteoAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(OpenMeteoAPI::class.java)
    }

    @Provides
    @Singleton
    fun providePlaningUseCases(repository: WeatherRepository): PlaningUseCases {
        return PlaningUseCases(
            getLocationsUseCase = GetLocationsUseCase(repository),
            fetchWeatherAtLocationUseCase = FetchWeatherAtLocationUseCase(repository),
            insertLocationUseCase = InsertLocationUseCase(repository),
            deleteLocationUseCase = DeleteLocationUseCase(repository),
            deleteWeatherAtLocationUseCase = DeleteWeatherAtLocationUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideWeatherUseCases(repository: WeatherRepository): WeatherUseCases {
        return WeatherUseCases(
            getLocationsWithWeatherUseCase = GetLocationsWithWeatherUseCase(repository),
            fetchWeatherAtLocationUseCase = FetchWeatherAtLocationUseCase(repository),
            insertLocationUseCase = InsertLocationUseCase(repository),
            deleteLocationUseCase = DeleteLocationUseCase(repository),
            deleteWeatherAtLocationUseCase = DeleteWeatherAtLocationUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideWeatherDetailsUseCases(repository: WeatherRepository): WeatherDetailsUseCases {
        return WeatherDetailsUseCases(
            getWeatherAtLocationUseCase = GetWeatherAtLocationUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideWeatherDatabase(app: Application): WeatherDatabase {
        return Room.databaseBuilder(
            app,
            WeatherDatabase::class.java,
            "weatherdb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(api: OpenMeteoAPI, db: WeatherDatabase): WeatherRepository {
        return WeatherRepositoryImpl(api, db)
    }
}