package com.fuleme.business.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.utils.Zxing;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 员工收款-收款码页面
 */
public class EmployeeCollectionActivity extends BaseActivity {
    @Bind(R.id.fl_save_image)
    FrameLayout flSaveImage;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    ////    public static String storeID = App.PLACEHOLDER;//查询店铺ID·初始为占位符，表示全部店铺
//    public static String storeName = App.PLACEHOLDER;//storeName·初始为占位符
    @Bind(R.id.tv_storeName)
    TextView tvStoreName;
    @Bind(R.id.iv_ba_qr_code)
    ImageView ivBaQrCode;
    @Bind(R.id.tv_text)
    TextView tvText;
    @Bind(R.id.tv_bt_save)
    TextView tvBtSave;
    @Bind(R.id.tv_name)
    TextView tvName;
    private Handler mHandler = new Handler();

    @OnClick({R.id.tv_left})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tv_bt_save:
//                saveView();
//                break;
            case R.id.tv_left:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_administrator_b);
        ButterKnife.bind(this);
        tvTitle.setText("收款码");
        initView();
    }

    public void initView() {
        tvText.setVisibility(View.GONE);
        tvBtSave.setVisibility(View.GONE);
        if (!App.PLACEHOLDER.equals(App.short_id)) {
            //店名
            tvStoreName.setText(App.merchant);
            tvName.setText(App.merchant);
        }
//        生成二维码
        ivBaQrCode.setImageBitmap(Zxing.getQrCode(App.qrcode));

    }


//    private void saveView() {
//        // 获取图片某布局
//
//        flSaveImage.setDrawingCacheEnabled(true);
//        flSaveImage.buildDrawingCache();
//
//        mHandler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                // 要在运行在子线程中
//                final Bitmap bmp = flSaveImage.getDrawingCache(); // 获取图片
//
//                savePicture(bmp, System.currentTimeMillis() + ".jpg");// 保存图片
//                flSaveImage.destroyDrawingCache(); // 保存过后释放资源
//            }
//        }, 0);
//    }

//    public void savePicture(Bitmap bm, String fileName) {
//        String imageurl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fuleme";
//        if (bm == null) {
//            Toast.makeText(EmployeeCollectionActivity.this, "savePicture null !", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        File foder = new File(imageurl);
//        if (!foder.exists()) {
//            foder.mkdirs();
//        }
//        File myCaptureFile = new File(foder, fileName);
//        try {
//            if (!myCaptureFile.exists()) {
//                myCaptureFile.createNewFile();
//            }
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
//            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
//            bos.flush();
//            bos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (!TextUtils.isEmpty(imageurl)) {
//            Toast.makeText(EmployeeCollectionActivity.this, "保存成功!图片路径:" + imageurl, Toast.LENGTH_SHORT).show();
//
//
//            EmployeeCollectionActivity.this.sendBroadcast(
//                    new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/fuleme/" + fileName)))
//            );
//
//
//        } else {
//            Toast.makeText(EmployeeCollectionActivity.this, "保存失败!", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }
}
