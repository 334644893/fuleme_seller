package com.fuleme.business.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DateUtil;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;

import java.text.ParseException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 添加优惠券
 */
public class AddCoupinsActivity extends BaseActivity {
    private static final String TAG = "AddCoupinsActivity";
    @Bind(R.id.tv_name)
    EditText tvName;
    @Bind(R.id.tv_tiaojian)
    EditText tvTiaojian;
    @Bind(R.id.tv_youhui)
    TextView tvYouhui;
    @Bind(R.id.et_youhui)
    EditText etYouhui;
    @Bind(R.id.tv_number)
    EditText tvNumber;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.tv_end_time)
    TextView tvEndTime;
    @Bind(R.id.tv_zhekou)
    TextView tvZhekou;
    @Bind(R.id.tv_manjian)
    TextView tvManjian;
    private int couFlag = 1;//true折扣券 false满减券
    Drawable drawable_41, drawable_40;
    int Year, Month, Day;
    private static int startTime = 0;//开始时间戳
    private static int endTime = 0;//结束时间戳

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupins);
        ButterKnife.bind(this);
        initView();
        select(1);
    }

    private void initView() {
        //初始化标记
        drawable_41 = getResources().getDrawable(R.mipmap.icon41);
        drawable_40 = getResources().getDrawable(R.mipmap.icon40);
        drawable_41.setBounds(0, 0, drawable_41.getMinimumWidth(), drawable_41.getMinimumHeight());
        drawable_40.setBounds(0, 0, drawable_40.getMinimumWidth(), drawable_40.getMinimumHeight());
        //初始化时间
        Calendar ca = Calendar.getInstance();
        Year = ca.get(Calendar.YEAR);
        Month = ca.get(Calendar.MONTH);
        Day = ca.get(Calendar.DAY_OF_MONTH);
        startTime = DateUtil.getTimesmorning();//开始时间设为今日0点的时间戳
        endTime = DateUtil.getTimesnight();//结束时间设为今日23点59分59秒的时间戳
        tvStartTime.setText(DateUtil.stampToDate(startTime + "", DateUtil.DATE_2));//初始化页面日期
        tvEndTime.setText(DateUtil.stampToDate(startTime + "", DateUtil.DATE_2));//初始化页面日期
    }

    /**
     * 优惠券
     */
    private void addcoupon() {
        showLoading("获取中...");
        Call<Object> call = getApi().addcoupon(
                App.token,
                couFlag + "",
                tvName.getText().toString(),
                App.short_id,
                tvTiaojian.getText().toString(),
                etYouhui.getText().toString(),
                tvNumber.getText().toString(),
                startTime,
                endTime);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    // do SomeThing
                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }
                closeLoading();//取消等待框
                finish();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }

    private void select(int i) {
        couFlag = i;
        switch (i) {
            case 1:
                couFlag = 1;
                tvZhekou.setCompoundDrawables(drawable_40, null, null, null);
                tvManjian.setCompoundDrawables(drawable_41, null, null, null);
                tvYouhui.setText("折扣率:");
                break;
            case 2:
                couFlag = 2;
                tvZhekou.setCompoundDrawables(drawable_41, null, null, null);
                tvManjian.setCompoundDrawables(drawable_40, null, null, null);
                tvYouhui.setText("满减额:");
                break;
        }
    }

    @OnClick({R.id.tv_zhekou, R.id.tv_manjian, R.id.tv_start_time, R.id.tv_end_time, R.id.btn_enter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_zhekou:
                select(1);
                break;
            case R.id.tv_manjian:
                select(2);
                break;
            case R.id.tv_start_time:
                showDialog(0);
                break;
            case R.id.tv_end_time:
                showDialog(1);
                break;
            case R.id.btn_enter:
                LogUtil.d("---------startTime", DateUtil.stampToDate(startTime + "", DateUtil.DATE_1));
                LogUtil.d("---------endTime", DateUtil.stampToDate(endTime + "", DateUtil.DATE_1));

                if (TextUtils.isEmpty(tvName.getText().toString())) {
                    ToastUtil.showMessage("请填写活动名称");
                } else if (TextUtils.isEmpty(tvTiaojian.getText().toString())) {
                    ToastUtil.showMessage("请填写使用条件");
                } else if (TextUtils.isEmpty(etYouhui.getText().toString())) {
                    ToastUtil.showMessage("请填写活动力度");
                } else if (couFlag==1&&new Double(etYouhui.getText().toString())>=10) {
                    ToastUtil.showMessage("请填写正确折扣");
                } else if (TextUtils.isEmpty(tvNumber.getText().toString())) {
                    ToastUtil.showMessage("请填写发放总数");
                }else {
                    addcoupon();
                }

                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new DatePickerDialog(this, startDateListener, Year, Month
                        , Day
                );
            case 1:
                return new DatePickerDialog(this, endDateListener, Year, Month, Day);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            startDisplay(0, year, monthOfYear, dayOfMonth);
        }
    };
    private DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            startDisplay(1, year, monthOfYear, dayOfMonth);
        }
    };

    public void startDisplay(int i, int Year, int Month, int Day) {
        if (i == 0) {
            tvStartTime.setText(
                    new StringBuffer().append(Year)
                            .append("-").append(add0(Month + 1))
                            .append("-").append(add0(Day)));
            try {
                startTime = new Integer(DateUtil.dateToStamp(tvStartTime.getText().toString(), DateUtil.DATE_2));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (i == 1) {
            tvEndTime.setText(new StringBuffer().append(Year).append("-").append(add0(Month + 1)).append("-").append(add0(Day)));
            try {
                endTime = new Integer(DateUtil.dateToStamp(tvEndTime.getText().toString(), DateUtil.DATE_2)) + 24 * 60 * 60 - 1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    public String add0(int i) {

        if (i < 10) {
            return "0" + i;
        } else {
            return i + "";
        }
    }
}
