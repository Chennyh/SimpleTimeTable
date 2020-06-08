package com.chennyh.simpletimetable.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.bean.Course;
import com.chennyh.simpletimetable.db.CourseDAO;
import com.chennyh.simpletimetable.db.MySQLiteOpenHelper;

public class AddCourseActivity extends AppCompatActivity {

    private EditText addCourseInputName;
    private EditText addCourseInputTeacher;
    private EditText addCourseInputDay;
    private EditText addCourseInputStart;
    private EditText addCourseInputStep;
    private EditText addCourseInputRoom;
    private EditText addCourseInputTime;
    private AppCompatButton addCourseBtnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init() {
        addCourseInputName = findViewById(R.id.addCourse_input_name);
        addCourseInputTeacher = findViewById(R.id.addCourse_input_teacher);
        addCourseInputDay = findViewById(R.id.addCourse_input_day);
        addCourseInputStart = findViewById(R.id.addCourse_input_start);
        addCourseInputStep = findViewById(R.id.addCourse_input_step);
        addCourseInputRoom = findViewById(R.id.addCourse_input_room);
        addCourseInputTime = findViewById(R.id.addCourse_input_time);
        addCourseBtnAdd = findViewById(R.id.addCourse_btn_add);

        addCourseInputTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addCourseBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = addCourseInputName.getText().toString();
                String teacher = addCourseInputTeacher.getText().toString();
                int day = Integer.parseInt(addCourseInputDay.getText().toString());
                int start = Integer.parseInt(addCourseInputStart.getText().toString());
                int step = Integer.parseInt(addCourseInputStep.getText().toString());
                String room = addCourseInputRoom.getText().toString();
                String time = addCourseInputTime.getText().toString();

                Course course = new Course();
                course.setUid(SPStaticUtils.getInt(MySQLiteOpenHelper.USER_COLUMN_ID));
                course.setColor(ColorUtils.getRandomColor());
                course.setName(name);
                course.setTeacher(teacher);
                course.setDay(day);
                course.setStart(start);
                course.setStep(step);
                course.setRoom(room);
                course.setTime(time);

                CourseDAO courseDAO = new CourseDAO(getApplicationContext());
                if (courseDAO.addCourse(course)) {
                    ToastUtils.showLong("添加成功！");
                    finish();
                } else {
                    ToastUtils.showLong("添加失败！");
                }
            }
        });
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
}
