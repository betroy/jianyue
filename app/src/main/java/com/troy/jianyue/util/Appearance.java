package com.troy.jianyue.util;

import android.content.Context;
import android.content.res.Resources;

import com.troy.jianyue.base.App;

/**
 * Created by chenlongfei on 15/5/3.
 */
public class Appearance {
    private static Context mContext;
    private static Resources mResources;
    private static float mDensity;

    static {
        mContext = App.mContext;
        mResources = mContext.getResources();
        mDensity = mResources.getDisplayMetrics().density;
    }

    public static int getStatusBarHeight() {
        int identifier = mContext.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (identifier > 0) {
            return mContext.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static int dp2pix(float dp) {
        return (int) (mDensity * dp + 0.5f);
    }

    public static float pix2dp(int pix) {
        return pix / mDensity;
    }
}
