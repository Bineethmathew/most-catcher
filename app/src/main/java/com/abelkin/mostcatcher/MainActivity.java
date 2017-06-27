package com.abelkin.mostcatcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.abelkin.mostcatcher.activities.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openLogins(View view) {
        Intent intent = new Intent(this, LoginsActivity.class);
        startActivity(intent);
    }

    public void openCities(View view) {
        Intent intent = new Intent(this, CitiesActivity.class);
        startActivity(intent);
    }

    public void openTime(View view) {
        Intent intent = new Intent(this, TimeActivity.class);
        startActivity(intent);
    }
}
