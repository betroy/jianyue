package com.troy.jianyue.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.troy.jianyue.R;
import com.troy.jianyue.bean.MenuItem;
import com.troy.jianyue.util.DisplayUtil;

import java.util.List;

/**
 * Created by chenlongfei on 15/5/4.
 */
public class DrawerLayoutAdapter extends AdapterBase<MenuItem> {
    public DrawerLayoutAdapter(Context context, List<MenuItem> list) {
        super(context, list);
    }

    @Override
    public View getConvertView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_drawerlayout_list_item, null);
        }
        TextView textView = (TextView) convertView;
        textView.setText(((MenuItem) mList.get(position)).mTitle);
        Drawable drawable=((MenuItem) mList.get(position)).mDrawable;
        drawable.setBounds(0,0, DisplayUtil.dp2pix(24), DisplayUtil.dp2pix(24));
        textView.setCompoundDrawables(drawable, null, null, null);
        textView.setCompoundDrawablePadding(DisplayUtil.dp2pix(30));
        return convertView;
    }
}
