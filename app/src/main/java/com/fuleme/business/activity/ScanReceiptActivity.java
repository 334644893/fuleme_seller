package com.fuleme.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 员工收款-扫一扫页面
 */
public class ScanReceiptActivity extends BaseActivity {
    public static int type = 0;//选中类型
    public static final int SAOYISAO = 1;//选中扫一扫
    public static final int ZHIFUMA = 2;//选中支付码
    public static final int ZHIFUBAO = 0;
    public static final int WEIXIN = 1;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_saoyisao)
    TextView tvSaoyisao;
    @Bind(R.id.tv_zhifuma)
    TextView tvZhifuma;
    @Bind(R.id.et_amount)
    EditText etAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receipt);
        ButterKnife.bind(this);

        init();
    }

    public void init() {
        tvTitle.setText("扫一扫");
        setOnClick(SAOYISAO);//默认选中扫一扫
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SAOYISAO) {
            if (data != null) {
                if (!TextUtils.isEmpty(data.getExtras().getString(CodeUtils.RESULT_STRING))) {
                    ToastUtil.showMessage(data.getExtras().getString(CodeUtils.RESULT_STRING));
                } else {
                    ToastUtil.showMessage("信息为空");
                }
            }
        }

        etAmount.setText("");


    }

    /**
     * 改变
     */
    private void setOnClick(int state) {
        type = state;
        switch (state) {
            case SAOYISAO:
                //改变UI(下同)
                tvSaoyisao.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_b_cheng));
                tvZhifuma.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_b_hui));
                break;
            case ZHIFUMA:
                tvSaoyisao.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_b_hui));
                tvZhifuma.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_b_cheng));
                break;
        }
    }

    @OnClick({R.id.tv_left, R.id.tv_saoyisao, R.id.tv_zhifuma, R.id.ll_zhifu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_saoyisao:
                setOnClick(SAOYISAO);
                break;
            case R.id.tv_zhifuma:
                setOnClick(ZHIFUMA);
                break;
            case R.id.ll_zhifu:
                if (!TextUtils.isEmpty(etAmount.getText().toString())) {
                    switch (type) {
                        case SAOYISAO:
                            startActivityForResult(new Intent(ScanReceiptActivity.this, SecondActivity.class), SAOYISAO);
                            break;
                        case ZHIFUMA:
                            Intent intent = new Intent(ScanReceiptActivity.this, PaymentCodeActivity.class);
                            intent.putExtra("etAmount", etAmount.getText().toString());
                            startActivityForResult(intent, ZHIFUMA);

                            break;
                    }

                } else {
                    ToastUtil.showMessage("请填写金额");
                }

                break;
            case R.id.tv_left:
                finish();
                break;
        }
    }
}
