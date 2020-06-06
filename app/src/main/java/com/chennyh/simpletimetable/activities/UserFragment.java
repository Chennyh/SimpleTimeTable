package com.chennyh.simpletimetable.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.dao.MySQLiteOpenHelper;
import com.chennyh.simpletimetable.dao.UserDAO;
import com.chennyh.simpletimetable.entity.User;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_REGISTER = 1;
    private CircleImageView userHeadImage;
    private TextView userTvUsername;
    private LinearLayout layoutAdd;
    private LinearLayout layoutCourseMg;
    private LinearLayout layoutShare;
    private LinearLayout layoutSetting;
    private LinearLayout layoutFeedback;
    private LinearLayout layoutAbout;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        setData();
    }

    private void setData() {
        if (!SPStaticUtils.getBoolean(LoginActivity.isLogin)) {
            userHeadImage.setImageResource(R.drawable.ic_account_circle_black_24dp);
            userTvUsername.setText("未登录");
            layoutAdd.setVisibility(View.VISIBLE);
        } else {
            setLoginInfo();
        }
        userHeadImage.setOnClickListener(this);
        userTvUsername.setOnClickListener(this);
    }


    private void init(View view) {
        userHeadImage = view.findViewById(R.id.user_head_image);
        userTvUsername = view.findViewById(R.id.user_tv_username);
        layoutAdd = view.findViewById(R.id.layout_add);
        layoutCourseMg = view.findViewById(R.id.layout_course_mg);
        layoutShare = view.findViewById(R.id.layout_share);
        layoutSetting = view.findViewById(R.id.layout_setting);
        layoutFeedback = view.findViewById(R.id.layout_feedback);
        layoutAbout = view.findViewById(R.id.layout_about);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_REGISTER) {
            if (resultCode == RESULT_OK) {
                setLoginInfo();
            }
        }
    }

    public void setLoginInfo() {
        UserDAO userDAO = new UserDAO(getContext());
        User user = userDAO.getUserInfo(SPStaticUtils.getString(MySQLiteOpenHelper.USER_TABLE_EMAIL));
        if (user != null) {
            userHeadImage.setImageResource(R.drawable.headerpic);
            userTvUsername.setText(user.getUsername());
            layoutAdd.setVisibility(View.VISIBLE);
        }

    }

    public void logOut() {
        new AlertDialog.Builder(getContext())
                .setMessage("确定退出吗？")
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPStaticUtils.put(LoginActivity.isLogin, false);
                        setData();
                    }
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_head_image:
            case R.id.user_tv_username:
                if (!SPStaticUtils.getBoolean(LoginActivity.isLogin)) {
                    ActivityUtils.startActivityForResult(UserFragment.this, LoginActivity.class, REQUEST_REGISTER);
                } else {
                    logOut();
                }
                break;
            default:
        }
    }
}
