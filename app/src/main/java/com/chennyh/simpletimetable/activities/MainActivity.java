package com.chennyh.simpletimetable.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.activities.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ActivityUtils.startActivity(LoginActivity.class);

        init();
    }

    private void init() {
//        if (!SPStaticUtils.getBoolean(LoginActivity.SP_SIGN_IN)) {
//            ActivityUtils.startActivity(LoginActivity.class);
//        }


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        if (navHostFragment != null) {
            NavigationUI.setupWithNavController(navView, navHostFragment.getNavController());
        }
    }

}
