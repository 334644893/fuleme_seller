package com.fuleme.business.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.ScrollWebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebActivity extends BaseActivity {
    private static final String TAG = "WebActivity";
    public static String url = "";
    public static String title = "";
    @Bind(R.id.activity_web)
    LinearLayout activityWeb;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    private boolean errFlag;
    ScrollWebView webView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        tvTitle.setText(title);
        webView1 = new ScrollWebView(WebActivity.this);
        activityWeb.addView(webView1);
        webView1.setVerticalScrollBarEnabled(false);
        webView1.setHorizontalScrollBarEnabled(false);
        webView1.getSettings().setJavaScriptEnabled(true); //加上这句话才能使用JavaScript方法
        showLoading("加载中...");
        //WebView加载web资源
        webView1.loadUrl(url);
        LogUtil.d(url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                LogUtil.d("-----访问网址111-----", url);

                if (url.contains("?")) {
                    url = url + "&token=" + App.token;
                } else {
                    url = url + "?&token=" + App.token;
                }
                LogUtil.d("-----访问网址222-----", url);
                view.loadUrl(url);
                return true;
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                closeLoading();//取消等待框
                if (errFlag) {
                    webView1.setVisibility(View.GONE);
                    errFlag = false;
                } else {
                    webView1.setVisibility(View.VISIBLE);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                ToastUtil.showMessage("网络出错了");
                errFlag = true;
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView1.canGoBack()) {
            webView1.goBack();
            return true;
        }
        finish();
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();
        webView1.removeAllViews();
        webView1.destroy();
    }

    @OnClick(R.id.tv_left)
    public void onClick() {
//        if (webView1.canGoBack()) {
//            webView1.goBack();
//        } else {
            finish();
//        }

    }
}
