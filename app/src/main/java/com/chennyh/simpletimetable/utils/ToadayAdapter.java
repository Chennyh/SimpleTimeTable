package com.chennyh.simpletimetable.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.bean.Course;
import com.chennyh.simpletimetable.constants.CommonConstants;
import com.chennyh.simpletimetable.http.ApiClient;
import com.chennyh.simpletimetable.http.TimeTableService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Objects;

public class ToadayAdapter extends ArrayAdapter<Course> {

    private Activity mActivity;
    private int mResource;
    private ArrayList<Course> courses;
    private Course course;
    private ListView mListView;

    public ToadayAdapter(Activity activity, ListView listView, int resource, ArrayList<Course> objects) {
        super(activity, resource, objects);
        mActivity = activity;
        mResource = resource;
        courses = objects;
        mListView = listView;
    }

    private static class ViewHolder {
        TextView className;
        TextView classTime;
        TextView classRoom;
        TextView classTeacher;
        ImageView classBtnPopup;
        CardView cardView;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String name = Objects.requireNonNull(getItem(position)).getName();
        String time = Objects.requireNonNull(getItem(position)).getTime();
        String room = Objects.requireNonNull(getItem(position)).getRoom();
        String teacher = Objects.requireNonNull(getItem(position)).getTeacher();
        int color = getItem(position).getColor();

        course = new Course(name, time, room, teacher, color);
        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(mResource, parent, false);

            holder = new ViewHolder();
            holder.className = convertView.findViewById(R.id.class_tv_name);
            holder.classTime = convertView.findViewById(R.id.class_tv_time);
            holder.classRoom = convertView.findViewById(R.id.class_tv_room);
            holder.classTeacher = convertView.findViewById(R.id.class_tv_teacher);
            holder.classBtnPopup = convertView.findViewById(R.id.class_btn_popup);
            holder.cardView = convertView.findViewById(R.id.class_info);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.className.setText(course.getName());
        holder.classTime.setText(course.getTime());
        holder.classRoom.setText(course.getRoom());
        holder.classTeacher.setText(course.getTeacher());
        holder.cardView.setCardBackgroundColor(course.getColor());
        holder.classBtnPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(mActivity, holder.classBtnPopup);
                TimeTableService timeTableService = ApiClient.getClient().create(TimeTableService.class);

                popupMenu.getMenuInflater().inflate(R.menu.class_btn_edit, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_delete:
                                Call<ResponseBody> call = timeTableService.deleteCourse(SPStaticUtils.getString(CommonConstants.AUTHORIZATION), getItem(position).getId());
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.code() == CommonConstants.REQUEST_OK) {
                                            ToastUtils.showLong("删除成功");
                                        } else {
                                            ToastUtils.showLong("删除失败");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        ToastUtils.showLong("服务器连接失败！");
                                        t.printStackTrace();
                                    }
                                });

                                courses.remove(position);
                                notifyDataSetChanged();
                                return true;
                            case R.id.menu_edit:
                                //TODO: impl
                                ToastUtils.showLong("未实现");
                                return true;
                            default:
                                return onMenuItemClick(item);
                        }
                    }
                });
                popupMenu.show();
            }
        });

        return convertView;
    }

}
