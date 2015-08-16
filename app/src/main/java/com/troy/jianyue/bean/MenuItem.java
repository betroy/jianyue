package com.troy.jianyue.bean;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.troy.jianyue.ui.fragment.PicturePopularFragment;
import com.troy.jianyue.ui.fragment.VideoPopularFragment;
import com.troy.jianyue.ui.fragment.WeiXinPopularFragment;

/**
 * Created by chenlongfei on 15/5/4.
 */
public class MenuItem {
    public CharSequence mTitle;
    public Drawable mDrawable;
    public Class<? extends Fragment> mFragment;
    public Type mType;

    public enum Type {
        WEIXINPOPULAR_FRAGMENT, PICTUREPOPULAR_FRAGMENT, VIDEOPOPULAR_FRAGMENT, SETTING_ACTIVITY
    }

    public MenuItem(CharSequence title, Drawable drawable, Class<? extends Fragment> fragment, Type type) {
        mTitle = title;
        mDrawable = drawable;
        mFragment = fragment;
        mType = type;
    }
}
