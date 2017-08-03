package com.fuleme.business.activity.Version2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.BalanceAdapter;
import com.fuleme.business.bean.BalanceBean;
import com.fuleme.business.bean.OrderBean;
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

public class BalanceActivity extends BaseActivity {
    private static final String TAG = "BalanceActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.rv_m_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    @Bind(R.id.none)
    LinearLayout none;
    @Bind(R.id.ll_visibo)
    LinearLayout llVisibo;
    private Context context;
    LinearLayoutManager linearLayoutManager;
    BalanceAdapter mAdapter;
    private List<BalanceBean.DataBean.ListBean> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        ButterKnife.bind(this);
        tvTitle.setText("我的余额");
        context = BalanceActivity.this;
        init();
    }

    public void init() {
        initView();
        initData();
    }

    public void initView() {
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new BalanceAdapter(context, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        //刷新控件
        demoSwiperefreshlayout.setColorSchemeResources(R.color.white);
        demoSwiperefreshlayout.setProgressBackgroundColorSchemeResource(R.color.theme);
        demoSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                drawmoneylist();
            }
        });


    }

    protected void initData() {
        drawmoneylist();
    }

    @OnClick({R.id.ll_set_s_a, R.id.btn_tj_1, R.id.tv_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_set_s_a:
                startActivity(new  Intent(context, MyCommissionActivity.class));
                break;
            case R.id.btn_tj_1:
                startActivity(new Intent(context, CashActivity.class));
                break;
        }
    }

    /**
     * 提现列表接口
     */
    private void drawmoneylist() {
        showLoading("获取中...");
        Call<BalanceBean> call = App.getInstance().getServerApi().drawmoneylist(App.token);
        call.enqueue(new Callback<BalanceBean>() {
            @Override
            public void onResponse(Call<BalanceBean> call, Response<BalanceBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        if (response.body().getData() != null) {
                            CashActivity.cash=response.body().getData().getMoney()+"";
                            tv1.setText("￥" + response.body().getData().getMoney());
                            tv2.setText("￥" + response.body().getData().getProfit());
                            mDatas.clear();
                            mDatas.addAll(response.body().getData().getList());
                        }
                        if (mDatas.size() > 0) {
                            none.setVisibility(View.GONE);
                            llVisibo.setVisibility(View.VISIBLE);
                        } else {
                            none.setVisibility(View.VISIBLE);
                            llVisibo.setVisibility(View.GONE);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }
                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }
                demoSwiperefreshlayout.setRefreshing(false);
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<BalanceBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                demoSwiperefreshlayout.setRefreshing(false);
                closeLoading();
                ToastUtil.showMessage("超时");
            }

        });
    }
}
