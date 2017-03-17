package com.fuleme.business;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.fuleme.business.helper.APIService;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
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
    public static String phone = "";//登录手机号
    public static String username = "";//昵称(付了么号)
    public static String merchant = "";//商户名称
    public static String role  = "";//用户角色，0 管理员，1店长，2店员
    public static String area  = "";//店铺地区
    public static String opr_cls  = "";//所属行业
    public static int login_type = 1;//登录状态 0:管理员 1：员工
    public static final int LOGIN_TYPE_ADMIN = 0;
    public static final int LOGIN_TYPE_EMPLOYEES = 1;
    private static App instance;
    private APIService serverApi;

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        initFresco();//初始化图片加载
        initRest();//初始化网络通信
        initCloudChannel(this);//阿里云
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
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
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
    }
    public APIService getServerApi() {
        return serverApi;
    }

    public static App getInstance() {
        return instance;
    }


}
