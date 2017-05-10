package com.fuleme.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
 * 扫一扫收款
 */
public class ScanEntranceActivity extends BaseActivity {
    private static final String TAG = "ScanEntranceActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_amount)
    EditText etAmount;
    public static final int SAOYISAO = 1;//选中扫一扫

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_entrance);
        ButterKnife.bind(this);
        tvTitle.setText("收款");
        etAmount.addTextChangedListener(watcher);
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
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
            // TODO Auto-generated method stub

        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (!TextUtils.isEmpty(data.getExtras().getString(CodeUtils.RESULT_STRING))) {
                CreditCardPayment(data.getExtras().getString(CodeUtils.RESULT_STRING), (int) (NumberUtils.StringToDouble(etAmount.getText().toString()) * 100) + "");
            }
        }
        etAmount.setText("");
    }

    @OnClick({R.id.tv_left, R.id.btn_enter_1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_enter_1:
                if (!TextUtils.isEmpty(etAmount.getText().toString())) {
                    startActivityForResult(new Intent(ScanEntranceActivity.this, SecondActivity.class), SAOYISAO);
                } else {
                    ToastUtil.showMessage("请输入正确金额");
                }
                break;
        }
    }

    /**
     * 威富通刷卡支付(统一支付接口)
     * auth_code 扫码支付授权码， 设备读取用户展示的条码或者二维码信息
     * total_fee 总金额，以分为单位，不允许包含任何字、符号
     */

    private void CreditCardPayment(String auth_code, String total_fee) {
        showLoading("收款中...");
        Call<Object> call = getApi().CreditCardPayment(
                App.token,
                auth_code,
                total_fee,
                App.merchant,
                App.short_id,
                App.phone);
        LogUtil.i("App.token:" + App.token
                + "``auth_code:" + auth_code
                + "```total_fee:" + total_fee
                + "``App.merchant:" + App.merchant
                + "``App.short_id:" + App.short_id
                + "App.phone:" + App.phone);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        ToastUtil.showMessage(GsonUtils.getResultData(response.body()).optString(GsonUtils.ERRMSG));
                    }
                }
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }
}
