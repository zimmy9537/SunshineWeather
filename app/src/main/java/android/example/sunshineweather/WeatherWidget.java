package android.example.sunshineweather;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
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
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget extends AppWidgetProvider {


    private static final String WEATHER_API = "http://api.weatherapi.com/v1/";
    private static SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String location_preference = "locationKey";
    public static final String unit_preference = "UnitsKey";
    private static String city;
    private static boolean isMetric;

    //intent service presenting data
    private String region;
    private String country;
    private String time;
    private String display;
    private String weatherType;
    private Forecastday forecastday;
    private Context context;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        context= context.getApplicationContext();
        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            views.setOnClickPendingIntent(R.id.today_ll_wid, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, context.MODE_PRIVATE);
        city = sharedPreferences.getString(location_preference, "Jamnagar");
        isMetric = sharedPreferences.getBoolean(unit_preference, true);

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);

        views.setViewVisibility(R.id.progress_WeatherWidget, View.VISIBLE);
        views.setViewVisibility(R.id.progress_WeatherWidget, View.INVISIBLE);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    private void loadWeatherFromJson(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//        views.setTextViewText(R.id.city_name_textview_wid,city);

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        Call<WeatherList> call = weatherApi.getWeatherLists(city);
        call.enqueue(new Callback<WeatherList>() {
            @Override
            public void onResponse(Call<WeatherList> call, Response<WeatherList> response) {
                WeatherList weatherList = response.body();
                if (weatherList == null) {
                    Log.d(WeatherWidget.class.getSimpleName(), "weatherList is null");
                    return;
                }
                List<Forecastday> foreCastList = weatherList.getForecast().getForecastday();
                Location location = weatherList.getLocation();
                country = location.getCountry();
                region = location.getRegion();
                if (region.isEmpty()) {
                    region = country;
                } else {
                    region = location.getRegion().concat(", ").concat(country);
                }

//                views.setTextViewText(R.id.region_country_textView_wid, region);

                //set Time
                time = location.getLocaltime().substring(11);
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    final Date dateObj = sdf.parse(time);
                    time = new SimpleDateFormat("K:mm a").format(dateObj);
                } catch (final ParseException e) {
                    e.printStackTrace();
                }
//                views.setTextViewText(R.id.local_time_wid, time);

                if (foreCastList == null) {
                    Log.d(WeatherWidget.class.getSimpleName(), "forecastList is null");
                    return;
                }
                forecastday = foreCastList.get(0);
                String PARSE_STRING = forecastday.getDate() + " 09:30:00";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = simpleDateFormat.parse(PARSE_STRING);
                    Long millis = date.getTime();
                    display = SunshineDateUtils.getDayName(context, millis);
                    weatherType = forecastday.getDay().getCondition().getText();//cloudy
//                    views.setTextViewText(R.id.current_date_wid, display);

                    //temp
//                    views.setTextViewText(R.id.maximum_current_wid, SunshineWeatherUtils.getTemperatureMaximum(forecastday.getDay(), isMetric));
//                    views.setTextViewText(R.id.minimum_current_wid, SunshineWeatherUtils.getTemperatureMinimum(forecastday.getDay(), isMetric));
//                    views.setTextViewText(R.id.current_weather_details_tt_wid, weatherType);
//                    views.setImageViewResource(R.id.current_weather_photo_wid, SunshineWeatherUtils.getImageResource(weatherList.getCurrent().getIsDay(), forecastday.getDay().getCondition().getCode()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(MainActivity.class.getSimpleName(), "we fucked up here" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<WeatherList> call, Throwable t) {
                if (t instanceof IOException) {
                    Log.d(WeatherWidget.class.getSimpleName(), "kind of exception ran here. " + t.getMessage());
                } else {
                    Log.d(WeatherWidget.class.getSimpleName(), "conversion issue faced here. probably");
                }
            }
        });
    }

    public class RemoteIntentService extends IntentService {
        private static final String TAG = "RemoteIntentService";

        public RemoteIntentService() {
            super("RemoteIntentService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            Bundle data = intent.getExtras();
            String command = data.getString("command");
            Log.d(TAG, "onHandleIntent: command = " + command);

            //todo start work from here
            loadWeatherFromJson(context);
        }
    }
}