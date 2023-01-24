package android.example.sunshineweather.ui

import android.example.sunshineweather.usecase.DataUseCase
import android.example.sunshineweather.weatherAPI.apidata.WeatherList
import android.example.sunshineweather.weatherAPI.network.ResultData
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val useCase: DataUseCase) :
    ViewModel() {
    suspend fun getWeatherList(city: String): LiveData<ResultData<WeatherList>> {
        return flow {
            emit(ResultData.Loading())
            try {
                emit(useCase.getWeatherList(city))
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                emit(ResultData.Exception())
            }
        }.asLiveData(Dispatchers.IO)
    }
}