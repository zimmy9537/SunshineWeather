package android.example.sunshineweather;

import android.example.sunshineweather.weatherAPI.apidata.WeatherList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    String API_KEY = "81d2f00f32eb4da4b4e45914210708";

    @GET("forecast.json?key=81d2f00f32eb4da4b4e45914210708%20&days=10&aqi=yes&alerts=yes")
    Call<WeatherList> getWeatherLists(@Query("q") String city);
}
