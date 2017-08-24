package com.fuleme.business.activity.Version2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.MyBankAdapter;
import com.fuleme.business.bean.MyBankBean;
import com.fuleme.business.bean.PromoteBean;
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
 * 推广-我的银行卡
 */
public class MyBankActivity extends BaseActivity {
    private static final String TAG = "MyBankActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_m_recyclerview)
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager;
    MyBankAdapter mAdapter;
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    @Bind(R.id.ll_meiyou)
    LinearLayout llMeiyou;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    private List<MyBankBean.DataBean> mDatas = new ArrayList<>();
    private int page = 1;
    private int list_rows = 10;
    public boolean state = true;//是否刷新
//    public static int type = 1;//  1提现页面跳转

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank);
        ButterKnife.bind(this);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.mipmap.icon_plus);
        init();
    }

    public void init() {
        initView();

    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    protected void initData() {
        bankcard();
    }

    public void initView() {
        tvTitle.setText("我的银行卡");
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(MyBankActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MyBankAdapter(MyBankActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MyBankAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, MyBankBean.DataBean bean) {
//                switch (type) {
//                    case 1:
                        CashActivity.bank=bean.getAccount_bank();
                        CashActivity.number=bean.getBankcard();
                        finish();
//                        break;
//                }
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
                    bankcard();
                }
            }
        });

    }

    /**
     * 重置数据
     */
    private void refresh() {
        page = 1;
        state = true;
        bankcard();
    }

    @OnClick({R.id.tv_left, R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.iv_right:
                startActivity(new Intent(MyBankActivity.this, BindBankCardActivity.class));

                break;
        }
    }

    /**
     * 我的推广团队
     */
    private void bankcard() {
        showLoading("获取中...");
        Call<MyBankBean>
                call = getApi().bankcard(
                App.token

        );
        call.enqueue(new Callback<MyBankBean>() {
            @Override
            public void onResponse(Call<MyBankBean> call, final Response<MyBankBean> response) {
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
            public void onFailure(Call<MyBankBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                demoSwiperefreshlayout.setRefreshing(false);
                ToastUtil.showMessage("超时");
            }

        });
    }
}
