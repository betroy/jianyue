package com.troy.jianyue.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.troy.jianyue.base.App;

/**
 * Created by chenlongfei on 15/5/9.
 */
public class AppInfoUtil {
    public static Context mContext = App.mContext;

    public static String getAppVersionName() {
        PackageManager pm = mContext.getPackageManager();
        try {
            return pm.getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "未知";
        }
    }

    public static int  getAppVersionCode(){
        PackageManager pm=mContext.getPackageManager();
        try {
            return pm.getPackageInfo(mContext.getPackageName(),0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
