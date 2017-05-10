package com.fuleme.business.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.bean.AnalysecouponBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DateUtil;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.CustomDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponsDetailsActivity extends BaseActivity {
    private static final String TAG = "CouponsDetailsActivity";
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_tiaojian)
    TextView tvTiaojian;
    @Bind(R.id.et_youhui)
    TextView etYouhui;
    @Bind(R.id.tv_number)
    TextView tvNumber;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;

    @Bind(R.id.time_1)
    TextView time1;
    @Bind(R.id.tv_jine)
    TextView tvJine;
    @Bind(R.id.time_2)
    TextView time2;
    @Bind(R.id.tv_state)
    TextView tvState;
    @Bind(R.id.time_3)
    TextView time3;
    @Bind(R.id.btn_enter_1)
    Button btnEnter1;
    @Bind(R.id.btn_enter_2)
    Button btnEnter2;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    public static String id = "";
    @Bind(R.id.tv_textjine)
    TextView tvTextjine;
    private int state = -2;//0暂停中，1进行中
    private static final int DELETESTATE = -1;//删除
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_details);
        ButterKnife.bind(this);
        addcoupon();
    }



    /**
     * 优惠券
     */
    private void addcoupon() {
        showLoading("获取中...");
        Call<AnalysecouponBean> call = getApi().analysecoupon(
                App.token,
                App.short_id,
                id);
        call.enqueue(new Callback<AnalysecouponBean>() {
            @Override
            public void onResponse(Call<AnalysecouponBean> call, Response<AnalysecouponBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        time1.setText(DateUtil.stampToDate(response.body().getData().getAddtime(), DateUtil.DATE_1));
                        time2.setText(DateUtil.stampToDate(response.body().getData().getStart(), DateUtil.DATE_1));
                        time3.setText(DateUtil.stampToDate(response.body().getData().getEnd(), DateUtil.DATE_1));

                        if ("2".equals(response.body().getData().getType())) {
                            tvTextjine.setText("金额:");
                            tvTitle.setText("满减券");
                            tvJine.setText(response.body().getData().getReduce() + "元");
                            etYouhui.setText(response.body().getData().getTerm() + "元立减" + response.body().getData().getReduce() + "元");

                        } else if ("1".equals(response.body().getData().getType())) {
                            tvTextjine.setText("折扣:");
                            tvTitle.setText("折扣券");
                            tvJine.setText(response.body().getData().getReduce() + "折");
                            etYouhui.setText(response.body().getData().getTerm() + "元" + response.body().getData().getReduce() + "折优惠");
                        }
                        if (response.body().getData().getState() == 0) {
                            tvState.setText("暂停");
                        } else if (response.body().getData().getState() == 1) {
                            tvState.setText("进行中");
                        }
                        tvName.setText(response.body().getData().getName());
                        tvTiaojian.setText(response.body().getData().getTotal() + "张");
                        tvNumber.setText(response.body().getData().getTerm() + "元可以使用");
                        tvStartTime.setText("本券仅限店内扫码支付时使用");//初始化页面日期


                        state = response.body().getData().getState();
                        select(state);
                    }
                    // do SomeThing
                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));


                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<AnalysecouponBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }

    /**
     * 打开暂停优惠券
     */
    private void opencoupon(final int type) {
        showLoading("请稍等...");
        Call<Object> call = getApi().opencoupon(
                App.token,
                App.short_id,
                id, type);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        select(type);
                        state = type;
                        if (state == 0) {
                            tvState.setText("暂停");
                        } else if (state == 1) {
                            tvState.setText("进行中");
                        }
                    }
                    // do SomeThing
                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    if (type == DELETESTATE) {
                        finish();
                    }

                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }


    @OnClick({R.id.btn_enter_1, R.id.btn_enter_2, R.id.tv_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_enter_1:
                if (state == 0) {
                    opencoupon(1);
                } else if (state == 1) {
                    opencoupon(0);
                }
                break;
            case R.id.btn_enter_2:
                CustomDialog.Builder customBuilder = new
                        CustomDialog.Builder(CouponsDetailsActivity.this);
                customBuilder
                        .setTitle("结束发放")
                        .setMessage("确定结束发放该活动吗?")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                opencoupon(DELETESTATE);
                            }
                        })
                        .setPositiveButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }
                                });
                dialog = customBuilder.create();
                dialog.show();

                break;
            case R.id.tv_left:
                finish();
                break;
        }
    }

    private void select(int i) {
        switch (i) {
            case 0:
                btnEnter1.setText("启动发布");
                btnEnter1.setBackgroundResource(R.drawable.shape_corner_blue_p);
                break;
            case 1:
                btnEnter1.setText("暂停发布");
                btnEnter1.setBackgroundResource(R.drawable.shape_corner_red_p);
                break;
        }
    }
}
