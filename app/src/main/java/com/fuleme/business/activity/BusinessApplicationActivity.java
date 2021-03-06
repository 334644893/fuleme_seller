package com.fuleme.business.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.Zxing;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商户应用
 */
public class BusinessApplicationActivity extends BaseActivity {

    private static final String TAG = "BusinessApplicationActi";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_ba_but_1)
    TextView tvBaBut1;
    @Bind(R.id.tv_ba_but_2)
    TextView tvBaBut2;
    @Bind(R.id.v_ba_line_l)
    View vBaLineL;
    @Bind(R.id.v_ba_line_r)
    View vBaLineR;
    @Bind(R.id.iv_ba_qr_code)
    ImageView ivBaQrCode;
    @Bind(R.id.tv_ba_codetext)
    TextView tvBaCodetext;
    public static String url = "https://www.fuleme.com";
    public static String gzh = "http://weixin.qq.com/r/2kXTyyzEJnFZrWio9xDI";
    @Bind(R.id.iv_ba_qr_code_gzh)
    ImageView ivBaQrCodeGzh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_application);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        onClickApp();
        tvTitle.setText("商户应用");
        ivBaQrCode.setImageBitmap(Zxing.getQrCode(url));
        ivBaQrCodeGzh.setImageBitmap(Zxing.getQrCode(gzh));
    }


    public void onClickApp() {
        ivBaQrCode.setVisibility(View.VISIBLE);
        ivBaQrCodeGzh.setVisibility(View.GONE);
        tvBaCodetext.setText("扫描二维码，下载最新商户App");
        vBaLineL.setBackgroundColor(getResources().getColor(R.color.theme));
        vBaLineR.setBackgroundColor(getResources().getColor(R.color.app_back_color));
        tvBaBut1.setTextColor(getResources().getColor(R.color.theme));
        tvBaBut2.setTextColor(getResources().getColor(R.color.black_87));
    }

    public void onClickGZH() {
        ivBaQrCodeGzh.setVisibility(View.VISIBLE);
        ivBaQrCode.setVisibility(View.GONE);
        tvBaCodetext.setText("微信扫一扫，关注商户公众号");
        vBaLineR.setBackgroundColor(getResources().getColor(R.color.theme));
        vBaLineL.setBackgroundColor(getResources().getColor(R.color.app_back_color));
        tvBaBut1.setTextColor(getResources().getColor(R.color.black_87));
        tvBaBut2.setTextColor(getResources().getColor(R.color.theme));
    }

    @OnClick({R.id.tv_ba_but_1, R.id.tv_ba_but_2, R.id.tv_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ba_but_1:
                onClickApp();
                break;
            case R.id.tv_ba_but_2:
                onClickGZH();
                break;
            case R.id.tv_left:
                finish();
                break;
        }
    }
}
