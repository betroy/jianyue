package com.troy.jianyue.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.troy.jianyue.base.App;

/**
 * Created by chenlongfei on 15/8/28.
 */
public class NetUtil {
    public static Context mContext = App.mContext;

    public static boolean hasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }
}
