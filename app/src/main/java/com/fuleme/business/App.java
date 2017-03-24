package com.fuleme.business;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;

import com.alibaba.idst.nls.NlsClient;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.notification.BasicCustomPushNotification;
import com.alibaba.sdk.android.push.notification.CustomNotificationBuilder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.fuleme.business.helper.APIService;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.utils.TtsUtil;
import com.fuleme.business.widget.LoadingDialogUtils;
import com.fuleme.business.widget.NoticeDialog;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/1/10.
 */

public class App extends Application {
    private static final String TAG = "App";
    public static String PLACEHOLDER = "";//占位符
    public static int uid = 0;//用户id
    public static String token = "";//用户标识，该token在其他用于获取用户信息的接口时必带
    public static String qrcode = "";//店铺付款转成二维码地址
    public static String phone = "";//登录手机号
    public static String username = "";//昵称(付了么号)
    public static String short_id = "";//商户ID
    public static String merchant = "";//商户名称
    public static String short_state = "";//商户名称
    public static String short_area = "";//商户地址
    public static String role = "";//用户角色，0 管理员，1店长，2店员
    public static int login_type = 1;//登录状态 0:管理员 1：员工
    public static boolean bindAccount = true;//通知开关
    final public static String alipay = "pay.alipay.jspay";
    final public static String weixin = "pay.weixin.jspay";
    public static final int LOGIN_TYPE_ADMIN = 0;//登录状态 0:管理员
    public static final int LOGIN_TYPE_EMPLOYEES = 1;//登录状态  1：员工
    private static App instance;
    private APIService serverApi;
    public static CloudPushService pushService;
    private static Dialog mLoading, mLoading_1;

    public APIService getServerApi() {
        return serverApi;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initFresco();//初始化图片加载
        initRest();//初始化网络通信
        initCloudChannel(this);//阿里云
        NlsClient.configure(this); //语音合成全局配置
        ZXingLibrary.initDisplayOpinion(this);//二维码

    }

    private void initFresco() {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)//支持各种格式
                .build();
        Fresco.initialize(this, config);
    }

    private void initRest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.SERVER_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serverApi = retrofit.create(APIService.class);
    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        pushService = PushServiceFactory.getCloudPushService();
//        pushService.setNotificationLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.d(TAG, "init cloudchannel success");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                LogUtil.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

        BasicCustomPushNotification notification = new BasicCustomPushNotification();
        notification.setServerOptionFirst(true);//设置服务端配置优先
        notification.setStatusBarDrawable(R.mipmap.icon_fan);

        notification.setBuildWhenAppInForeground(false);//设置当推送到达时如果应用处于前台不创建通知
        CustomNotificationBuilder.getInstance().setCustomNotification(2, notification);//注册该通知,并设置ID为2
        PushServiceFactory.getCloudPushService().clearNotifications();
    }

    /**
     * 绑定阿里云推送账号（为登录手机号）
     */
    public static void bindAccount() {
        mLoading_1 = LoadingDialogUtils.createLoadingDialog(instance, "请等待...");//添加等待框
        pushService.bindAccount(App.phone, new CommonCallback() {
            @Override
            public void onSuccess(String s) {

                LogUtil.d(TAG + "bindAccount()", s);
                bindAccount = true;
                mLoading_1.dismiss();
            }

            @Override
            public void onFailed(String s, String s1) {
                LogUtil.d(TAG + "bindAccount()onFailed", "s:" + s + "s1:" + s1);
                mLoading_1.dismiss();
                ToastUtil.showMessage("通知暂时无法使用");
            }
        });
    }


    /**
     * 解除绑定阿里云推送账号
     */
    public static void unbindAccount() {
        mLoading = LoadingDialogUtils.createLoadingDialog(instance, "请等待...");//添加等待框
        pushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) {

                LogUtil.d(TAG + "unbindAccount()", s);
                bindAccount = false;
                mLoading.dismiss();
            }

            @Override
            public void onFailed(String s, String s1) {
                LogUtil.d(TAG + "unbindAccount()onFailed", "s:" + s + "s1:" + s1);
                mLoading.dismiss();
            }

        });
    }

    /**
     * 收款成功弹窗
     */
    public static void NoticeDialog(Context context, String amount, String order) {
        NoticeDialog dialog1;
        NoticeDialog.Builder noticeBuilder = new
                NoticeDialog.Builder(context);
        noticeBuilder
                .setAmount(amount)
                .setOrder(order)
                .setPositiveButton(
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
        dialog1 = noticeBuilder.create();
        dialog1.show();
    }


    @Override
    public void onTerminate() {
        TtsUtil.audioTrack.release();//关闭语音播放
        super.onTerminate();
    }
}
