package com.fuleme.business.helper;

import com.fuleme.business.bean.AggregationQueryBean;
import com.fuleme.business.bean.ClerkInfoBean;
import com.fuleme.business.bean.IncomeBean;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2017/1/10.
 */

public interface APIService {
    //    String SERVER_IP = "http://192.168.1.138/";
    String SERVER_IP = "https://dev.fuleme.com/";//TEST

    /**
     * 注册
     *
     * @param phone
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("user/register")
    Call<Object> register(@Field("phone") String phone,
                          @Field("username") String username,
                          @Field("password") String password
    );

    /**
     * 修改密码
     *
     * @param token
     * @param oldpwd
     * @param newpwd
     * @return
     */
    @FormUrlEncoded
    @POST("user/modifypwd")
    Call<Object> modifypwd(@Field("token") String token,
                           @Field("oldpwd") String oldpwd,
                           @Field("newpwd") String newpwd
    );

    /**
     * 登录
     *
     * @param phone
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("user/login")
    Call<Object> login(@Field("phone") String phone,
                       @Field("password") String password,
                       @Field("role") int role);

    /**
     * 忘记密码
     *
     * @param phone    手机号
     * @param code     验证码
     * @param password 新密码
     * @return
     */
    @FormUrlEncoded
    @POST("user/forgetpwd")
    Call<Object> forgetpwd(@Field("phone") String phone,
                           @Field("code") String code,
                           @Field("password") String password);

    /**
     * 用户退出接口
     *
     * @param token 用户标识
     * @return
     */
    @FormUrlEncoded
    @POST("user/logout")
    Call<Object> logout(@Field("token") String token);

    /**
     * 发送验证码接口
     *
     * @param phone 手机号
     * @param imei  手机识别号
     * @return
     */
    @FormUrlEncoded
    @POST("user/send")
    Call<Object> send(@Field("phone") String phone,
                      @Field("imei") String imei);

    /**
     * 系统启动图接口
     *
     * @return
     */
    @GET("system/startup")
    Call<Object> startup();


    /**
     * 上传图片接口
     *
     * @param part 上传的文件
     */
    @Multipart
    @POST("user/upload")
    Call<Object> uploadMemberIcon(@Part MultipartBody.Part part);

    /**
     * 关于我们接口
     *
     * @return
     */
    @GET("system/about")
    Call<Object> about();

    /**
     * 版本更新接口
     *
     * @return
     */
    @GET("system/version")
    Call<Object> version();

//    /**
//     * 添加店铺接口(废弃)
//     *
//     * @param mercht_full_nm  商户全名称
//     * @param mercht_sht_nm   商户简称
//     * @param cust_serv_tel   客户服务电话
//     * @param contcr_nm       联系人名称
//     * @param contcr_tel      联系人电话
//     * @param contcr_mobl_num 联系人手机号
//     * @param contcr_eml      联系人邮箱
//     * @param opr_cls         经营类目
//     * @param mercht_memo     商户备注
//     * @param area            县区
//     * @param dtl_addr        详细地址
//     * @param acct_nm         账户名称
//     * @param opn_bnk         开户行
//     * @param acct_typ        帐号类型 1对私账户，2对公账户，3内部账户
//     * @param acct_num        商户结算账号
//     * @param is_nt_two_line  是否支持收支两条线
//     * @param biz_lics        营业证
//     * @param open_wx         0不开通1开通微信支付
//     * @param wx_rate         微信支付费率
//     * @param open_zfb        0不开通1开通支付宝支付
//     * @param zfb_rate        支付宝支付费率
//     * @param appl_typ        申请类型 新增：0；变更：1；停用：2
//     * @param token           用户token
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("merchant/addmerchant")
//    Call<Object> addmerchant(@Field("mercht_full_nm") String mercht_full_nm,
//                             @Field("mercht_sht_nm") String mercht_sht_nm,
//                             @Field("cust_serv_tel") String cust_serv_tel,
//                             @Field("contcr_nm") String contcr_nm,
//                             @Field("contcr_tel") String contcr_tel,
//                             @Field("contcr_mobl_num") String contcr_mobl_num,
//                             @Field("contcr_eml") String contcr_eml,
//                             @Field("opr_cls") String opr_cls,
//                             @Field("mercht_memo") String mercht_memo,
//                             @Field("area") String area,
//                             @Field("dtl_addr") String dtl_addr,
//                             @Field("acct_nm") String acct_nm,
//                             @Field("opn_bnk") String opn_bnk,
//                             @Field("acct_typ") String acct_typ,
//                             @Field("acct_num") String acct_num,
//                             @Field("is_nt_two_line") String is_nt_two_line,
//                             @Field("biz_lics") String biz_lics,
//                             @Field("open_wx") String open_wx,
//                             @Field("wx_rate") String wx_rate,
//                             @Field("open_zfb") String open_zfb,
//                             @Field("zfb_rate") String zfb_rate,
//                             @Field("appl_typ") String appl_typ,
//                             @Field("token") String token
//    );

    /**
     * 添加店铺接口
     * @param short_name 商户简称
     * @param contact_mobile 联系人手机号
     * @param account_num 商户结算账号
     * @param business_licence 营业证
     * @param identity_card 身份证
     * @param token 用户token
     * @return
     */
    @FormUrlEncoded
    @POST("merchant/addmerchant")
    Call<Object> addmerchant(@Field("short_name") String short_name,
                             @Field("contact_mobile") String contact_mobile,
                             @Field("account_num") String account_num,
                             @Field("business_licence") String business_licence,
                             @Field("identity_card") String identity_card,
                             @Field("token") String token

    );

    /**
     * 删除店铺店员接口
     * @param token
     * @param shopid 店铺ID
     * @param id   店员ID
     * @return
     */
    @FormUrlEncoded
    @POST("merchant/delclerk")
    Call<Object> delclerk(@Field("token") String token,
                            @Field("shopid") String shopid,
                            @Field("id") String id);

//    /**
//     * 省接口
//     *
//     * @return
//     */
//    @GET("merchant/getprov")
//    Call<ProvBean> getprov();

//    /**
//     * 市接口
//     */
//    @FormUrlEncoded
//    @POST("merchant/geturbn")
//    Call<ProvBean> geturbn(@Field("pcode") String pcode);
//
//    /**
//     * 区接口
//     */
//    @FormUrlEncoded
//    @POST("merchant/getarea")
//    Call<ProvBean> getarea(@Field("pcode") String pcode);
//
//    /**
//     * 行业接口
//     */
//    @GET("merchant/getoprcls")
//    Call<IndustryBean> getoprcls();
//
//    /**
//     * 获取开户行接口
//     */
//    @GET("merchant/getbank")
//    Call<BankBean> getbank();

    /**
     * 获取店铺店员接口
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("merchant/getmerchantclerkinfo")
    Call<ClerkInfoBean> getmerchantclerkinfo(@Field("token") String token);

    /**
     * 添加店铺员工接口
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("merchant/addclerk")
    Call<Object> addclerk(@Field("token") String token,
                          @Field("shopid") String shopid,
                          @Field("username") String username,
                          @Field("password") String password,
                          @Field("phone") String phone,
                          @Field("role") String role);

    /**
     * 我的账户收入接口
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("account/income")
    Call<IncomeBean> income(@Field("token") String token,
                            @Field("page") int page,
                            @Field("list_rows") int list_rows);

    /**
     * 汇总查询接口
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("aggregate/queries")
    Call<AggregationQueryBean> queries(@Field("token") String token,
                                       @Field("shopid") String shopid,
                                       @Field("starttime") String starttime,
                                       @Field("endtime") String endtime);


}