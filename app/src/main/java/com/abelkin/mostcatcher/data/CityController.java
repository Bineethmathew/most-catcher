package com.abelkin.mostcatcher.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.abelkin.mostcatcher.models.City;

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
        city.setChecked(cursor.getInt(cursor.getColumnIndex("checked")));

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

        City city = null;

        String sql = "SELECT * FROM cities WHERE lower(trim(name)) = '" + cityName.toLowerCase().trim() + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        return cursor.getCount() > 0;

    }

    public boolean update(City city) {

        ContentValues values = new ContentValues();

        values.put("checked", city.getChecked());

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(city.getId()) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("cities", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;
    }

    private boolean create(City city) {

        ContentValues values = new ContentValues();

        values.put("name", city.getName());
        values.put("checked", 0);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("cities", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public String mergeCities(List<String> cityNames) {

        String result = "";

        for (String cityName : cityNames) {

            if (!exist(cityName)) {
                City city = new City();
                city.setName(cityName.trim());
                result += cityName + ", ";
            }

        }

        if (!result.isEmpty()) {
            result = result.substring(0, result.length() - 1);
        }

        return result;

    }
}
