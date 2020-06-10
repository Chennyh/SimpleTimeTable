package com.chennyh.simpletimetable.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.bean.Course;
import com.chennyh.simpletimetable.db.CourseDAO;
import com.chennyh.simpletimetable.db.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class AddCourseActivity extends AppCompatActivity {

    private EditText addCourseInputName;
    private EditText addCourseInputTeacher;
    private AppCompatSpinner addCourseSelectDay;
    private AppCompatSpinner addCourseSelectStart;
    private AppCompatSpinner addCourseSelectStep;
    private EditText addCourseInputRoom;
    private AppCompatButton addCourseBtnAdd;
    private TextView addCourseTvFromTime;
    private TextView addCourseTvToTime;

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
        addCourseSelectDay = findViewById(R.id.addCourse_select_day);
        addCourseSelectStart = findViewById(R.id.addCourse_select_start);
        addCourseSelectStep = findViewById(R.id.addCourse_select_step);
        addCourseInputRoom = findViewById(R.id.addCourse_input_room);
        addCourseTvFromTime = findViewById(R.id.addCourse_tv_from_time);
        addCourseTvToTime = findViewById(R.id.addCourse_tv_to_time);
        addCourseBtnAdd = findViewById(R.id.addCourse_btn_add);

        addCourseTvFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddCourseActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                addCourseTvFromTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.setTitle("选择上课时间");
                timePickerDialog.show();
            }
        });

        addCourseTvToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddCourseActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                addCourseTvToTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, hour, minute, true);
                timePickerDialog.setTitle("选择下课时间");
                timePickerDialog.show();
            }
        });


        addCourseBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }

                String name = addCourseInputName.getText().toString();
                String teacher = addCourseInputTeacher.getText().toString();
                int day = addCourseSelectDay.getSelectedItemPosition() + 1;
                int start = addCourseSelectStart.getSelectedItemPosition() + 1;
                int step = addCourseSelectStep.getSelectedItemPosition() + 1;
                String room = addCourseInputRoom.getText().toString();
                String time = addCourseTvFromTime.getText().toString() + " - " + addCourseTvFromTime.getText().toString();

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

                ArrayList<Integer> weekList = new ArrayList<>();
                weekList.add(1);
                weekList.add(20);
                course.setWeekList(weekList);

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

    public boolean validate() {
        boolean valid = true;

        String name = addCourseInputName.getText().toString();
        String teacher = addCourseInputTeacher.getText().toString();
        String room = addCourseInputRoom.getText().toString();

        if (name.isEmpty()) {
            addCourseInputName.setError("课程名字未填写");
            valid = false;
        } else {
            addCourseInputName.setError(null);
        }

        if (teacher.isEmpty()) {
            addCourseInputTeacher.setError("授课老师未填写");
            valid = false;
        } else {
            addCourseInputTeacher.setError(null);
        }

        if (room.isEmpty()) {
            addCourseInputRoom.setError("上课教室未填写");
            valid = false;
        } else {
            addCourseInputRoom.setError(null);
        }

        return valid;
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
