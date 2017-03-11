package com.fuleme.business.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.fragment.FragmentActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.LoadingDialogUtils;

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
    @Bind(R.id.tv_dianyuan)
    TextView tvDianyuan;
    @Bind(R.id.tv_dianyuan_l)
    View tvDianyuanL;
    @Bind(R.id.tv_admin)
    TextView tvAdmin;
    @Bind(R.id.tv_admin_l)
    View tvAdminL;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_verify)
    EditText etVerify;
    private Dialog mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setState(App.LOGIN_TYPE_EMPLOYEES);
    }

    @OnClick({R.id.tv_wjmm,R.id.ll_dianyuan, R.id.ll_admin, R.id.btn_login, R.id.tv_zczh})
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

            case App.LOGIN_TYPE_EMPLOYEES:

                tvDianyuan.setTextColor(getResources().getColor(R.color.black_87));
                tvDianyuanL.setBackgroundColor(getResources().getColor(R.color.black_87));
                tvAdmin.setTextColor(getResources().getColor(R.color.black_26));
                tvAdminL.setBackgroundColor(getResources().getColor(R.color.app_back_color));
                break;
            case App.LOGIN_TYPE_ADMIN:
                tvAdmin.setTextColor(getResources().getColor(R.color.black_87));
                tvAdminL.setBackgroundColor(getResources().getColor(R.color.black_87));
                tvDianyuan.setTextColor(getResources().getColor(R.color.black_26));
                tvDianyuanL.setBackgroundColor(getResources().getColor(R.color.app_back_color));
                break;
        }
    }

    /**
     * 登录接口
     */
    private void Login() {
        mLoading = LoadingDialogUtils.createLoadingDialog(LoginActivity.this, "登录中...");//添加等待框
        Call<Object> call = getApi().login(etPhone.getText().toString(),
                            etVerify.getText().toString(),
                            App.login_type);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body())==GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("登陆成功");
                        LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                        //TODO 初始化数据
                        JSONObject data = GsonUtils.getResultData(response.body());
                        App.uid = data.optInt("uid");
                        App.phone = data.optString("phone");
                        App.username = data.optString("username");
                        App.role = data.optString("role");
                        App.merchant = data.optString("merchant");
                        App.area = data.optString("area");
                        App.opr_cls = data.optString("opr_cls");
                        App.token = data.optString("token");
                        //TODO 跳转主页
                        startActivity(new Intent(LoginActivity.this, FragmentActivity.class));
                        finish();
                    } else {
                        ToastUtil.showMessage("登录失败");
                        LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                    }

                } else {
                    LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                    LogUtil.i("登陆失败response.message():" + response.message());

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                ToastUtil.showMessage("登录超时");
            }

        });
    }
//    /**
//     * 获取开户行接口
//     */
//private Dialog mLoading;
//    private void tttt() {
//        mLoading = LoadingDialogUtils.createLoadingDialog(RegistrationInformationActivity.this, "获取中...");//添加等待框
//        Call<BankBean> call = getApi().getbank();
//
//        call.enqueue(new Callback<BankBean>() {
//            @Override
//            public void onResponse(Call<BankBean> call, final Response<BankBean> response) {
//                if (response.isSuccessful()) {
//                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
//                        // do SomeThing
//                        LogUtil.i("成功");
//                        //TODO 初始化数据
//
//                    } else {
//                        ToastUtil.showMessage("获取失败");
//                    }
//                } else {
//                    LogUtil.i("失败response.message():" + response.message());
//                }
//                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
//            }
//
//            @Override
//            public void onFailure(Call<BankBean> call, Throwable t) {
//                LogUtil.e(TAG, t.toString());
//                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
//                ToastUtil.showMessage("超时");
//            }
//
//        });
//    }
}
