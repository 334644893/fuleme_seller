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
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.LoadingDialogUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 添加店铺
 */
public class RegistrationStoreActivity extends BaseActivity {
    private static final String TAG = "RegistrationStoreActivi";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.short_name)
    EditText shortName;
    @Bind(R.id.contact_mobile)
    EditText contactMobile;
    @Bind(R.id.account_num)
    EditText accountNum;
    @Bind(R.id.tv_state)
    TextView tvState;
    @Bind(R.id.tv_state_sfz)
    TextView tvStateSfz;
    static final int BUSINESS_LICENCE = 110;
    static final int IDENTITY_CARD = 111;
    public static String url_business_licence = "";//上传营业证地址
    public static ArrayList<String> url_identity_card = new ArrayList<>();//上传身份证证地址集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_store);
        ButterKnife.bind(this);
        tvTitle.setText("添加店铺");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BUSINESS_LICENCE) {
            //营业证
            if (!TextUtils.isEmpty(url_business_licence)) {
                tvState.setText("已上传");
                tvState.setTextColor(getResources().getColor(R.color.theme));
            } else {
                tvState.setText("未上传");
                tvState.setTextColor(getResources().getColor(R.color.black_87));
            }
        }
        if (requestCode == IDENTITY_CARD) {
            //身份证
            if (url_identity_card.size() == 2) {

                tvStateSfz.setText("已上传");
                tvStateSfz.setTextColor(getResources().getColor(R.color.theme));
            } else {
                tvStateSfz.setText("未上传");
                tvStateSfz.setTextColor(getResources().getColor(R.color.black_87));
            }

        }
    }

    @OnClick({R.id.tv_left, R.id.business_licence, R.id.identity_card, R.id.btn_tj_1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.business_licence:
                //营业证
                Intent intent = new Intent(RegistrationStoreActivity.this, UploadPicturesActivity.class);
                intent.putExtra("intent", BUSINESS_LICENCE);
                startActivityForResult(intent, BUSINESS_LICENCE);
                break;
            case R.id.identity_card:
                //身份证
                Intent intent_2 = new Intent(RegistrationStoreActivity.this, UploadPicturesActivity.class);
                intent_2.putExtra("intent", IDENTITY_CARD);
                startActivityForResult(intent_2, IDENTITY_CARD);
                break;
            case R.id.btn_tj_1:
                //TODO 接口待完善
                if (comitJudge()) {
                    StringBuffer s = new StringBuffer("");
                    for (String url : url_identity_card) {
                        //拼接图片地址
                        s.append(url + ",");
                    }
                    addmerchant(url_business_licence, s.deleteCharAt(s.length() - 1).toString());
                }
                break;
        }
    }

    /**
     * 添加店铺接口
     */

    private void addmerchant(String url_business_licence_string, String url_identity_card_string) {
        showLoading("获取中...");
        Call<Object> call =
                getApi().addmerchant(
                        shortName.getText().toString(),
                        contactMobile.getText().toString(),
                        accountNum.getText().toString(),
                        url_business_licence_string,
                        url_identity_card_string,
                        App.token
                );

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, final Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        //TODO 初始化数据
                        ToastUtil.showMessage("已提交..请耐心等待");
                        finish();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }
                } else {
                    ToastUtil.showMessage("失败:" + response.message());
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
     * 判断是否符合提交
     *
     * @return
     */
    private boolean comitJudge() {
        if (TextUtils.isEmpty(shortName.getText().toString())) {
            ToastUtil.showMessage("商户简称不能为空");
            return false;
        } else if (TextUtils.isEmpty(contactMobile.getText().toString())) {
            ToastUtil.showMessage("联系人手机号不能为空");
            return false;
        } else if (TextUtils.isEmpty(accountNum.getText().toString())) {
            ToastUtil.showMessage("商户结算账号不能为空");
            return false;
        } else if (TextUtils.isEmpty(url_business_licence)) {
            ToastUtil.showMessage("请上传营业证");
            return false;
        } else if (url_identity_card.size() <2) {
            ToastUtil.showMessage("请上传身份证正反面(共2张)");
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UploadPicturesActivity.urlImg_business_licence = "";
        UploadPicturesActivity.urlImg_identity_card.clear();
        url_business_licence = "";
        url_identity_card.clear();
    }
}
