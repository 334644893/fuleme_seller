package com.fuleme.business.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fuleme.business.R;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * Created by Administrator on 2017/2/10.
 */

public class Zxing {
    /**
     * 生成二维码图片
     */
    public static Bitmap getQrCode(String content, Activity activity) {


        return CodeUtils.createImage(content, 400, 400, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher));

    }

    /**
     * 生成不带logo的二维码图片
     */
    public static Bitmap getQrCode(String content) {


        return CodeUtils.createImage(content, 400, 400, null);

    }

    public static Bitmap getQrCode(String content, int h, int w) {


        return CodeUtils.createImage(content, h, w, null);

    }
}

