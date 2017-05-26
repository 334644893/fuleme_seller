package com.fuleme.business.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.AboutUsActivity;
import com.fuleme.business.activity.BasicInformationActivity;
import com.fuleme.business.activity.BusinessApplicationActivity;
import com.fuleme.business.activity.ClerkManagementActivity;
import com.fuleme.business.activity.ContractrateActivity;
import com.fuleme.business.activity.EmployeeCollectionActivity;
import com.fuleme.business.activity.LoginActivity;
import com.fuleme.business.activity.Printer.BluetoothService;
import com.fuleme.business.activity.Printer.Command;
import com.fuleme.business.activity.Printer.DeviceListActivity;
import com.fuleme.business.activity.Printer.PrinterCommand;
import com.fuleme.business.activity.RegistrationStoreActivity;
import com.fuleme.business.activity.UserDetailsActivity;
import com.fuleme.business.download.DeviceUtils;
import com.fuleme.business.helper.APIService;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.SharedPreferencesUtils;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.CustomDialog;

import java.sql.Date;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.alibaba.sdk.android.ams.common.global.AmsGlobalHolder.getPackageName;
import static com.fuleme.business.fragment.FragmentActivity.imgurlFlag;

/**
 * 我的
 */
public class CFragment extends Fragment {
    private static final String TAG = "CFragment";
    @Bind(R.id.iv_btm_yy)
    ImageView ivBtmYY;
    @Bind(R.id.iv_btm_tongzhi)
    ImageView ivBtmTongzhi;
    @Bind(R.id.iv_btm_dayinji)
    ImageView ivBtmDayinji;
    final int EXIT_USERDETAIL = 100;//退出
    final int EXIT_TO_USERDETAIL = 101;
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 898;
    final String number = "02787376530";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;
    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_fulemenumber)
    TextView tvFulemenumber;
    @Bind(R.id.ll_addstore)
    LinearLayout llAddstore;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.ll_title_1)
    LinearLayout llTitle1;
    @Bind(R.id.ll_title_2)
    LinearLayout llTitle2;
    @Bind(R.id.ll_title_3)
    LinearLayout llTitle3;
    @Bind(R.id.logo)
    SimpleDraweeView logo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_administrator_c, container, false);

        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void initView() {
        //改变头像
        if (!TextUtils.isEmpty(App.short_logo)) {
            logo.setImageURI(APIService.SERVER_IP + App.short_logo);
        }
        tvPhone.setText(App.phone);
        tvFulemenumber.setText(App.username);
        tvVersion.setText("当前版本:" + DeviceUtils.getVersionName(getActivity()));
        if (App.bindAccount) {
            ivBtmTongzhi.setImageResource(R.mipmap.icon_on);
        } else {
            ivBtmTongzhi.setImageResource(R.mipmap.icon_off);
        }
        if (App.bindYY) {
            ivBtmYY.setImageResource(R.mipmap.icon_on);
        } else {
            ivBtmYY.setImageResource(R.mipmap.icon_off);
        }
        //根据登录类型显示隐藏员工管理
        if ("2".equals(App.role)) {
            llTitle2.setVisibility(View.GONE);
        } else {
            llTitle2.setVisibility(View.VISIBLE);
        }
        if ("0".equals(App.role)) {
            llTitle1.setVisibility(View.VISIBLE);
            llAddstore.setVisibility(View.VISIBLE);
        } else {
            llTitle1.setVisibility(View.GONE);
            llAddstore.setVisibility(View.GONE);
        }
        //初始化蓝牙
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            ToastUtil.showMessage("蓝牙不可用");
            ivBtmDayinji.setImageResource(R.mipmap.icon_off);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.ll_title_1, R.id.ll_lxwomen, R.id.ll_title_2, R.id.ll_title_3, R.id.ll_addstore, R.id.ll_adskm, R.id.ll_set_zh, R.id.ll_set_s_a, R.id.ll_zhsz, R.id.ll_guanyuwomen, R.id.iv_btm_yy, R.id.iv_btm_tongzhi, R.id.iv_btm_dayinji})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title_1:
                //签约信息
                if (!TextUtils.isEmpty(App.short_id)) {
                    startActivity(new Intent(getActivity(), ContractrateActivity.class));
                } else {
                    ToastUtil.showMessage(getActivity().getResources().getString(R.string.nostore));
                }

                break;
            case R.id.ll_title_2:
                // 店员管理
                if (!TextUtils.isEmpty(App.short_id)) {
                    startActivity(new Intent(getActivity(), ClerkManagementActivity.class));
                } else {
                    ToastUtil.showMessage(getActivity().getResources().getString(R.string.nostore));
                }

                break;
            case R.id.ll_title_3:
                //店铺基本信息
                if (!TextUtils.isEmpty(App.short_id)) {
                    startActivity(new Intent(getActivity(), BasicInformationActivity.class));
                } else {
                    ToastUtil.showMessage(getActivity().getResources().getString(R.string.nostore));
                }

                break;
            case R.id.ll_set_zh:
//                // 账户详情
//                startActivityForResult(new Intent(getActivity(), UserDetailsActivity.class), EXIT_TO_USERDETAIL);
                break;
            case R.id.ll_set_s_a:
                // 跳转商铺应用
                startActivity(new Intent(getActivity(), BusinessApplicationActivity.class));
                break;
            case R.id.ll_addstore:
                // 添加店铺
                startActivity(new Intent(getActivity(), RegistrationStoreActivity.class));
                break;
            case R.id.ll_adskm:
                // 店铺收款码
                startActivity(new Intent(getActivity(), EmployeeCollectionActivity.class));
                break;
            case R.id.ll_zhsz:
                // 账户详情
                startActivityForResult(new Intent(getActivity(), UserDetailsActivity.class), EXIT_TO_USERDETAIL);
                break;
            case R.id.iv_btm_yy:
                //语音播报
                setYY();
                break;
            case R.id.iv_btm_tongzhi:
                //通知
                setBtmTongzhi();
                break;
            case R.id.iv_btm_dayinji:
                if (App.bindPrinter) {
                    //关闭设备
                    mService.stop();
                    OffPrinter();
                } else {
                    //连接设备
                    // If Bluetooth is not on, request that it be enabled.
                    // setupChat() will then be called during onActivityResult
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    } else {
                        if (mService == null) {
                            mService = new BluetoothService(getActivity(), mHandler);
                        }
                        Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                    }
                }
                break;
            case R.id.ll_guanyuwomen:
                // 跳转关于我们
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            case R.id.ll_lxwomen:
                // 检查是否获得了权限（Android6.0运行时权限）
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
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
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }
                } else {
                    // 已经获得授权，可以打电话
                    CallPhone();
                }
                break;
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

        // 联系我们
        CustomDialog.Builder customBuilder = new
                CustomDialog.Builder(getActivity());
        customBuilder
                .setTitle("联系我们")
                .setMessage("是否现在拨打客服电话" + "\n" + number + "?")
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

    private void OffPrinter() {
        ivBtmDayinji.setImageResource(R.mipmap.icon_off);
        App.bindPrinter = false;
    }

    private void OnPrinter() {
        ivBtmDayinji.setImageResource(R.mipmap.icon_on);
        App.bindPrinter = true;
    }

    private void setBtmTongzhi() {
        if (App.bindAccount) {
            ivBtmTongzhi.setImageResource(R.mipmap.icon_off);
            App.unbindAccount();
            ToastUtil.showMessage("通知功能已关闭");
        } else {
            ivBtmTongzhi.setImageResource(R.mipmap.icon_on);
            App.bindAccount();
            ToastUtil.showMessage("通知功能已打开");
        }
    }

    private void setYY() {
        if (App.bindYY) {
            ivBtmYY.setImageResource(R.mipmap.icon_off);
            SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "bindYY", false);
            App.bindYY = false;
            ToastUtil.showMessage("语音功能已关闭");
        } else {
            ivBtmYY.setImageResource(R.mipmap.icon_on);
            SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "bindYY", true);
            App.bindYY = true;
            ToastUtil.showMessage("语音功能已打开");
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        if (mService != null) {

            if (mService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth services
                mService.start();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth services
        if (mService != null)
            mService.stop();
    }

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the services
    public static BluetoothService mService = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EXIT_TO_USERDETAIL && resultCode == EXIT_USERDETAIL) {
            //TODO 清空信息并跳转登录页
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
        //成功上传图片时修改logo
        if (imgurlFlag) {
            imgurlFlag = false;
            //改变头像
            if (!TextUtils.isEmpty(App.short_logo)) {
                logo.setImageURI(APIService.SERVER_IP + App.short_logo);
            }
        }
        if (requestCode == REQUEST_CONNECT_DEVICE) {
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                String address = data.getExtras().getString(
                        DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // Get the BLuetoothDevice object
                if (BluetoothAdapter.checkBluetoothAddress(address)) {
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(address);
                    // Attempt to connect to the device
                    mService.connect(device);
                }
            }
        }
        if (requestCode == REQUEST_ENABLE_BT) {
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a session
                if (mService == null) {
                    mService = new BluetoothService(getActivity(), mHandler);
                }
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

            } else {
                // User did not enable Bluetooth or an error occured
                ToastUtil.showMessage("连接失败");
            }
        }
    }

    /****************************************************************************************************/
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    LogUtil.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            ToastUtil.showMessage("连接成功");
                            Print_ExTest();
                            OnPrinter();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            ToastUtil.showMessage("正在连接…");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            ToastUtil.showMessage("连接失败");
                            OffPrinter();
                            break;
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    ToastUtil.showMessage(msg.getData().getString("Connected to " + msg.getData().getString(DEVICE_NAME)));
                    break;
                case MESSAGE_TOAST:
                    ToastUtil.showMessage(msg.getData().getString(TOAST));
                    break;
                case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    ToastUtil.showMessage(msg.getData().getString("蓝牙已断开连接"));
                    OffPrinter();
                    break;
                case MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    ToastUtil.showMessage(msg.getData().getString("无法连接设备"));
                    OffPrinter();
                    break;
            }
        }
    };

    /*
    *SendDataByte
    */
    public static void SendDataByte(byte[] data) {

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            ToastUtil.showMessage("请先连接蓝牙打印机");
            return;
        }
        mService.write(data);
    }

    private static final String CHINESE = "GBK";

    /**
     * 打印自定义小票
     */
    public static void Print_Ex(String short_name, String amount, String data, String number) {
        SendDataByte(Command.ESC_Init);
        SendDataByte(Command.LF);
        Command.ESC_Align[2] = 0x01;//设置对齐模式
        SendDataByte(Command.ESC_Align);//设置对齐模式
        Command.GS_ExclamationMark[2] = 0x12;//设置字体倍高倍宽
        SendDataByte(Command.GS_ExclamationMark);//设置字体倍高倍宽
        SendDataByte(PrinterCommand.POS_Print_Text(short_name + "\n", CHINESE, 0, 1, 1, 0));
        SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(10));//打印走纸
        Command.ESC_Align[2] = 0x00;
        SendDataByte(Command.ESC_Align);
        Command.GS_ExclamationMark[2] = 0x00;
        SendDataByte(Command.GS_ExclamationMark);
        SendDataByte(PrinterCommand.POS_Print_Text(
                "金额: " + amount + "\n" +
                        "交易日期：" + data + "\n" +
                        "订单号:" + number + "\n", CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));//打印走纸
    }

    /**
     * 打印test
     */
    public static void Print_ExTest() {
        SendDataByte(Command.ESC_Init);
        SendDataByte(Command.LF);
        Command.ESC_Align[2] = 0x01;//设置对齐模式
        SendDataByte(Command.ESC_Align);//设置对齐模式
        Command.GS_ExclamationMark[2] = 0x12;//设置字体倍高倍宽
        SendDataByte(Command.GS_ExclamationMark);//设置字体倍高倍宽
        SendDataByte(PrinterCommand.POS_Print_Text("欢迎使用付了么\n", CHINESE, 0, 1, 1, 0));
        SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(10));//打印走纸
    }
}
