package com.fuleme.business.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.OrderAdapter;
import com.fuleme.business.bean.OrderBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DividerItemDecoration;
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
 * 订单
 */
public class OrdersActivity extends BaseActivity {
    private static final String TAG = "BooksActivity";
    @Bind(R.id.rv_m_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.none)
    TextView none;
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    private Context context;
    LinearLayoutManager linearLayoutManager;
    OrderAdapter mAdapter;
    private List<OrderBean.DataBean> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);
        context = OrdersActivity.this;
        init();
    }

    public void init() {
        tvTitle.setText("账本");
        initView();
        initData();
    }

    public void initView() {
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new OrderAdapter(context, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(new OrderAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, OrderBean.DataBean dataBean) {
                OrderDetailsActivity.shopid = dataBean.getShort_id();
                OrderDetailsActivity.short_name = dataBean.getShort_name();

                startActivity(new Intent(context, OrderDetailsActivity.class));
            }
        });
        //刷新控件
        demoSwiperefreshlayout.setColorSchemeResources(R.color.white);
        demoSwiperefreshlayout.setProgressBackgroundColorSchemeResource(R.color.theme);
        demoSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list();
            }
        });


    }

    protected void initData() {
        list();
    }

    /**
     * 店员管理接口获取店铺
     */
    private void list() {
        showLoading("获取中...");
        Call<OrderBean> call = App.getInstance().getServerApi().list(App.token);
        call.enqueue(new Callback<OrderBean>() {
            @Override
            public void onResponse(Call<OrderBean> call, Response<OrderBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        //TODO 初始化数据
                        if (response.body().getData() != null) {
                            mDatas.clear();
                            mDatas.addAll(response.body().getData());
                        }
                        if (mDatas.size() > 0) {
                            none.setVisibility(View.GONE);
                        } else {
                            none.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<OrderBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                demoSwiperefreshlayout.setRefreshing(false);
                closeLoading();
                ToastUtil.showMessage("超时");
            }

        });
    }

    @OnClick(R.id.tv_left)
    public void onClick() {
        finish();
    }
}
