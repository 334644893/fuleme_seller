package com.fuleme.business.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.APIService;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.utils.Zxing;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收款码页面
 */
public class EmployeeCollectionActivity extends BaseActivity {


    @Bind(R.id.fl_save_image)
    FrameLayout flSaveImage;
    @Bind(R.id.tv_storeName)
    TextView tvStoreName;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.iv_ba_qr_code)
    ImageView ivBaQrCode;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.short_logo)
    SimpleDraweeView shortLogo;
    private Handler mHandler = new Handler();
    private Context context;
    final int TOSTORE = 998;
    public static String short_id = "";
    public static String name = "";
    public static String short_state = "";
    public static String qrcode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_collection);
        ButterKnife.bind(this);
        context = EmployeeCollectionActivity.this;
        tvTitle.setText("收款码");
        initView();
    }

    public void initView() {
        //店名
        if ("0".equals(short_state)) {
            tvStoreName.setText(name + "(审核中)");
            tvName.setText(name);
        } else if ("1".equals(short_state)) {
            tvStoreName.setText(name + "(已审核)");
            tvName.setText(name);
        } else {
            tvStoreName.setText("请选择店铺");
            tvName.setText("");
        }
        //生成二维码
        if (!TextUtils.isEmpty(qrcode)) {
            ivBaQrCode.setImageBitmap(Zxing.getQrCode(qrcode));
        }
        //添加商户logo

        if (!TextUtils.isEmpty(App.short_logo)) {
            shortLogo.setImageURI(APIService.SERVER_IP + App.short_logo);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOSTORE) {
            initView();

        }
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
            Toast.makeText(context, "savePicture null !", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, "保存成功!图片路径:" + imageurl, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(myCaptureFile);
            intent.setData(uri);
            context.sendBroadcast(intent);

            context.sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/fuleme/" + fileName)))
            );


        } else {
            Toast.makeText(context, "保存失败!", Toast.LENGTH_SHORT).show();
        }


    }

    @OnClick({R.id.tv_left, R.id.ll_store, R.id.tv_bt_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_store:
                //查询店铺
                if ("0".equals(App.role)) {
                    StoreAggregationQueryActivity.intentType = StoreAggregationQueryActivity.BFRAGMENT;
                    Intent intent = new Intent(context, StoreAggregationQueryActivity.class);
                    startActivityForResult(intent, TOSTORE);
                }
                break;
            case R.id.tv_bt_save:
                if (TextUtils.isEmpty(App.short_id)) {
                    ToastUtil.showMessage(R.string.nostore);
                } else {
                    saveView();
                }
                break;
        }
    }

}
