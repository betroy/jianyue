package com.troy.jianyue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.baidu.cyberplayer.utils.T;

/**
 * Created by chenlongfei on 15/8/22.
 */
public class RecyclerViewBaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> implements View.OnClickListener {

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(T holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onClick(View v) {

    }

    public void startAnimation(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(500L);
        animationSet.setFillAfter(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
        view.startAnimation(animationSet);
    }
}
