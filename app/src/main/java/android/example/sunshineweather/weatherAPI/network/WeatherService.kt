package android.example.sunshineweather.weatherAPI.network

import android.example.sunshineweather.constants.Constant
import android.example.sunshineweather.weatherAPI.apidata.WeatherList
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET(Constant.URL_PARAMS)
    suspend fun getWeatherData(@Query(Constant.QUERY_PARAM) city: String): WeatherList?
}