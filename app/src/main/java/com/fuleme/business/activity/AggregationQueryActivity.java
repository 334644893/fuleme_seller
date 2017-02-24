package com.fuleme.business.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 汇总查询
 */
public class AggregationQueryActivity extends BaseActivity {

    @Bind(R.id.tv_start_date)
    TextView tvStartDate;
    @Bind(R.id.tv_end_date)
    TextView tvEndDate;
    int startYear, startMonth, startDay;
    int endYear, endMonth, endDay;
    final int TOSTORE = 998;
    final int FROMSTORE = 888;
    final int DATE_DIALOG_START = 0;
    final int DATE_DIALOG_END = 1;
    final int DATE_DIALOG_DEFAULT = -1;
    int stateTime = -1;//0:今日 1：昨日 2：本周
    final int STATE_TIME_TODAY = 0;
    final int STATE_TIME_YESTERDAY = 1;
    final int STATE_TIME_WEEK = 2;
    Calendar ca = Calendar.getInstance();
    @Bind(R.id.tv_ag_today)
    TextView tvAgToday;
    @Bind(R.id.tv_ag_yesterday)
    TextView tvAgYesterday;
    @Bind(R.id.tv_ag_week)
    TextView tvAgWeek;
    @Bind(R.id.tv_agg_all)
    TextView tvAggAll;
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
        initView();
    }

    private void initView() {
        tvTitle.setText("汇总查询");
        //set date
        setStateTime(STATE_TIME_TODAY);

        startYear = ca.get(Calendar.YEAR);
        startMonth = ca.get(Calendar.MONTH);
        startDay = ca.get(Calendar.DAY_OF_MONTH);
        endYear = ca.get(Calendar.YEAR);
        endMonth = ca.get(Calendar.MONTH);
        endDay = ca.get(Calendar.DAY_OF_MONTH);
        startDisplay(DATE_DIALOG_DEFAULT);
    }

    @OnClick({R.id.ll_start_date, R.id.ll_end_date, R.id.tv_date_query, R.id.tv_ag_today, R.id.tv_ag_yesterday, R.id.tv_ag_week, R.id.ll_query_store})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_start_date:
                showDialog(DATE_DIALOG_START);
                break;
            case R.id.ll_end_date:
                showDialog(DATE_DIALOG_END);
                break;
            case R.id.tv_date_query:
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
            case R.id.ll_query_store:
                //查询店铺
                Intent intent = new Intent(AggregationQueryActivity.this, StoreAggregationQueryActivity.class);
                startActivityForResult(intent, TOSTORE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOSTORE && resultCode == FROMSTORE && data != null) {
            tvAggAll.setText(data.getExtras().getString("FromStore"));

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_START:
                return new DatePickerDialog(this, startDateListener, startYear, startMonth, startDay);
            case DATE_DIALOG_END:
                return new DatePickerDialog(this, endDateListener, endYear, endMonth, endDay);
        }
        return null;
    }

    public void startDisplay(int i) {
        if (i == DATE_DIALOG_START) {
            tvStartDate.setText(new StringBuffer().append(startYear).append(" — ").append(startMonth + 1).append(" — ").append(startDay));
        } else if (i == DATE_DIALOG_END) {
            tvEndDate.setText(new StringBuffer().append(endYear).append(" — ").append(endMonth + 1).append(" — ").append(endDay));
        } else {
            tvStartDate.setText(new StringBuffer().append(startYear).append(" — ").append(startMonth + 1).append(" — ").append(startDay));
            tvEndDate.setText(new StringBuffer().append(endYear).append(" — ").append(endMonth + 1).append(" — ").append(endDay));
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
                tvAgToday.setTextColor(getResources().getColor(R.color.color_line));
                tvAgYesterday.setTextColor(getResources().getColor(R.color.black_54));
                tvAgWeek.setTextColor(getResources().getColor(R.color.black_54));
                tvAgToday.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_red_line));
                tvAgYesterday.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_bai_line));
                tvAgWeek.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_bai_line));
                break;
            case STATE_TIME_YESTERDAY:
                tvAgToday.setTextColor(getResources().getColor(R.color.black_54));
                tvAgYesterday.setTextColor(getResources().getColor(R.color.color_line));
                tvAgWeek.setTextColor(getResources().getColor(R.color.black_54));
                tvAgToday.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_bai_line));
                tvAgYesterday.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_red_line));
                tvAgWeek.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_bai_line));
                break;
            case STATE_TIME_WEEK:
                tvAgToday.setTextColor(getResources().getColor(R.color.black_54));
                tvAgYesterday.setTextColor(getResources().getColor(R.color.black_54));
                tvAgWeek.setTextColor(getResources().getColor(R.color.color_line));
                tvAgToday.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_bai_line));
                tvAgYesterday.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_bai_line));
                tvAgWeek.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corner_red_line));
                break;
        }
    }

}
