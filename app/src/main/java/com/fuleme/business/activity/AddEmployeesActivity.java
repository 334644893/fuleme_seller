package com.fuleme.business.activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import com.fuleme.business.R;
import com.fuleme.business.bean.CMBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.utils.ToastUtil;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEmployeesActivity extends BaseActivity {
    int type = 0;
    final static int MANAGER = 0;
    final static int ASSISTANT = 1;
    final int FROM = 887;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.iv_dianzhang)
    ImageView ivDianzhang;
    @Bind(R.id.iv_dianyuan)
    ImageView ivDianyuan;
    @Bind(R.id.sv)
    ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employees);
        ButterKnife.bind(this);
        sv.setVerticalScrollBarEnabled(false);

    }

    @OnClick({R.id.ll_dianzhang, R.id.ll_dianyuan, R.id.btn_enter

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_dianzhang:
                setStatetype(MANAGER);
                break;
            case R.id.ll_dianyuan:
                setStatetype(ASSISTANT);
                break;
            case R.id.btn_enter:
                add();

                break;

        }
    }

    private void add() {
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            ToastUtil.showMessage("手机号不能为空");
        } else if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtil.showMessage("姓名不能为空");
        } else if (TextUtils.isEmpty(etPassword.getText().toString())) {
            ToastUtil.showMessage("密码不能为空");
        }else {
            CMBean cmBean = new CMBean();
            cmBean.setPhone(etPhone.getText().toString());
            cmBean.setType(type + "");
            cmBean.setName(etName.getText().toString());
            Intent intent = new Intent(AddEmployeesActivity.this, ClerkManagementActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("cmbean", cmBean);
            intent.putExtras(bundle);
            setResult(FROM, intent);
            finish();
        }

    }

    /**
     * 改变店员店长
     *
     * @param state 0:店长 1：店员
     */
    private void setStatetype(int state) {
        type = state;
        switch (state) {

            case MANAGER:
                ivDianzhang.setImageDrawable(getResources().getDrawable(R.mipmap.icon40));
                ivDianyuan.setImageDrawable(getResources().getDrawable(R.mipmap.icon41));
                break;
            case ASSISTANT:
                ivDianzhang.setImageDrawable(getResources().getDrawable(R.mipmap.icon41));
                ivDianyuan.setImageDrawable(getResources().getDrawable(R.mipmap.icon40));
                break;
        }
    }
}
