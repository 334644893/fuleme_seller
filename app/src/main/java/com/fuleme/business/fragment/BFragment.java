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
import android.widget.TextView;
import android.widget.Toast;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.EmployeeCollectionActivity;
import com.fuleme.business.activity.ScanReceiptActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BFragment extends Fragment {
    FrameLayout flSaveImage;
    TextView tv_bt_save;
    Button btn_employee_1;
    Button btn_employee_2;
    private Handler mHandler = new Handler();
    View view = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (App.login_type == App.LOGIN_TYPE_ADMIN) {
            // TODO 管理员登录
            view = inflater.inflate(R.layout.fragment_administrator_b, container, false);
            initAdminView();
        } else if (App.login_type == App.LOGIN_TYPE_EMPLOYEES) {
            // TODO 员工登录
            view = inflater.inflate(R.layout.fragment_employee_b, container, false);
            initEmployeeView();
        }


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

        flSaveImage=(FrameLayout) view.findViewById(R.id.fl_save_image);
        tv_bt_save=(TextView) view.findViewById(R.id.tv_bt_save);
        tv_bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveView();
            }
        });
    }

    /**
     * 员工页面
     */
    public void initEmployeeView() {
        btn_employee_1=(Button) view.findViewById(R.id.btn_employee_1);
        btn_employee_2=(Button) view.findViewById(R.id.btn_employee_2);
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

}
