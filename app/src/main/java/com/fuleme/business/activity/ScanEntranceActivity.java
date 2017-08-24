package com.fuleme.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.NumberUtils;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.VirtualKeyboardView;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 扫一扫收款
 */
public class ScanEntranceActivity extends BaseActivity {
    private static final String TAG = "ScanEntranceActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_amount)
    EditText etAmount;
    public static final int SAOYISAO = 1;//选中扫一扫
    private VirtualKeyboardView virtualKeyboardView;
    private GridView gridView;
    private ArrayList<Map<String, String>> valueList;
    private Animation enterAnim;
    private Animation exitAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_entrance);
        ButterKnife.bind(this);
        tvTitle.setText("收款");
        initAnim();
        initView();
        valueList = virtualKeyboardView.getValueList();
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                if ((new Float(Float.parseFloat(s.toString().trim()) * 100)).intValue() > 1000000) {
                    etAmount.setText("10000.00");
                    etAmount.setSelection(etAmount.getText().toString().length());
                    ToastUtil.showMessage("限额1万元");
                } else {
                    etAmount.setMaxEms(10);
                }
            }
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    etAmount.setText(s);
                    etAmount.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                etAmount.setText(s);
                etAmount.setSelection(2);
            }

            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    etAmount.setText(s.subSequence(0, 1));
                    etAmount.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (!TextUtils.isEmpty(data.getExtras().getString(CodeUtils.RESULT_STRING))) {
//                CreditCardPayment(data.getExtras().getString(CodeUtils.RESULT_STRING), NumberUtils.StringToAmount(etAmount.getText().toString().trim()) + "");
                fyPaycard(data.getExtras().getString(CodeUtils.RESULT_STRING), NumberUtils.StringToAmount(etAmount.getText().toString().trim()) + "");
            }
        }
        etAmount.setText("");
    }

    @OnClick({R.id.tv_left, R.id.btn_enter_1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_enter_1:
                if (!TextUtils.isEmpty(etAmount.getText().toString())) {
                    startActivityForResult(new Intent(ScanEntranceActivity.this, SecondActivity.class), SAOYISAO);
                } else {
                    ToastUtil.showMessage("请输入正确金额");
                }
                break;
        }
    }

//    /**
//     * 威富通刷卡支付(统一支付接口)
//     * auth_code 扫码支付授权码， 设备读取用户展示的条码或者二维码信息
//     * total_fee 总金额，以分为单位，不允许包含任何字、符号
//     */
//
//    private void CreditCardPayment(String auth_code, String total_fee) {
//        showLoading("收款中...");
//        Call<Object> call = getApi().CreditCardPayment(
//                App.token,
//                auth_code,
//                total_fee,
//                App.merchant,
//                App.short_id,
//                App.phone);
//        LogUtil.i("App.token:" + App.token
//                + "``auth_code:" + auth_code
//                + "```total_fee:" + total_fee
//                + "``App.merchant:" + App.merchant
//                + "``App.short_id:" + App.short_id
//                + "App.phone:" + App.phone);
//        call.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, Response<Object> response) {
//                if (response.isSuccessful()) {
//                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
//                        // do SomeThing
//                        ToastUtil.showMessage(GsonUtils.getResultData(response.body()).optString(GsonUtils.ERRMSG));
//                    }
//                }
//                closeLoading();//取消等待框
//            }
//
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//                LogUtil.e(TAG, t.toString());
//                closeLoading();//取消等待框
//                ToastUtil.showMessage("超时");
//            }
//
//        });
//    }

    /**
     * 富友刷卡支付(统一支付接口)
     * auth_code 扫码支付授权码， 设备读取用户展示的条码或者二维码信息
     * total_fee 总金额，以分为单位，不允许包含任何字、符号
     */

    private void fyPaycard(String auth_code, String total_fee) {
        showLoading("收款中...");
        Call<Object> call = getApi().fyPaycard(
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
                        if (!TextUtils.isEmpty(GsonUtils.getResultData(response.body()).optString(GsonUtils.ERRMSG))) {
                            ToastUtil.showMessage(GsonUtils.getResultData(response.body()).optString(GsonUtils.ERRMSG));
                        }

                    }
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
     * 数字键盘显示动画
     */
    private void initAnim() {
        enterAnim = AnimationUtils.loadAnimation(this, R.anim.push_bottom_in);
        exitAnim = AnimationUtils.loadAnimation(this, R.anim.push_bottom_out);
    }

    private void initView() {
        etAmount.addTextChangedListener(watcher);
        // 设置不调用系统键盘
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            etAmount.setInputType(InputType.TYPE_NULL);
        } else {
            this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(etAmount, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        virtualKeyboardView = (VirtualKeyboardView) findViewById(R.id.virtualKeyboardView);
        virtualKeyboardView.getLayoutBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                virtualKeyboardView.startAnimation(exitAnim);
                virtualKeyboardView.setVisibility(View.GONE);
            }
        });

        gridView = virtualKeyboardView.getGridView();
        gridView.setOnItemClickListener(onItemClickListener);

        etAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                virtualKeyboardView.setFocusable(true);
                virtualKeyboardView.setFocusableInTouchMode(true);

                virtualKeyboardView.startAnimation(enterAnim);
                virtualKeyboardView.setVisibility(View.VISIBLE);
            }
        });

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            if (position < 11 && position != 9) {    //点击0~9按钮

                String amount = etAmount.getText().toString().trim();
                amount = amount + valueList.get(position).get("name");

                etAmount.setText(amount);

                Editable ea = etAmount.getText();
                etAmount.setSelection(ea.length());
            } else {

                if (position == 9) {      //点击退格键
                    String amount = etAmount.getText().toString().trim();
                    if (!amount.contains(".")) {
                        amount = amount + valueList.get(position).get("name");
                        etAmount.setText(amount);

                        Editable ea = etAmount.getText();
                        etAmount.setSelection(ea.length());
                    }
                }

                if (position == 11) {      //点击退格键
                    String amount = etAmount.getText().toString().trim();
                    if (amount.length() > 0) {
                        amount = amount.substring(0, amount.length() - 1);
                        etAmount.setText(amount);

                        Editable ea = etAmount.getText();
                        etAmount.setSelection(ea.length());
                    }
                }
            }
        }
    };
}
