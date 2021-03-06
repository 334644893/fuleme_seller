/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */package com.fuleme.business.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ZebraRunCustomer 日志打印工具类
 * Created by fm on 2016/4/13.
 */
public class LogUtil {

    public static boolean isPrint = true;
    private static boolean isDebug = false;

    public static final String TAG = "fuleme";
    public static final String MSG = "log msg is null.";

    private static List<String> logList;

    static {
        //isPrint = WKApplication.getInstance().getLoggingSwitch();
    }

    public static void v(String tag, String msg) {
        print(Log.VERBOSE, tag, msg);
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void d(String tag, String msg) {
        print(Log.DEBUG, tag, msg);
        print(isDebug, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void i(String tag, String msg) {
        print(Log.INFO, tag, msg);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void w(String tag, String msg) {
        print(Log.WARN, tag, msg);
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void e(String tag, String msg) {
        print(Log.ERROR, tag, msg);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    private static void print(int mode, final String tag, String msg) {
        if (!isPrint) {
            return;
        }
        if (msg == null) {
            Log.e(tag, MSG);
            return;
        }
        switch (mode) {
            case Log.VERBOSE:
                Log.v(tag, msg);
                break;
            case Log.DEBUG:
                Log.d(tag, msg);
                break;
            case Log.INFO:
                Log.i(tag, msg);
                break;
            case Log.WARN:
                Log.w(tag, msg);
                break;
            case Log.ERROR:
                Log.e(tag, msg);
                break;
            default:
                Log.d(tag, msg);
                break;
        }
    }

    private static void print(boolean flag, String msg) {
        if (flag && logList != null) {
            logList.add(msg);
        }
    }

    public static void setState(boolean flag) {
        if (flag) {
            if (logList == null) {
                logList = new ArrayList<String>();
            } else {
                logList.clear();
            }
        } else {
            if (logList != null) {
                logList.clear();
                logList = null;
            }
        }

        isDebug = flag;
    }

    public static void printFile(InputStream is, String path) {
        FileOutputStream fos = null;
        byte[] temp = null;
        try {
            if (isPrint) {
                File file = new File(path);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                temp = new byte[1024];
                int i = 0;
                while ((i = is.read(temp)) > -1) {
                    if (i < temp.length) {
                        byte[] b = new byte[i];
                        System.arraycopy(temp, 0, b, 0, b.length);
                        fos.write(b);
                    } else {
                        fos.write(temp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                is = null;
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
            }
            temp = null;
        }
    }

    public static void printErrorStackTrace(Exception ex) {
        File f = new File("/sdcard/Hisun/error.log");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            ex.printStackTrace(new PrintStream(f));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  String getLogUtilsTag(Class<? extends Object> clazz) {
        return LogUtil.TAG + "." + clazz.getSimpleName();
    }
}
