package com.fuleme.business.activity.Version2;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.RegistrationStoreActivity;
import com.fuleme.business.adapter.BankAdapter;
import com.fuleme.business.adapter.TextAdapter;
import com.fuleme.business.bean.BankBean;
import com.fuleme.business.bean.MyCommissionBean;
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
 * 银行卡列表
 */
public class ListActivity extends BaseActivity {
    private static final String TAG = "ListActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private List<BankBean.DataBean> mDatas = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    BankAdapter mAdapter;
    public static int INTENTTYPE = 0;//0添加店铺银行 1绑定银行卡

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        tvTitle.setText("选择银行");
        initView();
        getbank();
    }

    public void initView() {
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(ListActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new BankAdapter(ListActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BankAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, BankBean.DataBean bank) {
                switch (INTENTTYPE) {
                    case 0:
                        RegistrationStoreActivity.BANK = bank.getBankName();
                        finish();
                        break;
                    case 1:
                        BindBankCardActivity.BANK = bank.getBankName();
                        finish();
                        break;
                }
            }
        });
    }

    @OnClick(R.id.tv_left)
    public void onClick() {
        finish();
    }

    /**
     * 获取银行卡
     */

    private void getbank() {
        showLoading("获取中...");
        Call<BankBean>
                call;
        call = getApi().getbank(
        );
        call.enqueue(new Callback<BankBean>() {
            @Override
            public void onResponse(Call<BankBean> call, final Response<BankBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        mDatas.addAll(response.body().getData());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }
                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<BankBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }
}
