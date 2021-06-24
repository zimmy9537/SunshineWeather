package android.example.sunshineweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.example.sunshineweather.data.SunshinePreferences;
import android.example.sunshineweather.utilities.NetworkUtils;
import android.example.sunshineweather.utilities.OpenWeatherJsonUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.ForecastAdapterOnClickHandler {

    private static final String TAG= MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private TextView errorTextView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        mRecyclerView=findViewById(R.id.recyclerview_forecast);
        errorTextView=findViewById(R.id.eror_text);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);//so that every element of the list is of same size.
        mForecastAdapter=new ForecastAdapter(this);
        mRecyclerView.setAdapter(mForecastAdapter);
        progressBar=findViewById(R.id.progress);
        loadWeatherData();
    }
    private void loadWeatherData() {
        showWeatherData();
        String userLocation= SunshinePreferences.getPreferredWeatherLocation(this);
        new sunshineAyncTsak().execute(userLocation);
    }

    private void showWeatherData(){
        errorTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(String WeatherForDay) {
        Intent intent=new Intent(MainActivity.this,DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,WeatherForDay);
        startActivity(intent);
    }

    class sunshineAyncTsak extends AsyncTask<String,Void,String[]>
    {
        // for an asyncTask we have 3 generic Types.
        //first one is params :- the type of parameter that the asyncTask takes upon execution. here String.
        //second one is progress :- the type of Progress units published during the background computation. here it is Void.
        //third one is result :- the type of type of result produced by the backGround Computation. here it is string[].


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... strings) {
            if(strings.length==0)
            {
                return null;
            }
            String location=strings[0];
            URL weatherRequestUrl= NetworkUtils.buildUrl(location);
            try {
                String jsonWeatherResponse=NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                String[] simpleJsonData= OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this,jsonWeatherResponse);
                return simpleJsonData;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            progressBar.setVisibility(View.INVISIBLE);
            if(weatherData!=null)
            {
                showWeatherData();
                mForecastAdapter.setWeatherData(weatherData);
            }
            else {
               showErrorMessage();
            }
        }
        private void openLocationInMap()
        {
            String addressString="1600 Amphitheatre ParkWay, CA";
            Uri geoLocation=Uri.parse("geo:0,0?q="+addressString);

            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(geoLocation);

            if(intent.resolveActivity(getPackageManager())!=null)
            {
                startActivity(intent);
            }
            else
            {
                Log.d(TAG,"couldn't call "+geoLocation.toString()+", no receiving apps installed!");
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemThatWasClicked=item.getItemId();
        if(itemThatWasClicked==R.id.action_Refresh)
        {
            mForecastAdapter=null;
            loadWeatherData();
            return true;
        }
        if(itemThatWasClicked==R.id.action_map)
        {

        }
        return super.onOptionsItemSelected(item);
    }
}