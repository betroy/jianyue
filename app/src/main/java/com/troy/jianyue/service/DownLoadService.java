package com.troy.jianyue.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * Created by chenlongfei on 15/5/25.
 */
public class DownLoadService extends Service {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        return super.onStartCommand(intent, flags, startId);
    }

    public class DownLoadTask extends AsyncTask<String,Integer,Void>{

        @Override
        protected Void doInBackground(String... params) {
            return null;
        }
    }


    public class MyBinder extends Binder {
        DownLoadService getService() {
            return DownLoadService.this;
        }
    }
}
