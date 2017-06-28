package com.abelkin.mostcatcher.controllers;

import android.content.Context;
import com.abelkin.mostcatcher.data.LoginsController;
import com.abelkin.mostcatcher.helpers.Md5;
import com.abelkin.mostcatcher.models.Login;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by abelkin on 28.06.2017.
 */
public class RestController {

    private Context mContext;

    public RestController(Context context){
        mContext = context;
    }

    private void badTry(Login login) {
        login.setBadTries(login.getBadTries() + 1);
        LoginsController loginsController = new LoginsController(mContext);
        loginsController.update(login);
    }

    private void goodTry(Login login) {
        login.setBadTries(0);
        LoginsController loginsController = new LoginsController(mContext);
        loginsController.update(login);
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
                jsessionid = cookie.substring(0, cookie.indexOf(';'));
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



}
