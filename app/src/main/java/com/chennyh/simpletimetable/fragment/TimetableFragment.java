package com.chennyh.simpletimetable.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.bean.Course;
import com.chennyh.simpletimetable.constants.CommonConstants;
import com.chennyh.simpletimetable.constants.DatabaseConstants;
import com.chennyh.simpletimetable.http.ApiClient;
import com.chennyh.simpletimetable.http.TimeTableService;
import com.chennyh.simpletimetable.utils.ToadayAdapter;
import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.model.Schedule;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimetableFragment extends Fragment {

    public TimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TimeTableService timeTableService = ApiClient.getClient().create(TimeTableService.class);

        if (SPStaticUtils.getBoolean(CommonConstants.isLogin)) {
            Call<ArrayList<Course>> call = timeTableService.getCoursesById(SPStaticUtils.getString(CommonConstants.AUTHORIZATION), SPStaticUtils.getLong(DatabaseConstants.USER_COLUMN_ID), false);
            call.enqueue(new Callback<ArrayList<Course>>() {
                @Override
                public void onResponse(Call<ArrayList<Course>> call, Response<ArrayList<Course>> response) {
                    if (response.code() == CommonConstants.REQUEST_OK) {
                        List<Schedule> schedules = new ArrayList<>();
                        ArrayList<Course> courses = response.body();
                        for (Course course : courses) {
                            schedules.add(course.getSchedule());
                        }
                        if (schedules != null) {
                            TimetableView timetableView = view.findViewById(R.id.timetable);
                            timetableView
                                    .data(schedules)
                                    .curWeek(1)
                                    .isShowWeekends(false)
                                    .showView();
                        }
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
