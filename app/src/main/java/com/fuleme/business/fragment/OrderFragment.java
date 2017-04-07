package com.fuleme.business.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.OrderDetailsActivity;
import com.fuleme.business.adapter.OrderAdapter;
import com.fuleme.business.bean.OrderBean;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.LoadingDialogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {
    private static final String TAG = "OrderFragment";
    @Bind(R.id.rv_m_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    LinearLayoutManager linearLayoutManager;
    OrderAdapter mAdapter;
    @Bind(R.id.none)
    TextView none;
    private List<OrderBean.DataBean> mDatas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_order_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init() {
        initView();
        initData();
    }

    public void initView() {

        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new OrderAdapter(getActivity(), mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(new OrderAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, OrderBean.DataBean dataBean) {
                OrderDetailsActivity.shopid=dataBean.getShort_id();
                OrderDetailsActivity.short_name=dataBean.getShort_name();
                startActivity(new Intent(getActivity(), OrderDetailsActivity.class));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 店员管理接口获取店铺
     */
    private Dialog mLoading;

    private void list() {
        mLoading = LoadingDialogUtils.createLoadingDialog(getActivity(), "获取中...",false);//添加等待框
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
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
            }

            @Override
            public void onFailure(Call<OrderBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                demoSwiperefreshlayout.setRefreshing(false);
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }
}
