package com.fuleme.business.utils;

/**
 * Created by Administrator on 2017/3/27.
 */

public class NumberUtils {


    public static int StringToAmount(String string) {
        return (new Float(Float.parseFloat(string) * 100)).intValue();
    }
}
