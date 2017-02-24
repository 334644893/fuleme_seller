package com.fuleme.business;

import android.app.Application;

import com.fuleme.business.helper.APIService;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/1/10.
 */

public class App extends Application {
    public static  int login_type = 1;//登录状态 0:管理员 1：员工
    public static  final int LOGIN_TYPE_ADMIN = 0;
    public static  final int LOGIN_TYPE_EMPLOYEES= 1;
    private static App instance;
    private APIService serverApi;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        initRest();//初始化网络通信
        ZXingLibrary.initDisplayOpinion(this);//二维码
    }

    private void initRest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.SERVER_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serverApi = retrofit.create(APIService.class);
    }

    public APIService getServerApi() {
        return serverApi;
    }

    public static App getInstance() {
        return instance;
    }




}
