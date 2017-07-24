package com.fuleme.business.activity.Version2;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.CustomDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ContactServiceActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_lxr)
    TextView tvLxr;
    @Bind(R.id.tv_lxfs)
    TextView tvLxfs;
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 898;
    String number = "";
    String dialogTitle = "";
    String dialogcontent = "";
    final int LXKF = 0;
    final int LXFS = 1;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_service);
        ButterKnife.bind(this);
        tvTitle.setText("联系服务商");
    }

    @OnClick({R.id.tv_left, R.id.ll_lxkf, R.id.ll_lxfs})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_lxkf:
                flag = LXKF;
                checkPermissions();
                break;
            case R.id.ll_lxfs:
                flag = LXFS;
                checkPermissions();
                break;
        }
    }

    private void checkPermissions() {
        // 检查是否获得了权限（Android6.0运行时权限）
        if (ContextCompat.checkSelfPermission(ContactServiceActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(ContactServiceActivity.this,
                    Manifest.permission.CALL_PHONE)) {
                // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                // 弹窗需要解释为何需要该权限，再次请求授权
                ToastUtil.showMessage("请授权！");

                // 帮跳转到该应用的设置界面，让用户手动授权
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            } else {
                // 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions(ContactServiceActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        } else {
            // 已经获得授权，可以打电话
            CallPhone();
        }
    }

    // 处理权限申请的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功，继续打电话
                    CallPhone();
                } else {
                    // 授权失败！
                    ToastUtil.showMessage("授权失败！");
                }
                break;
            }
        }

    }

    private CustomDialog dialog;

    private void CallPhone() {
        if (flag == LXKF) {
            number = "02787376530";
            dialogTitle = "联系我们";
            dialogcontent = "是否现在拨打客服电话";
        } else if (flag == LXFS) {
            number = tvLxfs.getText().toString();
            dialogTitle = "联系推荐人";
            dialogcontent = "是否现在拨打推荐人电话";
        }
        // 联系我们
        CustomDialog.Builder customBuilder = new
                CustomDialog.Builder(ContactServiceActivity.this);
        customBuilder
                .setTitle(dialogTitle)
                .setMessage(dialogcontent + "\n" + number + "?")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 拨号：激活系统的拨号组件
                        Intent intent = new Intent(); // 意图对象：动作 + 数据
                        intent.setAction(Intent.ACTION_CALL); // 设置动作
                        Uri data = Uri.parse("tel:" + number); // 设置数据
                        intent.setData(data);
                        startActivity(intent); // 激活Activity组件
                    }
                })
                .setPositiveButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
        dialog = customBuilder.create();
        dialog.show();
    }

}
