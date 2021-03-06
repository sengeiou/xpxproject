package com.bigwiner.android.presenter;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.view.activity.SafeActivity;

import intersky.appbase.Presenter;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class SafePresenter implements Presenter {

    public SafeActivity mSafeActivity;
    public SafePresenter(SafeActivity mSafeActivity)
    {
        this.mSafeActivity = mSafeActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mSafeActivity, Color.argb(0, 255, 255, 255));
        mSafeActivity.setContentView(R.layout.activity_safe);
        mSafeActivity.mToolBarHelper.hidToolbar(mSafeActivity, (RelativeLayout) mSafeActivity.findViewById(R.id.buttomaciton));
        mSafeActivity.measureStatubar(mSafeActivity, (RelativeLayout) mSafeActivity.findViewById(R.id.stutebar));
        mSafeActivity.back = mSafeActivity.findViewById(R.id.back);
        mSafeActivity.back.setOnClickListener(mSafeActivity.backListener);
        mSafeActivity.webView = mSafeActivity.findViewById(R.id.show);
        mSafeActivity.webView.getSettings().setAllowFileAccess(true);
        // 开启脚本支持
        mSafeActivity.webView.getSettings().setJavaScriptEnabled(true);
        mSafeActivity.webView.setWebViewClient(mWebViewClient);
        mSafeActivity.webView.loadUrl("file:///android_asset/ys.html");
    }


    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {
        mSafeActivity.webView.destroy();
    }

    public void showSafe() {

    }

    public WebViewClient mWebViewClient = new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            // 当有新连接时使用当前的webview进行显示
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        // 开始加载网页时要做的工作
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mSafeActivity.waitDialog.show();
            super.onPageStarted(view, url, favicon);
        }

        // 加载完成时要做的工作
        public void onPageFinished(WebView view, String url) {
            mSafeActivity.waitDialog.hide();
            super.onPageFinished(view, url);

        }

        // 加载错误时要做的工作
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mSafeActivity.waitDialog.hide();
        }
    };
}
