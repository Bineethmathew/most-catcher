package com.abelkin.mostcatcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.abelkin.mostcatcher.activities.*;
import com.abelkin.mostcatcher.tasks.MainTask;

public class MainActivity extends AppCompatActivity {

    MainTask mainTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTask = new MainTask(getApplicationContext());
        mainTask.execute();
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
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }
}
