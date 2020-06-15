package com.chennyh.simpletimetable.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.blankj.utilcode.util.SPStaticUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.constants.CommonConstants;
import com.chennyh.simpletimetable.constants.DatabaseConstants;
import com.chennyh.simpletimetable.db.CourseDAO;
import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.model.Schedule;

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

        if (SPStaticUtils.getBoolean(CommonConstants.isLogin)) {
            CourseDAO courseDAO = new CourseDAO(getContext());
            List<Schedule> schedules = courseDAO.getCoursesSchedule(SPStaticUtils.getInt(DatabaseConstants.USER_COLUMN_ID));

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
}
