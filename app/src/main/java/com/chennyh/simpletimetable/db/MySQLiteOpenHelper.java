package com.chennyh.simpletimetable.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "timeTable.db";

    public static final String USER_TABLE_NAME = "user";
    public static final String USER_COLUMN_ID = "_id";
    public static final String USER_COLUMN_EMAIL = "email";
    public static final String USER_COLUMN_USERNAME = "username";
    public static final String USER_COLUMN_PASSWORD = "password";

    public static final String COURSE_TABLE_NAME = "classInfo";
    public static final String COURSE_COLUMN_ID = "_id";
    public static final String COURSE_COLUMN_UID = "uid";
    public static final String COURSE_COLUMN_NAME = "name";
    public static final String COURSE_COLUMN_ROOM = "room";
    public static final String COURSE_COLUMN_TEACHER = "teacher";
    public static final String COURSE_COLUMN_WEEKLIST = "weekList";
    public static final String COURSE_COLUMN_START = "start";
    public static final String COURSE_COLUMN_STEP = "step";
    public static final String COURSE_COLUMN_DAY = "day";
    public static final String COURSE_COLUMN_COLOR = "color";
    public static final String COURSE_COLUMN_TIME = "time";

    public MySQLiteOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USER_TABLE_NAME
                + "(" + USER_COLUMN_ID + " integer primary key autoincrement, "
                + USER_COLUMN_USERNAME + " text not null, "
                + USER_COLUMN_PASSWORD + " text not null, "
                + USER_COLUMN_EMAIL + " text not null)");

        db.execSQL("create table " + COURSE_TABLE_NAME
                + "(" + COURSE_COLUMN_ID + " integer primary key autoincrement, "
                + COURSE_COLUMN_UID + " integer not null, "
                + COURSE_COLUMN_NAME + " text not null, "
                + COURSE_COLUMN_ROOM + " text not null, "
                + COURSE_COLUMN_TEACHER + " text not null, "
                + COURSE_COLUMN_WEEKLIST + " text not null, "
                + COURSE_COLUMN_START + " integer not null, "
                + COURSE_COLUMN_STEP + " integer not null, "
                + COURSE_COLUMN_DAY + " integer not null, "
                + COURSE_COLUMN_COLOR + " integer not null, "
                + COURSE_COLUMN_TIME + " text not null, "
                + "foreign key(" + COURSE_COLUMN_ID + ") references " + USER_TABLE_NAME + "(" + USER_COLUMN_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
