package com.chennyh.simpletimetable.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.blankj.utilcode.util.EncryptUtils;
import com.chennyh.simpletimetable.entity.User;

public class UserDAO {

    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase db;
    private ContentValues values;

    public UserDAO(Context context) {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
    }

    public boolean registerUser(User user) {
        db = mySQLiteOpenHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(MySQLiteOpenHelper.USER_TABLE_USERNAME, user.getUsername());
        values.put(MySQLiteOpenHelper.USER_TABLE_EMAIL, user.getEmail());
        values.put(MySQLiteOpenHelper.USER_TABLE_PASSWORD, EncryptUtils.encryptSHA256ToString(user.getPassword()));

        if (db.insert(MySQLiteOpenHelper.USER_TABLE_NAME, null, values) != -1) {
            return true;
        }
        return false;
    }

    public boolean loginUser(User user) {
        db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(MySQLiteOpenHelper.USER_TABLE_NAME, null, MySQLiteOpenHelper.USER_TABLE_EMAIL + "=?", new String[]{user.getEmail()}, null, null, null);
        if (cursor.getCount()>=1) {
            cursor.moveToFirst();
            if (user.getEmail().equals(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.USER_TABLE_EMAIL)))
                    && EncryptUtils.encryptSHA256ToString(user.getPassword()).equals(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.USER_TABLE_PASSWORD)))) {
                return true;
            }
        }
        return false;
    }

    public boolean emailExists(String email) {
        db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(MySQLiteOpenHelper.USER_TABLE_NAME, null, MySQLiteOpenHelper.USER_TABLE_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor.getCount()>=1) {
            return true;
        }
        return false;
    }

    public User getUserInfo(String email) {
        db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(MySQLiteOpenHelper.USER_TABLE_NAME, null, MySQLiteOpenHelper.USER_TABLE_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor.getCount()>=1) {
            cursor.moveToFirst();
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.USER_TABLE_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.USER_TABLE_USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.USER_TABLE_EMAIL)));
            return user;
        }
        return null;
    }
}
