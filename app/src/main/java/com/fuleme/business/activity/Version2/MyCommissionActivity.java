package com.fuleme.business.activity.Version2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.CommissionAdapter;
import com.fuleme.business.bean.MyCommissionBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DateUtil;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCommissionActivity extends BaseActivity {
    public static int startTime = 0;//查询开始时间戳
    public static int endTime = 0;//查询结束时间戳
    private static final String TAG = "OrderDetailsActivity";
    NumberFormat nf = NumberFormat.getInstance();
    public static String short_name = "";
    private int page = 1;
    private int list_rows = 10;
    public boolean state = true;//是否刷新
    public static boolean textState = true;//true 显示正在加载，false显示 没有更多
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.tv_3)
    TextView tv3;
    @Bind(R.id.rv_m_recyclerview)
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager;
    CommissionAdapter mAdapter;
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    private List<MyCommissionBean.DataBean.ListBean> mDatas = new ArrayList<>();
    int Year, Month, Day;
    Calendar ca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_commission);
        ButterKnife.bind(this);
        nf.setGroupingUsed(false);
        ca = Calendar.getInstance();
        init();
    }

    public void init() {
        initView();
        initData();
        initTime();
    }

    protected void initData() {
        rebatelist();

    }
    public void initTime() {
        Year = ca.get(Calendar.YEAR);
        Month = ca.get(Calendar.MONTH);
        Day = ca.get(Calendar.DAY_OF_MONTH);
        startTime = DateUtil.getTimesmorning();//开始查询时间设为今日0点的时间戳
        endTime = DateUtil.getTimesnight();//结束查询时间设为今日23点59分59秒的时间戳
        tv1.setText(DateUtil.stampToDate(startTime + "", DateUtil.DATE_2));//初始化页面日期
    }
    public void initView() {
        tvTitle.setText("我的返佣");
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(MyCommissionActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CommissionAdapter(MyCommissionActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MyCommissionActivity.this, LinearLayoutManager.VERTICAL));
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
                    rebatelist();
                }
            }
        });

    }

    /**
     * 推广-我的返佣接口
     */

    private void rebatelist() {
        showLoading("获取中...");
        Call<MyCommissionBean> call = null;
        call = getApi().rebatelist(
                App.token,
                page,
                list_rows,
                startTime,
                endTime,
                App.PLACEHOLDER
        );

        call.enqueue(new Callback<MyCommissionBean>() {
            @Override
            public void onResponse(Call<MyCommissionBean> call, final Response<MyCommissionBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        LogUtil.i("page=" + page);
                        if (page == 1) {
                            mDatas.clear();
                        } else {
                            state = true;
                            textState = true;
                        }
                        if (response.body().getData() == null || response.body().getData().getList().size() < list_rows) {
                            state = false;
                            textState = false;
                        }
                        tv2.setText(response.body().getData().getInfo().getNum()+"");

                        tv3.setText("¥ " + nf.format(response.body().getData().getInfo().getMoney()));
                        mDatas.addAll(response.body().getData().getList());
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
            public void onFailure(Call<MyCommissionBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                demoSwiperefreshlayout.setRefreshing(false);
                ToastUtil.showMessage("超时");
            }

        });
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new DatePickerDialog(this, DateListener, Year, Month, Day);

        }
        return null;

    }
    /**
     * 重置数据
     */
    private void refresh() {
        page = 1;
        state = true;
        rebatelist();
    }
    private DatePickerDialog.OnDateSetListener DateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Year = year;
            Month = monthOfYear;
            Day = dayOfMonth;
            tv1.setText(
                    new StringBuffer().append(Year)
                            .append("-").append(add0(Month + 1))
                            .append("-").append(add0(Day)));
            try {
                startTime = new Integer(DateUtil.dateToStamp(tv1.getText().toString(), DateUtil.DATE_2));
                endTime = new Integer(DateUtil.dateToStamp(tv1.getText().toString(), DateUtil.DATE_2)) + 24 * 60 * 60 - 1;
                rebatelist() ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };
    public String add0(int i) {

        if (i < 10) {
            return "0" + i;
        } else {
            return i + "";
        }
    }
    @OnClick({R.id.tv_left, R.id.tv_1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_1:
                showDialog(0);
                break;
        }
    }
}
