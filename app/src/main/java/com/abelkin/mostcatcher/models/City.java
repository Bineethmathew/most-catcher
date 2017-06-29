package com.abelkin.mostcatcher.models;

/**
 * Created by abelkin on 28.06.2017.
 */
public class City {

    int id;
    String name;
    int fromChecked;
    int toChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFromChecked() {
        return fromChecked;
    }

    public void setFromChecked(int fromChecked) {
        this.fromChecked = fromChecked;
    }

    public int getToChecked() {
        return toChecked;
    }

    public void setToChecked(int toChecked) {
        this.toChecked = toChecked;
    }

    public String toString() {
        return name + ", брать ИЗ: " + ((fromChecked == 0) ? "нет" : "да") +
                " брать В: " + ((toChecked == 0) ? "нет" : "да");
    }
}
