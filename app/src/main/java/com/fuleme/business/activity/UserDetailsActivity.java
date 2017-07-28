package com.fuleme.business.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.Version2.InviteCodeActivity;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.fragment.CFragment;
import com.fuleme.business.fragment.FragmentActivity;
import com.fuleme.business.helper.APIService;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.PictureUtil;
import com.fuleme.business.utils.SharedPreferencesUtils;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.CustomDialog;
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

import static com.fuleme.business.fragment.FragmentActivity.imgurlFlag;

/**
 * 账号详情
 */
public class UserDetailsActivity extends BaseActivity {
    private static final String TAG = "UserDetailsActivity";
    private Context context;
    @Bind(R.id.tv_store_name)
    TextView tvStoreName;
    @Bind(R.id.tv_region)
    TextView tvRegion;
    @Bind(R.id.tv_industry)
    TextView tvIndustry;
    private CustomDialog dialog;
    final int EXIT_USERDETAIL = 100;//退出
    @Bind(R.id.tv_title)
    TextView tvTitle;
    int TOSTORE = 998;
    static final int LL_SHORT_IMG = 210;
    @Bind(R.id.logo)
    SimpleDraweeView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    public void initView() {
        tvTitle.setText("账号详情");
        //改变头像
        if (!TextUtils.isEmpty(App.short_logo)) {
            logo.setImageURI(APIService.SERVER_IP + App.short_logo);
        }
        if ("0".equals(App.short_state)) {
            tvStoreName.setText(App.merchant + "(审核中)");
        } else if ("1".equals(App.short_state)) {
            tvStoreName.setText(App.merchant + "(已审核)");
        } else {
            tvStoreName.setText("暂无店铺");
        }
        tvRegion.setText(App.short_area);

    }

    @OnClick({R.id.tv_left, R.id.btn_login, R.id.ll_forgotpassword,
            R.id.ll_short_img
            , R.id.ll_yaoqingma
            , R.id.shmc
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_login:
                // 退出登录
                CustomDialog.Builder customBuilder = new
                        CustomDialog.Builder(UserDetailsActivity.this);
                customBuilder
                        .setTitle("退出")
                        .setMessage("您确认退出该账号吗?")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(UserDetailsActivity.this, FragmentActivity.class);
                                setResult(EXIT_USERDETAIL, intent);

                                SharedPreferencesUtils.setParam(getApplicationContext(), "uid", 0);
//                                SharedPreferencesUtils.setParam(getApplicationContext(), "phone", "");
                                SharedPreferencesUtils.setParam(getApplicationContext(), "username", "");
                                SharedPreferencesUtils.setParam(getApplicationContext(), "role", "");
                                SharedPreferencesUtils.setParam(getApplicationContext(), "short_id", "");
                                SharedPreferencesUtils.setParam(getApplicationContext(), "merchant", "");
                                SharedPreferencesUtils.setParam(getApplicationContext(), "short_state", "");
                                SharedPreferencesUtils.setParam(getApplicationContext(), "short_area", "");
                                SharedPreferencesUtils.setParam(getApplicationContext(), "token", "");
                                SharedPreferencesUtils.setParam(getApplicationContext(), "qrcode", "");
                                SharedPreferencesUtils.setParam(getApplicationContext(), "head_img", "");
                                App.unbindAccount();
                                stopService(intent);
                                finish();
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
                break;
            case R.id.ll_short_img:
                // 上传头像
                //去选图片
                Album.startAlbum(UserDetailsActivity.this, LL_SHORT_IMG
                        , 1                                                         // 指定选择数量。
                        , ContextCompat.getColor(UserDetailsActivity.this, R.color.theme)        // 指定Toolbar的颜色。
                        , ContextCompat.getColor(UserDetailsActivity.this, R.color.theme));  // 指定状态栏的颜色。
                break;
            case R.id.ll_forgotpassword:
                // 修改密码
                startActivity(new Intent(UserDetailsActivity.this, ChangePasswordActivity.class));
                break;
            case R.id.ll_yaoqingma:
                // 邀请码
                startActivity(new Intent(UserDetailsActivity.this, InviteCodeActivity.class));

                break;
            case R.id.shmc:
                //切换店铺
                StoreAggregationQueryActivity.intentType = StoreAggregationQueryActivity.USERDETAILSACTIVITY;
                Intent intent = new Intent(context, StoreAggregationQueryActivity.class);
                startActivityForResult(intent, TOSTORE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOSTORE) {
            initView();
        }
        if (requestCode == LL_SHORT_IMG && data != null) {
            //logo头像
            if (resultCode == RESULT_OK) { // 判断是否成功。
                // 拿到用户选择的图片路径List：
                List<String> pathList = Album.parseResult(data);
                if (pathList.size() > 0) {
                    showLoading("上传中...");
                    uploadMemberIcon(PictureUtil.smallPic(pathList.get(0)));
                }
            } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。
                // 根据需要提示用户取消了选择。
                ToastUtil.showMessage("无法获取图片");
            }
        }
    }

    /**
     * 上传图片
     */
    private void uploadMemberIcon(String filePath) {
        File file = new File(filePath);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("file", file.getName(), imageBody);
        Call<Object> call = getApi().uploadMemberIcon(imageBodyPart);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                    JSONObject data = GsonUtils.getResultData(response.body());

                    modifyheadImg(data.optString("url"));
                } else {
                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    closeLoading();//取消等待框
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("图片上传失败");
            }
        });
    }

    /**
     * 提交logo图片
     */
    private void modifyheadImg(final String url) {
        Call<Object> call = getApi().modifyheadImg(App.token, url);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                    // do SomeThing
                    imgurlFlag = true;
                    App.short_logo = url;
                    SharedPreferencesUtils.setParam(UserDetailsActivity.this, "head_img", url);
                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    closeLoading();//取消等待框
                } else {
                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    closeLoading();//取消等待框
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("图片上传失败");
            }
        });
    }
}
