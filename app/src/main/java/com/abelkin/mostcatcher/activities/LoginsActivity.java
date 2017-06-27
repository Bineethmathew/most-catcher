package com.abelkin.mostcatcher.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import com.abelkin.mostcatcher.R;
import com.abelkin.mostcatcher.activities.views.OnClickListenerCreateLogin;

public class LoginsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logins);

        Button buttonCreateLogin = (Button)findViewById(R.id.activity_logins_create);
        buttonCreateLogin.setOnClickListener(new OnClickListenerCreateLogin());
    }
}
