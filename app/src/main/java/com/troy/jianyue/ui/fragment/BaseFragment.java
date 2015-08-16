package com.troy.jianyue.ui.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import com.troy.jianyue.R;
import com.troy.jianyue.ui.activity.MainActivity;

/**
 * Created by chenlongfei on 15/5/5.
 */
public abstract class BaseFragment extends Fragment {
    public ActionBar mActionBar;
    public String[] mTitles;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActionBar = ((MainActivity) activity).getSupportActionBar();
        mTitles = getResources().getStringArray(R.array.item_title);

    }
}
