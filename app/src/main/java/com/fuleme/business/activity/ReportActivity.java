package com.fuleme.business.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.ReportRAdapter;
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
import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
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
    @Bind(R.id.ll_day)
    LinearLayout llDay;
    @Bind(R.id.tv_month_1)
    TextView tvMonth1;
    @Bind(R.id.tv_month_2)
    TextView tvMonth2;
    @Bind(R.id.tv_month_3)
    TextView tvMonth3;
    @Bind(R.id.ll_month)
    LinearLayout llMonth;
    @Bind(R.id.v_month_1)
    View vMonth1;
    @Bind(R.id.v_month_2)
    View vMonth2;
    @Bind(R.id.v_month_3)
    View vMonth3;
    @Bind(R.id.line_chart_amout)
    LineChartView lineChartAmout;
    @Bind(R.id.line_chart_number)
    LineChartView lineChartNumber;
    @Bind(R.id.slview)
    ScrollView slview;
    private int[] mImageDatas = {R.mipmap.icon_weixin, R.mipmap.icon_zhifubao, R.mipmap.icon_baidu, R.mipmap.icon_jindong, R.mipmap.icon_qq};
    private String[] mTitleDatas = {"微信支付", "支付宝", "百度钱包", "京东钱包", "QQ钱包"};
    ReportRAdapter mAdapter;
    GridLayoutManager mGridLayoutManager;
    private List<ReportRBean> mDatas = new ArrayList<>();
    private int startTime = 0;//查询开始时间戳
    private int endTime = 0;//查询结束时间戳
    private static final int DAY = 0;
    private static final int MOTH = 1;
    int Year, Month, Day;
    Calendar ca;
    //报表
    List<String> date = new ArrayList<>();
    List<Float> weather = new ArrayList<>();
    List<Integer> numberWeather = new ArrayList<>();
    //        String[] date = {"10-22", "11-22", "12-22", "1-22", "6-22", "5-23", "5-22", "6-22", "5-23", "5-22"};//X轴的标注
//    float[] weather = {0.05f, 1.2f, 0f, 1f, 1f, 1f, 1f, 1f, 1f, 11f};//金额图表
//    int[] numberWeather = {500, 120, 190, 101, 110, 114, 110, 118, 10, 110};//笔数图表
    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> mAxisXValues = new ArrayList<>();
    private List<PointValue> mNumberPointValues = new ArrayList<>();
    private List<AxisValue> mNumberAxisXValues = new ArrayList<>();
    private Float weatherMax = 0f;
//    private int textsize = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        tvTitle.setText("报表");
        ca = Calendar.getInstance();
        initRecyclerView();
        lineChartAmout();
    }

    public void initTime() {
        Year = ca.get(Calendar.YEAR);
        Month = ca.get(Calendar.MONTH);
        Day = ca.get(Calendar.DAY_OF_MONTH);

        tvMonth1.setText(DateUtil.stampToDate(DateUtil.getMonthStartTime(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH) - 1), DateUtil.DATE_4));
        tvMonth2.setText(DateUtil.stampToDate(DateUtil.getMonthStartTime(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH)), DateUtil.DATE_4));
        tvMonth3.setText(DateUtil.stampToDate(DateUtil.getMonthStartTime(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH) + 1), DateUtil.DATE_4));
        setState(DAY);
    }

    public void initReportData(List<IncomeBean.DataBean.EveryDayBean> beanList) {
        date.clear();
        weather.clear();
        numberWeather.clear();
        for (IncomeBean.DataBean.EveryDayBean bean : beanList) {
            date.add(bean.getTime_end());
            weather.add(bean.getTotal_fee());
            numberWeather.add(bean.getNumber());
            if (weatherMax < bean.getTotal_fee()) {
                weatherMax = bean.getTotal_fee();
            }
        }
        date.add(" ");
        weather.add(0f);
        numberWeather.add(0);
//        if (weatherMax < 1000) {
//            textsize = 12;
//        } else if (weatherMax < 10000) {
//            textsize = 10;
//        } else if (weatherMax < 100000) {
//            textsize = 8;
//        }
        lineChartAmout();
    }

    public void lineChartAmout() {
        //金额表
        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart(8);//初始化
        //笔数表
        getNumberAxisXLables();//获取x轴的标注
        getNumberAxisPoints();//获取坐标点
        initNumberLineChart(8);//初始化
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
        initTime();
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

    @OnClick({R.id.tv_left, R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5, R.id.tv_month_1, R.id.tv_month_2, R.id.tv_month_3})
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
            case R.id.tv_month_1:
                setMonthState(0);

                break;
            case R.id.tv_month_2:
                setMonthState(1);
                break;
            case R.id.tv_month_3:
                setMonthState(2);
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
                llDay.setVisibility(View.VISIBLE);
                llMonth.setVisibility(View.GONE);
                startTime = DateUtil.getTimesmorning();//开始查询时间设为今日0点的时间戳
                endTime = DateUtil.getTimesnight();//结束查询时间设为今日23点59分59秒的时间戳
                tv4.setText(DateUtil.stampToDate(startTime + "", DateUtil.DATE_2));//初始化页面日期
                income();
                break;
            case MOTH:
                tv1.setTextColor(getResources().getColor(R.color.black_87));
                tv2.setTextColor(getResources().getColor(R.color.red));
                v1.setBackgroundColor(getResources().getColor(R.color.black_87));
                v2.setBackgroundColor(getResources().getColor(R.color.red));
                llDay.setVisibility(View.GONE);
                llMonth.setVisibility(View.VISIBLE);
                setMonthState(2);
                break;
        }
    }

    /**
     * 改变月日期
     *
     * @param type
     */
    private void setMonthState(int type) {
        switch (type) {
            case 0:
                tvMonth1.setTextColor(getResources().getColor(R.color.red));
                tvMonth2.setTextColor(getResources().getColor(R.color.black_87));
                tvMonth3.setTextColor(getResources().getColor(R.color.black_87));
                vMonth1.setBackgroundColor(getResources().getColor(R.color.red));
                vMonth2.setBackgroundColor(getResources().getColor(R.color.evaluate_content_1));
                vMonth3.setBackgroundColor(getResources().getColor(R.color.evaluate_content_1));
                startTime = new Integer(DateUtil.getMonthStartTime(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH) - 1));
                endTime = new Integer(DateUtil.getMonthEndTime(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH) - 1));
                income();
                break;
            case 1:
                tvMonth2.setTextColor(getResources().getColor(R.color.red));
                tvMonth1.setTextColor(getResources().getColor(R.color.black_87));
                tvMonth3.setTextColor(getResources().getColor(R.color.black_87));
                vMonth2.setBackgroundColor(getResources().getColor(R.color.red));
                vMonth3.setBackgroundColor(getResources().getColor(R.color.evaluate_content_1));
                vMonth1.setBackgroundColor(getResources().getColor(R.color.evaluate_content_1));
                startTime = new Integer(DateUtil.getMonthStartTime(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH)));
                endTime = new Integer(DateUtil.getMonthEndTime(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH)));
                income();
                break;
            case 2:
                tvMonth3.setTextColor(getResources().getColor(R.color.red));
                tvMonth1.setTextColor(getResources().getColor(R.color.black_87));
                tvMonth2.setTextColor(getResources().getColor(R.color.black_87));
                vMonth3.setBackgroundColor(getResources().getColor(R.color.red));
                vMonth2.setBackgroundColor(getResources().getColor(R.color.evaluate_content_1));
                vMonth1.setBackgroundColor(getResources().getColor(R.color.evaluate_content_1));
                startTime = new Integer(DateUtil.getMonthStartTime(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH) + 1));
                endTime = new Integer(DateUtil.getMonthEndTime(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH) + 1));
                income();
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
                        /**
                         * 解析数据绘制图标
                         */
                        initReportData(response.body().getData().getEveryDay());
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

    /**
     * 设置X 轴的显示(金额)
     */
    private void getAxisXLables() {
        mAxisXValues.clear();
        for (int i = 0; i < date.size(); i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date.get(i)));
        }
    }

    /**
     * 图表的每个点的显示(金额)
     */
    private void getAxisPoints() {
        mPointValues.clear();
        for (int i = 0; i < weather.size(); i++) {
            mPointValues.add(new PointValue(i, weather.get(i)));
        }
    }

    /**
     * 设置X 轴的显示(笔数)
     */
    private void getNumberAxisXLables() {
        mNumberAxisXValues.clear();
        for (int i = 0; i < date.size(); i++) {
            mNumberAxisXValues.add(new AxisValue(i).setLabel(date.get(i)));
        }
    }

    /**
     * 图表的每个点的显示(笔数)
     */
    private void getNumberAxisPoints() {
        mNumberPointValues.clear();
        for (int i = 0; i < numberWeather.size(); i++) {
            mNumberPointValues.add(new PointValue(i, numberWeather.get(i)));
        }
    }

    private void initLineChart(int size) {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#00A0E8"));  //折线的颜色
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setStrokeWidth(1);//线条的粗细，默认是3
        line.setPointRadius(3);
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        LineChartValueFormatter chartValueFormatter = new SimpleLineChartValueFormatter(2);
        line.setFormatter(chartValueFormatter);//显示小数点
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("交易时间");  //表格名称
        axisX.setTextSize(size);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线
        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("  ");//y轴标注
        axisY.setTextSize(size);//设置字体大小
        axisY.setTextColor(Color.BLACK);
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边
        data.setValueLabelBackgroundAuto(false);//设置数据背景是否跟随节点颜色
        data.setValueLabelBackgroundColor(Color.TRANSPARENT);//设置数据背景颜色
        data.setValueLabelsTextColor(Color.BLACK);//设置数据文字颜色
        //设置行为属性，支持缩放、滑动以及平移
        lineChartAmout.setInteractive(true);
        lineChartAmout.setZoomType(ZoomType.HORIZONTAL);
        lineChartAmout.setMaxZoom((float) 10);//最大方法比例
        lineChartAmout.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartAmout.setLineChartData(data);
        lineChartAmout.setVisibility(View.VISIBLE);


        lineChartAmout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int n = event.getPointerCount();
                if (n == 1) {
                    //允许ScrollView截断点击事件，ScrollView可滑动
                    slview.requestDisallowInterceptTouchEvent(false);
                } else {
                    //不允许ScrollView截断点击事件，点击事件由子View处理
                    slview.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChartAmout.getMaximumViewport());
        v.left = 0;                             //坐标原点在左下
        v.bottom = 0;
        v.top = lineChartAmout.getMaximumViewport().height();                            //最高点为100
        v.right = 7;           //右边为点 坐标从0开始 点号从1 需要 -1
//        lineChartAmout.setMaximumViewport(v);   //给最大的视图设置 相当于原图
        lineChartAmout.setCurrentViewport(v);   //给当前的视图设置 相当于当前展示的图
    }

    private void initNumberLineChart(int size) {
        Line line = new Line(mNumberPointValues).setColor(Color.parseColor("#00A0E8"));  //折线的颜色
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setStrokeWidth(1);//线条的粗细，默认是3
        line.setPointRadius(3);
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("交易时间");  //表格名称
        axisX.setTextSize(size);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线
        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("交易笔数");//y轴标注
        axisY.setTextSize(size);//设置字体大小
        axisY.setTextColor(Color.BLACK);
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        data.setValueLabelBackgroundAuto(false);//设置数据背景是否跟随节点颜色
        data.setValueLabelBackgroundColor(Color.TRANSPARENT);//设置数据背景颜色
        data.setValueLabelsTextColor(Color.BLACK);//设置数据文字颜色
        //设置行为属性，支持缩放、滑动以及平移
        lineChartNumber.setInteractive(true);
        lineChartNumber.setZoomType(ZoomType.HORIZONTAL);
        lineChartNumber.setMaxZoom((float) 10);//最大方法比例
        lineChartNumber.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartNumber.setLineChartData(data);
        lineChartNumber.setVisibility(View.VISIBLE);

        lineChartNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int n = event.getPointerCount();
                if (n == 1) {
                    //允许ScrollView截断点击事件，ScrollView可滑动
                    slview.requestDisallowInterceptTouchEvent(false);
                } else {
                    //不允许ScrollView截断点击事件，点击事件由子View处理
                    slview.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChartNumber.getMaximumViewport());
        v.left = 0;                             //坐标原点在左下
        v.bottom = 0;
        v.top = lineChartNumber.getMaximumViewport().height();                            //最高点为100
        v.right = 7;           //右边为点 坐标从0开始 点号从1 需要 -1
        lineChartNumber.setCurrentViewport(v);   //给当前的视图设置 相当于当前展示的图
    }
}
