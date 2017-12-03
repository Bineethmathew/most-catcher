package com.abelkin.mostcatcher.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.abelkin.mostcatcher.R;
import com.abelkin.mostcatcher.controllers.RestController;
import com.abelkin.mostcatcher.data.CityController;
import com.abelkin.mostcatcher.data.LoginController;
import com.abelkin.mostcatcher.models.City;
import com.abelkin.mostcatcher.models.Login;
import com.abelkin.mostcatcher.models.LoginSession;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by abelkin on 28.06.2017.
 */
public class MainTask extends AsyncTask<Void, String, Void> {

    private Context mContext;

    private static HashMap<String, City> cityHashMap;
    private static HashMap<String, LoginSession> loginSessionHashMap;

    public static Long LOWER_BOUND;
    public static Long UPPER_BOUND;
    public static Float PERIOD;
    public static Boolean GET_NEW_ORDERS;
    public static Boolean GET_FREE_ORDERS;
    public static Float LATITUDE;
    public static Float LONGITUDE;

    private static boolean error_shown = false;

    private Random random = new Random();
    private RestController restController;

    public MainTask(Context context){
        mContext = context;
        restController = new RestController(mContext);

        //let's get the cities right now
        CityController cityController = new CityController(context);
        List<City> cities = cityController.read();

        cityHashMap = new HashMap<>();
        for (City city : cities) {
            cityHashMap.put(city.getName(), city);
        }

        //let's get the logins
        LoginController loginController = new LoginController(context);
        List<Login> logins = loginController.readChecked();

        loginSessionHashMap = new HashMap<>();
        for (Login login : logins) {
            loginSessionHashMap.put(login.getLogin(), new LoginSession(login, null));
        }

        //and let's read preferences to work with
        SharedPreferences sharedPref = mContext.getSharedPreferences(
                mContext.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        LOWER_BOUND = sharedPref.getLong(mContext.getString(R.string.lower_bound),
                0L);
        UPPER_BOUND = sharedPref.getLong(mContext.getString(R.string.upper_bound),
                0L);
        PERIOD = sharedPref.getFloat(mContext.getString(R.string.period),
                1.0f);
        GET_NEW_ORDERS = sharedPref.getBoolean(mContext.getString(R.string.new_orders), false);
        GET_FREE_ORDERS = sharedPref.getBoolean(mContext.getString(R.string.free_orders), false);
        LATITUDE = sharedPref.getFloat(mContext.getString(R.string.lat_pref), 58.00454500000001f);
        LONGITUDE = sharedPref.getFloat(mContext.getString(R.string.long_pref), 56.215320000000006f);
    }

    public static HashMap<String, City> getCityHashMap() {
        return cityHashMap;
    }

    public static void updateCityHashMap(City city) {
        if (cityHashMap == null) {
            cityHashMap = new HashMap<>();
        }
        cityHashMap.put(city.getName(), city);
    }

    public static void putLoginToHashMap(Login login) {
        if (loginSessionHashMap == null) {
            loginSessionHashMap = new HashMap<>();
        }
        loginSessionHashMap.put(login.getLogin(), new LoginSession(login, null));
    }

    public static void removeLoginFromHashMap(Login login) {
        if (loginSessionHashMap != null) {
            loginSessionHashMap.remove(login.getLogin());
        }
        error_shown = false;
    }

    public static boolean getFromChecked(String cityName) {
        return cityHashMap.containsKey(cityName) &&
                cityHashMap.get(cityName).getFromChecked() == 1;
    }

    public static HashMap<String, LoginSession> getLoginSessionHashMap() {
        return loginSessionHashMap;
    }

    public static boolean getToChecked(String cityName) {
        return cityHashMap.containsKey(cityName) &&
                cityHashMap.get(cityName).getToChecked() == 1;
    }

    private LoginSession getRandomSession() {
        if (loginSessionHashMap.isEmpty()) {
            if (!error_shown) {
                publishProgress("Нужно создать хотя бы 1 логин");
                error_shown = true;
            }
            return null;
        }

        Object[] keys = loginSessionHashMap.keySet().toArray();
        String login = (String)keys[random.nextInt(keys.length)];
        LoginSession loginSession = loginSessionHashMap.get(login);

        if (loginSession.getSession() == null) {
            loginSession = restController.authorize(loginSession);
            if (loginSession == null) {
                publishProgress("Не удалось авторизоваться с логином " + login);
            } else {
                loginSessionHashMap.put(login, loginSession);
                publishProgress("Авторизовались с логином " + login);
            }
        }

        if (loginSession != null && loginSession.getSession() != null) {
            return loginSession;
        } else {
            return null;
        }


    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Date checkPoint = new Date();
            int i = 0;
            while (true) {

                LoginSession loginSession = getRandomSession();

                if (loginSession != null && loginSession.getSession() != null) {
                    //publishProgress("Интервал (мс): " + (new Date().getTime() - checkPoint.getTime()));
                    Log.i("com.abelkin.Response", "Интервал (мс): " + (new Date().getTime() - checkPoint.getTime()));
                    checkPoint = new Date();
                    String result = restController.processData(loginSession, GET_NEW_ORDERS, GET_FREE_ORDERS);
                    if (result != null && !result.isEmpty())
                    publishProgress(result);
                    // сессия пустая, если мы ничего не смогли получить, тогда и ждать не надо, надо получать новую сессию
                    if (loginSession.getSession() == null) {
                        continue;
                    }
                    TimeUnit.MILLISECONDS.sleep(Math.round(PERIOD * 1000L));
                } else {
                    TimeUnit.MILLISECONDS.sleep(1 * 1000L);
                }
                i++;
                if (i == 20) {
                    i = 0;
                    publishProgress("Сделано 20 запросов");
                }
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
