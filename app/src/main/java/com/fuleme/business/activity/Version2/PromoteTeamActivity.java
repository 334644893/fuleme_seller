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
import com.fuleme.business.adapter.PromoteAdapter;
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
 * 我的推广团队
 */
public class PromoteTeamActivity extends BaseActivity {
    private static final String TAG = "PromoteTeamActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_m_recyclerview)
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager;
    PromoteAdapter mAdapter;
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    @Bind(R.id.ll_meiyou)
    LinearLayout llMeiyou;
    private List<PromoteBean.DataBean> mDatas = new ArrayList<>();
    private int page = 1;
    private int list_rows = 10;
    public boolean state = true;//是否刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_team);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        initView();
        initData();
    }

    protected void initData() {
        promotionTeam();

    }

    public void initView() {
        tvTitle.setText("推广团队");
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(PromoteTeamActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PromoteAdapter(PromoteTeamActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new PromoteAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, PromoteBean.DataBean bean) {
                MerchantListActivity.team_id = bean.getId();
                startActivity(new Intent(PromoteTeamActivity.this, MerchantListActivity.class));
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
                    promotionTeam();
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
        promotionTeam();
    }

    @OnClick(R.id.tv_left)
    public void onClick() {
        finish();
    }

    /**
     * 我的推广团队
     */

    private void promotionTeam() {
        showLoading("获取中...");
        Call<PromoteBean>
                call = getApi().promotionTeam(
                App.token,
                page,
                list_rows
        );
        LogUtil.d("App.token-----------", App.token + "");
        LogUtil.d("list_rows-----------", list_rows + "");
        call.enqueue(new Callback<PromoteBean>() {
            @Override
            public void onResponse(Call<PromoteBean> call, final Response<PromoteBean> response) {
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
            public void onFailure(Call<PromoteBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                demoSwiperefreshlayout.setRefreshing(false);
                ToastUtil.showMessage("超时");
            }

        });
    }

}
