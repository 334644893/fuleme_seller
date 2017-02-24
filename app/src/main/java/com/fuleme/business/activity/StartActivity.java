package com.fuleme.business.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 启动页面
 */
public class StartActivity extends BaseActivity {
    private static final String TAG = "StartActivity";
    private static final long TIME = 5000;
    ObjectAnimator animator;
    @Bind(R.id.activity_start)
    LinearLayout activityStart;
    private Handler mHandler = new Handler() {
        //handleMessage函数对接收到的消息进行处理
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 0:
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    finish();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        alphaAnimator(activityStart);
        Message message = new Message();
        message.what = 0;
        mHandler.sendMessageDelayed(message, TIME);
    }

    /**
     * alpha动画
     *
     * @param activity
     */
    private void alphaAnimator(LinearLayout activity) {
        animator = ObjectAnimator.ofFloat(activity, "alpha", 0.1f, 1f);
        animator.setDuration(TIME).start();


    }
}
