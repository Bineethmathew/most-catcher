package com.abelkin.mostcatcher.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.abelkin.mostcatcher.R;

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

        EditText editTextFrom = (EditText)findViewById(R.id.activity_time_from);
        EditText editTextTo = (EditText)findViewById(R.id.activity_time_to);

        editTextFrom.setText(String.valueOf(new Double(lowerBound) / 1000 / 60 / 60));
        editTextTo.setText(String.valueOf(new Double(upperBound) / 1000L / 60L / 60L));
    }

    public void savePreferences(View view) {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        EditText editTextFrom = (EditText)findViewById(R.id.activity_time_from);
        EditText editTextTo = (EditText)findViewById(R.id.activity_time_to);

        editor.putLong(getString(R.string.lower_bound), Math.round(Double.parseDouble(editTextFrom.getText().toString())) *
            1000L * 60L * 60L);
        editor.putLong(getString(R.string.upper_bound), Math.round(Double.parseDouble(editTextTo.getText().toString())) *
                1000L * 60L * 60L);
        editor.commit();

        finish();
    }
}
