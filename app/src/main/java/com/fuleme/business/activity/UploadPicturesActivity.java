//package com.fuleme.business.activity;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.TextView;
//
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.fuleme.business.R;
//import com.fuleme.business.adapter.UploadPicturesAdapter;
//import com.fuleme.business.common.BaseActivity;
//import com.fuleme.business.helper.GsonUtils;
//import com.fuleme.business.utils.LogUtil;
//import com.fuleme.business.utils.PictureUtil;
//import com.fuleme.business.utils.ToastUtil;
//import com.yanzhenjie.album.Album;
//
//import org.json.JSONObject;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// *上传图片
// */
//public class UploadPicturesActivity extends BaseActivity {
//    private static final String TAG = "UploadPicturesActivity";
//    public static String urlImg_business_licence = "";//营业证显示图片地址
//    public static ArrayList<String> urlImg_identity_card = new ArrayList<>();//本地身份证图片地址集合
//    private static int IMAGE_NUM = 1;//最多上传数量
//    private int index_mDatas = -1;//当前选择的图片在mDatas中的索引
//    private int index_successful = 0;//用来记录成功上传的图片数量
//    private ArrayList<String> mDatas = new ArrayList<String>();
//    private UploadPicturesAdapter mAdapter;
//    public int ACTIVITY_REQUEST_SELECT_PHOTO = 1000;
//    GridLayoutManager mGridLayoutManager;
//    @Bind(R.id.recyclerview)
//    RecyclerView recyclerview;
//    @Bind(R.id.user_avator)
//    SimpleDraweeView userAvator;
//    @Bind(R.id.tv_title)
//    TextView tvTitle;
//    @Bind(R.id.tv_right)
//    TextView tvRight;
//    private int intent = 0;//区分进入页面
//
//    @OnClick({R.id.tv_left, R.id.tv_right, R.id.btn})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv_left:
//
//
//                finish();
//                break;
//            case R.id.tv_right:
//                if (mDatas.size() > 1) {
//
//                    mDatas.remove(index_mDatas);
//                    mAdapter.notifyDataSetChanged();
//                    if (mDatas.size() > 1) {
//                        index_mDatas = 0;
//                        userAvator.setImageURI(Uri.fromFile(new File(mDatas.get(index_mDatas))));
//                    } else {
//                        userAvator.setImageDrawable(getResources().getDrawable(R.mipmap.logo_de));
//                    }
//                } else {
//                    ToastUtil.showMessage("选张图片呗");
//                }
//                break;
//            case R.id.btn:
//                if (mDatas.size() - 1 == IMAGE_NUM) {
//                    //当图片集合减去“加号”图片后 等于需要上传的数量时  开始上传图片
//                    showLoading("上传中...");
//                    index_successful = 0;
//                    urlImg_identity_card.clear();
//                    for (int i = 0; i < mDatas.size() - 1; i++) {
//                        uploadMemberIcon(PictureUtil.smallPic(mDatas.get(i)));
//                    }
//                } else {
//                    switch (intent) {
//                        case RegistrationStoreActivity.BUSINESS_LICENCE:
//                            ToastUtil.showMessage("请添加营业证(共1张)");
//                            break;
//                        case RegistrationStoreActivity.IDENTITY_CARD:
//                            ToastUtil.showMessage("请添加身份证正反面(共2张)");
//                            break;
//                    }
//
//                }
//
//                break;
//        }
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_pictures);
//        ButterKnife.bind(this);
//        init();
//    }
//
//    public void init() {
//        intent = getIntent().getExtras().getInt("intent");
//        initData();
//        initView();
//        initClick();
//    }
//
//    public void initData() {
//
//        mDatas.add("add");//初始化一个加号
//        switch (intent) {
//            case RegistrationStoreActivity.BUSINESS_LICENCE:
//                tvTitle.setText("上传营业证");
//                IMAGE_NUM = 1;//营业证1张
//                if (!TextUtils.isEmpty(urlImg_business_licence) && new File(urlImg_business_licence).exists()) {
//                    mDatas.add(0, urlImg_business_licence);
//                    index_mDatas = 0;
//                    userAvator.setImageURI(Uri.fromFile(new File(urlImg_business_licence)));
//                }
//                break;
//            case RegistrationStoreActivity.IDENTITY_CARD:
//                tvTitle.setText("上传身份证");
//                IMAGE_NUM = 2;//身份证2张
//                if (urlImg_identity_card.size() > 0 && fileIsNull(urlImg_identity_card)) {
//                    mDatas.addAll(0, urlImg_identity_card);
//                    index_mDatas = 0;
//                    userAvator.setImageURI(Uri.fromFile(new File(urlImg_identity_card.get(index_mDatas))));
//                }
//                break;
//        }
//
//    }
//
//    /**
//     * 判断图片是否还在手机中
//     *
//     * @param list
//     * @return
//     */
//    private boolean fileIsNull(List<String> list) {
//        for (String s : list) {
//            if (!new File(s).exists()) {
//                return false;
//            }
//        }
//        return true;
//
//    }
//
//    public void initView() {
//
//
//        tvRight.setText("删除");
//        tvRight.setVisibility(View.VISIBLE);
//        //初始化列表
//        mGridLayoutManager = new GridLayoutManager(this, 4);
//        recyclerview.setLayoutManager(mGridLayoutManager);
//        mAdapter = new UploadPicturesAdapter(UploadPicturesActivity.this, mDatas);
//        recyclerview.setAdapter(mAdapter);
//
//    }
//
//    public void initClick() {
//        //列表item点击
//        mAdapter.setOnItemClickListener(new UploadPicturesAdapter.onRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View v, int position) {
//                if ((position + 1) == mDatas.size()) {
//                    //点击最后一个
//                    if (mDatas.size() <= IMAGE_NUM) {
//                        //去选图片
//                        Album.startAlbum(UploadPicturesActivity.this, ACTIVITY_REQUEST_SELECT_PHOTO
//                                , 1                                                         // 指定选择数量。
//                                , ContextCompat.getColor(UploadPicturesActivity.this, R.color.theme)        // 指定Toolbar的颜色。
//                                , ContextCompat.getColor(UploadPicturesActivity.this, R.color.theme));  // 指定状态栏的颜色。
//                    } else {
//                        ToastUtil.showMessage("最多添加" + IMAGE_NUM + "张图片");
//                    }
//
//                } else {
//                    //点击其余图片
//                    index_mDatas = position;
//                    userAvator.setImageURI(Uri.fromFile(new File(mDatas.get(index_mDatas))));
//
//                }
//            }
//        });
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ACTIVITY_REQUEST_SELECT_PHOTO && data != null) {
//            //营业证
//            if (resultCode == RESULT_OK) { // 判断是否成功。
//                // 拿到用户选择的图片路径List：
//                List<String> pathList = Album.parseResult(data);
//
//                if (pathList.size() > 0) {
//                    mDatas.addAll(mDatas.size() - 1, pathList);
//                    mAdapter.notifyDataSetChanged();
//                    userAvator.setImageURI(Uri.fromFile(new File(pathList.get(0))));//大图
//                    index_mDatas = mDatas.size() - 1;
//                }
//            } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。
//                // 根据需要提示用户取消了选择。
//                ToastUtil.showMessage("无法获取图片");
//            }
//        }
//
//    }
//    /**
//     * 上传图片
//     */
//    private void uploadMemberIcon(String filePath) {
//        if (TextUtils.isEmpty(filePath)) {
//            ToastUtil.showMessage("图片不见了");
//            finish();
//        }
//        File file = new File(filePath);
//        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("file", file.getName(), imageBody);
//        Call<Object> call = getApi().uploadMemberIcon(imageBodyPart);
//        call.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, Response<Object> response) {
//                if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
//                    // do SomeThing
//                    LogUtil.i("上传成功");
//                    ToastUtil.showMessage("上传成功");
//                    JSONObject data = GsonUtils.getResultData(response.body());
//                    switch (intent) {
//                        case RegistrationStoreActivity.BUSINESS_LICENCE:
//                            RegistrationStoreActivity.url_business_licence = data.optString("url");
//                            urlImg_business_licence = mDatas.get(0);
//                            break;
//                        case RegistrationStoreActivity.IDENTITY_CARD:
//                            if (RegistrationStoreActivity.url_identity_card.size() == 2) {
//                                RegistrationStoreActivity.url_identity_card.set(index_successful, data.optString("url"));
//                            } else {
//                                RegistrationStoreActivity.url_identity_card.add(data.optString("url"));
//                            }
//                            urlImg_identity_card.add(mDatas.get(index_successful));
//                            break;
//                    }
//                    index_successful++;
//                    if (index_successful == mDatas.size() - 1) {
//                        closeLoading();//取消等待框
//                        finish();
//                    }
//                } else {
//                    RegistrationStoreActivity.url_identity_card.clear();
//                    urlImg_identity_card.clear();
//                    ToastUtil.showMessage("上传失败");
//                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
//                    closeLoading();//取消等待框
//                    finish();
//                }
//            }
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//                RegistrationStoreActivity.url_identity_card.clear();
//                urlImg_identity_card.clear();
//                LogUtil.e(TAG, t.toString());
//                closeLoading();//取消等待框
//                ToastUtil.showMessage("图片上传失败");
//                finish();
//            }
//        });
//    }
//
//
//}
