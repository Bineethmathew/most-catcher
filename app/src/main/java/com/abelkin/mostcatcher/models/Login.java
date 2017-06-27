package com.abelkin.mostcatcher.models;

/**
 * Created by abelkin on 27.06.2017.
 */
public class Login {

    int id;
    String login;
    String password;
    String phone;

    public Login() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
