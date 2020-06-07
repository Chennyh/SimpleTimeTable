package com.chennyh.simpletimetable.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.chennyh.simpletimetable.bean.Course;

public class CourseDAO {

    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase db;
    private ContentValues values;

    public CourseDAO(Context context) {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
    }

    public boolean addCourse(Course course) {
        db = mySQLiteOpenHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(MySQLiteOpenHelper.COURSE_COLUMN_UID, course.getUid());
        values.put(MySQLiteOpenHelper.COURSE_COLUMN_NAME, course.getName());
        values.put(MySQLiteOpenHelper.COURSE_COLUMN_ROOM, course.getRoom());
        values.put(MySQLiteOpenHelper.COURSE_COLUMN_TEACHER, course.getTeacher());
        values.put(MySQLiteOpenHelper.COURSE_COLUMN_WEEKLIST, course.getWeekList().toString());
        values.put(MySQLiteOpenHelper.COURSE_COLUMN_START, course.getStart());
        values.put(MySQLiteOpenHelper.COURSE_COLUMN_STEP, course.getStep());
        values.put(MySQLiteOpenHelper.COURSE_COLUMN_DAY, course.getDay());
        values.put(MySQLiteOpenHelper.COURSE_COLUMN_COLOR, course.getColor());
        values.put(MySQLiteOpenHelper.COURSE_COLUMN_TIME, course.getTime());

        if (db.insert(MySQLiteOpenHelper.COURSE_TABLE_NAME, null, values) != 1) {
            return true;
        }
        return false;
    }
}
