package com.chennyh.simpletimetable.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.blankj.utilcode.util.SPStaticUtils;
import com.chennyh.simpletimetable.R;
import com.chennyh.simpletimetable.activities.ChangePasswordActivity;
import com.chennyh.simpletimetable.activities.LoginActivity;

public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting, rootKey);
        Preference changePassword = findPreference("changePassword");
        changePassword.setIntent(new Intent(getContext(), ChangePasswordActivity.class));
        if (SPStaticUtils.getBoolean(LoginActivity.isLogin)) {
            changePassword.setEnabled(true);
            return;
        }
        changePassword.setEnabled(false);
    }
}
