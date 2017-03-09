package com.fuleme.business.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.IncomeAdapter;
import com.fuleme.business.bean.IncomeBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.LoadingDialogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的账户-收入
 */
public class IncomeActivity extends BaseActivity {
    private static final String TAG = "IncomeActivity";
    int text_state = 0;//0：收缩状态 1：伸张状态
    @Bind(R.id.tv_tipstext)
    TextView tvTipstext;
    @Bind(R.id.rv_m_recyclerview)
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager;
    IncomeAdapter mAdapter;
    @Bind(R.id.iv_tubiao)
    ImageView ivTubiao;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_balance)
    TextView tvBalance;
    private List<IncomeBean.DataBean> mDatas = new ArrayList<IncomeBean.DataBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        initData();

    }

    public void initView() {

        tvTitle.setText("收入");
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(IncomeActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new IncomeAdapter(IncomeActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(IncomeActivity.this, LinearLayoutManager.VERTICAL));


    }

    protected void initData() {
        income();

    }


    @OnClick({R.id.tv_left, R.id.ll_tips})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_tips:
                if (text_state == 0) {
                    tvTipstext.setMaxLines(10);
                    text_state = 1;
                    ivTubiao.setImageDrawable(getResources().getDrawable(R.mipmap.icon31));
                } else if (text_state == 1) {
                    tvTipstext.setMaxLines(1);
                    text_state = 0;
                    ivTubiao.setImageDrawable(getResources().getDrawable(R.mipmap.icon32));
                }
                break;
        }
    }

    /**
     * 获取开户行接口
     */
    private Dialog mLoading;

    private void income() {
        mLoading = LoadingDialogUtils.createLoadingDialog(IncomeActivity.this, "获取中...");//添加等待框
        Call<IncomeBean> call = getApi().income(App.token);

        call.enqueue(new Callback<IncomeBean>() {
            @Override
            public void onResponse(Call<IncomeBean> call, final Response<IncomeBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        //TODO 初始化数据
                        tvBalance.setText(response.body().getBalance() + " 元");
                        tvTipstext.setText(response.body().getPrompt());
                        mDatas = response.body().getData();
//                         IncomeBean.DataBean a= new IncomeBean.DataBean();
//                        a.setResultStatus(1);
//                        a.setArrivalAmount(1000000000);
//                        a.setResultTime("0000-00-00");
//                        mDatas.add(a);
                        initView();
                    } else {
                        ToastUtil.showMessage("获取失败");
                    }
                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
            }

            @Override
            public void onFailure(Call<IncomeBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }
}
