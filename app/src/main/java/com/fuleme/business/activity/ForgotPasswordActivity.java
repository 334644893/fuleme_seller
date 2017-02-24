package com.fuleme.business.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 忘记密码
 */
public class ForgotPasswordActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;

    public void initView() {
        tvTitle.setText("忘记密码");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }
}
