package com.troy.jianyue.util;

import android.widget.Toast;

import com.troy.jianyue.base.App;

/**
 * Created by chenlongfei on 15/5/5.
 */
public class ToastUtil {
    public static void show(String msg) {
        Toast.makeText(App.mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
