package com.troy.jianyue.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.troy.jianyue.R;
import com.troy.jianyue.adapter.PictureAdapter;
import com.troy.jianyue.bean.Picture;
import com.troy.jianyue.cache.PictureCacheHelper;
import com.troy.jianyue.json.PictureJSONParser;
import com.troy.jianyue.util.NetUtil;
import com.troy.jianyue.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 15/5/8.
 */
public class PicturePopularFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PictureAdapter mPictureAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Picture> mPictureList = new ArrayList<Picture>();
    private static final int LIMIT = 5;
    private int mSkip = 0;
    private int mPage = 1;
    private boolean mIsLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            mActionBar.setTitle(mTitles[1]);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_picture, null);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mPictureAdapter = new PictureAdapter(getActivity(), mPictureList);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.picture_recycler_view);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mPictureAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.picture_swiperefresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        addListener();
    }

    @Override
    public void loadData() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mPictureList.clear();
        if (NetUtil.hasNetwork()) {
            PictureCacheHelper.getInstance().cleanAllCache();
            requestServer();
        } else {
            loadDataForCache(mPage);
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    @Override
    public void loadMoreData() {
        ++mPage;
        mSkip = mPictureList.size();
        if (NetUtil.hasNetwork()) {
            requestServer();
        } else {
            loadDataForCache(mPage);
        }
    }

    @Override
    public void loadDataForCache(int page) {
        List<Picture> pictureList = PictureCacheHelper.getInstance().readCacheForPage(page);
        mPictureList.addAll(pictureList);
        mPictureAdapter.notifyDataSetChanged();
        mIsLoading = false;
    }

    private void requestServer() {
        AVQuery<Picture> pictureAVQuery = AVObject.getQuery(Picture.class);
        pictureAVQuery.setLimit(LIMIT);
        pictureAVQuery.setSkip(mSkip);
        pictureAVQuery.orderByDescending("createdAt");
        pictureAVQuery.findInBackground(new FindCallback<Picture>() {
            @Override
            public void done(List<Picture> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                mIsLoading = false;
                if (e == null) {
                    if (list.size() > 0) {
                        mPictureList.addAll(list);
                        mPictureAdapter.notifyDataSetChanged();
                        PictureJSONParser pictureJSONParser = new PictureJSONParser();
                        PictureCacheHelper.getInstance().wirteCacheForPage(mPage, pictureJSONParser.PictureListToJSON(list));
                    }
                } else {
                    ToastUtil.show("数据加载失败");
                }
            }

        });
    }


    @Override
    public void addListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                int totalItem = mLinearLayoutManager.getItemCount();
                if (dy > 0) {
                    if (lastVisibleItemPosition == mPictureList.size() && !mIsLoading) {
                        mIsLoading = true;
                        loadMoreData();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 1000);
    }
}
