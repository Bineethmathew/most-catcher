package com.abelkin.mostcatcher.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.abelkin.mostcatcher.R;
import com.abelkin.mostcatcher.data.CityController;
import com.abelkin.mostcatcher.models.City;

import java.util.List;

public class CitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        readRecords();
    }

    public void readRecords() {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.activity_cities_linear_layout);
        linearLayoutRecords.removeAllViews();

        List<City> cities = new CityController(this).read();

        if (cities.size() > 0) {

            for (City city : cities) {

                TextView textViewStudentItem= new TextView(this);
                textViewStudentItem.setPadding(0, 10, 0, 10);
                textViewStudentItem.setText(city.toString());
                textViewStudentItem.setTag(Integer.toString(city.getId()));
                textViewStudentItem.setOnLongClickListener(new OnLongClickListenerCity());

                linearLayoutRecords.addView(textViewStudentItem);
            }

        }

        else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("Пока нет записей.");

            linearLayoutRecords.addView(locationItem);
        }

    }
}
