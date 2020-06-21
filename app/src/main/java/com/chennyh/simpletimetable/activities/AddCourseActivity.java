package com.chennyh.simpletimetable.activities;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
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
import androidx.preference.PreferenceManager;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chennyh.simpletimetable.R;
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
    private TimeTableService timeTableService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        timeTableService = ApiClient.getClient().create(TimeTableService.class);
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
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                AddCourseRquest addCourseRquest = new AddCourseRquest();
                addCourseRquest.setUserId(SPStaticUtils.getLong(DatabaseConstants.USER_COLUMN_ID));
                addCourseRquest.setColor(ColorUtils.getRandomColor());
                addCourseRquest.setName(name);
                addCourseRquest.setTeacher(teacher);
                addCourseRquest.setDay(day);
                addCourseRquest.setStart(start);
                addCourseRquest.setStep(step);
                addCourseRquest.setRoom(room);
                addCourseRquest.setTime(time);
                addCourseRquest.setTerm(sharedPreferences.getString("termSetting", ""));

                ArrayList<Integer> weekList = new ArrayList<>();
                weekList.add(1);
                weekList.add(20);
                addCourseRquest.setWeekList(weekList);

                Call<ResponseBody> call = timeTableService.addCourse(SPStaticUtils.getString(CommonConstants.AUTHORIZATION), addCourseRquest);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == CommonConstants.REQUEST_OK) {
                            ToastUtils.showLong("添加成功！");
                            finish();
                        } else {
                            ToastUtils.showLong("添加失败！");
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

        String name = addCourseInputName.getText().toString();
        String teacher = addCourseInputTeacher.getText().toString();
        String room = addCourseInputRoom.getText().toString();
        String startTime = addCourseTvFromTime.getText().toString();
        String endTime = addCourseTvToTime.getText().toString();

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

        if ("选择上课时间".equals(startTime)) {
            addCourseTvFromTime.setError("未选择上课时间");
            valid = false;
        } else {
            addCourseTvFromTime.setError(null);
        }

        if ("选择下课时间".equals(endTime)) {
            addCourseTvToTime.setError("未选择下课时间");
            valid = false;
        } else {
            addCourseTvToTime.setError(null);
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
