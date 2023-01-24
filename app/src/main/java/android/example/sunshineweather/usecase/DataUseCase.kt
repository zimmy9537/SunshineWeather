package android.example.sunshineweather.usecase

import android.example.sunshineweather.repository.WeatherRepository
import android.example.sunshineweather.weatherAPI.apidata.WeatherList
import android.example.sunshineweather.weatherAPI.network.ResultData
import javax.inject.Inject

class DataUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend fun getWeatherList(city: String): ResultData<WeatherList> {
        val weatherList = weatherRepository.getWeatherListData(city)
        val resultData = if (weatherList != null) {
            ResultData.Success(weatherList)
        } else {
            ResultData.Failed()
        }
        return resultData
    }
}