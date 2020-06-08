package com.chennyh.simpletimetable.fragment;

import android.os.Bundle;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.blankj.utilcode.util.SPStaticUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.activities.LoginActivity;
import com.chennyh.simpletimetable.bean.Course;
import com.chennyh.simpletimetable.db.CourseDAO;
import com.chennyh.simpletimetable.db.MySQLiteOpenHelper;
import com.chennyh.simpletimetable.utils.ToadayAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {

    private ListView listView;
    private ToadayAdapter adapter;

    public TodayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupAdapter(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    private void setupAdapter(View view) {
        if (SPStaticUtils.getBoolean(LoginActivity.isLogin)) {
            CourseDAO courseDAO = new CourseDAO(getContext());
            ArrayList<Course> courses = courseDAO.getTodayCourses(SPStaticUtils.getInt(MySQLiteOpenHelper.USER_COLUMN_ID));

            if (courses != null) {
                listView = view.findViewById(R.id.listView);
                adapter = new ToadayAdapter(getActivity(), listView, R.layout.list_item, courses);
                listView.setAdapter(adapter);
            }
        }

    }
}
