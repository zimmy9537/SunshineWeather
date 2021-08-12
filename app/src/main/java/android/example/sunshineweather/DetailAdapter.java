package android.example.sunshineweather;

import android.content.Context;
import android.example.sunshineweather.utilities.SunshineWeatherUtils;
import android.example.sunshineweather.weatherAPI.Hour;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {

    private Context context;
    private ArrayList<Hour> hourList;
    private boolean isMetric;

    public DetailAdapter(Context context, ArrayList<Hour> hourList,boolean isMetric) {
        this.context = context;
        this.hourList = hourList;
        this.isMetric=isMetric;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.hour_detail_item, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        Hour hour=hourList.get(position);
        //set Time
        String time = hour.getTime().substring(11);
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(time);
            time = new SimpleDateFormat("K:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        holder.timeDetails.setText(time);
        holder.weatherType.setText(hour.getCondition().getText());
        holder.weatherTypeImage.setImageResource(SunshineWeatherUtils.getImageResource(hour.getIsDay(),hour.getCondition().getCode()));
        holder.temperature.setText(SunshineWeatherUtils.getTemperaturePerHour(hour,isMetric));
        holder.cloudPercent.setText(hour.getCloud()+"%");
    }

    @Override
    public int getItemCount() {
        return hourList.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        private TextView timeDetails;
        private TextView cloudPercent;
        private TextView temperature;
        private TextView weatherType;
        private ImageView weatherTypeImage;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            timeDetails = itemView.findViewById(R.id.time_detailItem);
            cloudPercent = itemView.findViewById(R.id.cloud_detailItem);
            temperature = itemView.findViewById(R.id.temperature_detailItem);
            weatherType = itemView.findViewById(R.id.weather_detailItem);
            weatherTypeImage = itemView.findViewById(R.id.weather_type_detailItem);
        }
    }
}
