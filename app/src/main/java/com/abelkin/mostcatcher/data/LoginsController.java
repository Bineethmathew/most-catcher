package com.abelkin.mostcatcher.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.abelkin.mostcatcher.models.Login;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abelkin on 27.06.2017.
 */
public class LoginsController extends DatabaseHandler {

    public LoginsController(Context context) {
        super(context);
    }

    public boolean create(Login login) {

        ContentValues values = new ContentValues();

        values.put("login", login.getLogin());
        values.put("password", login.getPassword());
        values.put("phone", login.getPhone());

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

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String login = cursor.getString(cursor.getColumnIndex("login"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));

                Login login1 = new Login();
                login1.setId(id);
                login1.setLogin(login);
                login1.setPassword(password);
                login1.setPhone(phone);

                recordsList.add(login1);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }
}
