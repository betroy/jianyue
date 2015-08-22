package com.troy.jianyue.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.cyberplayer.core.BVideoView;
import com.troy.jianyue.R;
import com.troy.jianyue.bean.Video;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenlongfei on 15/5/16.
 */
public class VideoAdapter extends RecyclerViewBaseAdapter<VideoAdapter.ViewHolder> implements BVideoView.OnCompletionListener, BVideoView.OnPreparedListener, BVideoView.OnErrorListener {
    private Context mContext;
    private List<Video> mVideoList;
    private static final String AK = "EpSSwLhuvrMBwPDzd0GYo3LG";
    private static final String SK = "L9KrmivPef4DocCh";
    private static final int UPDATE_TIME_PROGRESS_MESSAGE = 0;
    private PLAY_STATE mPlayState = PLAY_STATE.NONE;
    private Handler handler = new Handler();
    private Timer mTimer;
    private UpDateProgressHandler mUpDateProgressHandler;
    private static final int VIEW_TYPE_FOODER = -1;
    private int mLastItemCount;
    private int mLastPosition = -1;

    public VideoAdapter(Context context, List<Video> list) {
        this.mContext = context;
        this.mVideoList = list;
        mLastItemCount = mVideoList.size();
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
        holder.itemView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(VideoAdapter.ViewHolder holder, int position) {
        if (position < mVideoList.size()) {
            initPlayer(holder, position);
            addLisener(holder);
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
            startAnimation(holder.itemView);
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
        protected BVideoView mBVideoView;
        protected Button mPlayOrPause;
        protected Button mFullscreenOnOrOff;
        protected TextView mCurrentTime;
        protected TextView mTotalTime;
        protected SeekBar mSeekBar;
        protected RelativeLayout mMediaController;
        protected TextView mLoadMore;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType != VIEW_TYPE_FOODER) {
                mBVideoView = (BVideoView) itemView.findViewById(R.id.adapter_video_b_videoview);
                mPlayOrPause = (Button) itemView.findViewById(R.id.adapter_video_btn_play);
                mFullscreenOnOrOff = (Button) itemView.findViewById(R.id.adapter_video_btn_fullscreen_on);
                mTotalTime = (TextView) itemView.findViewById(R.id.adapter_video_time_total);
                mCurrentTime = (TextView) itemView.findViewById(R.id.adapter_video_time_current);
                mSeekBar = (SeekBar) itemView.findViewById(R.id.adapter_video_seekbar);
                mMediaController = (RelativeLayout) itemView.findViewById(R.id.adapter_video_media_controller);
            } else {
                mLoadMore = (TextView) itemView.findViewById(R.id.adapter_loadmore);
            }
        }
    }

    public void initPlayer(ViewHolder viewHolder, int position) {
        BVideoView.setAKSK(AK, SK);
        viewHolder.mBVideoView.setOnCompletionListener(this);
        viewHolder.mBVideoView.setOnPreparedListener(this);
        viewHolder.mBVideoView.setOnErrorListener(this);
        viewHolder.mBVideoView.setVideoPath(mVideoList.get(position).getUrl());
        viewHolder.mBVideoView.setVideoScalingMode(BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        viewHolder.mBVideoView.seekTo(0);
        mUpDateProgressHandler = new UpDateProgressHandler(viewHolder);
    }

    public void addLisener(final ViewHolder viewHolder) {
        viewHolder.mPlayOrPause.setOnClickListener(new MyOnClickListener(viewHolder));
        viewHolder.mFullscreenOnOrOff.setOnClickListener(new MyOnClickListener(viewHolder));
        viewHolder.mBVideoView.setOnTouchListener(new MyOnTouchListener(viewHolder));
        viewHolder.mSeekBar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener(viewHolder));
    }

    public void updateTextView(ViewHolder viewHolder, int currentTime, int totalTime) {
        viewHolder.mCurrentTime.setText(formatTime(currentTime));
        viewHolder.mTotalTime.setText(formatTime(totalTime));
    }

    /**
     * 毫秒转换 mm：ss格式方法
     *
     * @param max
     * @return
     */
    public String converLongTimeToStr(long time) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;

        long hour = (time) / hh;
        long minute = (time - hour * hh) / mi;
        long second = (time - hour * hh - minute * mi) / ss;

        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        if (hour > 0) {
            return strHour + ":" + strMinute + ":" + strSecond;
        } else {
            return strMinute + ":" + strSecond;
        }
    }


    public String formatTime(int time) {
        int hour = time / 360;
        int minute = time / 60;
        int second = time % 60 != 01 ? time % 60 : time > 59 ? 0 : time;
        String strTime = null;
        if (hour != 0) {
            strTime = String.format(Locale.CHINA, "%02d:%02d:%02d", hour, minute, second);
        } else {
            strTime = String.format(Locale.CHINA, "%02d:%02d", minute, second);
        }
        return strTime;
    }

    public class UpDateProgressHandler extends Handler {
        public ViewHolder viewHolder;

        public UpDateProgressHandler(ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TIME_PROGRESS_MESSAGE:
                    int currentTime = viewHolder.mBVideoView.getCurrentPosition();
                    int totalTime = viewHolder.mBVideoView.getDuration();
                    Log.i("Troy", "totalTime:" + totalTime);
                    Log.i("Troy", "currentTime:" + currentTime);
                    viewHolder.mSeekBar.setMax(totalTime);
                    viewHolder.mSeekBar.setProgress(currentTime);
                    updateTextView(viewHolder, currentTime, totalTime);
                    sendEmptyMessageDelayed(UPDATE_TIME_PROGRESS_MESSAGE, 200);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class MyOnTouchListener implements View.OnTouchListener {
        private ViewHolder viewHolder;

        public MyOnTouchListener(ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            hideOrShowController(viewHolder);
            return false;
        }
    }

    public class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        private ViewHolder viewHolder;

        public MyOnSeekBarChangeListener(ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateTextView(viewHolder, progress, seekBar.getMax());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mUpDateProgressHandler.removeMessages(UPDATE_TIME_PROGRESS_MESSAGE);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            viewHolder.mBVideoView.seekTo(seekBar.getProgress());
            mUpDateProgressHandler.sendEmptyMessage(UPDATE_TIME_PROGRESS_MESSAGE);
        }
    }

    public class MyOnClickListener implements View.OnClickListener {
        private ViewHolder viewHolder;

        public MyOnClickListener(ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.adapter_video_btn_play:
                    playOrPauseVideo(viewHolder);
                    break;
                case R.id.adapter_video_btn_fullscreen_on:
                    break;
            }
        }
    }

    public void fullScreenOnOrOff() {

    }

    public void playOrPauseVideo(ViewHolder viewHolder) {
        if (viewHolder.mBVideoView.isPlaying()) {
            viewHolder.mBVideoView.pause();
            viewHolder.mPlayOrPause.setBackgroundResource(R.drawable.btn_play_select);
            mPlayState = PLAY_STATE.PAUSE;
        } else {
            if (mPlayState == PLAY_STATE.NONE) {
                viewHolder.mBVideoView.start();
            } else {
                viewHolder.mBVideoView.resume();

            }
            mPlayState = PLAY_STATE.PLAY;
            viewHolder.mPlayOrPause.setBackgroundResource(R.drawable.btn_pause_select);
        }

    }

    public void hideMediaController() {

    }

    public void hideOrShowController(final ViewHolder viewHolder) {
        if (viewHolder.mMediaController.getVisibility() == View.VISIBLE) {
            viewHolder.mMediaController.setVisibility(View.INVISIBLE);
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
        } else {
            viewHolder.mMediaController.setVisibility(View.VISIBLE);
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.mMediaController.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }, 3000);
        }
    }

    @Override
    public void onCompletion() {
        Log.i("Troy", "onCompletion");
    }

    @Override
    public boolean onError(int what, int extra) {
        Log.i("Troy", "onError");
        return false;
    }

    @Override
    public void onPrepared() {
        Log.i("Troy", "onPrepared");
        mUpDateProgressHandler.sendEmptyMessage(UPDATE_TIME_PROGRESS_MESSAGE);
    }

    public static enum PLAY_STATE {
        NONE, PAUSE, PLAY
    }
}
