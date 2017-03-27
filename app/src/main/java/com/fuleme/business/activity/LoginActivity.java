package com.fuleme.business.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.sdk.android.push.CommonCallback;
import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.fragment.FragmentActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.SharedPreferencesUtils;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.utils.TtsUtil;
import com.fuleme.business.widget.LoadingDialogUtils;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fuleme.business.App.pushService;


/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    @Bind(R.id.tv_dianyuan)
    TextView tvDianyuan;
    @Bind(R.id.tv_dianyuan_l)
    View tvDianyuanL;
    @Bind(R.id.tv_admin)
    TextView tvAdmin;
    @Bind(R.id.tv_jzmm)
    TextView tvJzmm;
    @Bind(R.id.tv_admin_l)
    View tvAdminL;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_verify)
    EditText etVerify;

    private int loginjzmm;
    Drawable drawable_41, drawable_40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //判断是否第一次启动
        if("".equals(SharedPreferencesUtils.getParam(getApplicationContext(), "start", ""))){

            startActivity(new Intent(LoginActivity.this,StartActivity.class));
        }
        initJzmm();
        setState(App.LOGIN_TYPE_EMPLOYEES);
    }

    /**
     * 初始化记住密码
     * loginjzmm 0：未选中 1：选中
     */
    private void initJzmm() {
        loginjzmm = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "loginjzmm", 0);
        //初始化账号密码
        etPhone.setText(SharedPreferencesUtils.getParam(getApplicationContext(), "phone", "") + "");
//        etPhone.setText(SharedPreferencesUtils.getParam(getApplicationContext(), "phone", "13419567515") + "");
        if (loginjzmm == 1) {
            etVerify.setText(SharedPreferencesUtils.getParam(getApplicationContext(), "password", "") + "");
        } else {
            etVerify.setText("");
//            etVerify.setText("666666");
        }
        //初始化标记
        drawable_41 = getResources().getDrawable(R.mipmap.icon41);
        drawable_40 = getResources().getDrawable(R.mipmap.icon40);
        drawable_41.setBounds(0, 0, drawable_41.getMinimumWidth(), drawable_41.getMinimumHeight());
        drawable_40.setBounds(0, 0, drawable_40.getMinimumWidth(), drawable_40.getMinimumHeight());

        if (loginjzmm == 0) {
            tvJzmm.setCompoundDrawables(drawable_41, null, null, null);
        } else if (loginjzmm == 1) {
            tvJzmm.setCompoundDrawables(drawable_40, null, null, null);
        }

        etVerify.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etVerify.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Login();
                return false;
            }
        });
    }

    @OnClick({R.id.tv_wjmm, R.id.ll_dianyuan, R.id.ll_admin, R.id.btn_login, R.id.tv_zczh, R.id.tv_jzmm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_dianyuan:
                setState(App.LOGIN_TYPE_EMPLOYEES);
                break;
            case R.id.ll_admin:
                setState(App.LOGIN_TYPE_ADMIN);
                break;
            case R.id.btn_login:

                //TODO 发送登录请求并跳转主页
                Login();

                break;
            case R.id.tv_zczh:
                startActivity(new Intent(LoginActivity.this, RegisteredActivity.class));

                break;
            case R.id.tv_wjmm:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));

                break;
            case R.id.tv_jzmm:
                LogUtil.i("jzmmmmmmmm", loginjzmm + "");
                if (loginjzmm == 0) {
                    loginjzmm = 1;
                    SharedPreferencesUtils.setParam(getApplicationContext(), "loginjzmm", 1);
                    tvJzmm.setCompoundDrawables(drawable_40, null, null, null);
                } else if (loginjzmm == 1) {
                    loginjzmm = 0;
                    SharedPreferencesUtils.setParam(getApplicationContext(), "loginjzmm", 0);
                    tvJzmm.setCompoundDrawables(drawable_41, null, null, null);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "password", "");
                }


                break;
        }
    }

    /**
     * 改变登录类型
     *
     * @param type
     */
    private void setState(int type) {
        App.login_type = type;
        switch (type) {

            case App.LOGIN_TYPE_ADMIN:
                tvAdmin.setTextColor(getResources().getColor(R.color.black_87));
                tvAdminL.setBackgroundColor(getResources().getColor(R.color.black_87));
                tvDianyuan.setTextColor(getResources().getColor(R.color.black_26));
                tvDianyuanL.setBackgroundColor(getResources().getColor(R.color.app_back_color));
                break;
            case App.LOGIN_TYPE_EMPLOYEES:

                tvDianyuan.setTextColor(getResources().getColor(R.color.black_87));
                tvDianyuanL.setBackgroundColor(getResources().getColor(R.color.black_87));
                tvAdmin.setTextColor(getResources().getColor(R.color.black_26));
                tvAdminL.setBackgroundColor(getResources().getColor(R.color.app_back_color));
                break;
        }
    }

    /**
     * 登录接口
     */
    private Dialog mLoading;
    private void Login() {
        mLoading = LoadingDialogUtils.createLoadingDialog(LoginActivity.this, "登录中...");//添加等待框
        Call<Object> call = getApi().login(etPhone.getText().toString(),
                etVerify.getText().toString(),
                App.login_type);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("登陆成功");
                        //TODO 初始化数据
                        JSONObject data = GsonUtils.getResultData(response.body());
                        App.uid = data.optInt("uid");
                        App.phone = data.optString("phone");
                        App.username = data.optString("username");
                        App.role = data.optString("role");
                        App.short_id = data.optString("short_id");
                        App.merchant = data.optString("short_name");
                        App.short_state = data.optString("short_state");
                        App.short_area = data.optString("short_area");
                        App.token = data.optString("token");
                        App.qrcode = data.optString("qrcode");
                        //绑定推送账号
                        App.bindAccount();
                        //记住手机号
                        SharedPreferencesUtils.setParam(getApplicationContext(), "phone", etPhone.getText().toString());
                        //是否保存密码
                        if (loginjzmm == 0) {
                            SharedPreferencesUtils.setParam(getApplicationContext(), "password", "");
                        } else if (loginjzmm == 1) {
                            SharedPreferencesUtils.setParam(getApplicationContext(), "password", etVerify.getText().toString());
                        }
                        //TODO 跳转主页
                        startActivity(new Intent(LoginActivity.this, FragmentActivity.class));
                        finish();
                    } else {
                        ToastUtil.showMessage("登录失败");
                    }

                } else {
                    LogUtil.i("登陆失败response.message():" + response.message());

                }
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                ToastUtil.showMessage("登录超时");
            }

        });
    }

}
