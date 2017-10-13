package com.fuleme.business.activity.Version2;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.APIService;
import com.fuleme.business.utils.Constant;
import com.fuleme.business.utils.Zxing;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 邀请码
 */
public class InviteCodeActivity extends BaseActivity {
    private static final String TAG = "InviteCodeActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.iv_ba_qr_code)
    ImageView ivBaQrCode;
    @Bind(R.id.logo)
    SimpleDraweeView logo;
    @Bind(R.id.tv_right)
    TextView tvRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("我的推荐码");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("分享");
        tv1.setText(App.invitation_code);
        //生成二维码
        if (!TextUtils.isEmpty(App.invitation_code)) {
            ivBaQrCode.setImageBitmap(Zxing.getQrCode(App.invitation_code));
        }

        //改变头像
        if (!TextUtils.isEmpty(App.short_logo)) {
            logo.setImageURI(APIService.SERVER_IP + App.short_logo);
        }
        tv2.setText(App.username);
    }

    Dialog mCameraDialog;

    @OnClick({R.id.tv_left, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_right:
                mCameraDialog = new Dialog(this, R.style.my_dialog);
                LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.layout_camera_control, null);
                root.findViewById(R.id.btn_open_camera).setOnClickListener(btnlistener);
                root.findViewById(R.id.btn_choose_img).setOnClickListener(btnlistener);
                root.findViewById(R.id.btn_cancel).setOnClickListener(btnlistener);
                mCameraDialog.setContentView(root);
                Window dialogWindow = mCameraDialog.getWindow();
                dialogWindow.setGravity(Gravity.BOTTOM);
                dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
                WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                lp.x = 0; // 新位置X坐标
                lp.y = -20; // 新位置Y坐标
                lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
                //      lp.alpha = 9f; // 透明度
                root.measure(0, 0);
//                lp.height = 500;
                lp.alpha = 9f; // 透明度
                dialogWindow.setAttributes(lp);
                mCameraDialog.show();

                break;

        }
    }

    private View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_open_camera:
                    //微信
                    App.registerWeChat(InviteCodeActivity.this);
                    shear(false, R.drawable.icon);
                    if (mCameraDialog != null) {
                        mCameraDialog.dismiss();
                    }
                    break;

                case R.id.btn_choose_img:
                    //朋友圈
                    App.registerWeChat(InviteCodeActivity.this);
                    shear(true, R.drawable.icon);
                    if (mCameraDialog != null) {
                        mCameraDialog.dismiss();
                    }
                    break;

                case R.id.btn_cancel:
                    // 取消
                    if (mCameraDialog != null) {
                        mCameraDialog.dismiss();
                    }
                    break;
            }
        }
    };
}
