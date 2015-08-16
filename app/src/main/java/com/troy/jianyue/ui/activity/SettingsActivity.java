package com.troy.jianyue.ui.activity;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.troy.jianyue.R;
import com.troy.jianyue.util.Appearance;

/**
 * Created by chenlongfei on 15/5/7.
 */
public class SettingsActivity extends BaseActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
        initFragment();
    }

    protected void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar();
    }

    @Override
    protected void initData() {

    }

    private void setToolbar() {
        mToolbar.setTitle(R.string.settings);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initFragment() {
        getFragmentManager().beginTransaction().replace(R.id.content_settings, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        private Preference mCleanCachePreference;
        private Preference mAboutPreference;
        private Preference mFeedbackPreference;
        private Preference mCheckUpdatePreference;
        private Resources mResources;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            initPreference();
        }

        private void initPreference() {
            addPreferencesFromResource(R.xml.settings_preference_fragment);
            mResources = getResources();
            mCleanCachePreference = findPreference(mResources.getString(R.string.settings_clean_cache));
            mAboutPreference = findPreference(mResources.getString(R.string.settings_about));
            mFeedbackPreference = findPreference(mResources.getString(R.string.settings_feedback));
            mCheckUpdatePreference = findPreference(mResources.getString(R.string.settings_check_update));
            mCleanCachePreference.setOnPreferenceClickListener(this);
            mAboutPreference.setOnPreferenceClickListener(this);
            mFeedbackPreference.setOnPreferenceClickListener(this);
            mCheckUpdatePreference.setOnPreferenceClickListener(this);
            mCleanCachePreference.setSummary("缓存大小:");
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();
            if (key.equals(mResources.getString(R.string.settings_clean_cache))) {
                Log.i("Troy","click");
            } else if (key.equals(mResources.getString(R.string.settings_about))) {

            } else if (key.equals(mResources.getString(R.string.settings_feedback))) {

            } else if (key.equals(mResources.getString(R.string.settings_check_update))) {

            }
            return false;
        }
    }
}
