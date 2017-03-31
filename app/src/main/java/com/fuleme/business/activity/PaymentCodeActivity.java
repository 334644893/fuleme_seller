package com.fuleme.business.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.NumberUtils;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.utils.Zxing;
import com.fuleme.business.widget.LoadingDialogUtils;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentCodeActivity extends BaseActivity {
    private static final String TAG = "PaymentCodeActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_qian)
    TextView tvQian;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.iv_ba_qr_code)
    ImageView ivBaQrCode;
    private String etAmount = "";
    private String payType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_code);
        ButterKnife.bind(this);

        initview();
    }

    public void initview() {
        tvTitle.setText("支付码");
        tvName.setText(App.merchant);
        etAmount = getIntent().getExtras().getString("etAmount");
        payType = getIntent().getExtras().getString("payType");
        tvQian.setText("￥" + etAmount);
        if (payType.equals(ScanReceiptActivity.ALIPAYSWEEPPAYMENT)) {
            //生成微信支付二维码接口
            AlipaySweepPayment((int) (NumberUtils.StringToDouble(etAmount) * 100) + "");
        } else if (payType.equals(ScanReceiptActivity.WEIXINSWEEPPAYMENT)) {
            //生成微信支付二维码接口
            WeixinSweepPayment((int) (NumberUtils.StringToDouble(etAmount) * 100) + "");
        } else {
            ToastUtil.showMessage("生成失败");
        }
//        ToastUtil.showMessage("生成金额为："+(int) (NumberUtils.StringToDouble(etAmount) * 100)+"分的支付码");
    }

    @OnClick(R.id.tv_left)
    public void onClick() {
        finish();
    }

    /**
     * 生成微信扫码二维码支付接口
     */

    private void WeixinSweepPayment(String total_fee) {
        showLoading("生成中...");
        Call<Object> call = getApi().WeixinSweepPayment(
                App.token,
                App.merchant,
                App.short_id,
                total_fee);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        ToastUtil.showMessage("生成微信码成功");
                        //TODO 初始化数据
                        JSONObject data = GsonUtils.getResultData(response.body());
                        ivBaQrCode.setImageBitmap(Zxing.getQrCode(data.optString("code_url")));
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }
                } else {
                    ToastUtil.showMessage("生成二维码失败：" + response.message());
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

    /**
     * 生成支付宝二维码支付接口
     */

    private void AlipaySweepPayment(String total_fee) {
        showLoading("生成中...");
        Call<Object> call = getApi().AlipaySweepPayment(
                App.token,
                App.merchant,
                App.short_id,
                total_fee);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        //TODO 初始化数据
                        ToastUtil.showMessage("生成支付宝码成功");
                        JSONObject data = GsonUtils.getResultData(response.body());
                        ivBaQrCode.setImageBitmap(Zxing.getQrCode(data.optString("code_url")));
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }
                } else {
                    ToastUtil.showMessage("生成二维码失败：" + response.message());
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
}
