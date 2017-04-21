package com.fuleme.business.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.AboutUsActivity;
import com.fuleme.business.activity.BasicInformationActivity;
import com.fuleme.business.activity.BusinessApplicationActivity;
import com.fuleme.business.activity.ClerkManagementActivity;
import com.fuleme.business.activity.ContractrateActivity;
import com.fuleme.business.activity.LoginActivity;
import com.fuleme.business.activity.RegistrationStoreActivity;
import com.fuleme.business.activity.UserDetailsActivity;
import com.fuleme.business.download.DeviceUtils;
import com.fuleme.business.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的
 */
public class CFragment extends Fragment {
    @Bind(R.id.iv_btm_yy)
    ImageView ivBtmYY;
    @Bind(R.id.iv_btm_tongzhi)
    ImageView ivBtmTongzhi;
    final int EXIT_USERDETAIL = 100;//退出
    final int EXIT_TO_USERDETAIL = 101;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_fulemenumber)
    TextView tvFulemenumber;
    @Bind(R.id.ll_addstore)
    LinearLayout llAddstore;
    @Bind(R.id.tv_version)
    TextView tvVersion;

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
        tvVersion.setText("当前版本:" + DeviceUtils.getVersionName(getActivity()));
        //根据登录类型显示隐藏员工管理
        if ("2".equals(App.role)) {
        } else {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.ll_title_1, R.id.ll_title_2, R.id.ll_title_3, R.id.ll_addstore, R.id.ll_set_zh, R.id.ll_set_s_a, R.id.ll_zhsz, R.id.ll_guanyuwomen, R.id.iv_btm_yy, R.id.iv_btm_tongzhi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title_1:
                //签约信息
                if(!TextUtils.isEmpty(App.short_id)){
                    startActivity(new Intent(getActivity(), ContractrateActivity.class));
                }else{
                    ToastUtil.showMessage("您还没有店铺，快去添加一个吧");
                }

                break;
            case R.id.ll_title_2:
                // 店员管理
                if(!TextUtils.isEmpty(App.short_id)){
                    startActivity(new Intent(getActivity(), ClerkManagementActivity.class));
                }else{
                    ToastUtil.showMessage("您还没有店铺，快去添加一个吧");
                }

                break;
            case R.id.ll_title_3:
                //店铺基本信息
                if(!TextUtils.isEmpty(App.short_id)){
                    startActivity(new Intent(getActivity(), BasicInformationActivity.class));
                }else{
                    ToastUtil.showMessage("您还没有店铺，快去添加一个吧");
                }

                break;
            case R.id.ll_set_zh:
                // 账户详情
                startActivityForResult(new Intent(getActivity(), UserDetailsActivity.class), EXIT_TO_USERDETAIL);
                break;
            case R.id.ll_set_s_a:
                // 跳转商铺应用
                startActivity(new Intent(getActivity(), BusinessApplicationActivity.class));
                break;
            case R.id.ll_addstore:
                // 添加店铺
                startActivity(new Intent(getActivity(), RegistrationStoreActivity.class));
                break;
            case R.id.ll_zhsz:
                // 账号设置
                startActivity(new Intent(getActivity(), UserDetailsActivity.class));
                break;
            case R.id.iv_btm_yy:
                //语音播报
                setYY();
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
            ivBtmTongzhi.setImageResource(R.mipmap.icon_off);
            App.unbindAccount();
        } else {
            ivBtmTongzhi.setImageResource(R.mipmap.icon_on);
            App.bindAccount();
        }
    }

    private void setYY() {
        if (App.bindYY) {
            ivBtmYY.setImageResource(R.mipmap.icon_off);
            App.bindYY = false;
        } else {
            ivBtmYY.setImageResource(R.mipmap.icon_on);
            App.bindYY = true;
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
