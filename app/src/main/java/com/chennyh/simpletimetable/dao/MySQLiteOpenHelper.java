package com.chennyh.simpletimetable.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "timeTable.db";
    public static final String USER_TABLE_NAME = "user";
    public static final String USER_TABLE_ID = "_id";
    public static final String USER_TABLE_EMAIL = "email";
    public static final String USER_TABLE_USERNAME = "username";
    public static final String USER_TABLE_PASSWORD = "password";

    public MySQLiteOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USER_TABLE_NAME
                + "(" + USER_TABLE_ID + " integer primary key autoincrement, "
                + USER_TABLE_USERNAME + " text not null, "
                + USER_TABLE_PASSWORD + " text not null, "
                + USER_TABLE_EMAIL + " text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
