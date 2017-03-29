package com.fuleme.business.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Window;


import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.LoginActivity;
import com.fuleme.business.fragment.FragmentActivity;
import com.fuleme.business.helper.APIService;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.SharedPreferencesUtils;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.LoadingDialogUtils;

import org.json.JSONObject;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/1/10.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ButterKnife.bind(this);

    }

    public APIService getApi() {
        return App.getInstance().getServerApi();
    }

    /**
     * 短信接口
     */
    public void send(final String TAG, String phone) {
        LogUtil.i("/TAG:" + TAG + "/phone:" + phone);
        Call<Object> call = getApi().send(phone, getIMEIorIMSI(BaseActivity.this));

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (!response.isSuccessful()) {
                    ToastUtil.showMessage("验证码失败:" + GsonUtils.getErrmsg(response.body()));
                } else {
                    ToastUtil.showMessage("发送成功请稍等...");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
            }

        });
    }

    /**
     * 获取手机IMEI号手机IMSI号
     */
    public static String getIMEIorIMSI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        String imsi = telephonyManager.getSubscriberId();
        if (!TextUtils.isEmpty(imei)) {
            return imei;
        } else if (!TextUtils.isEmpty(imsi)) {
            return imsi;
        } else {
            return "";
        }

    }


}
