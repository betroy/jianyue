package com.troy.jianyue.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by chenlongfei on 15/5/26.
 */
public class DBHelper extends SQLiteOpenHelper{
    private static final String DB_NAME="jianyue.db";
    private static final int DB_VERSION=1;
    private static final String SQL="CREATE TABLE IF NOT EXISTS weixin"+" (_id INTEGER PRIMARY KEY AUTOINCREMENT,title,url)";
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS weixin");
        onCreate(db);
    }
}
