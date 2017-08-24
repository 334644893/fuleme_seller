package com.fuleme.business.activity.Version2;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.MerchantListAdapter;
import com.fuleme.business.bean.MerchantListBean;
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
 * 商户名单
 */
public class MerchantListActivity extends BaseActivity {
    private static final String TAG = "MerchantListActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_m_recyclerview)
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager;
    MerchantListAdapter mAdapter;
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    @Bind(R.id.ll_meiyou)
    LinearLayout llMeiyou;
    private List<MerchantListBean.DataBean> mDatas = new ArrayList<>();
    private int page = 1;
    private int list_rows = 10;
    public boolean state = true;//是否刷新
    public static String team_id = "";//下级代理ID
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_list);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        initView();
        initData();
    }

    protected void initData() {
        promotionShop();
    }

    public void initView() {
        tvTitle.setText("商户名单");
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(MerchantListActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MerchantListAdapter(MerchantListActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
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
                    promotionShop();
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
        promotionShop();
    }

    @OnClick(R.id.tv_left)
    public void onClick() {
        finish();
    }
    /**
     * 我的推广团队
     */
    private void promotionShop() {
        showLoading("获取中...");
        Call<MerchantListBean>
                call = getApi().promotionShop(
                App.token,
                team_id,
                page,
                list_rows
        );
        call.enqueue(new Callback<MerchantListBean>() {
            @Override
            public void onResponse(Call<MerchantListBean> call, final Response<MerchantListBean> response) {
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
            public void onFailure(Call<MerchantListBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                demoSwiperefreshlayout.setRefreshing(false);
                ToastUtil.showMessage("超时");
            }

        });
    }
}
