package com.abelkin.mostcatcher.models;

/**
 * Created by abelkin on 02.07.2017.
 */
public class LoginSession {

    private Login login;
    private String session;

    public LoginSession() {
    }

    public LoginSession(Login login, String session) {
        this.login = login;
        this.session = session;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
