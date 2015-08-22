package com.troy.jianyue.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.thinkland.sdk.android.JuheSDKInitializer;
import com.troy.jianyue.bean.Picture;
import com.troy.jianyue.bean.Video;


/**
 * Created by chenlongfei on 15/4/29.
 */
public class App extends Application {
    public static Context mContext;
    public static final String APP_ID = "xlzayidaofty5p17cezdowdm8mj00sk4akdh3o6n0rh8axkv";
    public static final String APP_KEY = "opn407132fwi7x6vodnpu4h460e64rbkgt2qi930jbyqrxvp";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initImageLoad();
        AVObject.registerSubclass(Picture.class);
        AVObject.registerSubclass(Video.class);
        AVOSCloud.initialize(this, APP_ID, APP_KEY);
        JuheSDKInitializer.initialize(getApplicationContext());
    }

    private void initImageLoad() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).showImageOnLoading(new ColorDrawable(Color.TRANSPARENT)).build();
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(10 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.defaultDisplayImageOptions(options);
        ImageLoader.getInstance().init(config.build());
    }
}
