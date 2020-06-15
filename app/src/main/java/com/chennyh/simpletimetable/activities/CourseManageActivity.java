package com.chennyh.simpletimetable.activities;

import android.view.MenuItem;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.bean.Course;
import com.chennyh.simpletimetable.constants.CommonConstants;
import com.chennyh.simpletimetable.constants.DatabaseConstants;
import com.chennyh.simpletimetable.http.ApiClient;
import com.chennyh.simpletimetable.http.TimeTableService;
import com.chennyh.simpletimetable.utils.ToadayAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class CourseManageActivity extends AppCompatActivity {

    private ListView listView;
    private ToadayAdapter adapter;
    private TimeTableService timeTableService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_manage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        timeTableService = ApiClient.getClient().create(TimeTableService.class);
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
        if (SPStaticUtils.getBoolean(CommonConstants.isLogin)) {

            Call<ArrayList<Course>> call = timeTableService.getCoursesById(SPStaticUtils.getString(CommonConstants.AUTHORIZATION), SPStaticUtils.getLong(DatabaseConstants.USER_COLUMN_ID),false);
            call.enqueue(new Callback<ArrayList<Course>>() {
                @Override
                public void onResponse(Call<ArrayList<Course>> call, Response<ArrayList<Course>> response) {
                    if (response.code() == CommonConstants.REQUEST_OK) {
                        ArrayList<Course> courses = response.body();
                        if (courses != null) {
                            listView = findViewById(R.id.course_manage_listview);
                            adapter = new ToadayAdapter(CourseManageActivity.this, listView, R.layout.list_item, courses);
                            listView.setAdapter(adapter);
                        }
                    } else {
                        ToastUtils.showLong("数据获取失败！");
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Course>> call, Throwable t) {
                    ToastUtils.showLong("服务器连接失败！");
                    t.printStackTrace();
                }
            });

        }

    }
}
