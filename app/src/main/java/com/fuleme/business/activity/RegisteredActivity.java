package com.fuleme.business.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.LoadingDialogUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 注册基本信息
 */
public class RegisteredActivity extends BaseActivity {
    private static final String TAG = "RegisteredActivity";
    private Context context = RegisteredActivity.this;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_get_verify)
    TextView btnGetVerify;
    @Bind(R.id.et_f_p_nickname)
    EditText etFPNickname;
    @Bind(R.id.et_f_p_phone)
    EditText etFPPhone;
    @Bind(R.id.et_f_p_ps)
    EditText etFPPs;
    @Bind(R.id.et_f_p_ep)
    EditText etFPEp;
    @Bind(R.id.et_f_p_v)
    EditText etFPV;
    private int time;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        tvTitle.setText("注册账号");
    }

    @OnClick({R.id.tv_left, R.id.btn_get_verify, R.id.btn_tj})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_get_verify:
                if (TextUtils.isEmpty(etFPPhone.getText().toString()) || etFPPhone.getText().toString().length() != 11) {
                    ToastUtil.showMessage("手机号格式错误");
                } else {
                    startTimer(10);
                    send(TAG, etFPPhone.getText().toString());
                }
                break;
            case R.id.btn_tj:
                if (TextUtils.isEmpty(etFPNickname.getText().toString())) {
                    ToastUtil.showMessage("请设置用户名");
                } else if (TextUtils.isEmpty(etFPPhone.getText().toString()) || etFPPhone.getText().toString().length() != 11) {
                    ToastUtil.showMessage("手机号格式错误");
                } else if (TextUtils.isEmpty(etFPV.getText().toString())) {
                    ToastUtil.showMessage("请填写验证码");
                } else if (TextUtils.isEmpty(etFPPs.getText().toString())) {
                    ToastUtil.showMessage("请设置密码");
                } else if (!etFPPs.getText().toString().equals(etFPEp.getText().toString())) {
                    ToastUtil.showMessage("确认密码不一致");
                } else {
                    Register();
                }

                break;
        }
    }

    /**
     * 注册接口
     */
    private Dialog mLoading;

    private void Register() {
        mLoading = LoadingDialogUtils.createLoadingDialog(context, "获取中...");//添加等待框
        LogUtil.i("/etFPPhone:" + etFPPhone + "/etFPNickname:" + etFPNickname + "/etFPPs:" + etFPPs);
        Call<Object> call = getApi().register(etFPPhone.getText().toString(), etFPNickname.getText().toString(), etFPPs.getText().toString());

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    // do SomeThing
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        ToastUtil.showMessage("注册成功");
                        finish();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                        finish();
                    }


                } else {
                    ToastUtil.showMessage("注册失败");
                    LogUtil.i("注册失败response.message():" + response.message());
                }
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
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
