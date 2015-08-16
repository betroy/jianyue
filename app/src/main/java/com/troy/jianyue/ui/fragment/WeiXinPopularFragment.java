package com.troy.jianyue.ui.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.troy.jianyue.R;
import com.troy.jianyue.ui.activity.MainActivity;
import com.troy.jianyue.ui.activity.WeiXinDetailActivity;
import com.troy.jianyue.adapter.WeiXinAdapter;
import com.troy.jianyue.bean.WeiXin;
import com.troy.jianyue.db.DBManager;
import com.troy.jianyue.util.GsonUtil;
import com.troy.jianyue.util.OkHttpUtil;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 15/5/8.
 */
public class WeiXinPopularFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String URL = "http://v.juhe.cn/weixin/query?key=03c88d230bd2586bcad08acee6de402a&dtype=json&pno=%1$d&ps=%2$d";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WeiXinAdapter mWeiXinAdapter;
    private List<WeiXin> mListWeiXin = new ArrayList<WeiXin>();
    private LinearLayoutManager mLinearLayoutManager;
    private static final int PS = 6;  //每页item数量
    private static final int PNO = 1;  //第几页
    private boolean mIsLoading = false;
    private DBManager mDBManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar.setTitle(mTitles[0]);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_weixin, null);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) root.findViewById(R.id.weixin_recycler_view);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.weixin_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mWeiXinAdapter = new WeiXinAdapter(getActivity(), mListWeiXin);
        mRecyclerView.setAdapter(mWeiXinAdapter);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        addListener();
        initDBManager();
    }


    public void initDBManager() {
        mDBManager = new DBManager(getActivity());
        ContentValues contentValues = new ContentValues();
        contentValues.put("url", "1");
        contentValues.put("title", "title");
        mDBManager.insert(contentValues);
    }

    public void loadData() {
        mListWeiXin.clear();
        requestServer(URL, PNO, PS);
    }

    public void loadMoreData() {
        int pno = GsonUtil.getPno();
        int totalPage = GsonUtil.getTotalPage();
        if (pno < totalPage) {
            requestServer(URL, ++pno, PS);
        }
        mIsLoading = true;
    }

    public void requestServer(String url, int pno, int ps) {
        OkHttpUtil.request(String.format(url, pno, ps), new OkHttpUtil.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String json = response.body().string();
                final List<WeiXin> weiXinList = GsonUtil.jsonToWeiXinList(json);
                if (weiXinList != null) {
                    mListWeiXin.addAll(weiXinList);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mWeiXinAdapter.notifyDataSetChanged();
                            mIsLoading = false;
                        }
                    });
                }
            }
        });
    }

    private void addListener() {
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
//                Log.i("Troy", "onScrollStateChanged");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Log.i("Troy", "onScrolled");
                int lastVisiblePosition = mLinearLayoutManager.findLastVisibleItemPosition();
                int totalItem = mLinearLayoutManager.getItemCount();
                int firstVisiblePosition = mLinearLayoutManager.findFirstVisibleItemPosition();
//                Log.i("Troy", String.format("lastPosition:%1$d,totalItem:%2$d,firstPosition:%3$d", lastVisiblePosition, totalItem, firstVisiblePosition));
                if (dy > 0) {
                    if (lastVisiblePosition == mListWeiXin.size() && !mIsLoading) {
                        Log.i("Troy", "加载更多");
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
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

}
