package com.abelkin.mostcatcher.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.abelkin.mostcatcher.MainActivity;
import com.abelkin.mostcatcher.R;
import com.abelkin.mostcatcher.activities.views.OnClickListenerCreateLogin;
import com.abelkin.mostcatcher.data.LoginController;
import com.abelkin.mostcatcher.models.Login;
import com.abelkin.mostcatcher.tasks.MainTask;
import com.abelkin.mostcatcher.util.LoginAdapter;

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

        ListView listView = (ListView)findViewById(R.id.activity_logins_list_view);
        //listView.removeAllViews();
        final LoginController loginController = new LoginController(this);

        final List<Login> logins = loginController.read();

        if (logins.size() > 0) {

            final LoginAdapter loginAdapter = new LoginAdapter(logins, getApplicationContext());
            listView.setAdapter(loginAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Login login = logins.get(position);
                    login.setChecked(!login.isChecked());
                    loginAdapter.notifyDataSetChanged();
                    loginController.update(login);
                    if (login.isChecked()) {
                        MainTask.putLoginToHashMap(login);
                    } else {
                        MainTask.removeLoginFromHashMap(login);
                    }
                }
            });

            /*for (Login login : logins) {

                TextView textViewStudentItem= new TextView(this);
                textViewStudentItem.setPadding(0, 10, 0, 10);
                textViewStudentItem.setText(login.toString());
                textViewStudentItem.setTag(Integer.toString(login.getId()));
                textViewStudentItem.setOnLongClickListener(new OnLongClickListenerLogin());

                listView.addView(textViewStudentItem);
            }*/

        }
        /*else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("Пока нет записей.");

            listView.addView(locationItem);
        }*/

    }


}
