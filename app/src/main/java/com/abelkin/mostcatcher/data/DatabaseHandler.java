package com.abelkin.mostcatcher.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by abelkin on 27.06.2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 4;
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
                "bad_tries INT ); " +
                "CREATE TABLE cities " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "checked INT ); ";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS logins; " +
                "DROP TABLE IF EXISTS cities;";
        db.execSQL(sql);

        onCreate(db);
    }
}
