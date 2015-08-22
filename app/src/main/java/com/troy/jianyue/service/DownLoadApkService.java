package com.troy.jianyue.service;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.troy.jianyue.R;

import java.io.File;

/**
 * Created by chenlongfei on 15/8/16.
 */
public class DownLoadApkService extends Service {
    private DownloadReceiver mDownloadReceiver;
    private DownloadManager mDownloadManager;
    private long mEnqueue = 0;
    private static final String APK_NAME = "jianyue.apk";

    @Override
    public void onCreate() {
        mDownloadReceiver = new DownloadReceiver();
        registerReceiver(mDownloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        download(url);
        //服务被kill,服务将自动重启，并传入最后一个Intent
        return START_REDELIVER_INTENT;
    }


    public class CheckVersionBinder extends Binder {
        public DownLoadApkService getInstance() {
            return DownLoadApkService.this;
        }
    }


    private void download(String url) {
        if (mEnqueue == 0) {
            mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle("简阅");
            request.setDescription("下载中...");
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, APK_NAME);
            request.setVisibleInDownloadsUi(true);
            mEnqueue = mDownloadManager.enqueue(request);
        }
    }

    private void instalApk() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), APK_NAME);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
            startActivity(intent);
        }
    }

    private void createNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(getResources().getString(R.string.app_name));
        builder.setContentText("下载中...");
        builder.setProgress(100, 50, false);
        notificationManager.notify(0, builder.build());
    }

    private class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(mEnqueue);
                Cursor cursor = mDownloadManager.query(query);
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                        instalApk();
                    }
                }
                stopSelf();
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mDownloadReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
