package com.troy.jianyue.ui.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.troy.jianyue.R;

/**
 * Created by chenlongfei on 15/5/10.
 */
public class WeiXinDetailActivity extends BaseActivity {
    private WebView mWebView;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixin_detail);
        mUrl = getIntent().getStringExtra("url");
        initViews();
        setViews();
    }

    @Override
    protected void initViews() {
        mWebView = (WebView) findViewById(R.id.webview);
    }

    @Override
    protected void initData() {

    }

    private void setViews() {
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setHorizontalScrollbarOverlay(true);
        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mUrl);
    }

}
