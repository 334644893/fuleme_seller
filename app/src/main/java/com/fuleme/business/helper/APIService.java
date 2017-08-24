package com.fuleme.business.helper;

import com.fuleme.business.bean.AnalysecouponBean;
import com.fuleme.business.bean.BalanceBean;
import com.fuleme.business.bean.BankBean;
import com.fuleme.business.bean.ClerkInfoBean;
import com.fuleme.business.bean.ClerkOederBean;
import com.fuleme.business.bean.CommissionReportBean;
import com.fuleme.business.bean.ContractBean;
import com.fuleme.business.bean.CouponsBean;
import com.fuleme.business.bean.CustomerBean;
import com.fuleme.business.bean.IncomeBean;
import com.fuleme.business.bean.MemberManagementBean;
import com.fuleme.business.bean.MerchantDetailsBean;
import com.fuleme.business.bean.MerchantListBean;
import com.fuleme.business.bean.MyBankBean;
import com.fuleme.business.bean.MyCommissionBean;
import com.fuleme.business.bean.OrderBean;
import com.fuleme.business.bean.OrderContentBean;
import com.fuleme.business.bean.OrderDetailsBean;
import com.fuleme.business.bean.PromoteBean;
import com.fuleme.business.bean.ServiceBusinessesBean;
import com.fuleme.business.bean.SinceMediaBean;
import com.fuleme.business.bean.bannerBean;

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
//    String SERVER_IP = "http://192.168.1.155/";
    String SERVER_IP = "https://dev.fuleme.com/";//TEST
//    String SERVER_IP = "https://pay.fuleme.com/";
    /**
     * 关于我们接口
     *
     * @return
     */
    String ABOUT = "system/about";

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
                          @Field("password") String password,
                          @Field("code") String code,
                          @Field("invitation_code") String invitation_code
    );
    /**
     * 签约推广接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/promotion/sign")
    Call<Object> sign(@Field("idcard") String idcard,
                          @Field("truename") String truename,
                          @Field("promotion_phone") String promotion_phone,
                          @Field("idcard_img") String idcard_img,
                          @Field("token") String token
    );
    /**
     * 联系服务商接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/promotion/introducer")
    Call<Object> introducer(
                          @Field("token") String token
    );

    /**
     * 绑定银行卡接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/withdrawals/bindingbank")
    Call<Object> bindingbank(@Field("token") String token,
                             @Field("truename") String truename,
                             @Field("bankcard") String bankcard,
                             @Field("account_bank") String account_bank,
                             @Field("validate") String validate
    );
    /**
     * 补填邀请码接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/user/modifyInvite_code")
    Call<Object> modifyInvite_code(@Field("token") String token,
                             @Field("invitation_code") String invitation_code

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
                       @Field("password") String password);

    /**
     * 我的推广接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/promotion/promotion")
    Call<Object> promotion(@Field("token") String token
    );

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
    @POST("sms/send")
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
     * 获取轮播图接口
     */
    @GET("system/banner")
    Call<bannerBean> banner();

    /**
     * 获取开户行
     */
    @GET("merchant/getbank")
    Call<BankBean> getbank();


    /**
     * 版本更新接口
     *
     * @return
     */
    @GET("system/version")
    Call<Object> version();

    /**
     * 账单-获取店铺订单
     *
     * @return
     */
    @FormUrlEncoded
    @POST("shop/orderInfo")
    Call<OrderDetailsBean> orderInfo(@Field("token") String token,
                                     @Field("year") String year,
                                     @Field("month") String month,
                                     @Field("shopid") String shopid,
                                     @Field("page") int page,
                                     @Field("list_rows") int list_rows);

    /**
     * 推广-我的返佣接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/rebate/rebatelist")
    Call<MyCommissionBean> rebatelist(@Field("token") String token,
                                      @Field("page") int page,
                                      @Field("list_rows") int list_rows,
                                      @Field("begintime") int begintime,
                                      @Field("endtime") int endtime,
                                      @Field("mid") String shopid
    );

    /**
     * 推广-我的返佣接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/rebate/rebatelist")
    Call<MyCommissionBean> rebatelist(@Field("token") String token,
                                      @Field("page") int page,
                                      @Field("list_rows") int list_rows,
                                      @Field("begintime") int begintime,
                                      @Field("endtime") int endtime
    );

    /**
     * 推广-我的返佣接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/promotion/promotion_team")
    Call<PromoteBean> promotionTeam(@Field("token") String token,
                                    @Field("page") int page,
                                    @Field("list_rows") int list_rows
    );

    /**
     * 推广-我的推广团队接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/promotion/promotion_shop")
    Call<MerchantListBean> promotionShop(@Field("token") String token,
                                         @Field("team_id") String team_id,
                                         @Field("page") int page,
                                         @Field("list_rows") int list_rows
    );

    /**
     * 推广-
     * 我的银行卡接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/withdrawals/bankcard")
    Call<MyBankBean> bankcard(@Field("token") String token

    );

    /**
     * 推广-
     * 确认提现接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/withdrawals/drawmoney")
    Call<Object> drawmoney(
            @Field("token") String token,
            @Field("money") String money

    );


    /**
     * 推广-店铺详情接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/shop/shopdetail")
    Call<MerchantDetailsBean> shopdetail(@Field("token") String token,
                                         @Field("shop_type") String shop_type,
                                         @Field("shop_id") String shop_id
    );

    /**
     * 我的服务商户和团队商户接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/shop/shoplist")
    Call<ServiceBusinessesBean> shoplist(@Field("token") String token,
                                         @Field("type") String type,
                                         @Field("page") int page,
                                         @Field("list_rows") int list_rows
    );

    /**
     * 报表-获取店铺订单
     *
     * @return
     */
    @FormUrlEncoded
    @POST("shop/orderInfo")
    Call<OrderDetailsBean> orderInfo(@Field("token") String token,
                                     @Field("year") String year,
                                     @Field("month") String month,
                                     @Field("starttime") int starttime,
                                     @Field("endtime") int endtime,
                                     @Field("trade_type") String trade_type,
                                     @Field("shopid") String shopid,
                                     @Field("page") int page,
                                     @Field("list_rows") int list_rows);

    /**
     * 获取店铺用户接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("merchant/getmchuser")
    Call<MemberManagementBean> getmchuser(@Field("token") String token,
                                          @Field("mid") String mid,
                                          @Field("page") int page,
                                          @Field("list_rows") int list_rows);

    /**
     * 店铺评价
     *
     * @return
     */
    @FormUrlEncoded
    @POST("merchant/comment")
    Call<CustomerBean> comment(@Field("token") String token,
                               @Field("mid") String mid
            ,
                               @Field("page") int page,
                               @Field("list_rows") int list_rows
    );

    /**
     * 门店列表接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("shop/list")
    Call<OrderBean> list(@Field("token") String token);

    /**
     * 提现列表接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/withdrawals/drawmoneylist")
    Call<BalanceBean> drawmoneylist(@Field("token") String token);

    /**
     * 订单详情内容
     *
     * @return
     */
    @FormUrlEncoded
    @POST("/api/order/OrderDetails")
    Call<OrderContentBean> OrderDetails(@Field("out_trade_no") String out_trade_no);

    /**
     * 订单退款
     *
     * @return
     */
    @FormUrlEncoded
    @POST("pay/refund")
    Call<Object> refund(@Field("out_trade_no") String out_trade_no,
                        @Field("token") String token,
                        @Field("money") String money);
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
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/merchant/addshop")
    Call<Object> addshop(
            @Field("token") String token,
            @Field("short_name") String short_name,
            @Field("account_num") String account_num,
            @Field("account_name") String account_name,
            @Field("account_bank_address") String account_bank_address,
            @Field("account_mobile") String account_mobile,
            @Field("account_identity_card_num") String account_identity_card_num,
            @Field("legal_person_name") String legal_person_name,
            @Field("legal_person_mobile") String legal_person_mobile,
            @Field("business_licence") String business_licence,
            @Field("legal_person_identity_card") String legal_person_identity_card,
            @Field("merchant_address") String merchant_address
    );

    /**
     * 添加店铺LOGO接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("user/modifyheadImg")
    Call<Object> modifyheadImg(@Field("token") String token,
                               @Field("headImg") String headImg


    );

    /**
     * 删除店铺店员接口
     *
     * @param token
     * @param shopid 店铺ID
     * @param id     店员ID
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
     * 获取店员的收款信息
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("/api/order/clerk_oeder")
    Call<ClerkOederBean> clerk_oeder(@Field("clerk_id") String clerk_id,
                                     @Field("merchant_id") String merchant_id,
                                     @Field("starttime") String starttime,
                                     @Field("endtime") String endtime);

    /**
     * 活动列表接口
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("coupon/list")
    Call<CouponsBean> coupon(@Field("token") String token,
                             @Field("merchant_id") String merchant_id);

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
     * 累计收入
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("account/income")
    Call<IncomeBean> income(@Field("token") String token,
                            @Field("shopid") String shopid,
                            @Field("starttime") int starttime,
                            @Field("endtime") int endtime
    );

    /**
     * 报表=我的返佣
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/rebate/myrebate")
    Call<CommissionReportBean> myrebate(@Field("token") String token,
                                        @Field("shopid") String shopid,
                                        @Field("starttime") int starttime,
                                        @Field("endtime") int endtime
    );

    /**
     * 添加优惠券
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("coupon/add")
    Call<Object> addcoupon(@Field("token") String token,
                           @Field("type") String type,
                           @Field("name") String name,
                           @Field("merchant_id") String merchant_id,
                           @Field("term") String term,
                           @Field("reduce") String reduce,
                           @Field("total") String total,
                           @Field("start") int start,
                           @Field("end") int end
    );

    /**
     * 优惠券分析接口
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("coupon/analysecoupon")
    Call<AnalysecouponBean> analysecoupon(@Field("token") String token,
                                          @Field("merchant_id") String merchant_id,
                                          @Field("coupon_id") String coupon_id
    );

    /**
     * 删除暂停或打开活动接口
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("coupon/opencoupon")
    Call<Object> opencoupon(@Field("token") String token,
                            @Field("merchant_id") String merchant_id,
                            @Field("coupon_id") String coupon_id,
                            @Field("state") int state
    );


    /**
     * 威富通刷卡支付(统一支付接口)
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("wft/CreditCardPayment")
    Call<Object> CreditCardPayment(@Field("token") String token,
                                   @Field("auth_code") String auth_code,
                                   @Field("total_fee") String total_fee,
                                   @Field("body") String body,
                                   @Field("mid") String mid,
                                   @Field("account") String account

    );
    /**
     * 富友刷卡支付(统一支付接口)
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/wft/fyPaycard")
    Call<Object> fyPaycard(@Field("token") String token,
                                   @Field("auth_code") String auth_code,
                                   @Field("total_fee") String total_fee,
                                   @Field("body") String body,
                                   @Field("mid") String mid,
                                   @Field("account") String account

    );

    /**
     * 生成微信扫码二维码支付接口
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("wft/WeixinSweepPayment")
    Call<Object> WeixinSweepPayment(@Field("token") String token,
                                    @Field("body") String body,
                                    @Field("mid") String mid,
                                    @Field("total_fee") String total_fee
    );

    /**
     * 生成支付宝二维码支付接口
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("wft/AlipaySweepPayment")
    Call<Object> AlipaySweepPayment(@Field("token") String token,
                                    @Field("body") String body,
                                    @Field("mid") String mid,
                                    @Field("total_fee") String total_fee
    );
    /**
     * 富有扫码支付接口
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/wft/fyPayscan")
    Call<Object> fyPayscan(@Field("token") String token,
                                    @Field("body") String body,
                                    @Field("mid") String mid,
                                    @Field("order_type") String order_type,
                                    @Field("total_fee") String total_fee
    );

    /**
     * 自媒体
     *
     * @param
     * @return
     */
    @GET("system/selfmedia")
    Call<SinceMediaBean> selfmedia();

    /**
     * 获取签约信息
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("merchant/getcontractrate")
    Call<Object> getcontractrate(@Field("token") String token,
                                 @Field("merchant_id") String merchant_id
    );

    /**
     * 获取店铺基本信息
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("merchant/basicinfo")
    Call<Object> basicinfo(@Field("token") String token,
                           @Field("merchant_id") String merchant_id
    );
    /**
     * 2.0获取店铺基本信息
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/shop/shopdetails")
    Call<Object> shopdetails(@Field("token") String token,
                           @Field("shop_id") String shop_id
    );
}