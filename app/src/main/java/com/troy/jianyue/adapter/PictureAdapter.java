package com.troy.jianyue.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.troy.jianyue.R;
import com.troy.jianyue.bean.Picture;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by chenlongfei on 15/5/14.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {
    private Context mContext;
    private List<Picture> mPictureList;
    private ImageLoader mImageLoader;
    private static final int VIEW_TYPE_FOODER = -1;

    public PictureAdapter(Context context, List<Picture> list) {
        mContext = context;
        mPictureList = list;
        mImageLoader = ImageLoader.getInstance();
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
        if (position != mPictureList.size()) {
            holder.mSummary.setText(mPictureList.get(position).getSummary());
            mImageLoader.displayImage(mPictureList.get(position).getUrl(), holder.mPicture);
        }
    }

    @Override
    public int getItemCount() {
        return mPictureList.size() + 1;
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

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType != VIEW_TYPE_FOODER) {
                mPicture = (ImageView) itemView.findViewById(R.id.adapter_picture_imageview);
                mSummary = (TextView) itemView.findViewById(R.id.adapter_picture_summary);
            }
        }
    }
}
