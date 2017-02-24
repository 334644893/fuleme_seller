package com.fuleme.business.helper;

import com.fuleme.business.bean.AFragmentImageBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/1/10.
 */

public interface APIService {
    String SERVER_IP = "http://localhost:8080/";

    //URL形式使用Path
    @GET("{testpath}")
    Call<AFragmentImageBean> gettest(@Path("testpath") String testpath);

    @GET("login/")
    Call<AFragmentImageBean> voidName(@Query("name") String name, @Query("password") String pw);

    //传参数使用Query
    @POST("test")
    Call<AFragmentImageBean> testPost(@Query("test") String test);
    //使用BODY传递bean
    @POST("test")
    Call<AFragmentImageBean>sonbean(@Body AFragmentImageBean bean);
}