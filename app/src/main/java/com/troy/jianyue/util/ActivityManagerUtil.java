package com.troy.jianyue.util;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by chenlongfei on 15/8/13.
 */
public class ActivityManagerUtil {
    private static ActivityManagerUtil mInstance;
    private Stack<Activity> mActivityStack = new Stack<Activity>();

    public static ActivityManagerUtil getInstance() {
        if (mInstance == null) {
            mInstance = new ActivityManagerUtil();
        }
        return mInstance;
    }

    public Stack<Activity> getAllStack() {
        return mActivityStack;
    }

    public void addToStack(Activity activity) {
        if (activity != null) {
            mActivityStack.add(activity);
        }
    }

    public void finshActivity(Activity activity) {
        if (activity != null) {
            if (mActivityStack.equals(activity)) {
                mActivityStack.remove(activity);
            }
            activity.finish();
        }
    }

    public void finshActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                finshActivity(activity);
            }
        }
    }

    public void finshAllActivity() {
        for (Activity activity : mActivityStack) {
            finshActivity(activity);
        }
        mActivityStack.clear();
    }
}
