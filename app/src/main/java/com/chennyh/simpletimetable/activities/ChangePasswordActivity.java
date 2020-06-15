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
import com.chennyh.simpletimetable.constants.CommonConstants;
import com.chennyh.simpletimetable.constants.DatabaseConstants;
import com.chennyh.simpletimetable.http.ApiClient;
import com.chennyh.simpletimetable.http.TimeTableService;
import com.chennyh.simpletimetable.http.request.LoginUserRequests;
import com.chennyh.simpletimetable.http.request.UserUpdateRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText changePasswordInputOldPassword;
    private EditText changePasswordInputNewPassword;
    private EditText changePasswordInputRePassword;
    private AppCompatButton changePasswordBtnChange;
    private TimeTableService timeTableService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        timeTableService = ApiClient.getClient().create(TimeTableService.class);
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
                UserUpdateRequest userUpdateRequest = new UserUpdateRequest(SPStaticUtils.getString(DatabaseConstants.USER_COLUMN_USERNAME), password, SPStaticUtils.getString(DatabaseConstants.USER_COLUMN_EMAIL), true);

                Call<ResponseBody> call = timeTableService.updateUser(SPStaticUtils.getString(CommonConstants.AUTHORIZATION), userUpdateRequest);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == CommonConstants.REQUEST_OK) {
                            ToastUtils.showLong("修改成功！");
                            return;
                        }
                        ToastUtils.showLong("修改失败！");
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

        LoginUserRequests loginUserRequests = new LoginUserRequests(SPStaticUtils.getString(DatabaseConstants.USER_COLUMN_USERNAME), oldPassword, true);
        Call<ResponseBody> call = timeTableService.loginUser(loginUserRequests);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == CommonConstants.REQUEST_OK) {
                    changePasswordInputNewPassword.setError(null);
                } else {
                    changePasswordInputOldPassword.setError("原密码不正确！");
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ToastUtils.showLong("服务器连接失败！");
                t.printStackTrace();
            }
        });

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
