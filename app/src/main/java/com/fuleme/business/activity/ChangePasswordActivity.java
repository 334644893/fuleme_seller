package com.fuleme.business.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fuleme.business.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends AppCompatActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_f_p_phone)
    EditText etFPPhone;
    @Bind(R.id.et_f_p_ps)
    EditText etFPPs;
    @Bind(R.id.et_f_p_ep)
    EditText etFPEp;
    public void initView() {
        tvTitle.setText("修改密码");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick({R.id.tv_left, R.id.btn_tj})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_tj:
                break;
        }
    }
}
