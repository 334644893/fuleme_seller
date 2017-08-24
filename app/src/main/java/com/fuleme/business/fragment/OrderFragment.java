package com.fuleme.business.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.WebActivity;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.APIService;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.ScrollWebView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 订单页面改为商城
 */
public class OrderFragment extends Fragment {
    private static final String TAG = "OrderFragment";
    private String URL = "";
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    private Context context;
    ScrollWebView webView1;
    private boolean errFlag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_order_fragment, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        URL = APIService.SERVER_IP + "h5/integralmall/index?token=" + App.token;
        //刷新控件
        demoSwiperefreshlayout.setColorSchemeResources(R.color.white);
        demoSwiperefreshlayout.setProgressBackgroundColorSchemeResource(R.color.theme);
        demoSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉重置数据
                if (webView1 != null) {
                    webView1.reload();
                }
            }
        });
        //webview
        webView1 = new ScrollWebView(context.getApplicationContext());
        demoSwiperefreshlayout.addView(webView1);
        webView1.setVerticalScrollBarEnabled(false);
        webView1.setHorizontalScrollBarEnabled(false);
        webView1.getSettings().setJavaScriptEnabled(true); //加上这句话才能使用JavaScript方法
        webView1.setOnScrollChangeListener(new ScrollWebView.OnScrollChangeListener() {

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                //滑动中
                demoSwiperefreshlayout.setEnabled(false);
            }

            @Override
            public void onPageTop(int l, int t, int oldl, int oldt) {
                //滑动到顶部
                demoSwiperefreshlayout.setEnabled(true);
            }

            @Override
            public void onPageEnd(int l, int t, int oldl, int oldt) {
                //滑动到底部
            }
        });

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
                WebActivity.url = url;
                WebActivity.title = "商城";
                startActivity(new Intent(context, WebActivity.class));
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                ((BaseActivity) context).closeLoading();//取消等待框
                demoSwiperefreshlayout.setRefreshing(false);
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
                demoSwiperefreshlayout.setRefreshing(false);
                errFlag = true;
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((FragmentActivity) context).flagFragment == 1) {
            ((BaseActivity) context).showLoading("加载中...");
            //WebView加载web资源
            webView1.loadUrl(URL);
            LogUtil.d(URL);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        webView1.removeAllViews();
        webView1.destroy();
    }

}
