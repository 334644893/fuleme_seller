package com.fuleme.business.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.LRTextAdapter;
import com.fuleme.business.bean.ContractBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;

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
 * 店铺基本资料
 */
public class BasicInformationActivity extends BaseActivity {
    private static final String TAG = "ContractrateActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.m_recyclerview)
    RecyclerView mRecyclerview;
    private Context context;
    LinearLayoutManager linearLayoutManager;
    LRTextAdapter mAdapter;
    private List<ContractBean> mDatas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_information);
        ButterKnife.bind(this);
        context = this;
        tvTitle.setText("店铺基本资料");
        init();
    }
    public void init() {
        basicinfo();
    }
    public void initView() {
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mAdapter = new LRTextAdapter(context, mDatas);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
    }
    /**
     * 获取店铺基本资料
     */
    private void basicinfo() {

        showLoading("加载中...");
        Call<Object> call = getApi().basicinfo(App.token,App.short_id);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        mDatas.clear();
                        JSONObject data = GsonUtils.getResultData(response.body());
                        ContractBean ContractBean = new ContractBean();
                        ContractBean.setName("商户号:");
                        ContractBean.setRate(data.optString("id"));
                        mDatas.add(ContractBean);
                        ContractBean ContractBean1 = new ContractBean();
                        ContractBean1.setName("店铺名称:");
                        ContractBean1.setRate(data.optString("short_name"));
                        mDatas.add(ContractBean1);
                        ContractBean ContractBean2 = new ContractBean();
                        ContractBean2.setName("所在地区:");
                        ContractBean2.setRate(data.optString("merchant_area"));
                        mDatas.add(ContractBean2);
                        ContractBean ContractBean3 = new ContractBean();
                        ContractBean3.setName("详细地址:");
                        ContractBean3.setRate(data.optString("merchant_address"));
                        mDatas.add(ContractBean3);
                        ContractBean ContractBean4 = new ContractBean();
                        ContractBean4.setName("联系人:");
                        ContractBean4.setRate(data.optString("contact_person_name"));
                        mDatas.add(ContractBean4);
                        ContractBean ContractBean5 = new ContractBean();
                        ContractBean5.setName("联系电话:");
                        ContractBean5.setRate(data.optString("contact_person_mobile"));
                        mDatas.add(ContractBean5);
                        initView();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }
                } else {
                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    finish();
                }
               closeLoading();//取消等待框
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }
    @OnClick(R.id.tv_left)
    public void onClick() {
        finish();
    }
}
