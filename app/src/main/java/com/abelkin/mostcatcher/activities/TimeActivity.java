package com.abelkin.mostcatcher.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.abelkin.mostcatcher.R;
import com.abelkin.mostcatcher.tasks.MainTask;

public class TimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        Long lowerBound = sharedPref.getLong(getString(R.string.lower_bound),
                0L);
        Long upperBound = sharedPref.getLong(getString(R.string.upper_bound),
                0L);
        Float period = sharedPref.getFloat(getString(R.string.period), 1.0f);

        EditText editTextFrom = (EditText)findViewById(R.id.activity_time_from);
        EditText editTextTo = (EditText)findViewById(R.id.activity_time_to);
        EditText editTextPeriod = (EditText)findViewById(R.id.activity_time_period);

        editTextFrom.setText(String.valueOf(new Double(lowerBound) / 1000 / 60 / 60));
        editTextTo.setText(String.valueOf(new Double(upperBound) / 1000L / 60L / 60L));
        editTextPeriod.setText(String.valueOf(period));
    }

    public void savePreferences(View view) {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        EditText editTextFrom = (EditText)findViewById(R.id.activity_time_from);
        EditText editTextTo = (EditText)findViewById(R.id.activity_time_to);
        EditText editTextPeriod = (EditText)findViewById(R.id.activity_time_period);

        MainTask.LOWER_BOUND = Math.round(Double.parseDouble(editTextFrom.getText().toString())) *
                1000L * 60L * 60L;
        MainTask.UPPER_BOUND = Math.round(Double.parseDouble(editTextTo.getText().toString())) *
                1000L * 60L * 60L;
        MainTask.PERIOD = Float.parseFloat(editTextPeriod.getText().toString());

        editor.putLong(getString(R.string.lower_bound), MainTask.LOWER_BOUND);
        editor.putLong(getString(R.string.upper_bound), MainTask.UPPER_BOUND);
        editor.putFloat(getString(R.string.period), MainTask.PERIOD);
        editor.commit();



        finish();
    }
}
