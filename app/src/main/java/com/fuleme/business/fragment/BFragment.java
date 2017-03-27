package com.fuleme.business.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.EmployeeCollectionActivity;
import com.fuleme.business.activity.ScanReceiptActivity;
import com.fuleme.business.activity.StoreAggregationQueryActivity;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.Zxing;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;

public class BFragment extends Fragment {
    private static final String TAG = "BFragment";
    FrameLayout flSaveImage;
    TextView tv_bt_save, tv_storeName;
    Button btn_employee_1;
    Button btn_employee_2;
    private Handler mHandler = new Handler();
    View view = null;
    LinearLayout ll_store;
    final int TOSTORE = 998;
    ImageView ivBaQrCode;

//    public static String storeID = App.PLACEHOLDER;//查询店铺ID·初始为占位符，表示全部店铺
//    public static String storeName = App.PLACEHOLDER;//storeName·初始为占位符

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (App.login_type == App.LOGIN_TYPE_ADMIN) {
            // TODO 管理员登录
            view = inflater.inflate(R.layout.fragment_administrator_b, container, false);
            view.findViewById(R.id.ll_title).setVisibility(View.GONE);

            initAdminView();
        } else if (App.login_type == App.LOGIN_TYPE_EMPLOYEES) {
            // TODO 员工登录
            view = inflater.inflate(R.layout.fragment_employee_b, container, false);
            initEmployeeView();
        }


        ButterKnife.bind(this, view);
        return view;
    }

    private void saveView() {
        // 获取图片某布局

        flSaveImage.setDrawingCacheEnabled(true);
        flSaveImage.buildDrawingCache();

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 要在运行在子线程中
                final Bitmap bmp = flSaveImage.getDrawingCache(); // 获取图片

                savePicture(bmp, System.currentTimeMillis() + ".jpg");// 保存图片
                flSaveImage.destroyDrawingCache(); // 保存过后释放资源
            }
        }, 0);
    }


    public void savePicture(Bitmap bm, String fileName) {
        String imageurl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fuleme";
        if (bm == null) {
            Toast.makeText(getActivity(), "savePicture null !", Toast.LENGTH_SHORT).show();
            return;
        }
        File foder = new File(imageurl);
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(foder, fileName);
        try {
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(imageurl)) {
            Toast.makeText(getActivity(), "保存成功!图片路径:" + imageurl, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(myCaptureFile);
            intent.setData(uri);
            getActivity().sendBroadcast(intent);

            getActivity().sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/fuleme/" + fileName)))
            );


        } else {
            Toast.makeText(getActivity(), "保存失败!", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * 管理员页面
     */
    public void initAdminView() {
        //初始化
        flSaveImage = (FrameLayout) view.findViewById(R.id.fl_save_image);
        tv_bt_save = (TextView) view.findViewById(R.id.tv_bt_save);
        ll_store = (LinearLayout) view.findViewById(R.id.ll_store);
        tv_storeName = (TextView) view.findViewById(R.id.tv_storeName);
        ivBaQrCode = (ImageView) view.findViewById(R.id.iv_ba_qr_code);
        //店名
        if ("0".equals(App.short_state)) {
            tv_storeName.setText(App.merchant + "(审核中)");
        } else if ("1".equals(App.short_state)) {
            tv_storeName.setText(App.merchant + "(已审核)");
        } else {
            tv_storeName.setText("请选择店铺");
        }
        //生成二维码
        LogUtil.i("生成二维码，店铺名" + App.merchant + "店铺ID" + App.short_id);
        if (!TextUtils.isEmpty(App.qrcode)) {
            ivBaQrCode.setImageBitmap(Zxing.getQrCode(App.qrcode));
        }
        //保存二维码
        tv_bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveView();
            }
        });
        //切换店铺
        ll_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查询店铺
                StoreAggregationQueryActivity.intentType = StoreAggregationQueryActivity.BFRAGMENT;
                Intent intent = new Intent(getActivity(), StoreAggregationQueryActivity.class);
                startActivityForResult(intent, TOSTORE);
            }
        });
    }

    /**
     * 员工页面
     */
    public void initEmployeeView() {
        btn_employee_1 = (Button) view.findViewById(R.id.btn_employee_1);
        btn_employee_2 = (Button) view.findViewById(R.id.btn_employee_2);
        btn_employee_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScanReceiptActivity.class));
            }
        });
        btn_employee_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EmployeeCollectionActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOSTORE) {
            if ("0".equals(App.short_state)) {
                tv_storeName.setText(App.merchant + "(审核中)");
            } else if ("1".equals(App.short_state)) {
                tv_storeName.setText(App.merchant + "(已审核)");
            } else {
                tv_storeName.setText("暂无店铺");
            }

            if (!TextUtils.isEmpty(App.qrcode)) {
                LogUtil.i("生成二维码，店铺名" + App.merchant + "店铺ID" + App.short_id);
                ivBaQrCode.setImageBitmap(Zxing.getQrCode(App.qrcode));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
