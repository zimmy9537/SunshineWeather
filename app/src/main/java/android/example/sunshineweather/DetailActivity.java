package android.example.sunshineweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

public class DetailActivity extends AppCompatActivity {
    private String printData;
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details,menu);
        MenuItem menuItem=menu.findItem(R.id.share_weather_details);
        menuItem.setIntent(CreateShareForecastIntent());
        return true;
    }
    private Intent CreateShareForecastIntent()
    {
        ShareCompat.IntentBuilder intentBuilder=new ShareCompat.IntentBuilder(this);
        intentBuilder.setType("text/plain").setText(printData+FORECAST_SHARE_HASHTAG);
        return intentBuilder.getIntent();
        //this is the only way to fetch intent currently.
    }
    private TextView printThis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        printThis=findViewById(R.id.print_this);
        printData="";
        Intent intentThatWeDerived=getIntent();
        if(intentThatWeDerived!=null) {
            if (intentThatWeDerived.hasExtra(Intent.EXTRA_TEXT)) {
                printData = intentThatWeDerived.getStringExtra(Intent.EXTRA_TEXT);
                printThis.setText(printData);
            }
        }
    }
}
