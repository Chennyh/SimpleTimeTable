package com.chennyh.simpletimetable.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.chennyh.simpletimetable.constants.DatabaseConstants;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "timeTable.db";

    public MySQLiteOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DatabaseConstants.USER_TABLE_NAME
                + "(" + DatabaseConstants.USER_COLUMN_ID + " integer primary key autoincrement, "
                + DatabaseConstants.USER_COLUMN_USERNAME + " text not null, "
                + DatabaseConstants.USER_COLUMN_PASSWORD + " text not null, "
                + DatabaseConstants.USER_COLUMN_EMAIL + " text not null)");

        db.execSQL("create table " + DatabaseConstants.COURSE_TABLE_NAME
                + "(" + DatabaseConstants.COURSE_COLUMN_ID + " integer primary key autoincrement, "
                + DatabaseConstants.COURSE_COLUMN_UID + " integer not null, "
                + DatabaseConstants.COURSE_COLUMN_NAME + " text, "
                + DatabaseConstants.COURSE_COLUMN_ROOM + " text, "
                + DatabaseConstants.COURSE_COLUMN_TEACHER + " text, "
                + DatabaseConstants.COURSE_COLUMN_WEEKLIST + " text, "
                + DatabaseConstants.COURSE_COLUMN_START + " integer, "
                + DatabaseConstants.COURSE_COLUMN_STEP + " integer, "
                + DatabaseConstants.COURSE_COLUMN_DAY + " integer, "
                + DatabaseConstants.COURSE_COLUMN_COLOR + " integer, "
                + DatabaseConstants.COURSE_COLUMN_TIME + " text, "
                + "foreign key(" + DatabaseConstants.COURSE_COLUMN_ID + ") references " + DatabaseConstants.USER_TABLE_NAME + "(" + DatabaseConstants.USER_COLUMN_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
