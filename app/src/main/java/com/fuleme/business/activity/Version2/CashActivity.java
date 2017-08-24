package com.fuleme.business.activity.Version2;

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
import com.fuleme.business.bean.ServiceBusinessesBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CashActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.et_amount)
    EditText etAmount;
    @Bind(R.id.tv_3)
    TextView tv3;
    public static String cash = "";
    public static String bank = "";
    public static String number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        tvTitle.setText("提现");
        tv3.setText(cash + "");
        etAmount.addTextChangedListener(watcher);
    }

    @Override
    protected void onResume() {
        if (TextUtils.isEmpty(bank)) {
            tv1.setText("请选择银行");
        } else {
            tv1.setText(bank);
        }
        if (number.length() >= 4) {
            tv2.setText("**** **** **** " + number.substring(number.length() - 4));
        }
        super.onResume();
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                if ((new Float(Float.parseFloat(s.toString().trim()) * 100)).intValue() > (new Float(Float.parseFloat(cash) * 100)).intValue()) {
                    if ((new Float(Float.parseFloat(cash) * 100)).intValue() == 0) {
                        cash = "0";
                    }
                    etAmount.setText(cash + "");
                    etAmount.setSelection(etAmount.getText().toString().length());
                } else {
                    etAmount.setMaxEms(10);
                }
            }
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

    @OnClick({R.id.tv_left, R.id.ll_bt_1, R.id.bt_tv_1, R.id.btn_tj_1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_bt_1:
                startActivity(new Intent(CashActivity.this, MyBankActivity.class));
                break;
            case R.id.bt_tv_1:
                etAmount.setText(cash + "");
                break;
            case R.id.btn_tj_1:
                if (TextUtils.isEmpty(etAmount.getText().toString().trim()) || "0".equals(cash)) {
                    ToastUtil.showMessage("请输入正确金额");
                } else {
                    drawmoney();
                }
                break;
        }
    }

    /**
     * 提现
     */
    private void drawmoney() {
        showLoading("获取中...");
        Call<Object>
                call = getApi().drawmoney(
                App.token, etAmount.getText().toString().trim()

        );
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, final Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                        finish();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }
                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }

}
