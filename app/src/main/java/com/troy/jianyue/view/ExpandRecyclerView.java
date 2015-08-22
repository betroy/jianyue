package com.troy.jianyue.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by chenlongfei on 15/8/20.
 */
public class ExpandRecyclerView extends RecyclerView {
    private boolean isPauseOnScrollListener;
    private ImageLoader mImageLoader;
    private OnLoadMroeScrollListener mOnLoadMroeScrollListener;
    private boolean mPauserOnScroll;
    private boolean mPauseOnFling;
    private boolean mIsLoading;

    public ExpandRecyclerView(Context context) {
        this(context, null);
    }

    public ExpandRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnScrollListener(new LoadMroeScrollListener());
    }


    private class LoadMroeScrollListener extends OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (isPauseOnScrollListener) {
                switch (newState) {
                    //滑动停止
                    case RecyclerView.SCROLL_STATE_IDLE:
                        mImageLoader.resume();
                        break;
                    //滑动
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        if (mPauserOnScroll) {
                            mImageLoader.pause();
                        }
                        break;
                    //快速滑动
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        if (mPauseOnFling) {
                            mImageLoader.pause();
                        }
                        break;
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = linearLayoutManager.getItemCount();
                if (dy > 0 && lastVisibleItemPosition >= totalItemCount && !mIsLoading) {
                    if (mOnLoadMroeScrollListener != null) {
                        mOnLoadMroeScrollListener.loadMore();
                        mIsLoading = true;
                    }
                }
            }
        }
    }

    public void loadFinsh() {
        mIsLoading = false;
    }

    public void setPauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
        isPauseOnScrollListener = true;
        if (imageLoader == null) {
            new IllegalArgumentException("ImageLoader不能为空");
        }
        mImageLoader = imageLoader;
        mPauserOnScroll = pauseOnScroll;
        mPauseOnFling = pauseOnFling;
    }

    public void setLoadMoreScrollListener(OnLoadMroeScrollListener onLoadMroeScrollListener) {
        mOnLoadMroeScrollListener = onLoadMroeScrollListener;
    }

    public interface OnLoadMroeScrollListener {
        void loadMore();
    }

}
