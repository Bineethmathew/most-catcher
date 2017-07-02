package com.abelkin.mostcatcher.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.abelkin.mostcatcher.models.City;
import com.abelkin.mostcatcher.tasks.MainTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abelkin on 28.06.2017.
 */
public class CityController extends DatabaseHandler {

    public CityController(Context context) {
        super(context);
    }

    private City readRecord(Cursor cursor) {
        City city = new City();

        city.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
        city.setName(cursor.getString(cursor.getColumnIndex("name")));
        city.setFromChecked(cursor.getInt(cursor.getColumnIndex("from_checked")));
        city.setToChecked(cursor.getInt(cursor.getColumnIndex("to_checked")));

        return city;
    }

    public List<City> read() {

        List<City> recordsList = new ArrayList<>();

        String sql = "SELECT * FROM cities ORDER BY name";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                City city= readRecord(cursor);
                recordsList.add(city);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public City readSingleRecord(int cityId) {

        City city = null;

        String sql = "SELECT * FROM cities WHERE id = " + cityId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            city = readRecord(cursor);

        }

        cursor.close();
        db.close();

        return city;

    }

    public boolean exist(String cityName) {

        if (MainTask.getCityHashMap() != null) {

            return MainTask.getCityHashMap().containsKey(cityName);

        } else {

            String sql = "SELECT * FROM cities WHERE name = ?";

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(sql, new String[]{cityName.trim()});

            return cursor.getCount() > 0;

        }

    }

    public boolean update(City city) {

        ContentValues values = new ContentValues();

        values.put("from_checked", city.getFromChecked());
        values.put("to_checked", city.getToChecked());

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(city.getId()) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("cities", values, where, whereArgs) > 0;
        db.close();

        MainTask.updateCityHashMap(city);

        return updateSuccessful;
    }

    private boolean create(City city) {

        ContentValues values = new ContentValues();

        values.put("name", city.getName());
        values.put("from_checked", 0);
        values.put("to_checked", 0);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("cities", null, values) > 0;
        db.close();

        MainTask.updateCityHashMap(city);

        return createSuccessful;
    }

    public synchronized String mergeCities(List<String> cityNames) {

        String result = "";

        for (String cityName : cityNames) {

            if (!exist(cityName)) {
                City city = new City();
                city.setName(cityName.trim());
                create(city);
                result += cityName + ", ";
            }

        }

        if (!result.isEmpty()) {
            result = result.substring(0, result.length() - 2);
        }

        return result;

    }
}
