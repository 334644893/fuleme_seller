package com.fuleme.business.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.AFragmentAdapter;
import com.fuleme.business.adapter.ReportRAdapter;
import com.fuleme.business.bean.AFragmentImageBean;
import com.fuleme.business.bean.IncomeBean;
import com.fuleme.business.bean.ReportRBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DateUtil;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;

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

public class ReportActivity extends BaseActivity {
    private static final String TAG = "ReportActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.v_1)
    View v1;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.v_2)
    View v2;
    @Bind(R.id.tv_4)
    TextView tv4;
    @Bind(R.id.tv_6)
    TextView tv6;
    @Bind(R.id.tv_7)
    TextView tv7;
    @Bind(R.id.tv_8)
    TextView tv8;
    @Bind(R.id.rv_af)
    RecyclerView mRecyclerView;
    private int[] mImageDatas = {R.mipmap.icon_weixin, R.mipmap.icon_zhifubao, R.mipmap.icon_baidu, R.mipmap.icon_jindong, R.mipmap.icon_qq};
    private String[] mTitleDatas = {"微信支付", "支付宝", "百度钱包", "京东钱包", "QQ钱包"};
    ReportRAdapter mAdapter;
    GridLayoutManager mGridLayoutManager;
    private List<ReportRBean> mDatas = new ArrayList<>();
    private static int startTime = 0;//查询开始时间戳
    private static int endTime = 0;//查询结束时间戳
    private static final int DAY = 0;
    private static final int MOTH = 1;
    int Year, Month, Day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        tvTitle.setText("报表");
        initTime();
        initRecyclerView();

    }

    public void initTime() {

        Calendar ca = Calendar.getInstance();
        Year = ca.get(Calendar.YEAR);
        Month = ca.get(Calendar.MONTH);
        Day = ca.get(Calendar.DAY_OF_MONTH);
        startTime = DateUtil.getTimesmorning();//开始查询时间设为今日0点的时间戳
        endTime = DateUtil.getTimesnight();//结束查询时间设为今日23点59分59秒的时间戳
        tv4.setText(DateUtil.stampToDate(startTime + "", DateUtil.DATE_2));//初始化页面日期
    }

    public void initRecyclerView() {
        mGridLayoutManager = new GridLayoutManager(ReportActivity.this, 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new ReportRAdapter(ReportActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(ReportActivity.this, GridLayoutManager.HORIZONTAL));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(ReportActivity.this, GridLayoutManager.VERTICAL));
        IncomeBean bean = null;
        initData(bean);
        income();
    }

    public void initData(IncomeBean bean) {
        mDatas.clear();
        for (int i = 0; i < mImageDatas.length; i++) {
            ReportRBean reportRBean = new ReportRBean();
            reportRBean.setmItemImage(mImageDatas[i]);
            reportRBean.setTitle(mTitleDatas[i]);
            switch (mImageDatas[i]) {
                case R.mipmap.icon_weixin:
                    if (bean != null) {
                        reportRBean.setTotal_fee(bean.getData().getWeixin().getTotal_fee());
                        reportRBean.setNumber(bean.getData().getWeixin().getNumber() + "");
                    } else {
                        reportRBean.setNumber("0");
                        reportRBean.setTotal_fee(0);
                    }
                    break;
                case R.mipmap.icon_zhifubao:
                    if (bean != null) {
                        reportRBean.setTotal_fee(bean.getData().getAlipay().getTotal_fee());
                        reportRBean.setNumber(bean.getData().getAlipay().getNumber() + "");
                    } else {
                        reportRBean.setNumber("0");
                        reportRBean.setTotal_fee(0);
                    }
                    break;
                case R.mipmap.icon_baidu:
                    reportRBean.setNumber("0");
                    reportRBean.setTotal_fee(0);
                    break;
                case R.mipmap.icon_jindong:
                    reportRBean.setNumber("0");
                    reportRBean.setTotal_fee(0);
                    break;
                case R.mipmap.icon_qq:
                    reportRBean.setNumber("0");
                    reportRBean.setTotal_fee(0);
                    break;
            }
            mDatas.add(reportRBean);
        }
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.tv_left, R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_1:
                setState(DAY);
                break;
            case R.id.tv_2:
                setState(MOTH);
                break;
            case R.id.tv_3:
                //前一天
                startTime -= 24 * 60 * 60;
                endTime -= 24 * 60 * 60;
                tv4.setText(DateUtil.stampToDate(startTime + "", DateUtil.DATE_2));
                income();
                break;
            case R.id.tv_4:
                showDialog(0);
                break;
            case R.id.tv_5:
                //后一天
                startTime += 24 * 60 * 60;
                endTime += 24 * 60 * 60;
                tv4.setText(DateUtil.stampToDate(startTime + "", DateUtil.DATE_2));
                income();
                break;
        }
    }

    private DatePickerDialog.OnDateSetListener DateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Year = year;
            Month = monthOfYear;
            Day = dayOfMonth;
            tv4.setText(
                    new StringBuffer().append(Year)
                            .append("-").append(add0(Month + 1))
                            .append("-").append(add0(Day)));
            try {
                startTime = new Integer(DateUtil.dateToStamp(tv4.getText().toString(), DateUtil.DATE_2));
                endTime = new Integer(DateUtil.dateToStamp(tv4.getText().toString(), DateUtil.DATE_2)) + 24 * 60 * 60 - 1;
                income();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new DatePickerDialog(this, DateListener, Year, Month, Day);

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

    /**
     * 改变日和月
     *
     * @param type
     */
    private void setState(int type) {
        switch (type) {
            case DAY:
                tv1.setTextColor(getResources().getColor(R.color.red));
                tv2.setTextColor(getResources().getColor(R.color.black_87));
                v1.setBackgroundColor(getResources().getColor(R.color.red));
                v2.setBackgroundColor(getResources().getColor(R.color.black_87));
                break;
            case MOTH:
                tv1.setTextColor(getResources().getColor(R.color.black_87));
                tv2.setTextColor(getResources().getColor(R.color.red));
                v1.setBackgroundColor(getResources().getColor(R.color.black_87));
                v2.setBackgroundColor(getResources().getColor(R.color.red));
                break;
        }
    }

    /**
     * 获取报表信息
     */

    private void income() {
        showLoading("获取中...");
        Call<IncomeBean> call = getApi().income(App.token, App.short_id, startTime, endTime);
        LogUtil.d("---------startTime", DateUtil.stampToDate(startTime + "", DateUtil.DATE_1));
        LogUtil.d("---------endTime", DateUtil.stampToDate(endTime + "", DateUtil.DATE_1));
        call.enqueue(new Callback<IncomeBean>() {
            @Override
            public void onResponse(Call<IncomeBean> call, final Response<IncomeBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        tv6.setText(response.body().getData().getTotal_fee() + "");
                        tv8.setText(response.body().getData().getTotal_fee() + "");
                        tv7.setText(response.body().getData().getNumber() + "");
                        /**
                         * 刷新RecyclerView
                         *
                         */
                        initData(response.body());
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }
                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }
                ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<IncomeBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }
}
