package com.fuleme.business.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.download.UpdateManager;
import com.fuleme.business.fragment.FragmentActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.SharedPreferencesUtils;
import com.fuleme.business.utils.ToastUtil;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    public static boolean MANDATORY = false;//是否是强制更新，如果是则登录不可用
    public static  Dialog mUpdateLoading;
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
        if ("".equals(SharedPreferencesUtils.getParam(getApplicationContext(), "start", ""))) {

            startActivity(new Intent(LoginActivity.this, StartActivity.class));
        }
        initJzmm();
        setState(App.LOGIN_TYPE_EMPLOYEES);
        //更新
//       new UpdateManager(LoginActivity.this).checkUpdate(2, "啦啦啦啦", 1, "download/com.fuleme.business-release-v1.0-1.apk", false);
        version();
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
            SharedPreferencesUtils.setParam(getApplicationContext(), "password", "");
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
                if (MANDATORY) {
                    version();
                } else {
                    if (TextUtils.isEmpty(etPhone.getText().toString()) || etPhone.getText().toString().length() != 11) {
                        ToastUtil.showMessage("手机号格式错误");
                    } else if (TextUtils.isEmpty(etVerify.getText().toString())) {
                        ToastUtil.showMessage("密码不能为空");
                    } else {
                        Login();
                    }
                }
                break;
            case R.id.tv_zczh:
                startActivity(new Intent(LoginActivity.this, RegisteredActivity.class));

                break;
            case R.id.tv_wjmm:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));

                break;
            case R.id.tv_jzmm:
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

    private void Login() {
        showLoading("登录中...");
//        closeLoading();//取消等待框
//        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
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
                        LogUtil.d("-------登录返回App.token---------", App.token);
                        //绑定推送账号

                        App.bindAccount();
                        //记住手机号
                        SharedPreferencesUtils.setParam(getApplicationContext(), "phone", etPhone.getText().toString());
                        //刷新TOKEN用密码
                        SharedPreferencesUtils.setParam(getApplicationContext(), "token_password", etVerify.getText().toString());
                        //根据记住密码保存密码
                        if (loginjzmm == 1) {
                            SharedPreferencesUtils.setParam(getApplicationContext(), "password", etVerify.getText().toString());
                        } else {
                            SharedPreferencesUtils.setParam(getApplicationContext(), "password", "");
                        }
                        //TODO 跳转主页
                        startActivity(new Intent(LoginActivity.this, FragmentActivity.class));
                        finish();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }

                } else {
                    LogUtil.i("登陆失败response.message():" + response.message());

                }
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("登录超时");
            }

        });
    }

    int type = -1;
    private void version() {
        getApi().version().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("获取更新信息成功");
                        //TODO 初始化数据
                        JSONObject data = GsonUtils.getResultData(response.body());
                        LogUtil.d("---data--", data.toString());
                        int version = data.optInt("androidVersion");//版本标识
                        String prompt = data.optString("prompt");
                        type = data.optInt("type");
                        String url = data.optString("android");
                        LogUtil.d("---------", "-version:" + version + "-prompt:" + prompt + "-type:" + type + "-android:" + url);
                        if (type == 1) {
                            MANDATORY = true;
                        }
                        //信息对比是否更新
                        new UpdateManager(LoginActivity.this).checkUpdate(version, prompt, type, url, false);
                    }

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (type == 1) {
                    // 构造对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                    builder.setTitle("检查更新提示");
                    builder.setMessage("网络出现了问题");
                    // 更新
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    Dialog noticeDialog = builder.create();
                    noticeDialog.setCancelable(false);
                    noticeDialog.show();
                }else{
                    LogUtil.d("检测更新失败",t.toString());
                }
            }

        });
    }


}
