package com.fuleme.business.activity.Version2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.bean.MerchantDetailsBean;
import com.fuleme.business.bean.MerchantListBean;
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

public class MerchantDetailsActivity extends BaseActivity {
    private static final String TAG = "MerchantDetailsActivity";
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
    public static String shop_type = "";
    public static String shop_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_details);
        ButterKnife.bind(this);
        tvTitle.setText("商户详情");
        promotionShop();
    }

    @OnClick({R.id.tv_left, R.id.btn_enter_1, R.id.btn_enter_2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_enter_1:
                MyCommissionActivity.mid=tv1.getText().toString().trim();
                startActivity(new Intent(MerchantDetailsActivity.this, MyCommissionActivity.class));
                break;
            case R.id.btn_enter_2:
                CommissionReportActivity.short_id=tv1.getText().toString().trim();
                CommissionReportActivity.short_name=tv2.getText().toString().trim();
                startActivity(new Intent(MerchantDetailsActivity.this, CommissionReportActivity.class));
                break;
        }
    }
    /**
     * 我的推广团队
     */
    private void promotionShop() {
        showLoading("获取中...");
        Call<MerchantDetailsBean>
                call = getApi().shopdetail(
                App.token,
                shop_type,
                shop_id
        );
        LogUtil.d("------shop_id------",shop_id);
        call.enqueue(new Callback<MerchantDetailsBean>() {
            @Override
            public void onResponse(Call<MerchantDetailsBean> call, final Response<MerchantDetailsBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        tv1.setText(response.body().getData().getId()+"");
                        tv2.setText(response.body().getData().getShort_name());
                        tv3.setText(response.body().getData().getAlipay_operate_category());
                        tv4.setText(response.body().getData().getMerchant_area());
                        tv5.setText(response.body().getData().getMerchant_address());
                        tv6.setText(response.body().getData().getContact_person_name());
                        tv7.setText(response.body().getData().getContact_person_mobile());
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }
                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }
                closeLoading();//取消等待框
            }
            @Override
            public void onFailure(Call<MerchantDetailsBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }
}
