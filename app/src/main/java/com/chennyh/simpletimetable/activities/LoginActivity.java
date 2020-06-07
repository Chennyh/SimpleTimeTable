package com.chennyh.simpletimetable.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.chennyh.simpletimetable.db.MySQLiteOpenHelper;
import com.chennyh.simpletimetable.db.UserDAO;
import com.chennyh.simpletimetable.bean.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    public static final String isLogin = "isLogin";
    private static final int REQUEST_REGISTER = 0;
    private EditText loginInputEmail;
    private EditText loginInputPassword;
    private AppCompatButton loginBtnLogin;
    private TextView loginLinkSignup;
    private CircleImageView loginBtnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        loginInputEmail = findViewById(R.id.login_input_email);
        loginInputPassword = findViewById(R.id.login_input_password);
        loginBtnLogin = findViewById(R.id.login_btn_login);
        loginLinkSignup = findViewById(R.id.login_link_signup);
        loginBtnClose = findViewById(R.id.login_btn_close);

        if (SPStaticUtils.contains(MySQLiteOpenHelper.USER_COLUMN_EMAIL)) {
            loginInputEmail.setText(SPStaticUtils.getString(MySQLiteOpenHelper.USER_COLUMN_EMAIL));
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

        String email = loginInputEmail.getText().toString();
        String password = loginInputPassword.getText().toString();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        UserDAO userDAO = new UserDAO(getApplicationContext());
        if (userDAO.loginUser(user)) {
            onLoginSuccess(user.getEmail(), userDAO);
        } else {
            onLoginFailed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_REGISTER) {
            if (resultCode == RESULT_OK) {
                loginInputEmail.setText(data.getStringExtra(MySQLiteOpenHelper.USER_COLUMN_EMAIL));
                loginInputEmail.setFocusable(false);
                loginInputEmail.setFocusableInTouchMode(false);
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

    public void onLoginSuccess(String email, UserDAO userDAO) {
        loginBtnLogin.setEnabled(true);
        ToastUtils.showLong("登录成功");
        SPStaticUtils.put(MySQLiteOpenHelper.USER_COLUMN_ID, userDAO.queyID(email));
        SPStaticUtils.put(MySQLiteOpenHelper.USER_COLUMN_EMAIL, email);
        SPStaticUtils.put(isLogin, true);
        setResult(RESULT_OK);
        finish();
    }

    public void onLoginFailed() {
        ToastUtils.showLong("邮箱或密码错误");
        loginBtnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = loginInputEmail.getText().toString();
        String password = loginInputPassword.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginInputEmail.setError("邮箱地址不正确");
            valid = false;
        } else {
            loginInputEmail.setError(null);
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
