package com.fuleme.business.activity.Version2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.RegistrationStoreActivity;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.PictureUtil;
import com.fuleme.business.utils.ToastUtil;
import com.yanzhenjie.album.Album;

import org.json.JSONObject;

import java.io.File;
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

/**
 * 签约推广
 */
public class SignPromoteActivity extends BaseActivity {
    private static final String TAG = "SignPromoteActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_1)
    EditText et1;
    @Bind(R.id.et_2)
    EditText et2;
    @Bind(R.id.et_3)
    EditText et3;

    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.user_avator_2)
    SimpleDraweeView userAvator2;
    @Bind(R.id.ll_xx_2)
    LinearLayout llXx2;
    boolean onClick_2 = true;//身份证正面点击标识
    String url_ph_2 = "";//显示身份证正面地址
    String url_business_licence_2 = "";//上传身份证正面地址
    public int ACTIVITY_REQUEST_SELECT_PHOTO = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_promote);
        ButterKnife.bind(this);
        tvTitle.setText("签约推广");
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
                    url_ph_2 = pathList.get(0);
                    uploadMemberIcon(PictureUtil.smallPic(pathList.get(0)));
                }
            } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。
                // 根据需要提示用户取消了选择。
                ToastUtil.showMessage("无法获取图片");
            }
        }
    }

    @OnClick({R.id.tv_left, R.id.user_avator_2, R.id.ll_xx_2, R.id.btn_tj_1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.user_avator_2:
                //身份证正面
                if (onClick_2) {
                    Album.startAlbum(SignPromoteActivity.this, ACTIVITY_REQUEST_SELECT_PHOTO
                            , 1                                                         // 指定选择数量。
                            , ContextCompat.getColor(SignPromoteActivity.this, R.color.theme)        // 指定Toolbar的颜色。
                            , ContextCompat.getColor(SignPromoteActivity.this, R.color.theme));  // 指定状态栏的颜色。
                }
                break;
            case R.id.ll_xx_2:
                userAvator2.setImageURI((new Uri.Builder()).scheme("res").path(String.valueOf(R.drawable.icon_photo3)).build());
                url_business_licence_2 = "";
                llXx2.setVisibility(View.INVISIBLE);
                onClick_2 = true;
                break;
            case R.id.btn_tj_1:
                if (TextUtils.isEmpty(et1.getText().toString())) {
                    ToastUtil.showMessage("请填写真实姓名");
                } else if (TextUtils.isEmpty(et2.getText().toString())) {
                    ToastUtil.showMessage("请填写联系电话");
                } else if (TextUtils.isEmpty(et3.getText().toString())) {
                    ToastUtil.showMessage("请填写身份证号");
                } else if (TextUtils.isEmpty(url_business_licence_2)) {
                    ToastUtil.showMessage("请上传身份证照片");
                } else {
                    sign();
                }
                break;
        }
    }
    /**
     * 签约推广接口
     */

    private void sign() {
        showLoading("获取中...");
        Call<Object> call = getApi().sign(
                et3.getText().toString(),
                et1.getText().toString(),
                et2.getText().toString(),
                url_business_licence_2,
                App.token
        );

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    // do SomeThing
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        PayActivity.qrcode=GsonUtils.getStringDate(response.body());
                        startActivity(new Intent(SignPromoteActivity.this, PayActivity.class));
                        finish();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }


                } else {
                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    LogUtil.i("注册失败response.message():" + response.message());
                }
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }
    /**
     * 上传图片
     */
    private void uploadMemberIcon(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            ToastUtil.showMessage("图片不见了");
            return;
        }
        showLoading("上传中...");
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
                    JSONObject data = GsonUtils.getResultData(response.body());
                    url_business_licence_2 = data.optString("url");
                    userAvator2.setImageURI(Uri.fromFile(new File(url_ph_2)));
                    onClick_2 = false;
                    llXx2.setVisibility(View.VISIBLE);
                    closeLoading();//取消等待框
                } else {
                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    url_business_licence_2 = "";
                    closeLoading();//取消等待框
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                url_business_licence_2 = "";
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("图片上传失败");
            }
        });
    }
}
