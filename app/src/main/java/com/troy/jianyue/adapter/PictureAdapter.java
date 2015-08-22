package com.troy.jianyue.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.troy.jianyue.R;
import com.troy.jianyue.bean.Picture;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by chenlongfei on 15/5/14.
 */
public class PictureAdapter extends RecyclerViewBaseAdapter<PictureAdapter.ViewHolder> {
    private Context mContext;
    private List<Picture> mPictureList;
    private ImageLoader mImageLoader;
    private int mLastItemCount;
    private int mLastPosition = -1;
    private static final int VIEW_TYPE_FOODER = -1;

    public PictureAdapter(Context context, List<Picture> list) {
        mContext = context;
        mPictureList = list;
        mImageLoader = ImageLoader.getInstance();
        mLastItemCount = mPictureList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == VIEW_TYPE_FOODER) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_loading_more, null);
        } else {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_fragment_picture, null);
        }
        ViewHolder viewHolder = new ViewHolder(itemView, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < mPictureList.size()) {
            holder.mSummary.setText(mPictureList.get(position).getSummary());
            mImageLoader.displayImage(mPictureList.get(position).getUrl(), holder.mPicture);
        } else {
            //数据全部加载完毕，提示”没有更多了...”,否则提示"正在加载..."
            if (mLastItemCount == mPictureList.size()) {
                holder.mLoadMore.setText("没有更多了...");
            } else {
                holder.mLoadMore.setText("正在加载...");
            }
            mLastItemCount = mPictureList.size();
        }
        if (position > mLastPosition) {
            startAnimation(holder.itemView);
            mLastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mPictureList.size() > 0 ? mPictureList.size() + 1 : mPictureList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mPictureList.size()) {
            return VIEW_TYPE_FOODER;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mPicture;
        protected TextView mSummary;
        protected TextView mLoadMore;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType != VIEW_TYPE_FOODER) {
                mPicture = (ImageView) itemView.findViewById(R.id.adapter_picture_imageview);
                mSummary = (TextView) itemView.findViewById(R.id.adapter_picture_summary);
            } else {
                mLoadMore = (TextView) itemView.findViewById(R.id.adapter_loadmore);
            }
        }
    }
}
