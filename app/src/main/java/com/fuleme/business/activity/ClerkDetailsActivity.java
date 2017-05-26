package com.fuleme.business.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.bean.ClerkOederBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DateUtil;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 店员管理-汇总查询
 */
public class ClerkDetailsActivity extends BaseActivity {
    private static final String TAG = "AggregationQueryActivit";
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.tv_wx_1)
    TextView tvWx1;
    @Bind(R.id.tv_wx_2)
    TextView tvWx2;
    @Bind(R.id.tv_zfb_1)
    TextView tvZfb1;
    @Bind(R.id.tv_zfb_2)
    TextView tvZfb2;
    private Context context = ClerkDetailsActivity.this;
    @Bind(R.id.tv_start_date)
    TextView tvStartDate;
    @Bind(R.id.tv_end_date)
    TextView tvEndDate;
    int startYear, startMonth, startDay;
    int endYear, endMonth, endDay;
    final int TOSTORE = 998;
    final int DATE_DIALOG_START = 0;
    final int DATE_DIALOG_END = 1;
    final int DATE_DIALOG_DEFAULT = -1;
    int stateTime = -1;//0:今日 1：昨日 2：本周
    final int STATE_TIME_TODAY = 0;
    final int STATE_TIME_YESTERDAY = 1;
    final int STATE_TIME_WEEK = 2;
    public static String storeID = App.PLACEHOLDER;//查询店铺ID·初始为占位符，表示全部店铺
    public static String clerkId = App.PLACEHOLDER;//店员ID
    public static String startTime = "";//查询开始时间戳
    public static String endTime = "";//查询结束时间戳
    NumberFormat nf = NumberFormat.getInstance();

    @Bind(R.id.tv_ag_today)
    TextView tvAgToday;
    @Bind(R.id.tv_ag_yesterday)
    TextView tvAgYesterday;
    @Bind(R.id.tv_ag_week)
    TextView tvAgWeek;
    @Bind(R.id.ll_start_date)
    LinearLayout llStartDate;
    @Bind(R.id.ll_end_date)
    LinearLayout llEndDate;
    @Bind(R.id.tv_date_query)
    TextView tvDateQuery;
    @Bind(R.id.activity_aggregation_query)
    LinearLayout activityAggregationQuery;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggregation_query);
        ButterKnife.bind(this);
        nf.setGroupingUsed(false);
        initView();

    }

    private void initView() {

        tvTitle.setText("店员管理");
        //set date
        setStateTime(STATE_TIME_TODAY);
        Calendar ca = Calendar.getInstance();
        startYear = ca.get(Calendar.YEAR);
        startMonth = ca.get(Calendar.MONTH);
        startDay = ca.get(Calendar.DAY_OF_MONTH);

        endYear = ca.get(Calendar.YEAR);
        endMonth = ca.get(Calendar.MONTH);
        endDay = ca.get(Calendar.DAY_OF_MONTH);
        //初始为今日
        startDisplay(DATE_DIALOG_DEFAULT);
    }


    @OnClick({R.id.tv_left, R.id.ll_start_date, R.id.ll_end_date, R.id.tv_date_query, R.id.tv_ag_today, R.id.tv_ag_yesterday, R.id.tv_ag_week})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_start_date:
                showDialog(DATE_DIALOG_START);
                break;
            case R.id.ll_end_date:
                showDialog(DATE_DIALOG_END);
                break;
            case R.id.tv_date_query:
                clerk_oeder();
                break;
            case R.id.tv_ag_today:
                setStateTime(STATE_TIME_TODAY);
                break;
            case R.id.tv_ag_yesterday:
                setStateTime(STATE_TIME_YESTERDAY);
                break;
            case R.id.tv_ag_week:
                setStateTime(STATE_TIME_WEEK);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOSTORE) {
            LogUtil.i("选择店铺ID：" + storeID);
            startDisplay(DATE_DIALOG_DEFAULT);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_START:
                return new DatePickerDialog(this, startDateListener, startYear, startMonth
                        , startDay
                );
            case DATE_DIALOG_END:
                return new DatePickerDialog(this, endDateListener, endYear, endMonth, endDay);

        }
        return null;
    }


    public String add0(int i) {

        if (i < 10) {
            return "0" + i;
        } else {
            return i + "";
        }
    }

    public void startDisplay(int i) {
        if (i == DATE_DIALOG_START) {
            tvStartDate.setText(
                    new StringBuffer().append(startYear)
                            .append("-").append(add0(startMonth + 1))
                            .append("-").append(add0(startDay)));
            try {
                startTime = DateUtil.dateToStamp(tvStartDate.getText().toString(), DateUtil.DATE_2);
                String a = DateUtil.stampToDate(startTime, DateUtil.DATE_1);
                LogUtil.i("a", a);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (i == DATE_DIALOG_END) {
            tvEndDate.setText(new StringBuffer().append(endYear).append("-").append(add0(endMonth + 1)).append("-").append(add0(endDay)));
            try {
                endTime = new Long(DateUtil.dateToStamp(tvEndDate.getText().toString(), DateUtil.DATE_2)) + 24 * 60 * 60 - 1 + "";
                String a = DateUtil.stampToDate(endTime, DateUtil.DATE_1);
                LogUtil.i("a", a);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            tvStartDate.setText(new StringBuffer().append(startYear).append("-").append(add0(startMonth + 1)).append("-").append(add0(startDay)));
            tvEndDate.setText(new StringBuffer().append(endYear).append("-").append(add0(endMonth + 1)).append("-").append(add0(endDay)));
        }

    }

    private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            startYear = year;
            startMonth = monthOfYear;
            startDay = dayOfMonth;
            startDisplay(DATE_DIALOG_START);
        }
    };
    private DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            endYear = year;
            endMonth = monthOfYear;
            endDay = dayOfMonth;
            startDisplay(DATE_DIALOG_END);
        }
    };

    /**
     * 改变快速查询
     *
     * @param state 0:今日 1：昨日 2：本周
     */
    private void setStateTime(int state) {
        stateTime = state;
        switch (state) {
            case STATE_TIME_TODAY:
                //改变UI(下同)
                tvAgToday.setTextColor(getResources().getColor(R.color.color_line));
                tvAgYesterday.setTextColor(getResources().getColor(R.color.black_54));
                tvAgWeek.setTextColor(getResources().getColor(R.color.black_54));
                tvAgToday.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_red_line));
                tvAgYesterday.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_bai_line));
                tvAgWeek.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_bai_line));
                //设置查询时间(下同)
                startTime = DateUtil.getTimesmorning() + "";
                endTime = DateUtil.getTimesnight() + "";
                break;
            case STATE_TIME_YESTERDAY:
                tvAgToday.setTextColor(getResources().getColor(R.color.black_54));
                tvAgYesterday.setTextColor(getResources().getColor(R.color.color_line));
                tvAgWeek.setTextColor(getResources().getColor(R.color.black_54));
                tvAgToday.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_bai_line));
                tvAgYesterday.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_red_line));
                tvAgWeek.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_bai_line));
                startTime = DateUtil.getYesterdayTimesmorning() + "";
                endTime = DateUtil.getYesterdayTimesnight() + "";
                break;
            case STATE_TIME_WEEK:
                tvAgToday.setTextColor(getResources().getColor(R.color.black_54));
                tvAgYesterday.setTextColor(getResources().getColor(R.color.black_54));
                tvAgWeek.setTextColor(getResources().getColor(R.color.color_line));
                tvAgToday.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_bai_line));
                tvAgYesterday.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_bai_line));
                tvAgWeek.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_red_line));
                startTime = DateUtil.getWeekStartTime() + "";
                endTime = DateUtil.getWeekEndTime() + "";
                break;
        }
        tvStartDate.setText(DateUtil.stampToDate(startTime, DateUtil.DATE_2));
        tvEndDate.setText(DateUtil.stampToDate(endTime, DateUtil.DATE_2));
        clerk_oeder();
        LogUtil.i(storeID + DateUtil.stampToDate(startTime, DateUtil.DATE_1)
                + "至"
                + DateUtil.stampToDate(endTime, DateUtil.DATE_1));
    }

    /**
     * 获取店员的收款信息
     */
    private void clerk_oeder() {
        showLoading("获取中...");
        Call<ClerkOederBean> call =
                getApi().clerk_oeder(
                        clerkId,
                        storeID,
                        startTime,
                        endTime);

        call.enqueue(new Callback<ClerkOederBean>() {
            @Override
            public void onResponse(Call<ClerkOederBean> call, Response<ClerkOederBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        // 初始化数据
//                        nf.format(response.body().getTotal()) + "元"
                        int weixin = 0;
                        int zhifubao = 0;
                        double weixinTotal = 0;
                        double zhifubaoTotal = 0;
                        for (ClerkOederBean.DataBean bean : response.body().getData()) {
                            if (App.weixin.equals(bean.getTrade_type().split("\\.")[1])) {
                                weixin++;
                                weixinTotal += bean.getTotal_fee();
                            } else if (App.alipay.equals(bean.getTrade_type().split("\\.")[1])) {
                                zhifubao++;
                                zhifubaoTotal += bean.getTotal_fee();
                            }
                        }
                        tv1.setText("共" + response.body().getData().size() + "笔");
                        tv2.setText("订单总额:￥" + nf.format(response.body().getTotal()));
                        tvWx1.setText(nf.format(weixinTotal));
                        tvWx2.setText(weixin + "");
                        tvZfb1.setText(nf.format(zhifubaoTotal));
                        tvZfb2.setText(zhifubao + "");
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }

                } else {

                    LogUtil.i("失败response.message():" + response.message());

                }
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<ClerkOederBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }

}
