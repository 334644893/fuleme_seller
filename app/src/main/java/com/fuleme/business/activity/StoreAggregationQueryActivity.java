package com.fuleme.business.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.StoreAQAdapter;
import com.fuleme.business.bean.ClerkInfoBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.fragment.BFragment;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.LoadingDialogUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 汇总查询-查询店铺
 */
public class StoreAggregationQueryActivity extends BaseActivity {
    private static final String TAG = "StoreAggregationQueryAc";
    private Context context = StoreAggregationQueryActivity.this;
    public static int intentType = 0;//判断从哪个页面过来查询
    public static final int AGGREGATIONQUERYACTIVITY = 1;
    public static final int BFRAGMENT = 2;
    public static final int CLERKMANAGEMENTACTIVITY = 3;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private List<ClerkInfoBean.DataBean> mDatas = new ArrayList<ClerkInfoBean.DataBean>();
    LinearLayoutManager linearLayoutManager;
    StoreAQAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        ButterKnife.bind(this);
        initView();
        getmerchantclerkinfo();

    }


    public void initView() {
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(StoreAggregationQueryActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new StoreAQAdapter(StoreAggregationQueryActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new StoreAQAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, ClerkInfoBean.DataBean bean) {
                switch (intentType) {
                    case AGGREGATIONQUERYACTIVITY:
                        AggregationQueryActivity.storeName = bean.getName();
                        AggregationQueryActivity.storeID = bean.getId();
                        break;
                    case BFRAGMENT:
                        App.merchant = bean.getName();
                        App.short_id = bean.getId();
                        App.qrcode = bean.getQrcode();
                        App.short_state = bean.getState();
                        break;
                    case CLERKMANAGEMENTACTIVITY:
                        ClerkManagementActivity.storeName = bean.getName();
                        ClerkManagementActivity.storeID = bean.getId();
                        break;
                }
                finish();
            }
        });

    }

    /**
     * 店员管理接口获取店铺
     */
    private Dialog mLoading;

    private void getmerchantclerkinfo() {
        mLoading = LoadingDialogUtils.createLoadingDialog(context, "获取中...");//添加等待框
        Call<ClerkInfoBean> call = getApi().getmerchantclerkinfo(App.token);

        call.enqueue(new Callback<ClerkInfoBean>() {
            @Override
            public void onResponse(Call<ClerkInfoBean> call, Response<ClerkInfoBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        //TODO 初始化数据
                        mDatas.clear();
                        mDatas.addAll(response.body().getData());
                        if (intentType != BFRAGMENT) {
                            ClerkInfoBean.DataBean bean = new ClerkInfoBean.DataBean();
                            bean.setName("全部店铺");
                            bean.setId(App.PLACEHOLDER);
                            mDatas.add(0, bean);
                        }
                        mAdapter.notifyDataSetChanged();


                    } else {
                        ToastUtil.showMessage("失败");
                    }

                } else {

                    LogUtil.i("失败response.message():" + response.message());

                }
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
            }

            @Override
            public void onFailure(Call<ClerkInfoBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }

}
