package com.fuleme.business.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.CustomerEvationAdapter;
import com.fuleme.business.bean.CustomerBean;
import com.fuleme.business.bean.MemberManagementBean;
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
 * 顾客评价
 */
public class CustomerEvaluationActivity extends BaseActivity {
    private static final String TAG = "CustomerEvaluationActiv";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.m_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.rl_none)
    RelativeLayout rlNone;
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    LinearLayoutManager linearLayoutManager;
    CustomerEvationAdapter mAdapter;
    private List<CustomerBean.DataBean> mDatas = new ArrayList<>();
    private int page = 1;
    private int list_rows = 10;
    public boolean state = true;//是否刷新
    public static boolean textState = true;//true 显示正在加载，false显示 没有更多

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_evaluation);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        initView();
        initData();
    }

    protected void initData() {
        comment();

    }

    public void initView() {

        tvTitle.setText("顾客评价");
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(CustomerEvaluationActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CustomerEvationAdapter(CustomerEvaluationActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(CustomerEvaluationActivity.this, LinearLayoutManager.VERTICAL));
        //刷新控件
        demoSwiperefreshlayout.setColorSchemeResources(R.color.white);
        demoSwiperefreshlayout.setProgressBackgroundColorSchemeResource(R.color.theme);
        demoSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉重置数据
                refresh();
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
                    comment();
                }
            }
        });

    }

    @OnClick(R.id.tv_left)
    public void onClick() {
        finish();
    }

    /**
     * 重置数据
     */
    private void refresh() {
        page = 1;
        state = true;
        comment();
    }

    /**
     * 获取顾客评价
     */

    private void comment() {
        showLoading("获取中...");
        Call<CustomerBean> call =
                getApi().comment(
                        App.token,
                        App.short_id
                        ,
                        page,
                        list_rows
                );

        call.enqueue(new Callback<CustomerBean>() {
            @Override
            public void onResponse(Call<CustomerBean> call, final Response<CustomerBean> response) {
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
                        mDatas.addAll(response.body().getData());
                        mAdapter.notifyDataSetChanged();
                        if (mDatas.size() > 0) {
                            rlNone.setVisibility(View.GONE);
                        } else {
                            rlNone.setVisibility(View.VISIBLE);
                        }

                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }
                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }
                closeLoading();//取消等待框
                demoSwiperefreshlayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<CustomerBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                demoSwiperefreshlayout.setRefreshing(false);
                ToastUtil.showMessage("超时");
            }

        });
    }
}
