package android.example.sunshineweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private String[] mWeatherData;
    private ForecastAdapterOnClickHandler mClickHandler=null;

    interface ForecastAdapterOnClickHandler
    {
        void onClick(String WeatherForDay);
    }

    public ForecastAdapter(ForecastAdapterOnClickHandler holder){
        mClickHandler=holder;
    }
    @NonNull
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this is the function which is equivalent to that in arrayAdapter where we used to inflate the view that we wanted
        // in the list.xml file. like group of textViews in a single item of list.xml file.
        Context context=parent.getContext();
        int layoutIdForListListen=R.layout.forecast_list_item;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view=inflater.inflate(layoutIdForListListen,parent,shouldAttachToParentImmediately);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastAdapter.ForecastAdapterViewHolder holder, int position) {
        String weatherToday=mWeatherData[position];
        holder.mWeatherTextView.setText(weatherToday);
    }

    @Override
    public int getItemCount() {
        if(mWeatherData==null)
        {
            return 0;
        }
        return mWeatherData.length;
    }

    public void setWeatherData(String[] WeatherData){
        mWeatherData=WeatherData;
        notifyDataSetChanged();
    }

    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final TextView mWeatherTextView;
        public ForecastAdapterViewHolder(View view)
        {
            super(view);
            mWeatherTextView=view.findViewById(R.id.tv_weather_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition=getAdapterPosition();
            String weatherForDay=mWeatherData[adapterPosition];
            mClickHandler.onClick(weatherForDay);
        }
    }
}
