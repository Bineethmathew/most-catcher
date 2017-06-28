package com.abelkin.mostcatcher.models;

/**
 * Created by abelkin on 28.06.2017.
 */
public class City {

    int id;
    String name;
    int checked;

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

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public String toString() {
        return name + ", есть фильтр: " + ((checked == 0) ? "нет" : "да");
    }
}
