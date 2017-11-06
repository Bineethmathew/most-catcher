package com.abelkin.mostcatcher.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    public void deleteUnused(View view) {
        final CitiesActivity this$ = this;

        new AlertDialog.Builder(view.getContext())
                .setTitle("Удалить неиспользуемые")
                .setMessage("Вы уверены, что хотите удалить неиспользуемые города из списка?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        (new CityController(this$)).deleteUnused();
                        readRecords();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }
}
