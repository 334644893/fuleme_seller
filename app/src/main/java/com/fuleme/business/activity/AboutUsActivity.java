package com.fuleme.business.activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.APIService;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity {
    private static final String TAG = "AboutUsActivity";
    @Bind(R.id.tv_left)
    ImageView tvLeft;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        tvTitle.setText("关于我们");
        init();
    }

    @OnClick({R.id.tv_left, R.id.tv_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
        }
    }

    /**
     * 关于我们接口
     */
    private void init() {
        webView = (WebView) findViewById(R.id.webView);
        //WebView加载web资源
        webView.loadUrl(APIService.SERVER_IP+APIService.ABOUT);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }
}
