package com.abelkin.mostcatcher.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.abelkin.mostcatcher.activities.LoginsActivity;
import com.abelkin.mostcatcher.controllers.RestController;
import com.abelkin.mostcatcher.controllers.Session;
import com.abelkin.mostcatcher.data.LoginsController;
import com.abelkin.mostcatcher.models.Login;

import java.util.concurrent.TimeUnit;

/**
 * Created by abelkin on 28.06.2017.
 */
public class MainTask extends AsyncTask<Integer, String, Void> {

    private Context mContext;

    public MainTask(Context context){
        mContext = context;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        try {
            boolean errorShown = false;
            while (true) {
                RestController restController = new RestController(mContext);

                LoginsController loginsController = new LoginsController(mContext);
                Login login = loginsController.readRandomRecord();
                if (login == null) {
                    if (!errorShown) {
                        publishProgress("Нужно создать хотя бы 1 логин");
                        errorShown = true;
                    }
                } else {
                    errorShown = false;
                    Session session = restController.authorize(login, mContext);

                    if (session != null) {
                        publishProgress("Авторизовались");
                    } else {
                        publishProgress("Не удалось авторизоваться");
                    }
                }

                TimeUnit.SECONDS.sleep(params[0]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Toast.makeText(mContext, values[0], Toast.LENGTH_SHORT).show();
    }

}
