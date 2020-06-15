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

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {

    private ListView listView;
    private ToadayAdapter adapter;
    private TimeTableService timeTableService;

    public TodayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timeTableService = ApiClient.getClient().create(TimeTableService.class);
        setupAdapter(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    private void setupAdapter(View view) {
        if (SPStaticUtils.getBoolean(CommonConstants.isLogin)) {

            Call<ArrayList<Course>> call = timeTableService.getCoursesById(SPStaticUtils.getString(CommonConstants.AUTHORIZATION), SPStaticUtils.getLong(DatabaseConstants.USER_COLUMN_ID),true);
            call.enqueue(new Callback<ArrayList<Course>>() {
                @Override
                public void onResponse(Call<ArrayList<Course>> call, Response<ArrayList<Course>> response) {
                    if (response.code() == CommonConstants.REQUEST_OK) {
                        ArrayList<Course> courses = response.body();
                        if (courses != null) {
                            listView = view.findViewById(R.id.listView);
                            adapter = new ToadayAdapter(getActivity(), listView, R.layout.list_item, courses);
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
