package com.troy.jianyue.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.troy.jianyue.R;
import com.troy.jianyue.util.Appearance;

/**
 * Created by chenlongfei on 15/4/30.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
    }

    protected abstract void initViews();

    protected abstract void initData();

    //设置状态栏颜色
    public void setStatusBar() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup rootView = (ViewGroup) window.getDecorView();
            View statusBarBg;
            FrameLayout.LayoutParams layoutParams;
            if (rootView.getChildCount() > 1 && (statusBarBg = rootView.getChildAt(1)) != null && statusBarBg.getId() == R.id.startus_bar_bg) {
                layoutParams = (FrameLayout.LayoutParams) statusBarBg.getLayoutParams();
                layoutParams.height = Appearance.getStatusBarHeight();
                layoutParams.gravity = Gravity.TOP;
                statusBarBg.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                statusBarBg.setLayoutParams(layoutParams);
            } else {
                layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, Appearance.getStatusBarHeight());
                layoutParams.gravity = Gravity.TOP;
                statusBarBg = new View(this);
                statusBarBg.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                statusBarBg.setId(R.id.startus_bar_bg);
                rootView.addView(statusBarBg, layoutParams);
            }
        }
    }
}
