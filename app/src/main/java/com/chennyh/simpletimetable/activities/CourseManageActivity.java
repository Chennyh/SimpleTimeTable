package com.chennyh.simpletimetable.activities;

import android.view.MenuItem;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.blankj.utilcode.util.SPStaticUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.bean.Course;
import com.chennyh.simpletimetable.db.CourseDAO;
import com.chennyh.simpletimetable.db.MySQLiteOpenHelper;
import com.chennyh.simpletimetable.utils.ToadayAdapter;

import java.util.ArrayList;

public class CourseManageActivity extends AppCompatActivity {

    private ListView listView;
    private ToadayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_manage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupAdapter() {
        if (SPStaticUtils.getBoolean(LoginActivity.isLogin)) {
            CourseDAO courseDAO = new CourseDAO(getApplicationContext());
            ArrayList<Course> courses = courseDAO.getCourses(SPStaticUtils.getInt(MySQLiteOpenHelper.USER_COLUMN_ID));

            if (courses != null) {
                listView = findViewById(R.id.course_manage_listview);
                adapter = new ToadayAdapter(CourseManageActivity.this, listView, R.layout.list_item, courses);
                listView.setAdapter(adapter);
            }
        }

    }
}
