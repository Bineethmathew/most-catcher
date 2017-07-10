package com.abelkin.mostcatcher.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by abelkin on 27.06.2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 10;
    public static final String DATABASE_NAME = "MostCatcherDatabase";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE logins " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "login TEXT, " +
                "password TEXT, " +
                "phone TEXT," +
                "bad_tries INT )";

        db.execSQL(sql);

        sql = "CREATE TABLE cities " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "from_checked INT," +
                "to_checked INT )";

        db.execSQL(sql);

        sql = "INSERT INTO logins (login, password, phone, bad_tries) VALUES ('2002', '423198', '79124818074', 0)";

        db.execSQL(sql);

        sql = "INSERT INTO cities (name, from_checked, to_checked) VALUES ('Ижевск', 0, 0)";
        db.execSQL(sql);

        sql = "INSERT INTO cities (name, from_checked, to_checked) VALUES ('Пермь', 0, 0)";
        db.execSQL(sql);

        sql = "INSERT INTO cities (name, from_checked, to_checked) VALUES ('Казань', 0, 0)";
        db.execSQL(sql);

        sql = "INSERT INTO cities (name, from_checked, to_checked) VALUES ('Набережные Челны', 0, 0)";
        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS logins";
        db.execSQL(sql);

        sql = "DROP TABLE IF EXISTS cities";

        db.execSQL(sql);

        onCreate(db);
    }
}
