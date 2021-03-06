package com.fuleme.business.activity.Version2;

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
import com.fuleme.business.adapter.ServiceBusinessesAdapter;
import com.fuleme.business.bean.PromoteBean;
import com.fuleme.business.bean.ServiceBusinessesBean;
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
 * 我的商户
 */
public class ServiceBusinessesActivity extends BaseActivity {
    private static final String TAG = "ServiceBusinessesActivi";
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
    @Bind(R.id.rv_m_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.ll_meiyou)
    LinearLayout llMeiyou;
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    @Bind(R.id.tv_1)
    TextView tv1;
    private List<ServiceBusinessesBean.DataBean> mDatas = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    ServiceBusinessesAdapter mAdapter;
    int type = 1;//1我服务的商户  2团队商户
    private int page = 1;
    private int list_rows = 10;
    public boolean state = true;//是否刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_businesses);
        ButterKnife.bind(this);
        tvTitle.setText("我服务的商户");
        initView();
        setState(type);
    }

    public void initView() {
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(ServiceBusinessesActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ServiceBusinessesAdapter(ServiceBusinessesActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ServiceBusinessesAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, ServiceBusinessesBean.DataBean bean) {
                MerchantDetailsActivity.shop_id = bean.getId()+"";
                MerchantDetailsActivity.shop_type = type+"";
                startActivity(new Intent(ServiceBusinessesActivity.this, MerchantDetailsActivity.class));
            }
        });
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
                    shoplist();
                }
            }
        });
    }

    @OnClick({R.id.tv_left, R.id.ll_admin, R.id.ll_dianyuan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_dianyuan:
                setState(1);
                break;
            case R.id.ll_admin:
                setState(2);
                break;
        }
    }
    /**
     * 重置数据
     */
    private void refresh() {
        page = 1;
        state = true;
        shoplist();
    }
    /**
     * 改变登录类型
     *
     * @param types
     */
    private void setState(int types) {
        type = types;
        switch (type) {
            case 1:
                tvDianyuan.setTextColor(getResources().getColor(R.color.red_1));
                tvDianyuanL.setBackgroundColor(getResources().getColor(R.color.red_1));
                tvAdmin.setTextColor(getResources().getColor(R.color.progress_background));
                tvAdminL.setBackgroundColor(getResources().getColor(R.color.app_back_color));
                break;
            case 2:
                tvAdmin.setTextColor(getResources().getColor(R.color.red_1));
                tvAdminL.setBackgroundColor(getResources().getColor(R.color.red_1));
                tvDianyuan.setTextColor(getResources().getColor(R.color.progress_background));
                tvDianyuanL.setBackgroundColor(getResources().getColor(R.color.app_back_color));
                break;

        }
        shoplist();
    }

    /**
     * 我的推广团队
     */
    private void shoplist() {
        showLoading("获取中...");
        Call<ServiceBusinessesBean>
                call = getApi().shoplist(
                App.token,
                type + "",
                page,
                list_rows
        );
        call.enqueue(new Callback<ServiceBusinessesBean>() {
            @Override
            public void onResponse(Call<ServiceBusinessesBean> call, final Response<ServiceBusinessesBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        LogUtil.i("page=" + page);
                        if (page == 1) {
                            mDatas.clear();
                        } else {
                            state = true;
                        }
                        if (response.body().getData() == null || response.body().getData().size() < list_rows) {
                            state = false;
                        }
                        mDatas.addAll(response.body().getData());
                        if (mDatas.size() < 1) {
                            llMeiyou.setVisibility(View.VISIBLE);
                        } else {
                            llMeiyou.setVisibility(View.GONE);
                        }
                        tv1.setText(response.body().getData().size()+"");
                        mAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<ServiceBusinessesBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                demoSwiperefreshlayout.setRefreshing(false);
                ToastUtil.showMessage("超时");
            }

        });
    }
}
