package com.troy.jianyue.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.avos.avoscloud.FindCallback;
import com.troy.jianyue.R;
import com.troy.jianyue.adapter.VideoAdapter;
import com.troy.jianyue.bean.Video;
import com.troy.jianyue.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 15/5/8.
 */
public class VideoPopularFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private VideoAdapter mVideoAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Video> mVideoList = new ArrayList<Video>();
    private boolean mIsLoading;
    private static final int LIMIT = 10;
    private int mSkip = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null)
        mActionBar.setTitle(mTitles[2]);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, null);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.vide_recycler_view);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mVideoAdapter = new VideoAdapter(getActivity(), mVideoList);
        mRecyclerView.setAdapter(mVideoAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.video_swipe_refresh_layout);
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
    public void addListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                int totalItem = mLinearLayoutManager.getItemCount();
                if (dy > 0) {
                    if (lastVisibleItemPosition == mVideoList.size() && mIsLoading) {
                        loadMoreData();
                    }
                }
            }
        });
    }

    @Override
    public void loadDataForCache(int page) {

    }

    @Override
    public void loadMoreData() {
        mSkip = mVideoList.size();
        mIsLoading = true;
        requestServer();
    }

    @Override
    public void loadData() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mVideoList.clear();
        requestServer();
    }

    public void requestServer() {
        AVQuery<Video> videoAVQuery = AVObject.getQuery(Video.class);
        videoAVQuery.setLimit(LIMIT);
        videoAVQuery.setSkip(mSkip);
        videoAVQuery.orderByDescending("createdAt");
        videoAVQuery.findInBackground(new FindCallback<Video>() {
            @Override
            public void done(List<Video> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                mIsLoading = false;
                if (e == null) {
                    mVideoList.addAll(list);
                    mVideoAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show("数据加载失败...");
                }
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
