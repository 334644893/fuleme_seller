package com.fuleme.business.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.fuleme.business.widget.CustomDialog;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.alibaba.sdk.android.ams.common.global.AmsGlobalHolder.getPackageName;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    public static boolean MANDATORY = false;//是否是强制更新，如果是则登录不可用
    public static Dialog mUpdateLoading;
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
        if (Build.VERSION.SDK_INT >= 23) {
            verifyStoragePermissions();
        }
        //判断是否第一次启动
        if ("".equals(SharedPreferencesUtils.getParam(getApplicationContext(), "start", ""))) {
            startActivity(new Intent(LoginActivity.this, StartActivity.class));
        }
        initJzmm();
        //更新
        version();
        AutomaticLogin();
    }


    /**
     * 手动添加权限4
     */
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_PERMISSIOCAMERA = 2;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"

    };
    private static String[] PERMISSIOCAMERA = {
            "android.permission.CAMERA"

    };

    public void verifyStoragePermissions() {
        /**
         * 检查是否获得了ACTION_MANAGE_OVERLAY_PERMISSION权限（Android6.0运行时权限）
         * 弹窗
         */
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (!Settings.canDrawOverlays(LoginActivity.this)) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                        Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, 10);
//            }
//        }
        /**
         * 检查是否获得了android.permission-group.STORAGE权限（Android6.0运行时权限）
         *
         */
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(LoginActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            //检测是否有相机的权限
            int permissionCamera = ActivityCompat.checkSelfPermission(LoginActivity.this,
                    "android.permission.CAMERA");
            if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIOCAMERA, REQUEST_PERMISSIOCAMERA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void AutomaticLogin() {
        App.token = SharedPreferencesUtils.getParam(getApplicationContext(), "token", App.token).toString();
        if (!TextUtils.isEmpty(App.token)) {


            App.uid = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "uid", App.uid);
            App.phone = SharedPreferencesUtils.getParam(getApplicationContext(), "phone", App.phone).toString();
            App.username = SharedPreferencesUtils.getParam(getApplicationContext(), "username", App.username).toString();
            App.role = SharedPreferencesUtils.getParam(getApplicationContext(), "role", App.role).toString();
            App.short_id = SharedPreferencesUtils.getParam(getApplicationContext(), "short_id", App.short_id).toString();
            App.merchant = SharedPreferencesUtils.getParam(getApplicationContext(), "merchant", App.merchant).toString();
            App.short_state = SharedPreferencesUtils.getParam(getApplicationContext(), "short_state", App.short_state).toString();
            App.short_area = SharedPreferencesUtils.getParam(getApplicationContext(), "short_area", App.short_area).toString();
            App.qrcode = SharedPreferencesUtils.getParam(getApplicationContext(), "qrcode", App.qrcode).toString();
            App.short_logo = SharedPreferencesUtils.getParam(getApplicationContext(), "head_img", App.short_logo).toString();
            App.is_agent = SharedPreferencesUtils.getParam(getApplicationContext(), "is_agent", App.is_agent).toString();
            App.invitation_code = SharedPreferencesUtils.getParam(getApplicationContext(), "invitation_code", App.invitation_code).toString();
            App.pay_password = SharedPreferencesUtils.getParam(getApplicationContext(), "pay_password", App.pay_password).toString();
            App.create_time = SharedPreferencesUtils.getParam(getApplicationContext(), "create_time", App.create_time).toString();
            //绑定推送账号
            if (App.bindAccount) {
                App.bindAccount();
            }
            //自动登录开启检测更新
            FragmentActivity.isAutomaticLogin = true;
            startActivity(new Intent(LoginActivity.this, FragmentActivity.class));
            finish();
        }
    }

    /**
     * 初始化记住密码
     * loginjzmm 0：未选中 1：选中
     */
    private void initJzmm() {
        loginjzmm = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "loginjzmm", 0);
        //初始化账号密码
        etPhone.setText(SharedPreferencesUtils.getParam(getApplicationContext(), "phone", "") + "");
        if (loginjzmm == 1) {
            etVerify.setText(SharedPreferencesUtils.getParam(getApplicationContext(), "password", "") + "");
        } else {
            etVerify.setText("");
            SharedPreferencesUtils.setParam(getApplicationContext(), "password", "");
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
                return false;
            }
        });
    }

    @OnClick({R.id.tv_wjmm, R.id.ll_dianyuan, R.id.ll_admin, R.id.btn_login, R.id.tv_zczh, R.id.tv_jzmm})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.ll_dianyuan:
//                setState(App.LOGIN_TYPE_EMPLOYEES);
//                break;
//            case R.id.ll_admin:
//                setState(App.LOGIN_TYPE_ADMIN);
//                break;
            case R.id.btn_login:
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

//    /**
//     * 改变登录类型
//     *
//     * @param type
//     */
//    private void setState(int type) {
//        App.login_type = type;
//        switch (type) {
//
//            case App.LOGIN_TYPE_ADMIN:
//                tvAdmin.setTextColor(getResources().getColor(R.color.black_87));
//                tvAdminL.setBackgroundColor(getResources().getColor(R.color.black_87));
//                tvDianyuan.setTextColor(getResources().getColor(R.color.black_26));
//                tvDianyuanL.setBackgroundColor(getResources().getColor(R.color.app_back_color));
//                break;
//            case App.LOGIN_TYPE_EMPLOYEES:
//
//                tvDianyuan.setTextColor(getResources().getColor(R.color.black_87));
//                tvDianyuanL.setBackgroundColor(getResources().getColor(R.color.black_87));
//                tvAdmin.setTextColor(getResources().getColor(R.color.black_26));
//                tvAdminL.setBackgroundColor(getResources().getColor(R.color.app_back_color));
//                break;
//        }
//    }

    /**
     * 登录接口
     */

    private void Login() {
        showLoading("登录中...");
        Call<Object> call = getApi().login(etPhone.getText().toString(),
                etVerify.getText().toString());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("登陆成功");
                        JSONObject data = GsonUtils.getResultData(response.body());
//                        SharedPreferencesUtils.setParam(getApplicationContext(), "login_type", App.login_type);
                        App.uid = data.optInt("uid");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "uid", App.uid);
                        App.phone = data.optString("phone");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "phone", App.phone);
                        App.username = data.optString("username");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "username", App.username);
                        App.role = data.optString("role");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "role", App.role);
                        App.short_id = data.optString("short_id");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "short_id", App.short_id);
                        App.merchant = data.optString("short_name");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "merchant", App.merchant);
                        App.short_state = data.optString("short_state");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "short_state", App.short_state);
                        App.short_area = data.optString("short_area");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "short_area", App.short_area);
                        App.token = data.optString("token");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "token", App.token);
                        App.qrcode = data.optString("qrcode");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "qrcode", App.qrcode);
                        App.short_logo = data.optString("head_img");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "head_img", App.short_logo);

                        App.is_agent = data.optString("is_agent");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "is_agent", App.is_agent);

                        App.invitation_code = data.optString("invitation_code");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "invitation_code", App.invitation_code);

                        App.pay_password = data.optString("pay_password");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "pay_password", App.pay_password);

                        App.create_time = data.optString("create_time");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "create_time", App.create_time);


                        LogUtil.d("-------登录返回App.token---------", App.token);
                        //绑定推送账号
                        App.bindAccount();
                        //刷新TOKEN用密码
                        SharedPreferencesUtils.setParam(getApplicationContext(), "token_password", etVerify.getText().toString());

                        //根据记住密码保存密码
                        if (loginjzmm == 1) {
                            SharedPreferencesUtils.setParam(getApplicationContext(), "password", etVerify.getText().toString());
                        } else {
                            SharedPreferencesUtils.setParam(getApplicationContext(), "password", "");
                        }
                        closeLoading();//取消等待框
                        startActivity(new Intent(LoginActivity.this, FragmentActivity.class));
                        finish();

                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                        closeLoading();//取消等待框
                    }

                } else {
                    LogUtil.i("登陆失败response.message():" + response.message());
                    closeLoading();//取消等待框
                }

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
    private CustomDialog dialog;

    private void version() {
        getApi().version().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("获取更新信息成功");
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
                // 构造对话框
                CustomDialog.Builder customBuilder = new
                        CustomDialog.Builder(LoginActivity.this);
                customBuilder
                        .setTitle("检查更新提示")
                        .setMessage("网络出现了问题")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                System.exit(0);
                            }
                        })
                ;

                dialog = customBuilder.create();
                dialog.setCancelable(false);
                dialog.show();
            }

        });
    }


}
