package com.troy.jianyue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by chenlongfei on 15/5/4.
 */
public abstract class AdapterBase<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mList;
    protected LayoutInflater mInflater;

    public AdapterBase(Context context, List<T> list) {
        mList = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void updata(List<T> list) {
        mList.clear();
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getConvertView(position, convertView, parent);
    }

    public abstract View getConvertView(int position, View convertView, ViewGroup parent);
}
