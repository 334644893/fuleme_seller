package com.fuleme.business.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.fuleme.business.utils.LogUtil;


/**
 * Created by Administrator on 2017/4/15.
 */

public class ScrollWebView extends WebView {
    public OnScrollChangeListener listener;

    public ScrollWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);

//        float webcontent = getContentHeight() * getScale();// webview的高度
//        float webnow = getHeight() + getScrollY();// 当前webview的高度
//        LogUtil.i("TAG1", "webview.getScrollY()====>>" + getScrollY());
//        if (listener != null) {
//
//            if (Math.abs(webcontent - webnow) < 1) {
//                 已经处于底端
//                LogUtil.i("TAG1", "已经处于底端");
//                listener.onPageEnd(l, t, oldl, oldt);
//            } else if (getScrollY() == 0) {
//                LogUtil.i("TAG1", "已经处于顶端");
//                listener.onPageTop(l, t, oldl, oldt);
//
//            } else {
//                listener.onScrollChanged(l, t, oldl, oldt);
//
//            }
//        }
    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener) {

        this.listener = listener;

    }

    public interface OnScrollChangeListener {
        public void onPageEnd(int l, int t, int oldl, int oldt);

        public void onPageTop(int l, int t, int oldl, int oldt);

        public void onScrollChanged(int l, int t, int oldl, int oldt);

    }

}