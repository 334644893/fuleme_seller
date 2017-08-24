package com.fuleme.business.activity.Version2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.BasicInformationActivity;
import com.fuleme.business.activity.RegistrationStoreActivity;
import com.fuleme.business.adapter.StoreAQAdapter;
import com.fuleme.business.bean.ClerkInfoBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SBasicInformationActivity extends BaseActivity {
    private static final String TAG = "SBasicInformationActivi";
    @Bind(R.id.tv_ts)
    TextView tvTs;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_dianyuan)
    TextView tvDianyuan;
    @Bind(R.id.tv_dianyuan_l)
    View tvDianyuanL;
    @Bind(R.id.tv_admin)
    TextView tvAdmin;
    @Bind(R.id.tv_admin_l)
    View tvAdminL;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    int type = 1;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private List<ClerkInfoBean.DataBean> mDatas = new ArrayList<ClerkInfoBean.DataBean>();
    LinearLayoutManager linearLayoutManager;
    StoreAQAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbasic_information);
        ButterKnife.bind(this);
        tvTitle.setText("店铺管理");
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.mipmap.icon_plus);
        setState(type);
        initView();
    }

    public void initView() {
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(SBasicInformationActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new StoreAQAdapter(SBasicInformationActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new StoreAQAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, ClerkInfoBean.DataBean bean) {
                if (type == 1) {
                    BasicInformationActivity.short_id = bean.getId();
                    BasicInformationActivity.name = bean.getName();
                    BasicInformationActivity.state = bean.getState();
                    BasicInformationActivity.qrcode = bean.getQrcode();
                    startActivity(new Intent(SBasicInformationActivity.this, BasicInformationActivity.class));
                }
            }
        });
    }

    /**
     * 店员管理接口获取店铺
     */

    private void getmerchantclerkinfo() {
        showLoading("获取中...");
        Call<ClerkInfoBean> call = getApi().getmerchantclerkinfo(App.token);

        call.enqueue(new Callback<ClerkInfoBean>() {
            @Override
            public void onResponse(Call<ClerkInfoBean> call, Response<ClerkInfoBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        mDatas.clear();
                        for (ClerkInfoBean.DataBean cb : response.body().getData()) {
                            if (type == 0 && "0".equals(cb.getState())) {
                                mDatas.add(cb);
                            } else if (type == 1 && "1".equals(cb.getState())) {
                                mDatas.add(cb);
                            }
                        }
                        mAdapter.notifyDataSetChanged();

                        if (mDatas != null && mDatas.size() > 0) {
                            tvTs.setVisibility(View.GONE);
                        } else {
                            tvTs.setVisibility(View.VISIBLE);
                        }

                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }

                } else {

                    LogUtil.i("失败response.message():" + response.message());

                }
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<ClerkInfoBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }

    @OnClick({R.id.tv_left, R.id.ll_dianyuan, R.id.ll_admin, R.id.fl_r})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.fl_r:
                // 添加店铺
                startActivityForResult(new Intent(SBasicInformationActivity.this, TreatyActivity.class),0);
                break;
            case R.id.ll_dianyuan:
                setState(1);
                break;
            case R.id.ll_admin:
                setState(0);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setState(type);
    }
    /**
     * 改变登录类型
     *
     * @param types
     */
    private void setState(int types) {
        type = types;
        switch (type) {

            case 0:
                tvAdmin.setTextColor(getResources().getColor(R.color.red_1));
                tvAdminL.setBackgroundColor(getResources().getColor(R.color.red_1));
                tvDianyuan.setTextColor(getResources().getColor(R.color.progress_background));
                tvDianyuanL.setBackgroundColor(getResources().getColor(R.color.app_back_color));
                break;
            case 1:

                tvDianyuan.setTextColor(getResources().getColor(R.color.red_1));
                tvDianyuanL.setBackgroundColor(getResources().getColor(R.color.red_1));
                tvAdmin.setTextColor(getResources().getColor(R.color.progress_background));
                tvAdminL.setBackgroundColor(getResources().getColor(R.color.app_back_color));
                break;
        }
        getmerchantclerkinfo();
    }

}
