package com.troy.jianyue.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.cyberplayer.core.BVideoView;
import com.troy.jianyue.R;

import java.util.Locale;

/**
 * Created by chenlongfei on 15/8/26.
 */
public class VideoDetailActivity extends AppCompatActivity {
    private static final int UPDATE_TIME_PROGRESS_MESSAGE = 0;
    private static final int UPDATE_UI_MPROGRESSCONTROLLER_VISIBLE = 1;
    private static final int UPDATE_UI_MPROGRESSCONTROLLER_GONE_DELAY = 2;
    private UpDateProgressHandler mUpDateProgressHandler = new UpDateProgressHandler();
    private UpDateUIHandler mUpDateUIHandler = new UpDateUIHandler();
    private BVideoView mBVideoView;
    private Button mPlayOrPause;
    private Button mFullscreenOnOrOff;
    private SeekBar mSeekBar;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private LinearLayout mProgressController;
    private RelativeLayout mRelativeLayout;
    private PLAY_STATE mPlayState = PLAY_STATE.NONE;
    private String mVideoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        mVideoUrl = getIntent().getExtras().getString("url");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
        setupPlayer();
        addListener();
    }

    protected void initViews() {
        mBVideoView = (BVideoView) findViewById(R.id.activity_video_detail_videoview);
        mPlayOrPause = (Button) findViewById(R.id.activity_video_detail_btn_play);
        mPlayOrPause.setBackgroundResource(R.drawable.btn_pause_select);
        mFullscreenOnOrOff = (Button) findViewById(R.id.activity_video_detail_btn_fullscreen_on);
        mSeekBar = (SeekBar) findViewById(R.id.activity_video_detail_seekbar);
        mCurrentTime = (TextView) findViewById(R.id.activity_video_detail_time_current);
        mTotalTime = (TextView) findViewById(R.id.activity_video_detail_time_total);
        mProgressController = (LinearLayout) findViewById(R.id.activity_video_detail_progress_control);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.activity_video_detail_media_controller);
    }

    public void setupPlayer() {
        mBVideoView.setVideoPath(mVideoUrl);
        mBVideoView.setVideoScalingMode(BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        mBVideoView.seekTo(0);
        mBVideoView.start();
    }

    public void addListener() {
        BVideoViewListener bVideoViewListener = new BVideoViewListener();
        mBVideoView.setOnCompletionListener(bVideoViewListener);
        mBVideoView.setOnPreparedListener(bVideoViewListener);
        mBVideoView.setOnErrorListener(bVideoViewListener);
        mPlayOrPause.setOnClickListener(onClickListener);
        mFullscreenOnOrOff.setOnClickListener(onClickListener);
        mBVideoView.setOnTouchListener(onTouchListener);
        mSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    public void updateTextView(int currentTime, int totalTime) {
        mCurrentTime.setText(formatTime(currentTime));
        mTotalTime.setText(formatTime(totalTime));
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

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TIME_PROGRESS_MESSAGE:
                    int currentTime = mBVideoView.getCurrentPosition();
                    int totalTime = mBVideoView.getDuration();
                    Log.i("Troy", "totalTime:" + totalTime);
                    Log.i("Troy", "currentTime:" + currentTime);
                    mSeekBar.setMax(totalTime);
                    mSeekBar.setProgress(currentTime);
                    updateTextView(currentTime, totalTime);
                    sendEmptyMessageDelayed(UPDATE_TIME_PROGRESS_MESSAGE, 200);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class UpDateUIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_UI_MPROGRESSCONTROLLER_VISIBLE:
                    mProgressController.setVisibility(View.VISIBLE);
                    break;
                case UPDATE_UI_MPROGRESSCONTROLLER_GONE_DELAY:
                    mProgressController.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_video_detail_btn_play:
                    playOrPauseVideo();
                    break;

            }
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mBVideoView.isPlaying())
                hideOrShowController();
            return false;
        }
    };

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateTextView(progress, seekBar.getMax());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mUpDateProgressHandler.removeMessages(UPDATE_TIME_PROGRESS_MESSAGE);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mBVideoView.seekTo(seekBar.getProgress());
            mUpDateProgressHandler.sendEmptyMessage(UPDATE_TIME_PROGRESS_MESSAGE);
        }
    };

    private class BVideoViewListener implements BVideoView.OnCompletionListener, BVideoView.OnPreparedListener, BVideoView.OnErrorListener {

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
            mUpDateUIHandler.sendEmptyMessage(UPDATE_UI_MPROGRESSCONTROLLER_VISIBLE);
            mUpDateUIHandler.sendEmptyMessageDelayed(UPDATE_UI_MPROGRESSCONTROLLER_GONE_DELAY, 3000);
            mUpDateProgressHandler.sendEmptyMessage(UPDATE_TIME_PROGRESS_MESSAGE);
        }
    }

    public void fullScreenOnOrOff() {

    }

    public void playOrPauseVideo() {
        if (mBVideoView.isPlaying()) {
            mBVideoView.pause();
            mPlayOrPause.setBackgroundResource(R.drawable.btn_play_select);
            mPlayState = PLAY_STATE.PAUSE;
        } else {
            if (mPlayState == PLAY_STATE.NONE) {
                mBVideoView.start();
            } else {
                mBVideoView.resume();
            }
            mPlayState = PLAY_STATE.PLAY;
            mProgressController.setVisibility(View.VISIBLE);
            mPlayOrPause.setBackgroundResource(R.drawable.btn_pause_select);
            mPlayOrPause.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressController.setVisibility(View.GONE);
                }
            }, 3000);
        }
    }

    public void hideMediaController() {

    }

    public void hideOrShowController() {
        mPlayOrPause.setVisibility(View.VISIBLE);
        mProgressController.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPlayState != PLAY_STATE.PAUSE) {
                    mPlayOrPause.setVisibility(View.GONE);
                }
                mProgressController.setVisibility(View.GONE);
            }
        }, 3000);
    }

    public static enum PLAY_STATE {
        NONE, PAUSE, PLAY
    }

    @Override
    protected void onDestroy() {
        mUpDateUIHandler.removeMessages(UPDATE_UI_MPROGRESSCONTROLLER_VISIBLE);
        mUpDateUIHandler.removeMessages(UPDATE_UI_MPROGRESSCONTROLLER_GONE_DELAY);
        mUpDateProgressHandler.removeMessages(UPDATE_TIME_PROGRESS_MESSAGE);
        super.onDestroy();
    }
}
