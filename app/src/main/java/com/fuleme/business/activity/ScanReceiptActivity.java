package com.fuleme.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.NumberUtils;
import com.fuleme.business.utils.ToastUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 收款码页面
 */
public class ScanReceiptActivity extends BaseActivity {
    private static final String TAG = "ScanReceiptActivity";
    public static final String ALIPAYSWEEPPAYMENT = "AlipaySweepPayment";
    public static final String WEIXINSWEEPPAYMENT = "WeixinSweepPayment";
    @Bind(R.id.tv_title)
    TextView tvTitle;
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
        tvTitle.setText("收款码");
        etAmount.addTextChangedListener(watcher);
    }

    @OnClick({R.id.tv_left, R.id.ll_zhifubao, R.id.ll_weixin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_zhifubao:
                if (!TextUtils.isEmpty(etAmount.getText().toString())) {
                    Intent intent = new Intent(ScanReceiptActivity.this, PaymentCodeActivity.class);
                    intent.putExtra("etAmount", etAmount.getText().toString());
                    intent.putExtra("payType", ALIPAYSWEEPPAYMENT);
                    startActivity(intent);
                } else {
                    ToastUtil.showMessage("请填写金额");
                }
                break;
            case R.id.ll_weixin:
                if (!TextUtils.isEmpty(etAmount.getText().toString())) {
                    Intent intent = new Intent(ScanReceiptActivity.this, PaymentCodeActivity.class);
                    intent.putExtra("etAmount", etAmount.getText().toString());
                    intent.putExtra("payType", WEIXINSWEEPPAYMENT);
                    startActivity(intent);
                } else {
                    ToastUtil.showMessage("请填写金额");
                }
                break;
            case R.id.tv_left:
                finish();
                break;
        }
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    etAmount.setText(s);
                    etAmount.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                etAmount.setText(s);
                etAmount.setSelection(2);
            }

            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    etAmount.setText(s.subSequence(0, 1));
                    etAmount.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

    };
}
