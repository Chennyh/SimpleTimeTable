package com.chennyh.simpletimetable.activities;

import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.blankj.utilcode.util.ToastUtils;
import com.chennyh.simpletimetable.R;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.btn_send:
                EditText mFeedbackInputEmail = findViewById(R.id.feedback_input_email);
                EditText mFeedbackInputContext = findViewById(R.id.feedback_input_context);
                String email = mFeedbackInputEmail.getText().toString();
                String context = mFeedbackInputContext.getText().toString();

                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mFeedbackInputEmail.setError("邮箱地址不正确");
                } else {
                    mFeedbackInputEmail.setError(null);
                    ToastUtils.showLong("反馈已发送");
                    finish();
                }
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.send, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
