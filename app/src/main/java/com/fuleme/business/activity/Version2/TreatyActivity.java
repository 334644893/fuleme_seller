package com.fuleme.business.activity.Version2;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.activity.RegistrationStoreActivity;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TreatyActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.iv_gougou)
    ImageView ivGougou;
    @Bind(R.id.btn_tj_1)
    Button btnTj1;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treaty);
        ButterKnife.bind(this);
        tvTitle.setText("服务条款");
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @OnClick({R.id.tv_left, R.id.ll_yes, R.id.btn_tj_1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_yes:
                if (flag) {
                    ivGougou.setImageResource(R.mipmap.icon_xuanze);
                    btnTj1.setBackgroundResource(R.drawable.shape_corner_hui_p);
                    flag = false;
                } else {
                    flag = true;
                    ivGougou.setImageResource(R.mipmap.icon_xuanzhong);
                    btnTj1.setBackgroundResource(R.drawable.shape_corner_0);
                }
                break;
            case R.id.btn_tj_1:
                if (flag) {
                    // 添加店铺
                    startActivity(new Intent(TreatyActivity.this, RegistrationStoreActivity.class));
                    finish();
                } else {
                    ToastUtil.showMessage("请同意服务条款");
                }

                break;
        }
    }
}
