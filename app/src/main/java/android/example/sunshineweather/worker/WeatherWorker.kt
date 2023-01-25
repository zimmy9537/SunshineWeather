package android.example.sunshineweather.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.example.sunshineweather.R
import android.example.sunshineweather.application.WeatherApplication
import android.example.sunshineweather.constants.Constant
import android.example.sunshineweather.weatherAPI.apidata.WeatherList
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherWorker(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {

    private val TAG = WeatherWorker::class.java.simpleName

    override fun doWork(): Result {
        val city = context.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE)
            .getString(Constant.location_preference, Constant.JAMNAGAR)
        val repository = (context as WeatherApplication).repository
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.getWeatherListData(city!!)
            createNotification(result, city)
        }
        return Result.success()
    }

    private fun createNotification(weatherList: WeatherList?, city: String) {
        createNotificationChannel()
        val weatherCondition = weatherList?.current?.condition?.text ?: return
        val textTitle = "Weather info @$city"
        val textContent = "It seems to be $weatherCondition right now"
        var builder = NotificationCompat.Builder(context, Constant.CHANNEL_ID)
            .setSmallIcon(R.drawable.sunrise)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.d(TAG, "Doesn't have permission to post notification")
                return
            }
            notify(Constant.NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = Constant.CHANNEL_NAME
            val descriptionText = Constant.CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Constant.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}