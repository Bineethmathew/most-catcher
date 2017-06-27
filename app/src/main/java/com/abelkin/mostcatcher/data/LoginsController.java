package com.abelkin.mostcatcher.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.abelkin.mostcatcher.models.Login;

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
}
