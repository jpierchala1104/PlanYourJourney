package com.example.planyourjourney.di

import android.app.Application
import androidx.room.Room
import com.example.planyourjourney.feature_planing.data.local.WeatherDatabase
import com.example.planyourjourney.feature_planing.data.remote.OpenMeteoAPI
import com.example.planyourjourney.feature_planing.domain.repository.WeatherRepository
import com.example.planyourjourney.feature_planing.domain.use_case.GetWeatherUseCase
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
    fun provideGetWeatherUseCase(repository: WeatherRepository): GetWeatherUseCase {
        return GetWeatherUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideWeatherDatabase(app: Application): WeatherDatabase{
        return Room.databaseBuilder(
            app,
            WeatherDatabase::class.java,
            "weatherdb.db"
        ).build()
    }
}