package com.abelkin.mostcatcher.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.abelkin.mostcatcher.models.Login;
import com.abelkin.mostcatcher.tasks.MainTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by abelkin on 27.06.2017.
 */
public class LoginController extends DatabaseHandler {

    private static Random random = new Random();

    public LoginController(Context context) {
        super(context);
    }

    public boolean create(Login login) {

        ContentValues values = new ContentValues();

        values.put("login", login.getLogin());
        values.put("password", login.getPassword());
        values.put("phone", login.getPhone());
        values.put("bad_tries", 0);
        values.put("checked", 1);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("logins", null, values) > 0;
        db.close();

        MainTask.putLoginToHashMap(login);

        return createSuccessful;
    }

    public List<Login> read() {

        List<Login> recordsList = new ArrayList<>();

        String sql = "SELECT * FROM logins ORDER BY login";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                Login login = readRecord(cursor);
                recordsList.add(login);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public List<Login> readChecked() {

        List<Login> recordsList = new ArrayList<>();

        String sql = "SELECT * FROM logins WHERE checked = 1 ORDER BY login";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                Login login = readRecord(cursor);
                recordsList.add(login);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public Login readSingleRecord(long loginId) {

        Login login = null;

        String sql = "SELECT * FROM logins WHERE id = " + loginId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            login = readRecord(cursor);

        }

        cursor.close();
        db.close();

        return login;

    }

    public boolean update(Login login) {

        ContentValues values = new ContentValues();

        values.put("login", login.getLogin());
        values.put("password", login.getPassword());
        values.put("phone", login.getPhone());
        values.put("bad_tries", login.getBadTries());
        values.put("checked", login.isChecked() ? 1 : 0);

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(login.getId()) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("logins", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;
    }

    public boolean delete(int id) {

        List<Login> logins = read();
        for (Login login : logins) {
            if (login.getId() == id) {
                if (MainTask.getLoginSessionHashMap() != null) {
                    MainTask.getLoginSessionHashMap().remove(login.getLogin());
                }
                break;
            }
        }

        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("logins", "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;

    }

    private Login readRecord(Cursor cursor) {
        Login login = new Login();

        login.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
        login.setLogin(cursor.getString(cursor.getColumnIndex("login")));
        login.setPassword(cursor.getString(cursor.getColumnIndex("password")));
        login.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
        login.setBadTries(cursor.getInt(cursor.getColumnIndex("bad_tries")));
        login.setChecked(cursor.getInt(cursor.getColumnIndex("checked")) == 1);

        return login;
    }
}
