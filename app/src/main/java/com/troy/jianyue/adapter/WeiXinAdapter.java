package com.troy.jianyue.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.troy.jianyue.R;
import com.troy.jianyue.bean.WeiXin;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by chenlongfei on 15/5/10.
 */
public class WeiXinAdapter extends BaseAdapterRecyclerView<WeiXinAdapter.ViewHolder> {
    private Context mContext;
    private List<WeiXin> mWeiXinList;
    private OnItemClickListener mOnItemClickListener;
    private ImageLoader mImageLoader;
    private LayoutInflater mInflater;
    private int mLastItemCount;
    private int mLastPosition = -1;
    private static final int VIEW_TYPE_FOODER = -1;

    public WeiXinAdapter(Context context, List<WeiXin> list) {
        mContext = context;
        mWeiXinList = list;
        mImageLoader = ImageLoader.getInstance();
        mInflater = LayoutInflater.from(mContext);
        mLastItemCount = mWeiXinList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == VIEW_TYPE_FOODER) {
            view = mInflater.inflate(R.layout.adapter_loading_more, null);
        } else {
            view = mInflater.inflate(R.layout.adapter_fragment_weixin, null);
        }
        ViewHolder viewHolder = new ViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < mWeiXinList.size()) {
            holder.mTitle.setText(mWeiXinList.get(position).getTitle());
            holder.mSource.setText(mWeiXinList.get(position).getSource());
            mImageLoader.displayImage(mWeiXinList.get(position).getFirstImg(), holder.mFirstImg);
            holder.mItemtView.setOnClickListener(this);
            holder.mItemtView.setTag(position);
        } else {
            //数据全部加载完毕，提示”没有更多了...”,否则提示"正在加载..."
            if (mLastItemCount == mWeiXinList.size()) {
                holder.mLoadMore.setText("没有更多了...");
            } else {
                holder.mLoadMore.setText("正在加载...");
            }
            mLastItemCount = mWeiXinList.size();
        }
        if (position > mLastPosition) {
            startAnimation(holder.mItemtView);
            mLastPosition = position;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == mWeiXinList.size()) {
            return VIEW_TYPE_FOODER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mWeiXinList.size() > 0 ? mWeiXinList.size() + 1 : mWeiXinList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView mTitle;
        protected TextView mSource;
        protected TextView mLoadMore;
        protected ImageView mFirstImg;
        protected View mItemtView;
        protected int viewType;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            mItemtView = itemView;
            if (viewType != VIEW_TYPE_FOODER) {
                mTitle = (TextView) itemView.findViewById(R.id.adapter_weixin_title);
                mSource = (TextView) itemView.findViewById(R.id.adapter_weixin_source);
                mFirstImg = (ImageView) itemView.findViewById(R.id.adapter_weixin_first_img);
            } else {
                mLoadMore = (TextView) itemView.findViewById(R.id.adapter_loadmore);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        mOnItemClickListener.onItemClick((int) v.getTag());
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
