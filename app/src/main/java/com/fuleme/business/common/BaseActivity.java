package com.fuleme.business.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
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
import com.fuleme.business.utils.Constant;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.SharedPreferencesUtils;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.utils.WxUtil;
import com.fuleme.business.widget.LoadingDialogUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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
    private Dialog mLoading;

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

    public void showLoading(String text) {
        mLoading = LoadingDialogUtils.createLoadingDialog(BaseActivity.this, text

        );//添加等待框
    }

    public void closeLoading() {
        if (mLoading.isShowing()) {
            mLoading.dismiss();
        }

    }

    /**
     * 短信接口
     */
    public void send(final String TAG, String phone) {
        LogUtil.i("/TAG:" + TAG + "/phone:" + phone);
        Call<Object> call = getApi().send(phone, getIMEIorIMSI());

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (!response.isSuccessful()) {
                    ToastUtil.showMessage("验证码失败:" + GsonUtils.getErrmsg(response.body()));
                } else {
                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                ToastUtil.showMessage("获取验证码失败");
            }

        });
    }

    /**
     * 获取手机唯一标示
     */
    public static String getIMEIorIMSI() {
        if (!TextUtils.isEmpty(Build.SERIAL)) {
            if (Build.SERIAL.length() < 15) {
                return Build.SERIAL;
            } else {
                return Build.SERIAL.substring(0, 15);
            }

        } else {
            return "";
        }
    }

    public static void shear(boolean b,int icon) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = Constant.URL;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title=Constant.TITLE;
        msg.description=Constant.description;
        Bitmap thumb= BitmapFactory.decodeResource(App.getInstance().getResources(),icon);
        msg.thumbData= WxUtil.bmpToByteArray(thumb,true);
        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction=WxUtil.buildTransaction("webpage");
        req.message=msg;
        req.scene = b?SendMessageToWX.Req.WXSceneTimeline:SendMessageToWX.Req.WXSceneSession;
        App.api.sendReq(req);
    }

}
