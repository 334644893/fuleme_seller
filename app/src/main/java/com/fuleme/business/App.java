package com.fuleme.business;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.alibaba.idst.nls.NlsClient;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.notification.BasicCustomPushNotification;
import com.alibaba.sdk.android.push.notification.CustomNotificationBuilder;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.fuleme.business.download.DeviceUtils;
import com.fuleme.business.helper.APIService;
import com.fuleme.business.helper.TokenAPIService;
import com.fuleme.business.helper.TokenInterceptor;
import com.fuleme.business.posliandi.MyService;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.SharedPreferencesUtils;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.utils.TtsUtil;
import com.fuleme.business.widget.LoadingDialogUtils;
import com.fuleme.business.widget.NoticeDialog;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/1/10.
 */

public class App extends Application {
    private static final String TAG = "App";
    public static int ver = 60;//验证码时间
    public static String VERSIONNAME = "";//
    public static String PLACEHOLDER = "";//占位符
    public static int uid = 0;//用户id
    public static String token = "";//用户标识，该token在其他用于获取用户信息的接口时必带
    public static String qrcode = "";//店铺付款转成二维码地址
    public static String phone = "";//登录手机号
    public static String username = "";//昵称(付了么号)
    public static String short_id = "";//商户ID
    public static String merchant = "";//商户名称
    public static String short_state = "";//商户审核状态
    public static String short_area = "";//商户地址
    public static String role = "";//用户角色，0 管理员，1店长，2店员
    public static String short_logo = "";//店铺logo
    public static String is_agent = "";//1代理 0非代理
    public static String invitation_code = "";//代理商邀请码
    public static String pay_password = "";//支付密码
    public static String create_time = "";//注册时间
    //    public static int login_type = 1;//登录状态 0:管理员 1：员工
    public static boolean bindYY;//语音开关
    public static boolean bindAccount;//通知开关
    public static boolean bindPrinter;//打印机蓝牙连接开关
    public static boolean POS = false;//是否是POS机版本
    final public static String alipay = "alipay";
    final public static String weixin = "weixin";
    public static final int LOGIN_TYPE_ADMIN = 0;//登录状态 0:管理员
    public static final int LOGIN_TYPE_EMPLOYEES = 1;//登录状态  1：员工
    private static App instance;
    private static NoticeDialog dialog1;
    private APIService serverApi;
    private TokenAPIService tokenapiservice;
    public static CloudPushService pushService;

    public APIService getServerApi() {
        return serverApi;
    }

    public TokenAPIService getTokenAPIService() {
        return tokenapiservice;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        VERSIONNAME = DeviceUtils.getVersionName(this);
//        login_type = (int) SharedPreferencesUtils.getParam(App.getInstance(), "login_type", 1);
        initFresco();//初始化图片加载
        initRest();//初始化网络通信
        bindAccount = (boolean) SharedPreferencesUtils.getParam(getApplicationContext(), "bindAccount", true);
        bindYY = (boolean) SharedPreferencesUtils.getParam(getApplicationContext(), "bindYY", true);
        initCloudChannel(this);//阿里云
        NlsClient.configure(this); //语音合成全局配置
        ZXingLibrary.initDisplayOpinion(this);//二维码
        isPrintLog();
//        POS=true;//开启POS机模式
        POS = false;//开启POS机模式

        // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
        MiPushRegister.register(this, "2882303761517571909", "5141757128909");
        // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
        HuaWeiRegister.register(this);
    }

    private void isPrintLog() {
        if ("https://dev.fuleme.com/".equals(APIService.SERVER_IP)) {
            LogUtil.isPrint = true;// 设置开启日志,发布时请关闭日志
        } else {
            LogUtil.isPrint = false;// 设置开启日志,发布时请关闭日志
        }
    }

    private void initFresco() {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)//支持各种格式
                .build();
        Fresco.initialize(this, config);
    }

    private void initRest() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.SERVER_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        serverApi = retrofit.create(APIService.class);
        //获取token时用到
        Retrofit retrofit_token = new Retrofit.Builder()
                .baseUrl(APIService.SERVER_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tokenapiservice = retrofit_token.create(TokenAPIService.class);
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
        pushService.bindAccount(App.phone, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                LogUtil.d("---绑定成功----bindAccount()", "绑定成功" + s);
                App.bindAccount = true;
                SharedPreferencesUtils.setParam(getInstance().getApplicationContext(), "bindAccount", true);
            }

            @Override
            public void onFailed(String s, String s1) {
                LogUtil.d(TAG + "bindAccount()onFailed", "s:" + s + "s1:" + s1);
                ToastUtil.showMessage("通知暂时无法使用");
            }
        });
    }

    /**
     * 解除绑定阿里云推送账号
     */
    public static void unbindAccount() {
        pushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                App.bindAccount = false;
                SharedPreferencesUtils.setParam(getInstance().getApplicationContext(), "bindAccount", false);
                LogUtil.d("---解绑成功----unbindAccount()", s);
            }

            @Override
            public void onFailed(String s, String s1) {
                LogUtil.d(TAG + "unbindAccount()onFailed", "s:" + s + "s1:" + s1);
                ToastUtil.showMessage("解除失败");
            }

        });
    }

    /**
     * 收款成功弹窗
     */

    public static void NoticeDialog(Context context, String amount, String order) {
        if (dialog1 != null) {
            dialog1.dismiss();
        }
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

    /**
     * POS收款成功打印
     */

    public static void startPosService(String short_name, String total_fee, String time_end, String out_trade_no) {
        Intent intent = new Intent(getInstance().getApplicationContext(), MyService.class);
        intent.putExtra("short_name", short_name);
        intent.putExtra("total_fee", total_fee);
        intent.putExtra("time_end", time_end);
        intent.putExtra("out_trade_no", out_trade_no);
        getInstance().getApplicationContext().startService(intent);
    }


    @Override
    public void onTerminate() {
        TtsUtil.audioTrack.release();//关闭语音播放
        super.onTerminate();
    }
    private static final String APP_ID = "wx0c962071b3960450";    //这个APP_ID就是注册APP的时候生成的
    private static final String APP_SECRET = "e18ad56b0aa1005a7e5810e355a00c87";
    public static IWXAPI api;
    public static void registerWeChat(Context context) {   //向微信注册app
        api = WXAPIFactory.createWXAPI(context, APP_ID, true);
        api.registerApp(APP_ID);
    }
}
