package com.chennyh.simpletimetable.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.blankj.utilcode.util.EncryptUtils;
import com.chennyh.simpletimetable.bean.User;
import com.chennyh.simpletimetable.constants.DatabaseConstants;

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
        values.put(DatabaseConstants.USER_COLUMN_USERNAME, user.getUsername());
        values.put(DatabaseConstants.USER_COLUMN_EMAIL, user.getEmail());
        values.put(DatabaseConstants.USER_COLUMN_PASSWORD, EncryptUtils.encryptSHA256ToString(user.getPassword()));

        if (db.insert(DatabaseConstants.USER_TABLE_NAME, null, values) != -1) {
            return true;
        }
        return false;
    }

    public boolean loginUser(User user) {
        db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseConstants.USER_TABLE_NAME, null, DatabaseConstants.USER_COLUMN_USERNAME + "=?", new String[]{user.getUsername()}, null, null, null);
        if (cursor.getCount()>=1) {
            cursor.moveToFirst();
            if (user.getUsername().equals(cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER_COLUMN_USERNAME)))
                    && EncryptUtils.encryptSHA256ToString(user.getPassword()).equals(cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER_COLUMN_PASSWORD)))) {
                return true;
            }
        }
        return false;
    }

    public boolean emailExists(String email) {
        db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseConstants.USER_TABLE_NAME, null, DatabaseConstants.USER_COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor.getCount()>=1) {
            return true;
        }
        return false;
    }

    public User getUserInfo(String email) {
        db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseConstants.USER_TABLE_NAME, null, DatabaseConstants.USER_COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor.getCount()>=1) {
            cursor.moveToFirst();
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.USER_COLUMN_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER_COLUMN_USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER_COLUMN_EMAIL)));
            return user;
        }
        return null;
    }

    public boolean matchPassword(User user) {
        db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseConstants.USER_TABLE_NAME, null, DatabaseConstants.USER_COLUMN_EMAIL + "=?", new String[]{user.getEmail()}, null, null, null);
        if (cursor.getCount()>=1) {
            cursor.moveToFirst();
            if (cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER_COLUMN_PASSWORD)).equals(EncryptUtils.encryptSHA256ToString(user.getPassword()))) {
                return true;
            }
        }
        return false;
    }

    public boolean changePassword(User user) {
        db = mySQLiteOpenHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(DatabaseConstants.USER_COLUMN_PASSWORD, EncryptUtils.encryptSHA256ToString(user.getPassword()));
        if (db.update(DatabaseConstants.USER_TABLE_NAME, values, DatabaseConstants.USER_COLUMN_EMAIL + "=?", new String[]{user.getEmail()})>=1) {
            return true;
        }
        return false;
    }

    public int queyID(String username) {
        db = mySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseConstants.USER_TABLE_NAME, null, DatabaseConstants.USER_COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
        if (cursor.getCount()>=1) {
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(DatabaseConstants.USER_COLUMN_ID));
        }
        return -1;
    }
}
