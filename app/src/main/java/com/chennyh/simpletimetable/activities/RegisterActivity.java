package com.chennyh.simpletimetable.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.constants.CommonConstants;
import com.chennyh.simpletimetable.constants.DatabaseConstants;
import com.chennyh.simpletimetable.constants.ErrorCode;
import com.chennyh.simpletimetable.http.ApiClient;
import com.chennyh.simpletimetable.http.TimeTableService;
import com.chennyh.simpletimetable.http.request.RegisterUserRequests;
import com.chennyh.simpletimetable.http.response.ErrorResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText registerInputName;
    private EditText registerInputEmail;
    private EditText registerInputPassword;
    private EditText registerInputReEnterPassword;
    private AppCompatButton registerBtnSignup;
    private TextView registerLinkLogin;
    private CircleImageView registerBtnClose;
    private TimeTableService timeTableService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        timeTableService = ApiClient.getClient().create(TimeTableService.class);
        init();
    }

    private void init() {
        registerInputName = findViewById(R.id.register_input_name);
        registerInputEmail = findViewById(R.id.register_input_email);
        registerInputPassword = findViewById(R.id.register_input_password);
        registerInputReEnterPassword = findViewById(R.id.register_input_reEnterPassword);
        registerBtnSignup = findViewById(R.id.register_btn_signup);
        registerLinkLogin = findViewById(R.id.register_link_login);
        registerBtnClose = findViewById(R.id.register_btn_close);

        registerBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        registerLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_FIRST_USER);
                ActivityUtils.finishActivity(RegisterActivity.this, R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        registerBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_FIRST_USER);
        ActivityUtils.finishActivity(RegisterActivity.this, R.anim.push_right_in, R.anim.push_right_out);
    }

    private void register() {
        Log.d(TAG, "Register()");

        if (!validate()) {
            onRegisterFailed();
            return;
        }

        String name = registerInputName.getText().toString();
        String email = registerInputEmail.getText().toString();
        String password = registerInputPassword.getText().toString();

        RegisterUserRequests registerUserRequests = new RegisterUserRequests(email, name, password);
        Call<ResponseBody> call = timeTableService.registerUser(registerUserRequests);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == CommonConstants.REQUEST_OK) {
                    onRegisterSuccess(registerUserRequests.getUsername());
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<ErrorResponse>() {
                }.getType();
                ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                if (errorResponse.getCode() == ErrorCode.USER_NAME_ALREADY_EXIST.getCode()) {
                    registerInputName.setError(ErrorCode.USER_NAME_ALREADY_EXIST.getMessage());
                    return;
                } else {
                    registerInputName.setError(null);
                }
                if (errorResponse.getCode() == ErrorCode.EMAIL_ALREADY_EXIST.getCode()) {
                    registerInputEmail.setError(ErrorCode.EMAIL_ALREADY_EXIST.getMessage());
                    return;
                } else {
                    registerInputEmail.setError(null);
                }
                onRegisterFailed();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ToastUtils.showLong("服务器连接失败！");
                t.printStackTrace();
            }
        });

    }

    public void onRegisterSuccess(String username) {
        ToastUtils.showLong("注册成功！");
        registerBtnSignup.setEnabled(true);
        setResult(RESULT_OK, new Intent().putExtra(DatabaseConstants.USER_COLUMN_USERNAME, username));
        ActivityUtils.finishActivity(RegisterActivity.this, R.anim.push_right_in, R.anim.push_right_out);
    }

    public void onRegisterFailed() {
        ToastUtils.showLong("注册失败！");
        registerBtnSignup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = registerInputName.getText().toString();
        String email = registerInputEmail.getText().toString();
        String password = registerInputPassword.getText().toString();
        String reEnterPassword = registerInputReEnterPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            registerInputName.setError("至少输入 3 个字符");
            valid = false;
        } else {
            registerInputName.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerInputEmail.setError("邮箱地址不正确");
            valid = false;
        } else {
            registerInputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            registerInputPassword.setError("密码在 4 到 10 之间");
            valid = false;
        } else {
            registerInputPassword.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            registerInputReEnterPassword.setError("密码不匹配");
            valid = false;
        } else {
            registerInputReEnterPassword.setError(null);
        }

        return valid;
    }
}
