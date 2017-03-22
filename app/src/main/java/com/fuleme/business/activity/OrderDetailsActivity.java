package com.fuleme.business.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.alibaba.idst.nls.internal.common.PhoneInfo;
import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.OrderDetailsAdapter;
import com.fuleme.business.bean.OrderDetailsBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.LoadingDialogUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 订单详情
 */
public class OrderDetailsActivity extends BaseActivity {
    private static final String TAG = "OrderDetailsActivity";
    NumberFormat nf = NumberFormat.getInstance();
    public static String year = "2017";
    public static String month = "03";
    public static String shopid = "";
    public static String short_name = "";
    public static int page = 1;
    public static int list_rows = 5;
    public boolean state = true;//是否刷新
    public static boolean textState = true;//true 显示正在加载，false显示 没有更多
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.tv_3)
    TextView tv3;
    @Bind(R.id.rv_m_recyclerview)
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager;
    OrderDetailsAdapter mAdapter;
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    private List<OrderDetailsBean.DataBean> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        nf.setGroupingUsed(false);
        init();
    }

    public void init() {
        initView();
        initData();
    }

    protected void initData() {
        orderInfo();

    }

    public void initView() {

        tvTitle.setText(short_name);
        tv1.setText(year + "-" + month);
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(OrderDetailsActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new OrderDetailsAdapter(OrderDetailsActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(OrderDetailsActivity.this, LinearLayoutManager.VERTICAL));
        //刷新控件
        demoSwiperefreshlayout.setColorSchemeResources(R.color.white);
        demoSwiperefreshlayout.setProgressBackgroundColorSchemeResource(R.color.theme);
        demoSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉重置数据
                page = 1;
                state = true;
                orderInfo();
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = linearLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 1 表示剩下1个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0 && state == true) {
                    state = false;
                    page += 1;
                    orderInfo();
                }
            }
        });

    }

    @OnClick({R.id.tv_left, R.id.tv_1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_1:
                break;
        }
    }

    /**
     * 获取店铺订单
     */
    private Dialog mLoading;

    private void orderInfo() {
        mLoading = LoadingDialogUtils.createLoadingDialog(OrderDetailsActivity.this, "获取中...");//添加等待框
        Call<OrderDetailsBean> call =
                getApi().orderInfo(
                        App.token,
                        year,
                        month,
                        shopid,
                        page,
                        list_rows);

        call.enqueue(new Callback<OrderDetailsBean>() {
            @Override
            public void onResponse(Call<OrderDetailsBean> call, final Response<OrderDetailsBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        LogUtil.i("page=" + page);
                        //TODO 初始化数据
                        if (page == 1) {
                            mDatas.clear();
                        } else {
                            state = true;
                            textState = true;
                        }
                        if (response.body().getData() == null || response.body().getData().size() < list_rows) {
                            state = false;
                            textState = false;
                        }
                        tv2.setText("共" + response.body().getTotalOrder() + "笔");

                        tv3.setText("¥ " + nf.format(response.body().getTotalAmount()));
                        mDatas.addAll(response.body().getData());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showMessage("获取失败");
                    }
                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                demoSwiperefreshlayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<OrderDetailsBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                demoSwiperefreshlayout.setRefreshing(false);
                ToastUtil.showMessage("超时");
            }

        });
    }
}
