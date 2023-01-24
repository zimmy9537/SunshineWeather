package android.example.sunshineweather;

import android.content.Context;
import android.example.sunshineweather.utilities.SunshineDateUtils;
import android.example.sunshineweather.utilities.SunshineWeatherUtils;
import android.example.sunshineweather.weatherAPI.apidata.Forecastday;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>{

    private Context context;
    private boolean isMetric;
    private List<Forecastday> forecastDaysList;

    public WeatherAdapter(Context context, List<Forecastday> forecastDays,boolean isMetric) {
        this.context = context;
        this.forecastDaysList = forecastDays;
        this.isMetric=isMetric;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.forecast_list_item,parent,false);
        return new WeatherViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Forecastday forecastday=forecastDaysList.get(position);
        String PARSE_STRING=forecastday.getDate()+" 09:30:00";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date=simpleDateFormat.parse(PARSE_STRING);
            Long millis=date.getTime();
            String display= SunshineDateUtils.getDayName(context,millis);
            holder.dayOfTheWeek.setText(display);

            //temp
            String maxTemp=String.valueOf(forecastday.getDay().getMaxtempC());
            String minTemp=String.valueOf(forecastday.getDay().getMintempC());
            holder.minimumTemperature.setText(SunshineWeatherUtils.getTemperatureMinimum(forecastday.getDay(),isMetric));
            holder.maximumTemperature.setText(SunshineWeatherUtils.getTemperatureMaximum(forecastday.getDay(),isMetric));

            String weatherType=forecastday.getDay().getCondition().getText();//cloudy
            holder.weatherType.setText(weatherType);
            holder.weatherImage.setImageResource(SunshineWeatherUtils.getImageResource(1,forecastday.getDay().getCondition().getCode()));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(MainActivity.class.getSimpleName(),"we fucked up here"+e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return forecastDaysList.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {

        private TextView dayOfTheWeek;
        private ImageView weatherImage;
        private TextView weatherType;
        private TextView maximumTemperature;
        private TextView minimumTemperature;
        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfTheWeek=itemView.findViewById(R.id.day_of_the_week);
            weatherImage=itemView.findViewById(R.id.weather_type_image);
            weatherType=itemView.findViewById(R.id.weather_type);
            maximumTemperature=itemView.findViewById(R.id.maximum_of_the_day);
            minimumTemperature=itemView.findViewById(R.id.minimum_of_the_day);
        }
    }
}
