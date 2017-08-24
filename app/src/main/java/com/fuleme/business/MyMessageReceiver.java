package com.fuleme.business;

import android.content.Context;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DateUtil;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.SharedPreferencesUtils;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.utils.TtsUtil;

import java.util.Map;

import static com.fuleme.business.fragment.CFragment.Print_Ex;

/**
 * 阿里推送
 * Created by Administrator on 2017/3/15.
 */

public class MyMessageReceiver extends MessageReceiver {

    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";

    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        //  处理推送通知
        LogUtil.e("MyMessageReceiver", "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
        if ("100".equals(extraMap.get("type"))) {
            if (App.bindYY) {
                TtsUtil.play("收到" + extraMap.get("total_fee") + "元");
            }
            if (App.bindPrinter) {
                Print_Ex(extraMap.get("short_name"), extraMap.get("total_fee") + "元", DateUtil.stampToDate(extraMap.get("time_end"), DateUtil.DATE_1), extraMap.get("out_trade_no"));
            }
            if (App.POS) {
                App.startPosService(extraMap.get("short_name"), extraMap.get("total_fee") + "元", DateUtil.stampToDate(extraMap.get("time_end"), DateUtil.DATE_1), extraMap.get("out_trade_no"));
            }
        }else if ("200".equals(extraMap.get("type"))) {
            App.is_agent = "1";
            SharedPreferencesUtils.setParam(context, "is_agent", App.is_agent);
        }
    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        LogUtil.e("MyMessageReceiver", "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        LogUtil.e("MyMessageReceiver", "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        String type = GsonUtils.getStringV(extraMap, "type");
        String total_fee = GsonUtils.getStringV(extraMap, "total_fee");
        String out_trade_no = GsonUtils.getStringV(extraMap, "out_trade_no");
        if ("100".equals(type)) {
            App.NoticeDialog(context, total_fee, out_trade_no);
        }
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        LogUtil.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        LogUtil.e("MyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
        if ("100".equals(extraMap.get("type"))) {
            App.NoticeDialog(context, extraMap.get("total_fee"), extraMap.get("out_trade_no"));
            if (App.bindYY) {
                TtsUtil.play("收到" + extraMap.get("total_fee") + "元");
            }
            if (App.bindPrinter) {
                Print_Ex(extraMap.get("short_name"), extraMap.get("total_fee") + "元", DateUtil.stampToDate(extraMap.get("time_end"), DateUtil.DATE_1), extraMap.get("out_trade_no"));
            }
            if (App.POS) {
                App.startPosService(extraMap.get("short_name"), extraMap.get("total_fee") + "元", DateUtil.stampToDate(extraMap.get("time_end"), DateUtil.DATE_1), extraMap.get("out_trade_no"));
            }
        } else if ("200".equals(extraMap.get("type"))) {
            App.is_agent = "1";
            SharedPreferencesUtils.setParam(context, "is_agent", App.is_agent);
            ToastUtil.showMessage("代理开通成功");
        }

    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        LogUtil.e("MyMessageReceiver", "onNotificationRemoved");
    }
}
