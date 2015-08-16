package com.troy.jianyue.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.troy.jianyue.ui.activity.MainActivity;
import com.troy.jianyue.util.CommonUtil;

import java.util.List;

/**
 * Created by chenlongfei on 15/8/16.
 */
public class CheckVersionService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void checkVersion() {
        AVQuery<AVObject> query = new AVQuery<AVObject>("Version");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    AVObject avObject = list.get(0);
                    String url = avObject.getAVFile("url").getUrl();
                    String str_versionCode = avObject.getString("versionCode");
                    Log.i("Troy", String.format("url:%1$s,versionVode:%2$s", url, str_versionCode));
                    int versionCode = Integer.parseInt(str_versionCode);
                    if (versionCode > CommonUtil.getAppVersionCode()) {
                        Intent intent = new Intent(CheckVersionService.this, DownLoadService.class);
                        intent.putExtra("url", url);
                        startService(intent);
                    }

                }
            }
        });
        startService(new Intent(this, DownLoadService.class));
    }
}
