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
public class WeiXinAdapter extends RecyclerView.Adapter<WeiXinAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<WeiXin> mWeiXinList;
    private OnItemClickListener mOnItemClickListener;
    private ImageLoader mImageLoader;
    private LayoutInflater mInflater;
    private static final int VIEW_TYPE_FOODER = -1;

    public WeiXinAdapter(Context context, List<WeiXin> list) {
        mContext = context;
        mWeiXinList = list;
        mImageLoader = ImageLoader.getInstance();
        mInflater = LayoutInflater.from(mContext);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position != mWeiXinList.size()) {
            holder.mTitle.setText(mWeiXinList.get(position).getTitle());
            holder.mSource.setText(mWeiXinList.get(position).getSource());
            mImageLoader.displayImage(mWeiXinList.get(position).getFirstImg(), holder.mFirstImg);
            holder.mItemtView.setOnClickListener(this);
            holder.mItemtView.setTag(position);
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
        return mWeiXinList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView mTitle;
        protected TextView mSource;
        protected ImageView mFirstImg;
        protected View mItemtView;
        protected int viewType;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType != VIEW_TYPE_FOODER) {
                mItemtView = itemView;
                mTitle = (TextView) itemView.findViewById(R.id.adapter_weixin_title);
                mSource = (TextView) itemView.findViewById(R.id.adapter_weixin_source);
                mFirstImg = (ImageView) itemView.findViewById(R.id.adapter_weixin_first_img);
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
