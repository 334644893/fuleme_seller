package com.fuleme.business.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.AboutUsActivity;
import com.fuleme.business.activity.BusinessApplicationActivity;
import com.fuleme.business.activity.ClerkManagementActivity;
import com.fuleme.business.activity.LoginActivity;
import com.fuleme.business.activity.RegistrationStoreActivity;
import com.fuleme.business.activity.UserDetailsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的
 */
public class CFragment extends Fragment {
    @Bind(R.id.iv_btm_tongzhi)
    ImageView ivBtmTongzhi;
    final int EXIT_USERDETAIL = 100;//退出
    final int EXIT_TO_USERDETAIL = 101;
    @Bind(R.id.ll_set_dygl_line)
    View llSetDyglLine;
    @Bind(R.id.ll_set_dygl)
    LinearLayout llSetDygl;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_fulemenumber)
    TextView tvFulemenumber;
    @Bind(R.id.ll_addstore)
    LinearLayout llAddstore;
    @Bind(R.id.v_add_line)
    View vAddLine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_administrator_c, container, false);

        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void initView() {
        tvPhone.setText(App.phone);
        tvFulemenumber.setText(App.username);
        //根据登录类型显示隐藏员工管理
        if ("2".equals(App.role)) {
            llSetDygl.setVisibility(View.GONE);
            llSetDyglLine.setVisibility(View.GONE);
            llAddstore.setVisibility(View.GONE);
            vAddLine.setVisibility(View.GONE);
        } else {
            llSetDygl.setVisibility(View.VISIBLE);
            llSetDyglLine.setVisibility(View.VISIBLE);
            llAddstore.setVisibility(View.VISIBLE);
            vAddLine.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.ll_addstore, R.id.ll_set_zh, R.id.ll_set_s_a, R.id.ll_set_dygl, R.id.ll_guanyuwomen, R.id.iv_btm_tongzhi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_set_zh:
                // 账户详情
                startActivityForResult(new Intent(getActivity(), UserDetailsActivity.class), EXIT_TO_USERDETAIL);
                break;
            case R.id.ll_set_s_a:
                // 跳转商铺应用
                startActivity(new Intent(getActivity(), BusinessApplicationActivity.class));
                break;
            case R.id.ll_set_dygl:
                // 跳转店员管理
                startActivity(new Intent(getActivity(), ClerkManagementActivity.class));
                break;
            case R.id.ll_addstore:
                // 注册店铺
                startActivity(new Intent(getActivity(), RegistrationStoreActivity.class));
                break;
            case R.id.iv_btm_tongzhi:
                //通知
                setBtmTongzhi();
                break;
            case R.id.ll_guanyuwomen:
                // 跳转关于我们
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
        }
    }

    private void setBtmTongzhi() {
        if (App.bindAccount) {
            ivBtmTongzhi.setImageDrawable(getResources().getDrawable(R.mipmap.icon_off));
            App.unbindAccount();
        } else {
            ivBtmTongzhi.setImageDrawable(getResources().getDrawable(R.mipmap.icon_on));
            App.bindAccount();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EXIT_TO_USERDETAIL && resultCode == EXIT_USERDETAIL) {
            //TODO 清空信息并跳转登录页
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

}
