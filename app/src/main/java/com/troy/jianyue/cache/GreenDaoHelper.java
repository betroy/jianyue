package com.troy.jianyue.cache;

import android.database.sqlite.SQLiteDatabase;

import com.troy.greendao.DaoMaster;
import com.troy.greendao.DaoSession;
import com.troy.greendao.PictureCacheDao;
import com.troy.greendao.VideoCacheDao;
import com.troy.greendao.WeiXinCacheDao;
import com.troy.jianyue.base.App;

/**
 * Created by chenlongfei on 15/8/27.
 */
public class GreenDaoHelper {
    private static final String DB_NAME = "jianyue";
    private static GreenDaoHelper mInstance;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private static SQLiteDatabase db;

    private GreenDaoHelper() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(App.mContext, DB_NAME, null);
        db = devOpenHelper.getWritableDatabase();
    }

    public static GreenDaoHelper getInstance() {
        if (mInstance == null) {
            mInstance = new GreenDaoHelper();
        }
        return mInstance;
    }


    public static DaoMaster getDaoMaster() {
        if (mDaoMaster == null) {
            mDaoMaster = new DaoMaster(db);
        }
        return mDaoMaster;
    }

    public static DaoSession getDaoSeesion() {
        if (mDaoSession == null) {
            mDaoSession = getDaoMaster().newSession();
        }
        return mDaoSession;
    }

    public static WeiXinCacheDao getWeiXinCacheDao() {
        return getDaoSeesion().getWeiXinCacheDao();
    }

    public static PictureCacheDao getPictureCacheDao() {
        return getDaoSeesion().getPictureCacheDao();
    }

    public static VideoCacheDao getVideoCacheDao() {
        return getDaoSeesion().getVideoCacheDao();
    }

}
