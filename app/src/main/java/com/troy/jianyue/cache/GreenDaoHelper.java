package com.troy.jianyue.cache;

import android.database.sqlite.SQLiteDatabase;

import com.troy.greendao.DaoMaster;
import com.troy.jianyue.base.App;

/**
 * Created by chenlongfei on 15/8/27.
 */
public class GreenDaoHelper {
    private static final String DB_NAME = "jianyue";
    private static GreenDaoHelper mInstance;
    private DaoMaster mDaoMaster;

    private GreenDaoHelper() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(App.mContext, DB_NAME, null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);

    }

    public static GreenDaoHelper getInstance() {
        if (mInstance == null) {
            mInstance = new GreenDaoHelper();
        }
        return mInstance;
    }




}
