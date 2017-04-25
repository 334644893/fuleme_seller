package com.fuleme.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.CouponsAdapter;
import com.fuleme.business.bean.CouponsBean;
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

/**
 * 优惠券(商家活动)
 */
public class CouponsActivity extends BaseActivity {
    private static final String TAG = "CouponsActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.rl_none)
    RelativeLayout rlNone;
    private List<CouponsBean.DataBean> mDatas = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    CouponsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);
        ButterKnife.bind(this);
        tvTitle.setText(App.merchant);
        initView();
    }

    public void initView() {
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(CouponsActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CouponsAdapter(CouponsActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new CouponsAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, CouponsBean.DataBean bean) {
                if (!"2".equals(App.role)) {
                    CouponsDetailsActivity.id = bean.getId();
                    startActivity(new Intent(CouponsActivity.this, CouponsDetailsActivity.class));
                }

            }
        });

    }

    @Override
    protected void onResume() {
        coupon();
        super.onResume();
    }

    @OnClick({R.id.tv_left, R.id.tv_new_action,R.id.im_tianjia})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_new_action:
                //根据登录类型
                if ("2".equals(App.role)) {
                    ToastUtil.showMessage("您没有权限这么做");
                } else {
                    startActivity(new Intent(CouponsActivity.this, AddCoupinsActivity.class));
                }
                break;
            case R.id.im_tianjia:
                //根据登录类型
                if ("2".equals(App.role)) {
                    ToastUtil.showMessage("您没有权限这么做");
                } else {
                    startActivity(new Intent(CouponsActivity.this, AddCoupinsActivity.class));
                }
                break;
        }
    }

    /**
     * 优惠券
     */
    private void coupon() {
        showLoading("获取中...");
        Call<CouponsBean> call = getApi().coupon(App.token, App.short_id);
        call.enqueue(new Callback<CouponsBean>() {
            @Override
            public void onResponse(Call<CouponsBean> call, Response<CouponsBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        //TODO 初始化数据
                        mDatas.clear();
                        mDatas.addAll(response.body().getData());
                        if (mDatas.size() > 0) {
                            rlNone.setVisibility(View.GONE);
                        } else {
                            rlNone.setVisibility(View.VISIBLE);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }
                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<CouponsBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }
}
