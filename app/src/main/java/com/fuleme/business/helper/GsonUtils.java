package com.fuleme.business.helper;

import com.fuleme.business.utils.LogUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/2/27.
 * error_code  错误码
 * data 数据集
 */

public class GsonUtils {
    public static final int SUCCESSFUL = 200;
    //    public static final String SUCCESSFUL="0";
    private static final String ERROR_CODE = "error_code";
    private static final String DATA = "data";
    private static final String ERRMSG = "errmsg";
    private static Gson gson = new Gson();

    public static JSONObject getResultData(Object responseBody) {
        JSONObject result = null;
        JSONObject resultdata = null;

        String json = gson.toJson(responseBody);
        try {
            result = new JSONObject(json);//转换为JSONObject
            resultdata = result.getJSONObject(DATA);//获取JSONArray

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultdata;
    }

    public static String getErrmsg(Object responseBody) {
        String Errmsg = "";

        try {
            Errmsg = new JSONObject(gson.toJson(responseBody)).optString(ERRMSG);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Errmsg;

    }

    public static int getError_code(Object responseBody) {
        int resultError_code = 0;

        try {
            resultError_code = new JSONObject(gson.toJson(responseBody)).optInt(ERROR_CODE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultError_code;

    }


}
