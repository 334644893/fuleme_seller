package com.fuleme.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.fragment.FragmentActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setState(App.LOGIN_TYPE_EMPLOYEES);
    }

    @OnClick({R.id.ll_dianyuan, R.id.ll_admin, R.id.btn_login, R.id.tv_zczh})
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
//                if (App.login_type == App.LOGIN_TYPE_ADMIN) {
//                    // TODO 管理员登录
//                } else if (App.login_type == App.LOGIN_TYPE_EMPLOYEES) {
//                    // TODO 员工登录
//                }
                startActivity(new Intent(LoginActivity.this, FragmentActivity.class));
                finish();
                break;
            case R.id.tv_zczh:
                startActivity(new Intent(LoginActivity.this, RegistrationInformationActivity.class));

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
}
