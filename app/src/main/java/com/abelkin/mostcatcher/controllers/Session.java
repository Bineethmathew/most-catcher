package com.abelkin.mostcatcher.controllers;

import com.abelkin.mostcatcher.models.Login;

/**
 * Created by abelkin on 28.06.2017.
 */
public class Session {

    private Login login;
    private String jsessionid;

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getJsessionid() {
        return jsessionid;
    }

    public void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    }
}
