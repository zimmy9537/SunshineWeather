package android.example.sunshineweather.repository

import android.example.sunshineweather.weatherAPI.apidata.WeatherList
import android.example.sunshineweather.weatherAPI.network.WeatherService
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherService: WeatherService) {
    suspend fun getWeatherListData(city: String): WeatherList? {
        return weatherService.getWeatherData(city)
    }
}