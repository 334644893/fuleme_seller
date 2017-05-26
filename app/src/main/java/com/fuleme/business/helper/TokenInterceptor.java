package com.fuleme.business.helper;
import com.fuleme.business.App;
import com.fuleme.business.utils.DateUtil;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.SharedPreferencesUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/3/28.
 */

public class TokenInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String errcode = "300";//TOKEN过期时的code
    Calendar cal = Calendar.getInstance();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        //获取自定义code
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        String bodyString = buffer.clone().readString(charset);
        String code = GsonUtils.getStringV(bodyString, GsonUtils.ERROR_CODE);
        LogUtil.d("errcode---------->" + code);
        if (errcode.equals(code)) {
            //根据和服务端的约定判断token过期
            //同步请求方式，获取最新的Token
            final String username = SharedPreferencesUtils.getParam(App.getInstance(), "phone", "") + "";
            final String password = SharedPreferencesUtils.getParam(App.getInstance(), "token_password", "") + "";
            App.login_type = (int) SharedPreferencesUtils.getParam(App.getInstance(), "login_type", App.login_type);
            Call<Object> weather = App.getInstance().getTokenAPIService().login(
                    username,
                    password,
                    App.login_type);
            retrofit2.Response<Object> execute = weather.execute();
            App.token = GsonUtils.getResultData(execute.body()).optString("token");
            SharedPreferencesUtils.setParam(App.getInstance(), "token", App.token);
            //使用新的Token，创建新的请求
            // create a new request and modify it accordingly using the new token
            FormBody.Builder newFormBody = new FormBody.Builder();
            FormBody oidFormBody = (FormBody) request.body();
            for (int i = 0; i < oidFormBody.size(); i++) {
                if ("token".equals(oidFormBody.encodedName(i))) {
                    newFormBody.addEncoded(oidFormBody.encodedName(i), App.token);
                } else {
                    newFormBody.addEncoded(oidFormBody.encodedName(i), oidFormBody.encodedValue(i));
                }
            }
            Request newRequest = chain.request()
                    .newBuilder()
                    .method(request.method(), newFormBody.build())
                    .build();

//            LogUtil.d("-------再次请求的token---------", App.token + "时间" + cal.getTimeInMillis());
            response = chain.proceed(newRequest);
            //log
            ResponseBody responseBody1 = response.body();
            BufferedSource source1 = responseBody1.source();
            source1.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer1 = source1.buffer();
            Charset charset1 = UTF8;
            MediaType contentType1 = responseBody1.contentType();
            if (contentType != null) {
                charset1 = contentType1.charset(UTF8);
            }
            String bodyString1 = buffer1.clone().readString(charset1);
            LogUtil.d("时间----------->" + DateUtil.stampToDate(cal.getTimeInMillis()/1000+"",DateUtil.DATE_1));
            LogUtil.d("request---------->" + request);
            LogUtil.d("数据集---------->" + bodyString1);
            return response;
        }
        //log
        ResponseBody responseBody1 = response.body();
        BufferedSource source1 = responseBody1.source();
        source1.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer1 = source1.buffer();
        Charset charset1 = UTF8;
        MediaType contentType1 = responseBody1.contentType();
        if (contentType != null) {
            charset1 = contentType1.charset(UTF8);
        }
        String bodyString1 = buffer1.clone().readString(charset1);
        LogUtil.d("时间----------->" + DateUtil.stampToDate(cal.getTimeInMillis()/1000+"",DateUtil.DATE_1));
        LogUtil.d("request---------->" + request);
        LogUtil.d("数据集---------->" + bodyString1);
        return response;
    }

//    /**
//     * 根据Response，判断Token是否失效
//     *
//     * @param response
//     * @return
//     */
//    private boolean isTokenExpired(Response response) {
//
//        if (errcode.equals(code)) {
//            return true;
//        }
//        return false;
//    }

//    /**
//     * 同步请求方式，获取最新的Token
//     *
//     * @return
//     */
//    private String getNewToken() throws IOException {
//        // 通过一个特定的接口获取新的token，此处要用到同步的retrofit请求
//        final String username = SharedPreferencesUtils.getParam(App.getInstance(), "phone", "") + "";
//        final String password = SharedPreferencesUtils.getParam(App.getInstance(), "token_password", "") + "";
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Call<Object> weather = App.getInstance().getServerApi().login(
//                        username,
//                        password,
//                        App.login_type);
//                try {
//                    retrofit2.Response<Object> execute = weather.execute();
//                        LogUtil.d("------------body:" + GsonUtils.getResultData(execute.body()).optString("token"));
//                    return GsonUtils.getResultData(callexecute.body()).optString("token");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
////        Call<Object> call = App.getInstance().getServerApi().login(
////                username,
////                password,
////                App.login_type);
////        try {
////            retrofit2.Response<Object> callexecute = call.execute();
////            LogUtil.d("------------body:" + GsonUtils.getResultData(callexecute.body()).optString("token"));
////            return GsonUtils.getResultData(callexecute.body()).optString("token");
////        } catch (IOException e) {
////            e.printStackTrace();
////            return "";
////        }
////        ToastUtil.showMessage(execute.body()+"");
////        LogUtil.d("dsfsdfsdfsfd",GsonUtils.getResultData(execute.body()).optString("token"));
////        return GsonUtils.getResultData(execute.body()).optString("token");
//
//
//    }
}