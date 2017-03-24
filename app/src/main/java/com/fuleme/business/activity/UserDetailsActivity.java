package com.fuleme.business.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.sdk.android.push.CommonCallback;
import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.fragment.FragmentActivity;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.widget.CustomDialog;
import com.fuleme.business.widget.NoticeDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fuleme.business.App.pushService;

/**
 * 账号详情
 */
public class UserDetailsActivity extends BaseActivity {
    private static final String TAG = "UserDetailsActivity";
    @Bind(R.id.tv_store_name)
    TextView tvStoreName;
    @Bind(R.id.tv_region)
    TextView tvRegion;
    @Bind(R.id.tv_industry)
    TextView tvIndustry;
    private CustomDialog dialog;
    final int EXIT_USERDETAIL = 100;//退出
    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        tvTitle.setText("账号详情");
        if("0".equals(App.short_state)){
            tvStoreName.setText(App.merchant+"(审核中)");
        }else{
            tvStoreName.setText(App.merchant+"(已审核)");
        }
        tvRegion.setText(App.short_area);

    }

    @OnClick({R.id.tv_left, R.id.btn_login, R.id.ll_forgotpassword})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_login:
                //TODO 退出登录
                CustomDialog.Builder customBuilder = new
                        CustomDialog.Builder(UserDetailsActivity.this);
                customBuilder
                        .setTitle("确定退出")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(UserDetailsActivity.this, FragmentActivity.class);
                                        setResult(EXIT_USERDETAIL, intent);
                                        //绑定推送账号
                                        App.unbindAccount();
                                        finish();
                                    }
                                });
                dialog = customBuilder.create();
                dialog.show();
                break;
            case R.id.ll_forgotpassword:
                // 修改密码
                startActivity(new Intent(UserDetailsActivity.this, ChangePasswordActivity.class));
                break;
        }
    }

}
