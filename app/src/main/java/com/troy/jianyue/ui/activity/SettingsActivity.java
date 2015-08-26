package com.troy.jianyue.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.troy.jianyue.R;
import com.troy.jianyue.service.DownLoadApkService;
import com.troy.jianyue.util.CommonUtil;
import com.troy.jianyue.util.ToastUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

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
        private DownLoadApkService mCheckVersionService;
        private boolean mIsBind;
        private SettingsActivity mSettingsActivity;
        private static final String APK_KEY = "apk";
        private static final String VERSIONCODE_KEY = "versionCode";
        private String mUrl;

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            mSettingsActivity = (SettingsActivity) activity;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            initPreference();
        }

        public ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownLoadApkService.CheckVersionBinder mBinder = ((DownLoadApkService.CheckVersionBinder) service);
                mCheckVersionService = mBinder.getInstance();
                mIsBind = true;
                Log.i("Troy", "onServiceConnected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mCheckVersionService = null;
                mIsBind = false;
            }
        };

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
            DecimalFormat decimalFormat=new DecimalFormat("#0.00");
            mCleanCachePreference.setSummary("缓存大小:"+decimalFormat.format(getCacheSize())+"MB");
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();
            if (key.equals(mResources.getString(R.string.settings_clean_cache))) {
                cleanCache();
            } else if (key.equals(mResources.getString(R.string.settings_about))) {
                showAboutDialog();
            } else if (key.equals(mResources.getString(R.string.settings_feedback))) {
                startFeedback();
            } else if (key.equals(mResources.getString(R.string.settings_check_update))) {
                checkVersion();
            }
            return false;
        }

        private void startFeedback() {
            FeedbackAgent agent = new FeedbackAgent(mSettingsActivity);
            agent.sync();
            agent.startDefaultThreadActivity();
        }

        private double getCacheSize() {
            File cacheFile = ImageLoader.getInstance().getDiskCache().getDirectory();
            long bytesCount = 0;
            if (cacheFile.exists()) {
                if (cacheFile.isDirectory()) {
                    File[] files = cacheFile.listFiles();
                    for (File file : files) {
                        bytesCount += file.length();
                    }
                } else {
                    bytesCount = cacheFile.length();
                }
            }
            double cacheSize = (double)bytesCount / 1024 / 1024;
            return cacheSize;
        }

        private void cleanCache() {
            ImageLoader.getInstance().clearDiskCache();
            mCleanCachePreference.setSummary("缓存大小:0.00MB");
            ToastUtil.show("缓存已清除");
        }

        private void checkVersion() {
            ToastUtil.show("正在检查更新中...");
            AVQuery<AVObject> query = new AVQuery<AVObject>("Version");
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        AVObject avObject = list.get(0);
                        mUrl = avObject.getAVFile(APK_KEY).getUrl();
                        int versionCode = avObject.getNumber(VERSIONCODE_KEY).intValue();
                        Log.i("Troy", String.format("url:%1$s,versionVode:%2$s", mUrl, versionCode));
                        if (versionCode > CommonUtil.getAppVersionCode()) {
                            showUpdateDialog();
                        }

                    }
                }
            });
        }

        private void showAboutDialog() {
            new MaterialDialog.Builder(mSettingsActivity)
                    .title(R.string.app_name)
                    .content("版本:" + CommonUtil.getAppVersionName() + CommonUtil.getAppVersionCode())
                    .positiveText("确定")
                    .show();
        }

        private void showUpdateDialog() {
            new AlertDialogWrapper.Builder(mSettingsActivity).setTitle("提示").setMessage("检测到新版本,是否立即更新?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (CommonUtil.hasNetwork()) {
                        startService();
                    } else {
                        ToastUtil.show("请检查网络连接");
                    }
                    dialog.dismiss();
                }
            }).show();
        }

        private void startService() {
            Intent intent = new Intent(mSettingsActivity.getApplicationContext(), DownLoadApkService.class);
            intent.putExtra("url", mUrl);
            mSettingsActivity.startService(intent);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }

    public interface UpdateAppCallback {
        void result(boolean isUpdate);
    }

}
