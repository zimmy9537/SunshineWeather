package android.example.sunshineweather

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.example.sunshineweather.databinding.ActivityMainBinding
import android.example.sunshineweather.ui.WeatherViewModel
import android.example.sunshineweather.utilities.SunshineDateUtils
import android.example.sunshineweather.utilities.SunshineWeatherUtils
import android.example.sunshineweather.weatherAPI.apidata.Forecast
import android.example.sunshineweather.weatherAPI.apidata.Forecastday
import android.example.sunshineweather.weatherAPI.apidata.WeatherList
import android.example.sunshineweather.weatherAPI.network.ResultData
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var city: String
    private var isMetric = true
    private val WEATHER_API = "http://api.weatherapi.com/v1/"
    private val LOG_TAG = MainActivity::class.java.simpleName
    private lateinit var foreCastList: ArrayList<Forecastday>
    private var forecast: Forecast? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var weatherList: WeatherList? = null

    private val observer = Observer<ResultData<WeatherList>> { resultData ->
        when (resultData) {
            is ResultData.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is ResultData.Success -> {
                binding.progressBar.visibility = View.GONE
                val weatherList = resultData.data
                if (weatherList == null) {
                    Toast.makeText(this, "Try again later", Toast.LENGTH_SHORT).show()
                    return@Observer
                }
                foreCastList = weatherList.forecast.forecastday as ArrayList<Forecastday>
                forecast = weatherList.forecast
                val current = weatherList.current
                val location = weatherList.location
                val country = location.country
                var region = location.region
                region = if (region.isEmpty()) {
                    country
                } else {
                    location.region + ", " + country
                }
                binding.respectiveRegionCountry.text = region
                val sunrise = foreCastList.get(0).astro.sunrise
                val sunset = foreCastList.get(0).astro.sunset
                val uvIndex = current.uv
                val aqi = current.airQuality.usEpaIndex
                val humidity = current.humidity

                //set Time
                var time: String? = location.localtime.substring(11)
                try {
                    val sdf = SimpleDateFormat("H:mm")
                    val dateObj = sdf.parse(time)
                    time = SimpleDateFormat("K:mm a").format(dateObj)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                binding.todayTime.text = time
                binding.uvIndexView.text = uvIndex.toString()
                binding.sunriseView.text = sunrise
                binding.sunsetView.text = sunset
                val wind = current.windKph
                binding.windView.text = "$wind km/h"
                binding.aqiView.text = aqi.toString()
                binding.humidityView.text = "$humidity%"

                //temp
                val feelsLike = current.feelslikeC
                binding.feelsLikeTemperature.text =
                    SunshineWeatherUtils.getTemperatureFeelsLike(current, isMetric)
                val forecastday = foreCastList[0]
                val PARSE_STRING = forecastday.date + " 09:30:00"
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                try {
                    val date = simpleDateFormat.parse(PARSE_STRING)
                    val millis = date.time
                    val display = SunshineDateUtils.getDayName(this@MainActivity, millis)
                    val weatherType = forecastday.day.condition.text //cloudy
                    binding.dayOfTheWeek.text = display

                    //temp
                    binding.maximumTemperature.text =
                        SunshineWeatherUtils.getTemperatureMaximum(forecastday.day, isMetric)
                    binding.minimumTemperature.text =
                        SunshineWeatherUtils.getTemperatureMinimum(forecastday.day, isMetric)
                    binding.weatherTypeTextView.text = weatherType
                    binding.weatherTypeImage.setImageResource(
                        SunshineWeatherUtils.getImageResource(
                            weatherList.current.isDay, forecastday.day.condition.code
                        )
                    )
                } catch (e: ParseException) {
                    e.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    Log.e(MainActivity::class.java.simpleName, "we fucked up here" + e.message)
                }
                foreCastList.removeAt(0)
                val linearLayoutManager = LinearLayoutManager(this@MainActivity)
                binding.weatherRecyclerView.layoutManager = linearLayoutManager
                binding.weatherRecyclerView.adapter =
                    WeatherAdapter(this@MainActivity, foreCastList, isMetric)
                binding.progressBar.visibility = View.GONE
                binding.scrollViewMain.visibility = View.VISIBLE

            }
            else -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Try again later", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @delegate:Inject
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //sharedPreferences
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        city = sharedPreferences.getString(location_preference, "Jamnagar").toString()
        isMetric = sharedPreferences.getBoolean(unit_preference, true)
        binding.preferredCity.setText(city)
        setSupportActionBar(binding.weatherToolbar) //remember that when you create toolbar it is completely androidx type.
        supportActionBar!!.setDisplayShowTitleEnabled(false) //this is written so that extra name of the toolbar is not shown up.
        loadWeatherFromJson()
        binding.refreshButton.setOnClickListener(View.OnClickListener { loadWeatherFromJson() })
        binding.todayLinearLayout.setOnClickListener(View.OnClickListener {
            forecast = weatherList!!.forecast
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("forecast", forecast)
            intent.putExtra("current", weatherList!!.current)
            intent.putExtra("isMetric", isMetric)
            startActivity(intent)
        })
    }

    private fun loadWeatherFromJson() {
        GlobalScope.launch {
            val liveData = weatherViewModel.getWeatherList(city)
            liveData.observe(this@MainActivity, observer)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == location_preference) {
            city = sharedPreferences.getString(key, "Jamnagar").toString()
            binding.preferredCity.text = city
            loadWeatherFromJson()
        }
        if (key == unit_preference) {
            isMetric = sharedPreferences.getBoolean(key, true)
            loadWeatherFromJson()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val MyPREFERENCES = "MyPrefs"
        const val location_preference = "locationKey"
        const val unit_preference = "UnitsKey"
    }
}