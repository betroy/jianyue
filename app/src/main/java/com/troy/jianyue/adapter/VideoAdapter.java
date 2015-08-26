package com.troy.jianyue.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.cyberplayer.core.BVideoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.troy.jianyue.R;
import com.troy.jianyue.bean.Video;
import com.troy.jianyue.ui.activity.VideoDetailActivity;

import java.util.List;
import java.util.Locale;
import java.util.Timer;

/**
 * Created by chenlongfei on 15/5/16.
 */
public class VideoAdapter extends BaseAdapterRecyclerView<VideoAdapter.ViewHolder> {
    private Context mContext;
    private List<Video> mVideoList;
    private static final int VIEW_TYPE_FOODER = -1;
    private int mLastItemCount;
    private int mLastPosition = -1;
    private ImageLoader mImageLoader;

    public VideoAdapter(Context context, List<Video> list) {
        this.mContext = context;
        this.mVideoList = list;
        mLastItemCount = mVideoList.size();
        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == VIEW_TYPE_FOODER) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_loading_more, null);
        } else {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_fragment_video, null);
        }
        ViewHolder viewHolder = new ViewHolder(itemView, viewType);
        return viewHolder;
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.mItemtView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(VideoAdapter.ViewHolder holder, final int position) {
        if (position < mVideoList.size()) {
            holder.mTitle.setText(mVideoList.get(position).getSummary());
            mImageLoader.displayImage(mVideoList.get(position).getThumbnailsUrl(), holder.mThumbnails);
            holder.mPlayOrPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, VideoDetailActivity.class);
                    intent.putExtra("url", mVideoList.get(position).getVideoUrl());
                    mContext.startActivity(intent);
                }
            });
        } else {
            //数据全部加载完毕，提示”没有更多了...”,否则提示"正在加载..."
            if (mLastItemCount == mVideoList.size()) {
                holder.mLoadMore.setText("没有更多了...");
            } else {
                holder.mLoadMore.setText("正在加载...");
            }
            mLastItemCount = mVideoList.size();
        }
        if (position > mLastPosition) {
            startAnimation(holder.mItemtView);
            mLastPosition = position;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mVideoList.size()) {
            return VIEW_TYPE_FOODER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mVideoList.size() > 0 ? mVideoList.size() + 1 : mVideoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected Button mPlayOrPause;
        protected TextView mTitle;
        protected TextView mLoadMore;
        protected ImageView mThumbnails;
        protected View mItemtView;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            mItemtView = itemView;
            if (viewType != VIEW_TYPE_FOODER) {
                mPlayOrPause = (Button) itemView.findViewById(R.id.adapter_video_btn_play);
                mTitle = (TextView) itemView.findViewById(R.id.adapter_video_title);
                mThumbnails = (ImageView) itemView.findViewById(R.id.adapter_video_thumbnails);
            } else {
                mLoadMore = (TextView) itemView.findViewById(R.id.adapter_loadmore);
            }
        }
    }

}
