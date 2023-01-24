package android.example.sunshineweather.weatherAPI.network.di

import android.example.sunshineweather.repository.WeatherRepository
import android.example.sunshineweather.weatherAPI.network.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object DataModule {
    @Provides
    fun providesDataRepository(weatherService: WeatherService): WeatherRepository {
        return WeatherRepository(weatherService)
    }
}