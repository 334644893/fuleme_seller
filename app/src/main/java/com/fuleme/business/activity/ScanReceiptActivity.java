package com.fuleme.business.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.fragment.FragmentActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.NumberUtils;
import com.fuleme.business.utils.SharedPreferencesUtils;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.LoadingDialogUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 员工收款-扫一扫页面
 */
public class ScanReceiptActivity extends BaseActivity {
    private static final String TAG = "ScanReceiptActivity";
    public static int type = 0;//选中类型
    public static final int SAOYISAO = 1;//选中扫一扫
    public static final int ZHIFUMA = 2;//选中支付码
    public static final String ALIPAYSWEEPPAYMENT = "AlipaySweepPayment";
    public static final String WEIXINSWEEPPAYMENT = "WeixinSweepPayment";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_saoyisao)
    TextView tvSaoyisao;
    @Bind(R.id.tv_zhifuma)
    TextView tvZhifuma;
    @Bind(R.id.et_amount)
    EditText etAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receipt);
        ButterKnife.bind(this);

        init();
    }

    public void init() {
        tvTitle.setText("扫一扫");
        setOnClick(SAOYISAO);//默认选中扫一扫
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SAOYISAO) {
            if (data != null) {
                if (!TextUtils.isEmpty(data.getExtras().getString(CodeUtils.RESULT_STRING))) {
                    CreditCardPayment(data.getExtras().getString(CodeUtils.RESULT_STRING), (int) (NumberUtils.StringToDouble(etAmount.getText().toString()) * 100) + "");
//                    LogUtil.i("授权码：" + data.getExtras().getString(CodeUtils.RESULT_STRING) + "金额:" + (int) (NumberUtils.StringToDouble(etAmount.getText().toString()) * 100));
                }
            }
        }

        etAmount.setText("");


    }

    /**
     * 改变
     */
    private void setOnClick(int state) {
        type = state;
        switch (state) {
            case SAOYISAO:
                //改变UI(下同)
                tvSaoyisao.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_b_cheng));
                tvZhifuma.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_b_hui));
                break;
            case ZHIFUMA:
                tvSaoyisao.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_b_hui));
                tvZhifuma.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_b_cheng));
                break;
        }
    }

    @OnClick({R.id.tv_left, R.id.tv_saoyisao, R.id.tv_zhifuma, R.id.ll_zhifubao, R.id.ll_weixin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_saoyisao:
                setOnClick(SAOYISAO);
                break;
            case R.id.tv_zhifuma:
                setOnClick(ZHIFUMA);
                break;
            case R.id.ll_zhifubao:
                if (!TextUtils.isEmpty(etAmount.getText().toString())) {
                    switch (type) {
                        case SAOYISAO:
                            startActivityForResult(new Intent(ScanReceiptActivity.this, SecondActivity.class), SAOYISAO);
                            break;
                        case ZHIFUMA:
                            Intent intent = new Intent(ScanReceiptActivity.this, PaymentCodeActivity.class);
                            intent.putExtra("etAmount", etAmount.getText().toString());
                            intent.putExtra("payType", ALIPAYSWEEPPAYMENT);
                            startActivity(intent);

                            break;
                    }

                } else {
                    ToastUtil.showMessage("请填写金额");
                }

                break;
            case R.id.ll_weixin:
                if (!TextUtils.isEmpty(etAmount.getText().toString())) {
                    switch (type) {
                        case SAOYISAO:
                            startActivityForResult(new Intent(ScanReceiptActivity.this, SecondActivity.class), SAOYISAO);
                            break;
                        case ZHIFUMA:
                            Intent intent = new Intent(ScanReceiptActivity.this, PaymentCodeActivity.class);
                            intent.putExtra("etAmount", etAmount.getText().toString());
                            intent.putExtra("payType", WEIXINSWEEPPAYMENT);
                            startActivity(intent);

                            break;
                    }

                } else {
                    ToastUtil.showMessage("请填写金额");
                }

                break;
            case R.id.tv_left:
                finish();
                break;
        }
    }

    /**
     * 威富通刷卡支付(统一支付接口)
     * auth_code 扫码支付授权码， 设备读取用户展示的条码或者二维码信息
     * total_fee 总金额，以分为单位，不允许包含任何字、符号
     */
    private Dialog mLoading;

    private void CreditCardPayment(String auth_code, String total_fee) {
        mLoading = LoadingDialogUtils.createLoadingDialog(ScanReceiptActivity.this, "收款中...");//添加等待框
        Call<Object> call = getApi().CreditCardPayment(
                App.token,
                auth_code,
                total_fee,
                App.merchant,
                App.short_id,
                App.phone);
        LogUtil.i("App.token:" + App.token
                + "``auth_code:" + auth_code
                + "```total_fee:" + total_fee
                + "``App.merchant:" + App.merchant
                + "``App.short_id:" + App.short_id
                + "App.phone:" + App.phone);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        //TODO 初始化数据
                        ToastUtil.showMessage("收款成功");
                    } else {
                        ToastUtil.showMessage("收款失败");
                    }
                } else {
                    ToastUtil.showMessage("收款失败：" + response.message());
                }
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }
}
