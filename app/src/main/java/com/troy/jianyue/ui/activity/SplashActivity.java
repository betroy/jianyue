package com.troy.jianyue.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.troy.jianyue.R;
import com.troy.jianyue.util.CommonUtil;

/**
 * Created by chenlongfei on 15/4/29.
 */
public class SplashActivity extends Activity{
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mTextView=(TextView)findViewById(R.id.activity_splash_version);
        mTextView.setText("当前版本号:"+ CommonUtil.getAppVersionName());
        start();
    }

    private void start(){
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashActivity.this,MainActivity.class));
//                finish();
//            }
//        },200);

        CountDownTimer timer=new CountDownTimer(10*100,100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        };
        timer.start();
    }
}
