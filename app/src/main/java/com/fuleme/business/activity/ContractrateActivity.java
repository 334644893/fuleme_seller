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

public class ContractrateActivity extends BaseActivity {
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
        setContentView(R.layout.activity_contractrate);
        ButterKnife.bind(this);
        context = this;
        tvTitle.setText("签约信息");
        init();
    }
    public void init() {
        getcontractrate();
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
     * 获取自媒体
     */
    private void getcontractrate() {

        showLoading("加载中...");
        Call<Object> call = getApi().getcontractrate(App.token,App.short_id);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        //TODO 初始化数据
                        mDatas.clear();
                        JSONObject data = GsonUtils.getResultData(response.body());
                        ContractBean alipayrateContractBean=new ContractBean();
                        alipayrateContractBean.setName("支付宝");
                        alipayrateContractBean.setRate(data.optString("alipayrate")+"‰");
                        ContractBean wechatrateContractBean=new ContractBean();
                        wechatrateContractBean.setName("微信");
                        wechatrateContractBean.setRate(data.optString("wechatrate")+"‰");
                        mDatas.add(alipayrateContractBean);
                        mDatas.add(wechatrateContractBean);
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
