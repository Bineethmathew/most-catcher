package com.abelkin.mostcatcher.controllers;

import android.content.Context;
import com.abelkin.mostcatcher.data.CityController;
import com.abelkin.mostcatcher.data.LoginController;
import com.abelkin.mostcatcher.helpers.Md5;
import com.abelkin.mostcatcher.models.Login;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by abelkin on 28.06.2017.
 */
public class RestController {

    private Context mContext;

    private static final MediaType JSON
            = MediaType.parse("application/json");

    public RestController(Context context){
        mContext = context;
    }

    private void badTry(Login login) {
        login.setBadTries(login.getBadTries() + 1);
        LoginController loginController = new LoginController(mContext);
        loginController.update(login);
    }

    private void goodTry(Login login) {
        login.setBadTries(0);
        LoginController loginController = new LoginController(mContext);
        loginController.update(login);
    }

    public Session authorize(Login login) {

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder;

        try {
            urlBuilder = HttpUrl.parse("http://most.373soft.ru/bridge-1.1/ws/driverServices/authorization")
                    .newBuilder();
        } catch (Exception e) {
            return null;
        }

        urlBuilder.addQueryParameter("contractNumber", login.getLogin());
        urlBuilder.addQueryParameter("password", Md5.md5(login.getPassword()));
        urlBuilder.addQueryParameter("phone", login.getPhone());

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .header("Accept", "application/json, text/javascript; q=0.9")
                .header("User-Agent", "Android::4.4.2::19::MDR::IQ4502 Quad::Fly_Era_Energy_1::terminal_bridge::1.0.17::17")
                .url(url)
                .build();

        Response response = null;

        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            badTry(login);
            return null;
        }

        String jsessionid = "";

        if (response != null && response.code() == 200) {
            try {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                if (!jsonObject.getBoolean("isSuccess")) {
                    badTry(login);
                    return null;
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                badTry(login);
                return null;
            }

            String cookie = response.header("Set-Cookie");
            try {
                jsessionid = cookie.substring(0, cookie.indexOf(';')+1);
            } catch (Exception e) {

            }

            if (jsessionid.isEmpty()) {
                badTry(login);
                return null;
            }


        } else {
            badTry(login);
            return null;
        }

        goodTry(login);
        Session session = new Session();
        session.setLogin(login);
        session.setJsessionid(jsessionid);

        return session;

    }

    public String processData(Session session) {

        String result = "";

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder;

        try {
            urlBuilder = HttpUrl.parse("http://most.373soft.ru/bridge-1.1/ws/driverServices/putGpsData")
                    .newBuilder();
        } catch (Exception e) {
            return "Ошибка при получении заказов";
        }

        urlBuilder.addQueryParameter("needAvailableOrders", "true");
        urlBuilder.addQueryParameter("needCurrentOrder", "false");
        urlBuilder.addQueryParameter("needReservationOrdersForEndPoint", "false");

        String url = urlBuilder.build().toString();

        RequestBody body = RequestBody.create(JSON, "{\"datetime\":" +
                new Date().getTime() + ",\"isTaximeterOn\":false,\"latitude\":58.00454500000001,\"longitude\"" +
                ":56.215320000000006,\"speed\":21.32988224029541,\"course\":65}");

        Request request = new Request.Builder()
                .header("Accept", "application/json, text/javascript; q=0.9")
                .header("User-Agent", "Android::4.4.2::19::MDR::IQ4502 Quad::Fly_Era_Energy_1::terminal_bridge::1.0.17::17")
                .header("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
                .header("Content-Type", "application/json")
                .header("Cookie", session.getJsessionid())
                .url(url)
                .post(body)
                .build();

        Response response = null;

        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            return "Ошибка при получении заказов";
        }

        if (response != null && response.code() == 200) {
            try {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);

                JSONArray orders = jsonObject.getJSONArray("reservationOrders");

                List<String> cityNames = new ArrayList<String>();

                for (int i = 0; i < orders.length(); i++) {
                    JSONObject order = orders.getJSONObject(i);
                    JSONObject addressFrom = order.getJSONObject("addressFrom");
                    String cityName = addressFrom.getString("city");
                    int position = cityName.indexOf(' ');
                    int position2 = cityName.indexOf(',');
                    if (position2 != -1 && position2 < position) {
                        position = position2;
                    }
                    if (position != -1) {
                        cityName = cityName.substring(0, position);
                    }
                    cityNames.add(cityName);
                }

                CityController cityController = new CityController(mContext);
                result = "Добавлены города: " + cityController.mergeCities(cityNames);

            } catch (Exception e) {
                return "Ошибка при получении заказов";
            }


        } else {
            return "Ошибка при получении заказов";
        }

        return result;

    }


}