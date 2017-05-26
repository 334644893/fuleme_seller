package com.fuleme.business.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 退款
 */
public class OrderRefundActivity extends BaseActivity {
    private static final String TAG = "OrderRefundActivity";

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.et_amount)
    EditText etAmount;
    @Bind(R.id.tv_3)
    TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_refund);
        ButterKnife.bind(this);
        tvTitle.setText("退款详情");
        tv1.setText(OrderContentActivity.out_trade_no);
        tv2.setText(OrderContentActivity.primeCost + " 元");
    }

    @OnClick({R.id.tv_left, R.id.btn_enter_1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_enter_1:

                if (TextUtils.isEmpty(etAmount.getText().toString())) {
                    ToastUtil.showMessage("请输入退款金额");
                } else if (Double.valueOf(etAmount.getText().toString()) > Double.valueOf(OrderContentActivity.primeCost)) {
                    ToastUtil.showMessage("退款金额必须小于订单金额");
                } else {
                    refund(OrderContentActivity.out_trade_no, etAmount.getText().toString());
                }
                break;
        }
    }

    /**
     * 退款
     */
    private void refund(String out_trade_no, String money) {
        showLoading("请稍等...");
        Call<Object> call = App.getInstance().getServerApi().refund(out_trade_no, App.token, money);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                OrderContentActivity.refund = true;
                finish();
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();
                ToastUtil.showMessage("超时");
            }

        });
    }
}
