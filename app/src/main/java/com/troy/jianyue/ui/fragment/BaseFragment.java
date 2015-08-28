package com.troy.jianyue.ui.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.troy.jianyue.R;
import com.troy.jianyue.ui.activity.MainActivity;

/**
 * Created by chenlongfei on 15/5/5.
 */
public abstract class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public ActionBar mActionBar;
    public String[] mTitles;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActionBar = ((MainActivity) activity).getSupportActionBar();
        mTitles = getResources().getStringArray(R.array.item_title);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            loadData();
            return true;
        }
        return false;
    }

    public abstract void loadData();

    public abstract void loadMoreData();

    public abstract void addListener();

    public abstract void loadDataForCache(int page);
}
