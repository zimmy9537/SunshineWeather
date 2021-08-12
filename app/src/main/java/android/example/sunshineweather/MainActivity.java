package android.example.sunshineweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.example.sunshineweather.utilities.SunshineDateUtils;
import android.example.sunshineweather.utilities.SunshineWeatherUtils;
import android.example.sunshineweather.weatherAPI.Current;
import android.example.sunshineweather.weatherAPI.Forecast;
import android.example.sunshineweather.weatherAPI.Forecastday;
import android.example.sunshineweather.weatherAPI.Location;
import android.example.sunshineweather.weatherAPI.WeatherList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar weatherToolbar;
    private ProgressBar progressBar;
    private RecyclerView weatherRecyclerView;
    private TextView weatherTypeTextView;
    private TextView maximumTemperature;
    private TextView minimumTemperature;
    private TextView dayOfTheWeek;
    private ImageView weatherTypeImage;
    private TextView feelsLikeTemperature;
    private TextView preferredCity;
    private TextView respectiveRegionCountry;
    private LinearLayout todayLinearLayout;
    private TextView todayTime;
    private ScrollView scrollViewMain;
    private FloatingActionButton refreshButton;

    private String city;
    private boolean isMetric = true;

    private TextView uv_index_view;
    private TextView sunrise_view;
    private TextView sunset_view;
    private TextView wind_view;
    private TextView aqi_view;
    private TextView humidity_view;

    private final String WEATHER_API = "http://api.weatherapi.com/v1/";
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private List<Forecastday> foreCastList = null;
    private Forecast forecast;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String location_preference = "locationKey";
    public static final String unit_preference = "UnitsKey";

    private WeatherList weatherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //findViewById
        weatherToolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progress_MainActivity);
        weatherRecyclerView = findViewById(R.id.weather_recyclerview);
        weatherTypeTextView = findViewById(R.id.current_weather_details_tt);
        weatherTypeImage = findViewById(R.id.current_weather_photo);
        maximumTemperature = findViewById(R.id.maximum_current);
        minimumTemperature = findViewById(R.id.minimum_current);
        dayOfTheWeek = findViewById(R.id.current_date);
        uv_index_view = findViewById(R.id.uv_valueText);
        sunrise_view = findViewById(R.id.sunrise_valueText);
        sunset_view = findViewById(R.id.sunset_valueText);
        wind_view = findViewById(R.id.wind_valueText);
        aqi_view = findViewById(R.id.AQI_valueText);
        humidity_view = findViewById(R.id.humidity_valueText);
        feelsLikeTemperature = findViewById(R.id.feels_like_temp);
        preferredCity = findViewById(R.id.city_name_textview);
        respectiveRegionCountry = findViewById(R.id.region_country_textView);
        todayLinearLayout = findViewById(R.id.today_ll);
        todayTime = findViewById(R.id.local_time);
        scrollViewMain = findViewById(R.id.scrollviewMain);
        refreshButton = findViewById(R.id.refresh_floatingButton);


        //sharedPreferences
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        city = sharedPreferences.getString(location_preference, "Jamnagar");
        isMetric = sharedPreferences.getBoolean(unit_preference, true);
        preferredCity.setText(city);

        setSupportActionBar(weatherToolbar);//remember that when you create toolbar it is completely androidx type.
        getSupportActionBar().setDisplayShowTitleEnabled(false);//this is written so that extra name of the toolbar is not shown up.
        loadWeatherFromJson();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadWeatherFromJson();
            }
        });

        todayLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forecast = weatherList.getForecast();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("forecast", forecast);
                intent.putExtra("current",weatherList.getCurrent());
                intent.putExtra("isMetric",isMetric);
                startActivity(intent);
            }
        });
    }

    private void loadWeatherFromJson() {
        progressBar.setVisibility(View.VISIBLE);
        scrollViewMain.setVisibility(View.INVISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        Call<WeatherList> call = weatherApi.getWeatherLists(city);
        call.enqueue(new Callback<WeatherList>() {
            @Override
            public void onResponse(Call<WeatherList> call, Response<WeatherList> response) {
                weatherList = response.body();
                if (weatherList == null) {
                    Log.d(LOG_TAG, "weatherList is null");
                    return;
                }
                foreCastList = weatherList.getForecast().getForecastday();
                forecast=weatherList.getForecast();
                Current current = weatherList.getCurrent();
                Location location = weatherList.getLocation();
                String country = location.getCountry();
                String region = location.getRegion();
                if (region.isEmpty()) {
                    region = country;
                } else {
                    region = location.getRegion().concat(", ").concat(country);
                }
                respectiveRegionCountry.setText(region);
                String sunrise = foreCastList.get(0).getAstro().getSunrise();
                String sunset = foreCastList.get(0).getAstro().getSunset();
                Double uvIndex = current.getUv();
                Integer aqi = current.getAirQuality().getUsEpaIndex();
                Integer humidity = current.getHumidity();

                //set Time
                String time = location.getLocaltime().substring(11);
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    final Date dateObj = sdf.parse(time);
                    time = new SimpleDateFormat("K:mm a").format(dateObj);
                } catch (final ParseException e) {
                    e.printStackTrace();
                }
                todayTime.setText(time);

                uv_index_view.setText(String.valueOf(uvIndex));
                sunrise_view.setText(sunrise);
                sunset_view.setText(sunset);
                Double wind = current.getWindKph();
                wind_view.setText(String.valueOf(wind) + " km/h");

                aqi_view.setText(String.valueOf(aqi));
                humidity_view.setText(String.valueOf(humidity) + "%");

                //temp
                Double feelsLike = current.getFeelslikeC();
                feelsLikeTemperature.setText(SunshineWeatherUtils.getTemperatureFeelsLike(current, isMetric));


                if (foreCastList == null) {
                    Log.d(LOG_TAG, "forecastList is null");
                    return;
                }
                Forecastday forecastday = foreCastList.get(0);
                String PARSE_STRING = forecastday.getDate() + " 09:30:00";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = simpleDateFormat.parse(PARSE_STRING);
                    Long millis = date.getTime();
                    String display = SunshineDateUtils.getDayName(MainActivity.this, millis);
                    String weatherType = forecastday.getDay().getCondition().getText();//cloudy
                    dayOfTheWeek.setText(display);

                    //temp
                    maximumTemperature.setText(SunshineWeatherUtils.getTemperatureMaximum(forecastday.getDay(), isMetric));
                    minimumTemperature.setText(SunshineWeatherUtils.getTemperatureMinimum(forecastday.getDay(), isMetric));

                    weatherTypeTextView.setText(weatherType);
                    weatherTypeImage.setImageResource(SunshineWeatherUtils.getImageResource(weatherList.getCurrent().getIsDay(), forecastday.getDay().getCondition().getCode()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Log.e(MainActivity.class.getSimpleName(), "we fucked up here" + e.getMessage());
                }
                foreCastList.remove(0);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                weatherRecyclerView.setLayoutManager(linearLayoutManager);
                weatherRecyclerView.setAdapter(new WeatherAdapter(MainActivity.this, foreCastList, isMetric));
                progressBar.setVisibility(View.GONE);
                scrollViewMain.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<WeatherList> call, Throwable t) {
                if (t instanceof IOException) {
                    Log.d(LOG_TAG, "kind of exception ran here. " + t.getMessage());
                } else {
                    Log.d(LOG_TAG, "conversion issue faced here. probably");
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(location_preference)) {
            city = sharedPreferences.getString(key, "Jamnagar");
            preferredCity.setText(city);
            loadWeatherFromJson();
        }
        if (key.equals(unit_preference)) {
            isMetric = sharedPreferences.getBoolean(key, true);
            loadWeatherFromJson();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

}