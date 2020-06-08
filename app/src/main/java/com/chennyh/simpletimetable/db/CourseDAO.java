package com.chennyh.simpletimetable.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.blankj.utilcode.util.ObjectUtils;
import com.chennyh.simpletimetable.bean.Course;
import com.zhuangfei.timetable.model.Schedule;

import java.util.ArrayList;
import java.util.List;

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

        if (ObjectUtils.isNotEmpty(course.getUid())) {
            values.put(MySQLiteOpenHelper.COURSE_COLUMN_UID, course.getUid());
        }

        if (ObjectUtils.isNotEmpty(course.getName())) {
            values.put(MySQLiteOpenHelper.COURSE_COLUMN_NAME, course.getName());
        }

        if (ObjectUtils.isNotEmpty(course.getRoom())) {
            values.put(MySQLiteOpenHelper.COURSE_COLUMN_ROOM, course.getRoom());
        }

        if (ObjectUtils.isNotEmpty(course.getTeacher())) {
            values.put(MySQLiteOpenHelper.COURSE_COLUMN_TEACHER, course.getTeacher());
        }

        if (ObjectUtils.isNotEmpty(course.getWeekList())) {
            values.put(MySQLiteOpenHelper.COURSE_COLUMN_WEEKLIST, course.getWeekList().toString());
        }

        if (ObjectUtils.isNotEmpty(course.getTime())) {
            values.put(MySQLiteOpenHelper.COURSE_COLUMN_TIME, course.getTime());
        }

        if (ObjectUtils.isNotEmpty(course.getStart())) {
            values.put(MySQLiteOpenHelper.COURSE_COLUMN_START, course.getStart());
        }

        if (ObjectUtils.isNotEmpty(course.getStep())) {
            values.put(MySQLiteOpenHelper.COURSE_COLUMN_STEP, course.getStep());
        }

        if (ObjectUtils.isNotEmpty(course.getDay())) {
            values.put(MySQLiteOpenHelper.COURSE_COLUMN_DAY, course.getDay());
        }

        if (ObjectUtils.isNotEmpty(course.getColor())) {
            values.put(MySQLiteOpenHelper.COURSE_COLUMN_COLOR, course.getColor());
        }

        if (db.insert(MySQLiteOpenHelper.COURSE_TABLE_NAME, null, values) > 0) {
            return true;
        }
        return false;
    }

    public ArrayList<Course> getCourses(int uid) {
        db = mySQLiteOpenHelper.getReadableDatabase();
        ArrayList<Course> courses = new ArrayList<>();
        Cursor cursor = db.query(MySQLiteOpenHelper.COURSE_TABLE_NAME, null, MySQLiteOpenHelper.COURSE_COLUMN_UID + "=?", new String[]{uid + ""}, null, null, null);
        if (cursor.getCount()>=1) {
            cursor.moveToFirst();
            do {
                Course course = new Course();
                course.setId(cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_ID)));
                course.setName(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_NAME)));
                course.setTime(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_TIME)));
                course.setRoom(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_ROOM)));
                course.setTeacher(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_TEACHER)));
                course.setColor(cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_COLOR)));

                courses.add(course);
            } while (cursor.moveToNext());
            return courses;
        }
        return null;
    }

    public List<Schedule> getCoursesSchedule(int uid) {
        db = mySQLiteOpenHelper.getReadableDatabase();
        List<Schedule> courses = new ArrayList<>();
        Cursor cursor = db.query(MySQLiteOpenHelper.COURSE_TABLE_NAME, null, MySQLiteOpenHelper.COURSE_COLUMN_UID + "=?", new String[]{uid + ""}, null, null, null);
        if (cursor.getCount()>=1) {
            cursor.moveToFirst();
            do {
                Course course = new Course();

                course.setName(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_NAME)));
                course.setRoom(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_ROOM)));
                course.setTeacher(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_TEACHER)));
//                course.setWeekList(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_TEACHER)));
                ArrayList<Integer> integers = new ArrayList<>();
                integers.add(1);
                integers.add(16);

                course.setWeekList(integers);
                course.setStart(cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_START)));
                course.setStep(cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_STEP)));
                course.setDay(cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_DAY)));
                course.setColor(cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_COLOR)));
                course.setTime(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COURSE_COLUMN_TIME)));

                courses.add(course.getSchedule());
            } while (cursor.moveToNext());
            return courses;
        }
        return null;
    }

    public boolean deleteCourse(Course course) {
        db = mySQLiteOpenHelper.getWritableDatabase();
        if (db.delete(MySQLiteOpenHelper.COURSE_TABLE_NAME, MySQLiteOpenHelper.COURSE_COLUMN_ID + "=?", new String[]{course.getId() + ""}) > 0) {
            return true;
        }
        return false;
    }
}
