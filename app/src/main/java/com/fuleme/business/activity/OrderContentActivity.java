package com.fuleme.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.bean.OrderContentBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DateUtil;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;

import java.text.NumberFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderContentActivity extends BaseActivity {
    private static final String TAG = "OrderContentActivity";
    public static String out_trade_no = "";
    public static String title = "";
    public static String primeCost = "";
    private int toOrderRefundActivity = 101;
    public static boolean refund = false;//完成退款操作后刷新标识
    @Bind(R.id.btn_enter_1)
    Button btnEnter1;
    private String refundState = "";
    NumberFormat nf = NumberFormat.getInstance();
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.tv_3)
    TextView tv3;
    @Bind(R.id.tv_4)
    TextView tv4;
    @Bind(R.id.tv_5)
    TextView tv5;
    @Bind(R.id.tv_6)
    TextView tv6;
    @Bind(R.id.tv_7)
    TextView tv7;
    @Bind(R.id.tv_8)
    TextView tv8;
    @Bind(R.id.tv_9)
    TextView tv9;
    @Bind(R.id.tv_10)
    TextView tv10;
    @Bind(R.id.ll_tuikuan)
    LinearLayout llTuikuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_content);
        ButterKnife.bind(this);
        nf.setGroupingUsed(false);
        tvTitle.setText(title);
        OrderDetails();
    }

    @OnClick({R.id.tv_left, R.id.btn_enter_1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_enter_1:
                startActivityForResult(new Intent(OrderContentActivity.this, OrderRefundActivity.class), toOrderRefundActivity);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == toOrderRefundActivity && refund) {
            refund = false;
            OrderDetails();
        }
    }

    /**
     * 订单内容
     */
    private void OrderDetails() {
        showLoading("获取中...");
        Call<OrderContentBean> call = App.getInstance().getServerApi().OrderDetails(out_trade_no);
        call.enqueue(new Callback<OrderContentBean>() {
            @Override
            public void onResponse(Call<OrderContentBean> call, Response<OrderContentBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        tv1.setText(response.body().getData().getOut_trade_no());
                        tv2.setText("¥ " + nf.format(response.body().getData().getPrime_cost()));
                        primeCost = response.body().getData().getPrime_cost() + "";
                        tv3.setText("¥ " + nf.format(response.body().getData().getDiscount()));
                        tv4.setText(nf.format(response.body().getData().getTotal_fee()) + "");
                        tv5.setText(DateUtil.stampToDate(response.body().getData().getTime_end(), DateUtil.DATE_1));
                        if (App.weixin.equals(response.body().getData().getTrade_type().split("\\.")[1])) {
                            tv6.setText("微信支付");
                        } else if (App.alipay.equals(response.body().getData().getTrade_type().split("\\.")[1])) {
                            tv6.setText("支付宝支付");
                        }
                        if ("1".equals(response.body().getData().getState())) {
                            tv7.setText("支付成功");
                        } else {
                            tv7.setText("支付失败");
                        }
                        //昵称
                        tv8.setText(response.body().getData().getNick_name());
                        //退款
                        refundState = response.body().getData().getRefund_state();
                        if (!TextUtils.isEmpty(refundState)) {
                            llTuikuan.setVisibility(View.VISIBLE);
                            if ("0".equals(refundState)) {
                                btnEnter1.setVisibility(View.INVISIBLE);
                                tv9.setText("退款中");
                                tv9.setTextColor(getResources().getColor(R.color.theme));
                            } else if ("1".equals(refundState)) {
                                btnEnter1.setVisibility(View.INVISIBLE);
                                tv9.setText("退款成功");
                                tv9.setTextColor(getResources().getColor(R.color.green_1));
                            } else if ("2".equals(refundState)) {
                                tv9.setText("退款失败");
                                tv9.setTextColor(getResources().getColor(R.color.red));
                            }
                            //金额
                            tv10.setText("¥ " + response.body().getData().getRefund_fee());
                        }

                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }
                } else {
                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                }
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<OrderContentBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();
                ToastUtil.showMessage("超时");
            }

        });
    }

}
