package com.fuleme.business.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.R;


public class LoadingDialogUtils {

    public static Dialog createLoadingDialog(Context context, String msg
    ) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        Dialog progressDialog;
        progressDialog = new Dialog(context, R.style.MyDialogStyle);
        progressDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView amsg = (TextView) progressDialog.findViewById(R.id.tipTextView);
        amsg.setText(msg);
//        amsg.setText("加载中...");
        progressDialog.show();
        return progressDialog;
    }


    /**
     * 关闭dialog
     * <p>
     * http://blog.csdn.net/qq_21376985
     *
     * @param mDialogUtils
     */
    public static void closeDialog(Dialog mDialogUtils) {
        if (mDialogUtils != null && mDialogUtils.isShowing()) {
            mDialogUtils.dismiss();
        }
    }
//    public static Dialog createLoadingDialog(Context context, String msg
//            ,boolean type_system_alert
//    ) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
//        LinearLayout layout = (LinearLayout) v
//                .findViewById(R.id.dialog_loading_view);// 加载布局
//        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
//        tipTextView.setText(msg);// 设置加载信息
//
//        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
//
//        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
//        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
//        if(type_system_alert){
//            loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
//            loadingDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        }else{
//            loadingDialog.setCancelable(false); // 是否可以按“返回键”消失
//        }
//
//        /**
//         *将显示Dialog的方法封装在这里面
//         */
//        Window window = loadingDialog.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setGravity(Gravity.CENTER);
//        window.setAttributes(lp);
//        window.setWindowAnimations(R.style.PopWindowAnimStyle);
//        loadingDialog.show();
//
//        return loadingDialog;
//    }
//
//
//
//    /**
//     * 关闭dialog
//     * <p>
//     * http://blog.csdn.net/qq_21376985
//     *
//     * @param mDialogUtils
//     */
//    public static void closeDialog(Dialog mDialogUtils) {
//        if (mDialogUtils != null && mDialogUtils.isShowing()) {
//            mDialogUtils.dismiss();
//        }
//    }

}