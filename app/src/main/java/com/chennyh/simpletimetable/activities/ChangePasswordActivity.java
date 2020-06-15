package com.chennyh.simpletimetable.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.constants.DatabaseConstants;
import com.chennyh.simpletimetable.db.UserDAO;
import com.chennyh.simpletimetable.bean.User;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText changePasswordInputOldPassword;
    private EditText changePasswordInputNewPassword;
    private EditText changePasswordInputRePassword;
    private AppCompatButton changePasswordBtnChange;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userDAO = new UserDAO(getApplicationContext());
        init();
    }

    private void init() {
        changePasswordInputOldPassword = findViewById(R.id.changePassword_input_oldPassword);
        changePasswordInputNewPassword = findViewById(R.id.changePassword_input_newPassword);
        changePasswordInputRePassword = findViewById(R.id.changePassword_input_rePassword);
        changePasswordBtnChange = findViewById(R.id.changePassword_btn_change);

        changePasswordBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }
                String password = changePasswordInputNewPassword.getText().toString();
                User user = new User();
                user.setEmail(SPStaticUtils.getString(DatabaseConstants.USER_COLUMN_EMAIL));
                user.setPassword(password);
                if (userDAO.changePassword(user)) {
                    ToastUtils.showLong("修改成功！");
                    return;
                }
                ToastUtils.showLong("修改失败！");
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

    public boolean validate() {
        boolean valid = true;

        String oldPassword = changePasswordInputOldPassword.getText().toString();
        String newPassword = changePasswordInputNewPassword.getText().toString();
        String rePassword = changePasswordInputRePassword.getText().toString();

        User user = new User();
        user.setEmail(SPStaticUtils.getString(DatabaseConstants.USER_COLUMN_EMAIL));
        user.setPassword(oldPassword);
        if (!userDAO.matchPassword(user)) {
            changePasswordInputOldPassword.setError("原密码不正确！");
            valid = false;
        } else {
            changePasswordInputOldPassword.setError(null);
        }

        if (newPassword.isEmpty() || newPassword.length() < 4 || newPassword.length() > 10) {
            changePasswordInputNewPassword.setError("密码在 4 到 10 之间");
            valid = false;
        } else {
            changePasswordInputNewPassword.setError(null);
        }

        if (rePassword.isEmpty() || rePassword.length() < 4 || rePassword.length() > 10 || !(rePassword.equals(newPassword))) {
            changePasswordInputRePassword.setError("密码不匹配");
            valid = false;
        } else {
            changePasswordInputRePassword.setError(null);
        }

        return valid;
    }
}
