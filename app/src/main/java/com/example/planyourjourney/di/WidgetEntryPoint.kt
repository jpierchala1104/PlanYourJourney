package com.example.planyourjourney.di

import com.example.planyourjourney.feature_planing.domain.use_case.WeatherWidgetUseCases
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun weatherWidgetUseCases(): WeatherWidgetUseCases
}