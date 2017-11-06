package com.abelkin.mostcatcher.models;

/**
 * Created by abelkin on 27.06.2017.
 */
public class Login {

    int id;
    String login;
    String password;
    String phone;
    int badTries;
    boolean checked;

    public Login() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getBadTries() {
        return badTries;
    }

    public void setBadTries(int badTries) {
        this.badTries = badTries;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String toString() {
        return login + " неудачных попыток: " + badTries;
    }
}
