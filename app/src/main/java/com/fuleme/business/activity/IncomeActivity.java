package com.fuleme.business.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    public static final String TAG = "IncomeActivity";

//    public boolean state = true;//是否刷新
//    public static boolean textState = true;//true 显示正在加载，false显示 没有更多
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
//    private int page = 1;//当前加载的页数
//    public static int rows = 20;//传给服务器的需要加载的数量
//    int text_state = 0;//0：收缩状态 1：伸张状态
//    @Bind(R.id.tv_tipstext)
//    TextView tvTipstext;
    @Bind(R.id.rv_m_recyclerview)
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager;
    IncomeAdapter mAdapter;
//    @Bind(R.id.iv_tubiao)
//    ImageView ivTubiao;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_balance)
    TextView tvBalance;
    private List<IncomeBean.DataBean> mDatas = new ArrayList<IncomeBean.DataBean>();
    NumberFormat nf = NumberFormat.getInstance();
    int startYear, startMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        nf.setGroupingUsed(false);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        Calendar ca = Calendar.getInstance();
        startYear = ca.get(Calendar.YEAR);
        startMonth = ca.get(Calendar.MONTH)+1;
        initView();
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
        //刷新控件
        demoSwiperefreshlayout.setColorSchemeResources(R.color.white);
        demoSwiperefreshlayout.setProgressBackgroundColorSchemeResource(R.color.theme);
        demoSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉重置数据
//                page = 1;
//                state = true;
                income();
            }
        });
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                int totalItemCount = linearLayoutManager.getItemCount();
//                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
//                // dy>0 表示向下滑动
//                if (lastVisibleItem >= totalItemCount - 1 && dy > 0 && state == true) {
//                    state = false;
//                    page += 1;
//                    income();
//                }
//            }
//        });

    }

    protected void initData() {
        income();

    }


    @OnClick({R.id.tv_left
//            , R.id.ll_tips
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
//            case R.id.ll_tips:
//                if (text_state == 0) {
//                    tvTipstext.setMaxLines(10);
//                    text_state = 1;
//                    ivTubiao.setImageDrawable(getResources().getDrawable(R.mipmap.icon31));
//                } else if (text_state == 1) {
//                    tvTipstext.setMaxLines(1);
//                    text_state = 0;
//                    ivTubiao.setImageDrawable(getResources().getDrawable(R.mipmap.icon32));
//                }
//                break;
        }
    }

    /**
     * 获取收入记录接口
     */
    private Dialog mLoading;

    private void income() {
        mLoading = LoadingDialogUtils.createLoadingDialog(IncomeActivity.this, "获取中...");//添加等待框
        Call<IncomeBean> call = getApi().income(App.token, startYear, startMonth);

        call.enqueue(new Callback<IncomeBean>() {
            @Override
            public void onResponse(Call<IncomeBean> call, final Response<IncomeBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
//                        LogUtil.i("page=" + page);
                        //TODO 初始化数据
//                        if (page == 1) {
//                            mDatas.clear();
//                        } else {
//                            state = true;
//                            textState=true;
//                        }
//                        if (response.body().getData() == null || response.body().getData().size() < rows) {
//                            state = false;
//                            textState=false;
//                        }
                        mDatas.clear();
                        tvBalance.setText(nf.format(response.body().getTotalAmount()) + " 元");
//                        tvTipstext.setText(response.body().getPrompt());
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
            public void onFailure(Call<IncomeBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                demoSwiperefreshlayout.setRefreshing(false);
                ToastUtil.showMessage("超时");
            }

        });
    }
}
