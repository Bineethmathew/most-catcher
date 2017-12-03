package com.abelkin.mostcatcher.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.abelkin.mostcatcher.R;
import com.abelkin.mostcatcher.tasks.MainTask;

public class OptionsActivity extends AppCompatActivity {

    private Location mLocation;
    private Criteria criteria;
    private LocationListener locationListener;
    private LocationManager locationManager;
    // This is the Best And IMPORTANT part
    private Looper looper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        Long lowerBound = sharedPref.getLong(getString(R.string.lower_bound),
                0L);
        Long upperBound = sharedPref.getLong(getString(R.string.upper_bound),
                0L);
        Float period = sharedPref.getFloat(getString(R.string.period), 10.0f);
        Boolean getNewOrders = sharedPref.getBoolean(getString(R.string.new_orders), false);
        Boolean getFreeOrders = sharedPref.getBoolean(getString(R.string.free_orders), false);
        Double lat = (double)sharedPref.getFloat(getString(R.string.lat_pref), 58.00454500000001f);
        Double longi = (double)sharedPref.getFloat(getString(R.string.long_pref), 56.215320000000006f);

        EditText editTextFrom = (EditText)findViewById(R.id.activity_time_from);
        EditText editTextTo = (EditText)findViewById(R.id.activity_time_to);
        EditText editTextPeriod = (EditText)findViewById(R.id.activity_time_period);
        CheckBox checkBoxNew = (CheckBox)findViewById(R.id.activity_time_new);
        CheckBox checkBoxFree = (CheckBox)findViewById(R.id.activity_time_free);
        EditText editTextLat = (EditText)findViewById(R.id.activity_time_lat);
        EditText editTextLong = (EditText)findViewById(R.id.activity_time_long);

        editTextFrom.setText(String.valueOf(new Double(lowerBound) / 1000 / 60 / 60));
        editTextTo.setText(String.valueOf(new Double(upperBound) / 1000L / 60L / 60L));
        editTextPeriod.setText(String.valueOf(period));
        checkBoxNew.setChecked(getNewOrders);
        checkBoxFree.setChecked(getFreeOrders);
        editTextLat.setText(String.valueOf(lat));
        editTextLong.setText(String.valueOf(longi));

        final EditText editTextLat$ = editTextLat;
        final EditText editTextLong$ = editTextLong;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;
                Log.d("Location Changes", location.toString());
                editTextLat$.setText(String.valueOf(location.getLatitude()));
                editTextLong$.setText(String.valueOf(location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Status Changed", String.valueOf(status));
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Provider Enabled", provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Provider Disabled", provider);
            }
        };

        // Now first make a criteria with your requirements
        // this is done to save the battery life of the device
        // there are various other other criteria you can search for..
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

        // Now create a location manager
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

    }

    public void savePreferences(View view) {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        EditText editTextFrom = (EditText)findViewById(R.id.activity_time_from);
        EditText editTextTo = (EditText)findViewById(R.id.activity_time_to);
        EditText editTextPeriod = (EditText)findViewById(R.id.activity_time_period);
        CheckBox checkBoxNew = (CheckBox)findViewById(R.id.activity_time_new);
        CheckBox checkBoxFree = (CheckBox)findViewById(R.id.activity_time_free);
        EditText editTextLat = (EditText)findViewById(R.id.activity_time_lat);
        EditText editTextLong = (EditText)findViewById(R.id.activity_time_long);

        MainTask.LOWER_BOUND = Math.round(Double.parseDouble(editTextFrom.getText().toString())) *
                1000L * 60L * 60L;
        MainTask.UPPER_BOUND = Math.round(Double.parseDouble(editTextTo.getText().toString())) *
                1000L * 60L * 60L;
        MainTask.PERIOD = Float.parseFloat(editTextPeriod.getText().toString());
        MainTask.GET_NEW_ORDERS = checkBoxNew.isChecked();
        MainTask.GET_FREE_ORDERS = checkBoxFree.isChecked();
        MainTask.LATITUDE = Float.parseFloat(editTextLat.getText().toString());
        MainTask.LONGITUDE = Float.parseFloat(editTextLong.getText().toString());

        editor.putLong(getString(R.string.lower_bound), MainTask.LOWER_BOUND);
        editor.putLong(getString(R.string.upper_bound), MainTask.UPPER_BOUND);
        editor.putFloat(getString(R.string.period), MainTask.PERIOD);
        editor.putBoolean(getString(R.string.new_orders), MainTask.GET_NEW_ORDERS);
        editor.putBoolean(getString(R.string.free_orders), MainTask.GET_FREE_ORDERS);
        editor.putFloat(getString(R.string.lat_pref), MainTask.LATITUDE);
        editor.putFloat(getString(R.string.long_pref), MainTask.LONGITUDE);
        editor.commit();

        finish();
    }

    public void getGPS(View view) {
        locationManager.requestSingleUpdate(criteria, locationListener, looper);
    }
}
