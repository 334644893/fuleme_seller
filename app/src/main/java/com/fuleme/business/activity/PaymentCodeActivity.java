package com.fuleme.business.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.utils.Zxing;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentCodeActivity extends BaseActivity {
    private static final String TAG = "PaymentCodeActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_qian)
    TextView tvQian;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.iv_ba_qr_code)
    ImageView ivBaQrCode;
    private String etAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_code);
        ButterKnife.bind(this);

        initview();
    }

    public void initview() {
        etAmount = getIntent().getExtras().getString("etAmount");
        tvQian.setText("￥" + etAmount);
        ivBaQrCode.setImageBitmap(Zxing.getQrCode("请给" + etAmount + "块钱"));
    }

    @OnClick(R.id.tv_left)
    public void onClick() {
        finish();
    }
}
