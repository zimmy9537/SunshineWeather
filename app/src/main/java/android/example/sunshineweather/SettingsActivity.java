package android.example.sunshineweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar settingsToolbar;
    private ImageView backHit;

    private LinearLayout location;
    private LinearLayout temperatureUnits;

    private TextView location_text;
    private TextView temperatureUnits_text;

    private String METRIC = "Metric";
    private String IMPERIAL = "Imperial";

    private boolean isMetric;

    SharedPreferences sharedPreferences;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String location_preference = "locationKey";
    public static final String unit_preference = "UnitsKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settingsToolbar = findViewById(R.id.toolbar_settings);
        backHit = findViewById(R.id.back);
        location = findViewById(R.id.location_ll_settings);
        temperatureUnits = findViewById(R.id.temperatureUnits_ll);
        location_text = findViewById(R.id.location_text);
        temperatureUnits_text = findViewById(R.id.temperatureUnits_text);

        setSupportActionBar(settingsToolbar);//remember that when you create toolbar it is completely androidx type.
        getSupportActionBar().setDisplayShowTitleEnabled(false);//this is written so that extra name of the toolbar is not shown up.


        //back to home screen
        backHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //this shared preference is for retrieving the data;
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String location_saved = sharedPreferences.getString(location_preference, "Jamnagar");
        boolean units_saved = sharedPreferences.getBoolean(unit_preference, true);
        if (units_saved) {
            temperatureUnits_text.setText(METRIC);
        } else {
            temperatureUnits_text.setText(IMPERIAL);
        }
        location_text.setText(location_saved);


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                class CustomLocationDialog extends Dialog {
                    public EditText locationEditText;
                    public Button saveButton;

                    public CustomLocationDialog(@NonNull Context context) {
                        super(context);
                    }

                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        requestWindowFeature(Window.FEATURE_NO_TITLE);
                        setContentView(R.layout.location_item);

                        locationEditText = findViewById(R.id.location_editText);
                        saveButton = findViewById(R.id.location_save_button);
                    }
                }
                CustomLocationDialog customLocationDialog = new CustomLocationDialog(SettingsActivity.this);
                customLocationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customLocationDialog.show();
                customLocationDialog.saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(customLocationDialog.locationEditText.getText().toString().trim())) {
                            location_text.setText(customLocationDialog.locationEditText.getText().toString().trim());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(location_preference, customLocationDialog.locationEditText.getText().toString().trim());
                            editor.apply();
                            Toast.makeText(SettingsActivity.this, "settings updated!", Toast.LENGTH_SHORT).show();
                            customLocationDialog.dismiss();
                        } else {
                            Toast.makeText(SettingsActivity.this, "enter proper location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        temperatureUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                class CustomTemperatureUnitsDialog extends Dialog {
                    public RadioButton metric;
                    public RadioButton imperial;
                    public RadioGroup radioGroup;

                    public CustomTemperatureUnitsDialog(@NonNull Context context) {
                        super(context);
                    }

                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        requestWindowFeature(Window.FEATURE_NO_TITLE);
                        setContentView(R.layout.temperature_units_item);
                        radioGroup = findViewById(R.id.units_rg);

                        metric = findViewById(R.id.metric_rb);
                        imperial = findViewById(R.id.imperial_rb);
                    }
                }
                CustomTemperatureUnitsDialog customTemperatureUnitsDialog = new CustomTemperatureUnitsDialog(SettingsActivity.this);
                customTemperatureUnitsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customTemperatureUnitsDialog.show();
                customTemperatureUnitsDialog.radioGroup.clearCheck();

                customTemperatureUnitsDialog.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (checkedId == R.id.metric_rb) {
                            temperatureUnits_text.setText(METRIC);
                            isMetric = true;
                        } else {
                            temperatureUnits_text.setText(IMPERIAL);
                            isMetric = false;
                        }
                        editor.putBoolean(unit_preference, isMetric);
                        editor.apply();
                        customTemperatureUnitsDialog.dismiss();
                    }
                });

            }
        });
    }
}