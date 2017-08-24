package com.fuleme.business.activity.Version2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.util.LogTime;
import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 绑定银行卡
 */
public class BindBankCardActivity extends BaseActivity {
    private static final String TAG = "BindBankCardActivity";
    @Bind(R.id.btn_get_verify)
    TextView btnGetVerify;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_1)
    EditText et1;
    @Bind(R.id.et_2)
    EditText et2;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.et_f_p_v)
    EditText etFPV;
    public static String BANK = "";//银行
    private int time;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bank_card);
        ButterKnife.bind(this);
        tvTitle.setText("绑定银行卡");
    }

    @Override
    protected void onResume() {
        tv1.setText(BANK);
        super.onResume();
    }

    @OnClick({R.id.tv_left, R.id.ll_1, R.id.btn_get_verify, R.id.btn_tj})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_1:
                ListActivity.INTENTTYPE=1;
                startActivity(new Intent(BindBankCardActivity.this, ListActivity.class));
                break;
            case R.id.btn_get_verify:
                    startTimer(App.ver);
                    send(TAG, App.phone);
                break;
            case R.id.btn_tj:
                if (TextUtils.isEmpty(et1.getText().toString())) {
                    ToastUtil.showMessage("请填写持卡人姓名");
                } else if (TextUtils.isEmpty(et2.getText().toString())) {
                    ToastUtil.showMessage("请填写银行卡号");
                } else if (TextUtils.isEmpty(tv1.getText().toString())) {
                    ToastUtil.showMessage("请选择开户行");
                } else if (TextUtils.isEmpty(etFPV.getText().toString())) {
                    ToastUtil.showMessage("请填写验证码");
                } else {
                    bindingbank();
                }
                break;
        }
    }
    /**
     * 绑定银行卡
     */

    private void bindingbank() {
        showLoading("获取中...");
        Call<Object> call = getApi().bindingbank(
               App.token,
                et1.getText().toString().trim(),
                et2.getText().toString().trim(),
                tv1.getText().toString().trim(),
                etFPV.getText().toString().trim()
        );

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    // do SomeThing
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        ToastUtil.showMessage("绑定成功");
                        BalanceActivity.flag = true;
                        finish();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }


                } else {
                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    LogUtil.i("绑定失败:" + response.message());
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
    private void startTimer(final int t) {
        time = t;
        btnGetVerify.setEnabled(false);
        btnGetVerify.setBackgroundColor(getResources().getColor(R.color.black_26));
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (btnGetVerify != null) {
                            btnGetVerify.setText("请稍等(" + String.valueOf(time--) + ")");
                        }
                        if (time < 0) {
                            time = t;
                            btnGetVerify.setText("获取验证码");
                            btnGetVerify.setEnabled(true);
                            btnGetVerify.setBackgroundColor(getResources().getColor(R.color.theme));
                            timer.cancel();
                        }
                    }
                });
            }
        }, 0, 1000);
    }
}
