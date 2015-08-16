package com.troy.jianyue.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.troy.jianyue.bean.WeiXin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 15/5/26.
 */
public class DBManager {
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DBManager(Context context) {
        mDBHelper = new DBHelper(context);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void insert(ContentValues contentValues) {
        mDB.insert("weixin",null,contentValues);
    }

    public void delete() {
        mDB.delete("weixin","_id=1",null);

    }

    public void update(ContentValues contentValues) {
        mDB.update("weixin",contentValues,"_id=1",null);
    }

    public  List<WeiXin> query() {
        Cursor cursor = mDB.query("weixin", null, null, null, null, null, null);
        List<WeiXin> weiXinList = new ArrayList<WeiXin>();
        while (cursor.moveToNext()) {
            WeiXin weiXin = new WeiXin();
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            weiXin.setUrl(url);
            weiXin.setTitle(title);
            weiXinList.add(weiXin);
        }
        cursor.close();
        return weiXinList;
    }
}
