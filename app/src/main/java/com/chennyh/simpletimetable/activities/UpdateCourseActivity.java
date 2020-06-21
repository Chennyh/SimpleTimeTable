package com.chennyh.simpletimetable.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.bean.Course;
import com.chennyh.simpletimetable.constants.CommonConstants;
import com.chennyh.simpletimetable.constants.DatabaseConstants;
import com.chennyh.simpletimetable.http.ApiClient;
import com.chennyh.simpletimetable.http.TimeTableService;
import com.chennyh.simpletimetable.http.request.AddCourseRquest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Calendar;

public class UpdateCourseActivity extends AppCompatActivity {

    private EditText updateCourseInputName;
    private EditText updateCourseInputTeacher;
    private AppCompatSpinner updateCourseSelectDay;
    private AppCompatSpinner updateCourseSelectStart;
    private AppCompatSpinner updateCourseSelectStep;
    private EditText updateCourseInputRoom;
    private AppCompatButton updateCourseBtnUpdate;
    private TextView updateCourseTvFromTime;
    private TextView updateCourseTvToTime;
    private TimeTableService timeTableService;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        timeTableService = ApiClient.getClient().create(TimeTableService.class);
        init();
        setDate();
    }

    private void setDate() {
        Intent intent = getIntent();
        course = intent.getParcelableExtra("course");
        updateCourseInputName.setText(course.getName());
        updateCourseInputTeacher.setText(course.getTeacher());
        updateCourseInputRoom.setText(course.getRoom());
        updateCourseSelectDay.setSelection(course.getDay() - 1);
        updateCourseSelectStart.setSelection(course.getStart() - 1);
        updateCourseSelectStep.setSelection(course.getStep() - 1);
        String[] time = course.getTime().split(" - ");
        updateCourseTvFromTime.setText(time[0]);
        updateCourseTvToTime.setText(time[1]);

    }

    private void init() {
        updateCourseInputName = findViewById(R.id.updateCourse_input_name);
        updateCourseInputTeacher = findViewById(R.id.updateCourse_input_teacher);
        updateCourseSelectDay = findViewById(R.id.updateCourse_select_day);
        updateCourseSelectStart = findViewById(R.id.updateCourse_select_start);
        updateCourseSelectStep = findViewById(R.id.updateCourse_select_step);
        updateCourseInputRoom = findViewById(R.id.updateCourse_input_room);
        updateCourseTvFromTime = findViewById(R.id.updateCourse_tv_from_time);
        updateCourseTvToTime = findViewById(R.id.updateCourse_tv_to_time);
        updateCourseBtnUpdate = findViewById(R.id.updateCourse_btn_update);

        updateCourseTvFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateCourseActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                updateCourseTvFromTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.setTitle("选择上课时间");
                timePickerDialog.show();
            }
        });

        updateCourseTvToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateCourseActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                updateCourseTvToTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, hour, minute, true);
                timePickerDialog.setTitle("选择下课时间");
                timePickerDialog.show();
            }
        });

        updateCourseBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }

                String name = updateCourseInputName.getText().toString();
                String teacher = updateCourseInputTeacher.getText().toString();
                int day = updateCourseSelectDay.getSelectedItemPosition() + 1;
                int start = updateCourseSelectStart.getSelectedItemPosition() + 1;
                int step = updateCourseSelectStep.getSelectedItemPosition() + 1;
                String room = updateCourseInputRoom.getText().toString();
                String time = updateCourseTvFromTime.getText().toString() + " - " + updateCourseTvFromTime.getText().toString();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                AddCourseRquest updateCourseRquest = new AddCourseRquest();
                updateCourseRquest.setUserId(SPStaticUtils.getLong(DatabaseConstants.USER_COLUMN_ID));
                updateCourseRquest.setColor(ColorUtils.getRandomColor());
                updateCourseRquest.setName(name);
                updateCourseRquest.setTeacher(teacher);
                updateCourseRquest.setDay(day);
                updateCourseRquest.setStart(start);
                updateCourseRquest.setStep(step);
                updateCourseRquest.setRoom(room);
                updateCourseRquest.setTime(time);
                updateCourseRquest.setTerm(sharedPreferences.getString("termSetting", ""));

                ArrayList<Integer> weekList = new ArrayList<>();
                weekList.add(1);
                weekList.add(20);
                updateCourseRquest.setWeekList(weekList);

                Call<ResponseBody> call = timeTableService.updateCourse(SPStaticUtils.getString(CommonConstants.AUTHORIZATION), course.getId(), updateCourseRquest);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == CommonConstants.REQUEST_OK) {
                            ToastUtils.showLong("修改成功！");
                            finish();
                        } else {
                            ToastUtils.showLong("修改失败！");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        ToastUtils.showLong("服务器连接失败！");
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String name = updateCourseInputName.getText().toString();
        String teacher = updateCourseInputTeacher.getText().toString();
        String room = updateCourseInputRoom.getText().toString();
        String startTime = updateCourseTvFromTime.getText().toString();
        String endTime = updateCourseTvToTime.getText().toString();

        if (name.isEmpty()) {
            updateCourseInputName.setError("课程名字未填写");
            valid = false;
        } else {
            updateCourseInputName.setError(null);
        }

        if (teacher.isEmpty()) {
            updateCourseInputTeacher.setError("授课老师未填写");
            valid = false;
        } else {
            updateCourseInputTeacher.setError(null);
        }

        if (room.isEmpty()) {
            updateCourseInputRoom.setError("上课教室未填写");
            valid = false;
        } else {
            updateCourseInputRoom.setError(null);
        }

        if ("选择上课时间".equals(startTime)) {
            updateCourseTvFromTime.setError("未选择上课时间");
            valid = false;
        } else {
            updateCourseTvFromTime.setError(null);
        }

        if ("选择下课时间".equals(endTime)) {
            updateCourseTvToTime.setError("未选择下课时间");
            valid = false;
        } else {
            updateCourseTvToTime.setError(null);
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
