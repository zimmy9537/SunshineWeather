package android.example.sunshineweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.example.sunshineweather.weatherAPI.Current;
import android.example.sunshineweather.weatherAPI.Forecast;
import android.example.sunshineweather.weatherAPI.Hour;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private Forecast forecast;
    private Current current;
    private boolean isMetric;
    private ArrayList<Hour> hourArrayList=new ArrayList<>();

    private TextView uv_details;
    private TextView sunriseDetails;
    private TextView sunsetDetails;
    private TextView windValueDetails;
    private TextView AQIValueDetails;
    private TextView humidityDetails;

    private ImageView back_details;
    private ScrollView scrollViewDetails;
    private ImageView cancelDetails;
    private LinearLayout cancelLinearLayout;
    private ProgressBar progressBarDetail;

    private RecyclerView recyclerViewHourDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        forecast = (Forecast) getIntent().getSerializableExtra("forecast");
        current = (Current) getIntent().getSerializableExtra("current");
        isMetric=getIntent().getBooleanExtra("isMetric",true);

        uv_details = findViewById(R.id.uv_valueText_details);
        sunriseDetails = findViewById(R.id.sunrise_valueText_details);
        sunsetDetails = findViewById(R.id.sunset_valueText_details);
        windValueDetails = findViewById(R.id.wind_valueText_details);
        AQIValueDetails = findViewById(R.id.AQI_valueText_details);
        humidityDetails = findViewById(R.id.humidity_valueText_details);

        cancelDetails = findViewById(R.id.cancel_detail);
        cancelLinearLayout = findViewById(R.id.cancel_ll_details);
        back_details = findViewById(R.id.back_details);
        scrollViewDetails = findViewById(R.id.scrollviewDetails);
        progressBarDetail = findViewById(R.id.progressbar_details);
        recyclerViewHourDetail=findViewById(R.id.weather_recyclerview_details);

        back_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
                finish();
            }
        });

        cancelDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelLinearLayout.setVisibility(View.GONE);
            }
        });
        loadDataIntoTheViews();
    }

    private void loadDataIntoTheViews() {
        progressBarDetail.setVisibility(View.VISIBLE);
        scrollViewDetails.setVisibility(View.INVISIBLE);

        String sunrise = forecast.getForecastday().get(0).getAstro().getSunrise();
        String sunset = forecast.getForecastday().get(0).getAstro().getSunset();
        Double uvIndex = current.getUv();
        Integer aqi = current.getAirQuality().getUsEpaIndex();
        Integer humidity = current.getHumidity();

        uv_details.setText(String.valueOf(uvIndex));
        sunriseDetails.setText(sunrise);
        sunsetDetails.setText(sunset);
        Double wind = current.getWindKph();
        windValueDetails.setText(String.valueOf(wind) + " km/h");
        AQIValueDetails.setText(String.valueOf(aqi));
        humidityDetails.setText(String.valueOf(humidity) + "%");

        hourArrayList.addAll(forecast.getForecastday().get(0).getHour());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailActivity.this);
        recyclerViewHourDetail.setLayoutManager(linearLayoutManager);
        recyclerViewHourDetail.setAdapter(new DetailAdapter(DetailActivity.this, hourArrayList,true));
        progressBarDetail.setVisibility(View.GONE);
        scrollViewDetails.setVisibility(View.VISIBLE);
    }
}