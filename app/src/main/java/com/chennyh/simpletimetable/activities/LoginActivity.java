package com.chennyh.simpletimetable.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.constants.CommonConstants;
import com.chennyh.simpletimetable.constants.DatabaseConstants;
import com.chennyh.simpletimetable.http.ApiClient;
import com.chennyh.simpletimetable.http.TimeTableService;
import com.chennyh.simpletimetable.http.representation.UserRepresentation;
import com.chennyh.simpletimetable.http.request.LoginUserRequests;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_REGISTER = 0;
    private EditText loginInputUsername;
    private EditText loginInputPassword;
    private AppCompatButton loginBtnLogin;
    private TextView loginLinkSignup;
    private CircleImageView loginBtnClose;
    private TimeTableService timeTableService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        timeTableService = ApiClient.getClient().create(TimeTableService.class);
        init();
    }

    private void init() {
        loginInputUsername = findViewById(R.id.login_input_username);
        loginInputPassword = findViewById(R.id.login_input_password);
        loginBtnLogin = findViewById(R.id.login_btn_login);
        loginLinkSignup = findViewById(R.id.login_link_signup);
        loginBtnClose = findViewById(R.id.login_btn_close);

        if (SPStaticUtils.contains(DatabaseConstants.USER_COLUMN_USERNAME)) {
            loginInputUsername.setText(SPStaticUtils.getString(DatabaseConstants.USER_COLUMN_USERNAME));
        }

        loginBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        loginLinkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivityForResult(LoginActivity.this, RegisterActivity.class, REQUEST_REGISTER, R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        loginBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtnLogin.setEnabled(true);
                finish();
            }
        });
    }

    private void login() {
        Log.d(TAG, "Login()");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        String username = loginInputUsername.getText().toString();
        String password = loginInputPassword.getText().toString();

        LoginUserRequests loginUserRequests = new LoginUserRequests(username, password, true);

        Call<ResponseBody> call = timeTableService.loginUser(loginUserRequests);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == CommonConstants.REQUEST_OK) {
                    onLoginSuccess(response.headers().get(CommonConstants.AUTHORIZATION));
                } else {
                    onLoginFailed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ToastUtils.showLong("服务器连接失败！");
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_REGISTER) {
            if (resultCode == RESULT_OK) {
                loginInputUsername.setText(data.getStringExtra(DatabaseConstants.USER_COLUMN_USERNAME));
                loginInputUsername.setFocusable(false);
                loginInputUsername.setFocusableInTouchMode(false);
                loginInputPassword.setFocusable(true);
                loginInputPassword.setFocusableInTouchMode(true);
            }
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        loginBtnLogin.setEnabled(true);
        finish();
    }

    public void onLoginSuccess(String bearer) {
        loginBtnLogin.setEnabled(true);
        ToastUtils.showLong("登录成功");
        SPStaticUtils.put(CommonConstants.AUTHORIZATION, bearer);
        System.out.println(bearer);
        Call<UserRepresentation> call = timeTableService.getUserInfo(bearer);
        call.enqueue(new Callback<UserRepresentation>() {
            @Override
            public void onResponse(Call<UserRepresentation> call, Response<UserRepresentation> response) {
                if (response.code() == CommonConstants.REQUEST_OK) {
                    SPStaticUtils.put(DatabaseConstants.USER_COLUMN_ID, response.body().getId());
                    SPStaticUtils.put(DatabaseConstants.USER_COLUMN_USERNAME, response.body().getUsername());
                    SPStaticUtils.put(DatabaseConstants.USER_COLUMN_EMAIL, response.body().getEmail());
                    SPStaticUtils.put(CommonConstants.isLogin, true);
                } else {
                    ToastUtils.showLong("用户信息获取失败！");
                }
            }

            @Override
            public void onFailure(Call<UserRepresentation> call, Throwable t) {
                ToastUtils.showLong("服务器连接失败！");
                t.printStackTrace();
            }
        });

        setResult(RESULT_OK);
        finish();
    }

    public void onLoginFailed() {
        ToastUtils.showLong("用户名或密码错误");
        loginBtnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = loginInputUsername.getText().toString();
        String password = loginInputPassword.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            loginInputUsername.setError("至少输入 3 个字符");
            valid = false;
        } else {
            loginInputUsername.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            loginInputPassword.setError("密码在 4 到 10 之间");
            valid = false;
        } else {
            loginInputPassword.setError(null);
        }

        return valid;
    }
}
