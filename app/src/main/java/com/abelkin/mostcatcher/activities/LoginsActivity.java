package com.abelkin.mostcatcher.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.abelkin.mostcatcher.R;
import com.abelkin.mostcatcher.activities.views.OnClickListenerCreateLogin;
import com.abelkin.mostcatcher.data.LoginsController;
import com.abelkin.mostcatcher.models.Login;

import java.util.List;

public class LoginsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logins);

        Button buttonCreateLogin = (Button)findViewById(R.id.activity_logins_create);
        buttonCreateLogin.setOnClickListener(new OnClickListenerCreateLogin());

        readRecords();
    }

    public void readRecords() {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.activity_logins_linear_layout);
        linearLayoutRecords.removeAllViews();

        List<Login> logins = new LoginsController(this).read();

        if (logins.size() > 0) {

            for (Login login : logins) {

                String textViewContents = login.getLogin() + " - " + login.getPhone();

                TextView textViewStudentItem= new TextView(this);
                textViewStudentItem.setPadding(0, 10, 0, 10);
                textViewStudentItem.setText(textViewContents);
                textViewStudentItem.setTag(Integer.toString(login.getId()));
                textViewStudentItem.setOnLongClickListener(new OnLongClickListenerLogin());

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
