package com.fuleme.business.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.OrderDetailsAdapter;
import com.fuleme.business.bean.OrderDetailsBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DateUtil;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.LoadingDialogUtils;

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

/**
 * 订单详情
 */
public class OrderDetailsActivity extends BaseActivity {
    public static int activity_type = 0;//判断从哪个页面跳转进入：0订单1报表
    public static int startTime = 0;//查询开始时间戳
    public static int endTime = 0;//查询结束时间戳
    public static String report_trade_type = "";//报表查询的支付类型筛选
    private static final String TAG = "OrderDetailsActivity";
    NumberFormat nf = NumberFormat.getInstance();
    public static String year = "";
    public static String month = "";
    public static String shopid = "";
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
    OrderDetailsAdapter mAdapter;
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    private List<OrderDetailsBean.DataBean> mDatas = new ArrayList<>();
    final Calendar calendar = Calendar.getInstance();
    int yy = calendar.get(Calendar.YEAR);
    int mm = calendar.get(Calendar.MONTH);
    int dd = calendar.get(Calendar.DAY_OF_MONTH);
    private String tv1name = "";
    private int endMonth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        nf.setGroupingUsed(false);
        init();
    }

    public void init() {
        initView();
        initData();
    }

    protected void initData() {
        orderInfo();

    }

    public void initView() {
        tvTitle.setText(short_name);
        year = yy + "";
        month = mm + 1 + "";
        if (Integer.valueOf(month) < 10) {
            tv1name = year + " - 0" + month + "  ";
        } else {
            tv1name = year + "-" + month + "  ";
        }
        tv1.setText(tv1name);
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(OrderDetailsActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new OrderDetailsAdapter(OrderDetailsActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(OrderDetailsActivity.this, LinearLayoutManager.VERTICAL));
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
                    orderInfo();
                }
            }
        });

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

    @Override
    protected Dialog onCreateDialog(int i) {

        DatePickerDialog dlg = new DatePickerDialog(new ContextThemeWrapper(OrderDetailsActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar), DateListener, yy, mm, dd) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                LinearLayout mSpinners = (LinearLayout) findViewById(getContext().getResources().getIdentifier("android:id/pickers", null, null));
                if (mSpinners != null) {
                    NumberPicker mMonthSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/month", null, null));
                    NumberPicker mYearSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/year", null, null));
                    mSpinners.removeAllViews();
                    if (mYearSpinner != null) {
                        mSpinners.addView(mYearSpinner);
                    }
                    if (mMonthSpinner != null) {
                        mSpinners.addView(mMonthSpinner);
                    }
                }
                View dayPickerView = findViewById(getContext().getResources().getIdentifier("android:id/day", null, null));
                if (dayPickerView != null) {
                    dayPickerView.setVisibility(View.GONE);
                }
            }

        };
        return dlg;
    }

    private DatePickerDialog.OnDateSetListener DateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            yy = year;
            mm = monthOfYear;
            OrderDetailsActivity.year = year + "";
            OrderDetailsActivity.month = monthOfYear + 1 + "";
            if (Integer.valueOf(OrderDetailsActivity.month) < 10) {
                tv1name = OrderDetailsActivity.year + " - 0" + OrderDetailsActivity.month + "  ";
            } else {
                tv1name = OrderDetailsActivity.year + " - " + OrderDetailsActivity.month + "  ";
            }
            tv1.setText(tv1name);
            //报表模块
            if (activity_type == 1) {
                try {
                    if (monthOfYear < 10) {
                        startTime = new Integer(DateUtil.dateToStamp(year + "-0" + OrderDetailsActivity.month, DateUtil.DATE_4));
                    } else {
                        startTime = new Integer(DateUtil.dateToStamp(year + "-" + OrderDetailsActivity.month, DateUtil.DATE_4));
                    }
                    endMonth = monthOfYear + 2;
                    if (endMonth < 10) {
                        endTime = new Integer(DateUtil.dateToStamp(year + "-0" + endMonth, DateUtil.DATE_4));
                    } else {
                        endTime = new Integer(DateUtil.dateToStamp(year + "-" + endMonth, DateUtil.DATE_4));
                    }
//                    ToastUtil.showMessage(endMonth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            refresh();
        }
    };

    /**
     * 获取店铺订单
     */

    private void orderInfo() {
        showLoading("获取中...");
        Call<OrderDetailsBean> call = null;
        if (activity_type == 0) {
            //账单跳转-
            call = getApi().orderInfo(
                    App.token,
                    year,
                    month,
                    shopid,
                    page,
                    list_rows);
        } else if (activity_type == 1) {
            //报表跳转-
            call = getApi().orderInfo(
                    App.token,
                    App.PLACEHOLDER,
                    App.PLACEHOLDER,
                    startTime,
                    endTime,
                    report_trade_type,
                    shopid,
                    page,
                    list_rows);
        }
        call.enqueue(new Callback<OrderDetailsBean>() {
            @Override
            public void onResponse(Call<OrderDetailsBean> call, final Response<OrderDetailsBean> response) {
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
                        if (response.body().getData() == null || response.body().getData().size() < list_rows) {
                            state = false;
                            textState = false;
                        }
                        tv2.setText("共" + response.body().getTotalOrder() + "笔");

                        tv3.setText("¥ " + nf.format(response.body().getTotalAmount()));
                        mDatas.addAll(response.body().getData());
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
            public void onFailure(Call<OrderDetailsBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                demoSwiperefreshlayout.setRefreshing(false);
                ToastUtil.showMessage("超时");
            }

        });
    }

    /**
     * 重置数据
     */
    private void refresh() {
        page = 1;
        state = true;
        orderInfo();
    }
}
