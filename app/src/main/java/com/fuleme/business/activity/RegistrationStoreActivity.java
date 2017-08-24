package com.fuleme.business.activity;

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
import com.fuleme.business.activity.Version2.ListActivity;
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
 * 添加店铺
 */
public class RegistrationStoreActivity extends BaseActivity {
    private static final String TAG = "RegistrationStoreActivi";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.short_name)
    EditText shortName;
    @Bind(R.id.contact_mobile)
    EditText contactMobile;
    @Bind(R.id.account_num)
    EditText accountNum;
    String url_business_licence = "";//上传营业证地址
    String url_business_licence_2 = "";//上传身份证正面地址
    String url_business_licence_3 = "";//上传身份证反面地址
    String url_ph_1 = "";//显示营业证地址
    String url_ph_2 = "";//显示身份证正面地址
    String url_ph_3 = "";//显示身份证反面地址
    boolean onClick_1 = true;//营业证点击标识
    boolean onClick_2 = true;//身份证正面点击标识
    boolean onClick_3 = true;//身份证反面点击标识
    public int ACTIVITY_REQUEST_SELECT_PHOTO = 1000;
    @Bind(R.id.user_avator_1)
    SimpleDraweeView userAvator1;
    @Bind(R.id.user_avator_2)
    SimpleDraweeView userAvator2;
    @Bind(R.id.user_avator_3)
    SimpleDraweeView userAvator3;
    int SCBS = 0;//上传标识 1为营业证 2身份证正面 3身份证反面
    @Bind(R.id.ll_xx_1)
    LinearLayout llXx1;
    @Bind(R.id.ll_xx_2)
    LinearLayout llXx2;
    @Bind(R.id.ll_xx_3)
    LinearLayout llXx3;
    public static String BANK = "";//银行

    @Bind(R.id.payee)
    EditText payee;
    @Bind(R.id.short_dre)
    EditText shortDre;
    @Bind(R.id.tv_branch_bank)
    EditText tvBranchBank;
    @Bind(R.id.id_card)
    EditText idCard;
    @Bind(R.id.et_f_name)
    EditText etFName;
    @Bind(R.id.et_f_phone)
    EditText etFPhone;
    @Bind(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_store);
        ButterKnife.bind(this);
        tvTitle.setText("添加店铺");
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                    switch (SCBS) {
                        case 1:
                            url_ph_1 = pathList.get(0);
                            break;
                        case 2:
                            url_ph_2 = pathList.get(0);
                            break;
                        case 3:
                            url_ph_3 = pathList.get(0);
                            break;
                    }
                    uploadMemberIcon(PictureUtil.smallPic(pathList.get(0)));
                }
            } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。
                // 根据需要提示用户取消了选择。
                ToastUtil.showMessage("无法获取图片");
            }
        }
    }

    @OnClick({
            R.id.tv_left,
            R.id.user_avator_1,
            R.id.user_avator_2,
            R.id.user_avator_3,
            R.id.btn_tj_1,
            R.id.ll_xx_1,
            R.id.ll_xx_2,
            R.id.ll_xx_3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_xx_1:
                userAvator1.setImageURI((new Uri.Builder()).scheme("res").path(String.valueOf(R.drawable.icon_photo1)).build());
                url_business_licence = "";
                onClick_1 = true;
                llXx1.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_xx_2:
                userAvator2.setImageURI((new Uri.Builder()).scheme("res").path(String.valueOf(R.drawable.icon_photo3)).build());
                url_business_licence_2 = "";
                llXx2.setVisibility(View.INVISIBLE);
                onClick_2 = true;
                break;
            case R.id.ll_xx_3:
                userAvator3.setImageURI((new Uri.Builder()).scheme("res").path(String.valueOf(R.drawable.icon_photo2)).build());
                url_business_licence_3 = "";
                onClick_3 = true;
                llXx3.setVisibility(View.INVISIBLE);
                break;
            case R.id.user_avator_1:
                //营业证

                if (onClick_1) {
                    SCBS = 1;
                    Album.startAlbum(RegistrationStoreActivity.this, ACTIVITY_REQUEST_SELECT_PHOTO
                            , 1                                                         // 指定选择数量。
                            , ContextCompat.getColor(RegistrationStoreActivity.this, R.color.theme)        // 指定Toolbar的颜色。
                            , ContextCompat.getColor(RegistrationStoreActivity.this, R.color.theme));  // 指定状态栏的颜色。
                }

                break;
            case R.id.user_avator_2:
                //身份证正面
                if (onClick_2) {
                    SCBS = 2;
                    Album.startAlbum(RegistrationStoreActivity.this, ACTIVITY_REQUEST_SELECT_PHOTO
                            , 1                                                         // 指定选择数量。
                            , ContextCompat.getColor(RegistrationStoreActivity.this, R.color.theme)        // 指定Toolbar的颜色。
                            , ContextCompat.getColor(RegistrationStoreActivity.this, R.color.theme));  // 指定状态栏的颜色。
                }
                break;
            case R.id.user_avator_3:
                //身份证反面
                if (onClick_3) {
                    SCBS = 3;
                    Album.startAlbum(RegistrationStoreActivity.this, ACTIVITY_REQUEST_SELECT_PHOTO
                            , 1                                                         // 指定选择数量。
                            , ContextCompat.getColor(RegistrationStoreActivity.this, R.color.theme)        // 指定Toolbar的颜色。
                            , ContextCompat.getColor(RegistrationStoreActivity.this, R.color.theme));  // 指定状态栏的颜色。
                }
                break;
            case R.id.btn_tj_1:
                if (comitJudge()) {
                    LogUtil.d("url_business_licence--------", url_business_licence);
                    LogUtil.d("url_business_licence_2---------", url_business_licence_2);
                    LogUtil.d("url_business_licence_3--------", url_business_licence_3);
                    addshop();
                }

                break;
        }
    }

    /**
     * 添加店铺接口
     */

    private void addshop(
    ) {
        showLoading("获取中...");
        Call<Object> call =
                getApi().addshop(
                        App.token,
                        shortName.getText().toString().trim(),
                        accountNum.getText().toString().trim(),
                        payee.getText().toString().trim(),
                        tvBranchBank.getText().toString().trim(),
                        contactMobile.getText().toString().trim(),
                        idCard.getText().toString().trim(),
                        etFName.getText().toString().trim(),
                        etFPhone.getText().toString().trim(),
                        url_business_licence,
                        url_business_licence_2 + "," + url_business_licence_3,
                        shortDre.getText().toString().trim()
                );

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, final Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                        finish();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }
                } else {
                    ToastUtil.showMessage("失败:" + response.message());
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
     * 判断是否符合提交
     *
     * @return
     */
    private boolean comitJudge() {
        if (TextUtils.isEmpty(shortName.getText().toString())) {
            ToastUtil.showMessage("商户简称不能为空");
            return false;
        } else if (TextUtils.isEmpty(shortDre.getText().toString())) {
            ToastUtil.showMessage("门店地址不能为空");
            return false;
        } else if (TextUtils.isEmpty(payee.getText().toString())) {
            ToastUtil.showMessage("收款人不能为空");
            return false;
        } else if (TextUtils.isEmpty(accountNum.getText().toString())) {
            ToastUtil.showMessage("商户结算账号不能为空");
            return false;
        } else if (TextUtils.isEmpty(tvBranchBank.getText().toString())) {
            ToastUtil.showMessage("请填写开户行支行");
            return false;
        } else if (TextUtils.isEmpty(contactMobile.getText().toString())) {
            ToastUtil.showMessage("联系人手机号不能为空");
            return false;
        } else if (TextUtils.isEmpty(idCard.getText().toString())) {
            ToastUtil.showMessage("请填写收款人身份证号");
            return false;
        } else if (TextUtils.isEmpty(etFName.getText().toString())) {
            ToastUtil.showMessage("请填写法人姓名");
            return false;
        } else if (TextUtils.isEmpty(etFPhone.getText().toString())) {
            ToastUtil.showMessage("请填写法人手机号");
            return false;
        } else if (TextUtils.isEmpty(url_business_licence)) {
            ToastUtil.showMessage("请上传营业证");
            return false;
        } else if (TextUtils.isEmpty(url_business_licence_2)) {
            ToastUtil.showMessage("请上传身份证正面");
            return false;
        } else if (TextUtils.isEmpty(url_business_licence_3)) {
            ToastUtil.showMessage("请上传身份证反面");
            return false;
        }
        return true;
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
                    switch (SCBS) {
                        case 1:
                            url_business_licence = data.optString("url");
                            userAvator1.setImageURI(Uri.fromFile(new File(url_ph_1)));
                            onClick_1 = false;
                            llXx1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            url_business_licence_2 = data.optString("url");
                            userAvator2.setImageURI(Uri.fromFile(new File(url_ph_2)));
                            onClick_2 = false;
                            llXx2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            url_business_licence_3 = data.optString("url");
                            userAvator3.setImageURI(Uri.fromFile(new File(url_ph_3)));
                            onClick_3 = false;
                            llXx3.setVisibility(View.VISIBLE);
                            break;
                    }
                    closeLoading();//取消等待框
                } else {
                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    url_business_licence = "";
                    url_business_licence_2 = "";
                    url_business_licence_3 = "";
                    closeLoading();//取消等待框
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                url_business_licence = "";
                url_business_licence_2 = "";
                url_business_licence_3 = "";
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("图片上传失败");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        url_business_licence = "";
        url_business_licence_2 = "";
        url_business_licence_3 = "";
    }

}
