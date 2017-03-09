package com.fuleme.business.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fuleme.business.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 员工收款-收款码页面
 */
public class EmployeeCollectionActivity extends AppCompatActivity {
    @Bind(R.id.fl_save_image)
    FrameLayout flSaveImage;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    private Handler mHandler = new Handler();

    @OnClick({R.id.tv_left, R.id.tv_bt_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_bt_save:
                saveView();
                break;
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
            Toast.makeText(EmployeeCollectionActivity.this, "savePicture null !", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(EmployeeCollectionActivity.this, "保存成功!图片路径:" + imageurl, Toast.LENGTH_SHORT).show();


            EmployeeCollectionActivity.this.sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/fuleme/" + fileName)))
            );


        } else {
            Toast.makeText(EmployeeCollectionActivity.this, "保存失败!", Toast.LENGTH_SHORT).show();
        }


    }
}
