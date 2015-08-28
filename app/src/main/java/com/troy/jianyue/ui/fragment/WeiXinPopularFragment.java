package com.troy.jianyue.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.troy.jianyue.R;
import com.troy.jianyue.adapter.WeiXinAdapter;
import com.troy.jianyue.bean.WeiXin;
import com.troy.jianyue.cache.WeiXinCacheHelper;
import com.troy.jianyue.json.WeiXinJSONParser;
import com.troy.jianyue.ui.activity.WeiXinDetailActivity;
import com.troy.jianyue.util.NetUtil;
import com.troy.jianyue.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 15/5/8.
 */
public class WeiXinPopularFragment extends BaseFragment {
    private static final int JUHE_DATA_ID = 147;
    private static final String JUHE_API_URL = "http://v.juhe.cn/weixin/query";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WeiXinAdapter mWeiXinAdapter;
    private List<WeiXin> mListWeiXin = new ArrayList<WeiXin>();
    private LinearLayoutManager mLinearLayoutManager;
    private static final int PS = 6;  //每页item数量
    private static final int PNO = 1;  //第几页
    private boolean mIsLoading = false;
    private int mTotalPage;
    private int mCurrentPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            mActionBar.setTitle(mTitles[0]);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_weixin, null);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.weixin_recycler_view);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.weixin_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mWeiXinAdapter = new WeiXinAdapter(getActivity(), mListWeiXin);
        mRecyclerView.setAdapter(mWeiXinAdapter);

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
        mListWeiXin.clear();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        if (NetUtil.hasNetwork()) {
            WeiXinCacheHelper.getInstance().cleanAllCache();
            requestServer(PNO, PS);
        } else {
            loadDataForCache(PNO);
        }
    }

    @Override
    public void loadMoreData() {
        int pno = mCurrentPage;
        int totalPage = mTotalPage;
        if (pno < totalPage) {
            if (NetUtil.hasNetwork()) {
                requestServer(++pno, PS);
            } else {
                loadDataForCache(++pno);
            }
        }
        mIsLoading = true;
    }

    @Override
    public void loadDataForCache(int page) {
        List<WeiXin> weiXinList = WeiXinCacheHelper.getInstance().readCacheForPage(page);
        mListWeiXin.addAll(weiXinList);
        mWeiXinAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                mIsLoading = false;
            }
        });
    }

    public void requestServer(final int pno, int ps) {
        Parameters param = new Parameters();
        param.add("pno", pno);
        param.add("ps", ps);
        JuheData.executeWithAPI(getActivity(), JUHE_DATA_ID, JUHE_API_URL, JuheData.GET, param, new DataCallBack() {
            @Override
            public void onSuccess(int statusCode, String responseString) {
                WeiXinJSONParser weiXinJSONParser = new WeiXinJSONParser(responseString);
                if (weiXinJSONParser.getErrorCode() == 0) {
                    mCurrentPage = weiXinJSONParser.getPno();
                    mTotalPage = weiXinJSONParser.getTotalPage();
                    WeiXinCacheHelper.getInstance().wirteCacheForPage(pno, responseString);
                    mListWeiXin.addAll(weiXinJSONParser.getWeiXinList());
                    mWeiXinAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show(weiXinJSONParser.getReason());
                }
            }

            @Override
            public void onFinish() {
                mSwipeRefreshLayout.setRefreshing(false);
                mIsLoading = false;
            }

            @Override
            public void onFailure(int statusCode, String responseString, Throwable throwable) {
                ToastUtil.show("数据加载失败...");
            }
        });
    }

    @Override
    public void addListener() {
        mWeiXinAdapter.setOnItemClickListener(new WeiXinAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), WeiXinDetailActivity.class);
                intent.putExtra("url", mListWeiXin.get(position).getUrl());
                startActivity(intent);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = mLinearLayoutManager.findLastVisibleItemPosition();
                int totalItem = mLinearLayoutManager.getItemCount();
                int firstVisiblePosition = mLinearLayoutManager.findFirstVisibleItemPosition();
//                Log.i("Troy", String.format("lastPosition:%1$d,totalItem:%2$d,firstPosition:%3$d", lastVisiblePosition, totalItem, firstVisiblePosition));
                if (dy > 0) {
                    if (lastVisiblePosition == mListWeiXin.size() && !mIsLoading) {
                        loadMoreData();
                    }
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
