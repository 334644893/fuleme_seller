package com.fuleme.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.fuleme.business.R;
import com.fuleme.business.utils.Constant;
import com.fuleme.business.utils.ToastUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 员工收款-扫一扫页面
 */
public class ScanReceiptActivity extends AppCompatActivity {
    public static final int ZHIFUBAO = 0;
    public static final int WEIXIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receipt);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_zhifubao, R.id.ll_weixin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_zhifubao:
                startActivityForResult(new Intent(ScanReceiptActivity.this, SecondActivity.class), ZHIFUBAO);
                break;
            case R.id.ll_weixin:
                startActivityForResult(new Intent(ScanReceiptActivity.this, SecondActivity.class), WEIXIN);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ZHIFUBAO) {
            if (data != null) {
                if (!TextUtils.isEmpty(data.getExtras().getString(CodeUtils.RESULT_STRING))) {
                    ToastUtil.showMessage(data.getExtras().getString(CodeUtils.RESULT_STRING));
                } else {
                    ToastUtil.showMessage("信息为空");
                }
            }
        }
    }
}
