package android.example.sunshineweather.application

import android.app.Application
import android.example.sunshineweather.repository.WeatherRepository
import android.example.sunshineweather.worker.WeatherWorker
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class WeatherApplication : Application() {

    lateinit var repository: WeatherRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
        setUpWorker()
    }

    private fun initialize() {
        //initialize the values of weatherService and database.
    }

    private fun setUpWorker() {
        val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workerRequest =
            PeriodicWorkRequest.Builder(WeatherWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constraint)
                .build()
        WorkManager.getInstance(this).enqueue(workerRequest)
    }
}