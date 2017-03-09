package com.fuleme.business.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fuleme.business.R;
import com.fuleme.business.adapter.UploadPicturesAdapter;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.PictureUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.LoadingDialogUtils;
import com.yanzhenjie.album.Album;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadPicturesActivity extends BaseActivity {
    private static final String TAG = "UploadPicturesActivity";
    public static String urlImg = "";//营业证显示图片地址
    private static final int IMAGE_NUM = 1;//最多上传数量
    private ArrayList<String> mDatas = new ArrayList<String>();
    private UploadPicturesAdapter mAdapter;
    public int ACTIVITY_REQUEST_SELECT_PHOTO = 1000;
    private Dialog mLoading;
    GridLayoutManager mGridLayoutManager;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.user_avator)
    SimpleDraweeView userAvator;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_right)
    TextView tvRight;

    @OnClick({R.id.tv_left, R.id.tv_right, R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:


                finish();
                break;
            case R.id.tv_right:
                if (mDatas.size() > 1) {
                    userAvator.setImageDrawable(getResources().getDrawable(R.mipmap.logo_de));
                    mDatas.remove(0);
                    mAdapter.notifyDataSetChanged();
                    urlImg = "";
                    RegistrationInformationActivity.url="";
                } else {
                    ToastUtil.showMessage("选张图片呗");
                }
                break;
            case R.id.btn:
                if (mDatas.size() > 1) {
                    uploadMemberIcon(PictureUtil.smallPic(mDatas.get(0)));
                } else {
                    ToastUtil.showMessage("选张图片呗");
                }

                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pictures);
        ButterKnife.bind(this);
        init();
    }

    public void init() {

        initData();
        initView();
        initClick();
    }

    public void initData() {

        mDatas.add("add");//初始化一个加号
        if (!TextUtils.isEmpty(urlImg)&&new File(urlImg).exists()) {
            mDatas.add(0, urlImg);
            userAvator.setImageURI(Uri.fromFile(new File(urlImg)));
        }
    }

    public void initView() {
        tvTitle.setText("上传营业证");
        tvRight.setText("删除");
        tvRight.setVisibility(View.VISIBLE);
        //初始化列表
        mGridLayoutManager = new GridLayoutManager(this, 4);
        recyclerview.setLayoutManager(mGridLayoutManager);
        mAdapter = new UploadPicturesAdapter(UploadPicturesActivity.this, mDatas);
        recyclerview.setAdapter(mAdapter);

    }

    public void initClick() {
        //列表item点击
        mAdapter.setOnItemClickListener(new UploadPicturesAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if ((position + 1) == mDatas.size()) {
                    //点击最后一个
                    if (mDatas.size() <= IMAGE_NUM) {
                        //去选图片
                        Album.startAlbum(UploadPicturesActivity.this, ACTIVITY_REQUEST_SELECT_PHOTO
                                , 1                                                         // 指定选择数量。
                                , ContextCompat.getColor(UploadPicturesActivity.this, R.color.theme)        // 指定Toolbar的颜色。
                                , ContextCompat.getColor(UploadPicturesActivity.this, R.color.theme));  // 指定状态栏的颜色。
                    } else {
                        ToastUtil.showMessage("最多添加" + IMAGE_NUM + "张图片");
                    }

                } else {
                    //点击其余图片
                    userAvator.setImageURI(Uri.fromFile(new File(mDatas.get(position))));

                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST_SELECT_PHOTO && data != null) {
            //营业证
            if (resultCode == RESULT_OK) { // 判断是否成功。
                // 拿到用户选择的图片路径List：
                List<String> pathList = Album.parseResult(data);

                if (pathList.size() > 0) {
                    mDatas.add(0, pathList.get(0));
                    mAdapter.notifyDataSetChanged();
                    userAvator.setImageURI(Uri.fromFile(new File(pathList.get(0))));//大图
                }
            } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。
                // 根据需要提示用户取消了选择。
                ToastUtil.showMessage("无法获取图片");
            }
        }

    }

    /**
     * 上传营业证
     */
    private void uploadMemberIcon(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            ToastUtil.showMessage("图片不见了");
            finish();
        }
        mLoading = LoadingDialogUtils.createLoadingDialog(UploadPicturesActivity.this, "上传中...");//添加等待框
        File file = new File(filePath);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("file", file.getName(), imageBody);
        Call<Object> call = getApi().uploadMemberIcon(imageBodyPart);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                    // do SomeThing
                    LogUtil.i("上传成功");
                    ToastUtil.showMessage("上传成功");
                    //TODO 初始化数据
                    JSONObject data = GsonUtils.getResultData(response.body());
                    RegistrationInformationActivity.url = data.optString("url");
                    urlImg=mDatas.get(0);
                    LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                    finish();
                } else {
                    ToastUtil.showMessage("上传失败");
                    LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                ToastUtil.showMessage("图片上传失败");
            }
        });


    }


}
