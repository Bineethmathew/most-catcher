package com.abelkin.mostcatcher.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.abelkin.mostcatcher.models.Login;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by abelkin on 27.06.2017.
 */
public class LoginsController extends DatabaseHandler {

    private static Random random = new Random();

    public LoginsController(Context context) {
        super(context);
    }

    public boolean create(Login login) {

        ContentValues values = new ContentValues();

        values.put("login", login.getLogin());
        values.put("password", login.getPassword());
        values.put("phone", login.getPhone());
        values.put("bad_tries", 0);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("logins", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public List<Login> read() {

        List<Login> recordsList = new ArrayList<>();

        String sql = "SELECT * FROM logins ORDER BY login DESC";

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

    public Login readSingleRecord(int loginId) {

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

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(login.getId()) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("logins", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;
    }

    public boolean delete(int id) {
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

        return login;
    }

    public Login readRandomRecord() {

        Login login = null;

        String sql = "SELECT * FROM logins";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() == 0) {
            return null;
        }

        int position = random.nextInt(cursor.getCount());

        if (cursor.moveToPosition(position)) {

            login = readRecord(cursor);

        }

        cursor.close();
        db.close();

        return login;

    }
}
