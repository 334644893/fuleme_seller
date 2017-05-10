package com.fuleme.business.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.BusinessApplicationActivity;
import com.fuleme.business.activity.LoginActivity;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.download.UpdateManager;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.NumberUtils;
import com.fuleme.business.widget.CustomDialog;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 管理员主页
 */
public class FragmentActivity extends BaseActivity {
    private static final String TAG = "FragmentActivity";
    @Bind(R.id.tv_left)
    ImageView tvLeft;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.tv_but_im_1)
    ImageView tvButIm1;
    @Bind(R.id.tv_but_tv_1)
    TextView tvButTv1;
    @Bind(R.id.tv_but_1)
    LinearLayout tvBut1;
    @Bind(R.id.tv_but_im_2)
    ImageView tvButIm2;
    @Bind(R.id.tv_but_tv_2)
    TextView tvButTv2;
    @Bind(R.id.tv_but_2)
    LinearLayout tvBut2;
    @Bind(R.id.tv_but_im_3)
    ImageView tvButIm3;
    @Bind(R.id.tv_but_tv_3)
    TextView tvButTv3;
    @Bind(R.id.tv_but_3)
    LinearLayout tvBut3;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;
    @Bind(R.id.fl_page)
    FrameLayout flPage;
    @Bind(R.id.tv_but_im_order)
    ImageView tvButImOrder;
    @Bind(R.id.tv_but_tv_order)
    TextView tvButTvOrder;
    @Bind(R.id.tv_but_order)
    LinearLayout tvButOrder;
    private int[] mItemImage = {R.mipmap.icon_n_11_38, R.mipmap.icon_n_12_92,
            R.mipmap.icon_n_13_66, R.mipmap.icon_n_14_32};
    private int[] mItemCheckedImage = {R.mipmap.icon_n_11, R.mipmap.icon_n_12,
            R.mipmap.icon_n_13, R.mipmap.icon_n_14};
    private String[] mItemText = {"首页", "商城", "推广", "我的"};
    private long exitTime;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    AFragment aFragment;
    OrderFragment orderFragment;
    BFragment bFragment;
    CFragment cFragment;
    public static int flagFragment = 0;
    public static boolean isAutomaticLogin = false;
    public static boolean imgurlFlag = false;//修改头像是否成功
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);
        initView();
        select(0);
        //自动登录时检查更新
        if (isAutomaticLogin) {
            version();
            isAutomaticLogin = false;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    public void initView() {
        //设置bar
        tvLeft.setVisibility(View.INVISIBLE);
        tvRight.setVisibility(View.INVISIBLE);
        //TODO 填充底部
        tvButTv1.setText(mItemText[0]);
        tvButTvOrder.setText(mItemText[1]);
        tvButTv2.setText(mItemText[2]);
        tvButTv3.setText(mItemText[3]);
        tvButIm1.setImageResource(mItemImage[0]);
        tvButImOrder.setImageResource(mItemImage[1]);
        tvButIm2.setImageResource(mItemImage[2]);
        tvButIm3.setImageResource(mItemImage[3]);

    }

    public void select(int i) {
        flagFragment = i;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (i) {
            case 0:
                hideTab();
                setBottom(i);
                aFragment = (AFragment) fragmentManager.findFragmentByTag("aFragment");

                if (aFragment == null) {
                    aFragment = new AFragment();
                    fragmentTransaction.add(R.id.fl_page, aFragment, "aFragment");
                } else {
                    fragmentTransaction.show(aFragment);
                }

                break;
            case 1:
                hideTab();
                setBottom(i);
                orderFragment = (OrderFragment) fragmentManager.findFragmentByTag("orderFragment");

                if (orderFragment == null) {
                    orderFragment = new OrderFragment();
                    fragmentTransaction.add(R.id.fl_page, orderFragment, "orderFragment");
                } else {
                    fragmentTransaction.show(orderFragment);
                }

                break;
            case 2:
                hideTab();
                setBottom(i);
                bFragment = (BFragment) fragmentManager.findFragmentByTag("bFragment");

                if (bFragment == null) {
                    bFragment = new BFragment();
                    fragmentTransaction.add(R.id.fl_page, bFragment, "bFragment");
                } else {
                    fragmentTransaction.show(bFragment);
                }
                break;
            case 3:
                hideTab();
                setBottom(i);
                cFragment = (CFragment) fragmentManager.findFragmentByTag("cFragment");

                if (cFragment == null) {
                    cFragment = new CFragment();
                    fragmentTransaction.add(R.id.fl_page, cFragment, "cFragment");
                } else {
                    fragmentTransaction.show(cFragment);
                }
                break;

        }
        fragmentTransaction.commit();
    }

    //切换fragment
    private void hideTab() {
        if (aFragment != null) {
            fragmentTransaction.hide(aFragment);
        }
        if (orderFragment != null) {
            fragmentTransaction.hide(orderFragment);
        }
        if (bFragment != null) {
            fragmentTransaction.hide(bFragment);
        }
        if (cFragment != null) {
            fragmentTransaction.hide(cFragment);
        }

    }

    @OnClick({R.id.tv_but_1, R.id.tv_but_order, R.id.tv_but_2, R.id.tv_but_3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_but_1:
                select(0);

                break;
            case R.id.tv_but_order:
                select(1);

                break;
            case R.id.tv_but_2:
                select(2);
                break;
            case R.id.tv_but_3:
                select(3);
                break;
        }
    }

    /**
     * 改变底部导航
     *
     * @param i
     */
    private void setBottom(int i) {
        switch (i) {
            case 0:
                tvTitle.setText(mItemText[0]);
                tvButIm1.setImageResource(mItemCheckedImage[0]);
                tvButImOrder.setImageResource(mItemImage[1]);
                tvButIm2.setImageResource(mItemImage[2]);
                tvButIm3.setImageResource(mItemImage[3]);
                tvButTv1.setTextColor(getResources().getColor(R.color.theme));
                tvButTvOrder.setTextColor(getResources().getColor(R.color.black_54));
                tvButTv2.setTextColor(getResources().getColor(R.color.black_54));
                tvButTv3.setTextColor(getResources().getColor(R.color.black_54));
                break;
            case 1:
                tvTitle.setText(mItemText[1]);
                tvButIm1.setImageResource(mItemImage[0]);
                tvButImOrder.setImageResource(mItemCheckedImage[1]);
                tvButIm2.setImageResource(mItemImage[2]);
                tvButIm3.setImageResource(mItemImage[3]);
                tvButTv1.setTextColor(getResources().getColor(R.color.black_54));
                tvButTvOrder.setTextColor(getResources().getColor(R.color.theme));
                tvButTv2.setTextColor(getResources().getColor(R.color.black_54));
                tvButTv3.setTextColor(getResources().getColor(R.color.black_54));
                break;
            case 2:
                tvTitle.setText(mItemText[2]);
                tvButIm1.setImageResource(mItemImage[0]);
                tvButImOrder.setImageResource(mItemImage[1]);
                tvButIm2.setImageResource(mItemCheckedImage[2]);
                tvButIm3.setImageResource(mItemImage[3]);
                tvButTv1.setTextColor(getResources().getColor(R.color.black_54));
                tvButTvOrder.setTextColor(getResources().getColor(R.color.black_54));
                tvButTv2.setTextColor(getResources().getColor(R.color.theme));
                tvButTv3.setTextColor(getResources().getColor(R.color.black_54));
                break;
            case 3:
                tvTitle.setText(mItemText[3]);
                tvButIm1.setImageResource(mItemImage[0]);
                tvButImOrder.setImageResource(mItemImage[1]);
                tvButIm2.setImageResource(mItemImage[2]);
                tvButIm3.setImageResource(mItemCheckedImage[3]);
                tvButTv1.setTextColor(getResources().getColor(R.color.black_54));
                tvButTvOrder.setTextColor(getResources().getColor(R.color.black_54));
                tvButTv2.setTextColor(getResources().getColor(R.color.black_54));
                tvButTv3.setTextColor(getResources().getColor(R.color.theme));
                break;
        }

    }

    /*
    退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 退出接口
     */

    private void logout() {
        Call<Object> call = getApi().logout(App.token);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                    } else {
                        LogUtil.i("失败");
                    }

                } else {
                    LogUtil.i("失败response.message():" + response.message());

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logout();
    }

    int type = -1;
    private CustomDialog dialog;

    private void version() {
        getApi().version().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("获取更新信息成功");
                        //TODO 初始化数据
                        JSONObject data = GsonUtils.getResultData(response.body());
                        LogUtil.d("---data--", data.toString());
                        int version = data.optInt("androidVersion");//版本标识
                        String prompt = data.optString("prompt");
                        type = data.optInt("type");
                        String url = data.optString("android");
                        LogUtil.d("---------", "-version:" + version + "-prompt:" + prompt + "-type:" + type + "-android:" + url);
                        //信息对比是否更新
                        new UpdateManager(FragmentActivity.this).checkUpdate(version, prompt, type, url, false);
                    }

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                // 构造对话框
                // 构造对话框
                CustomDialog.Builder customBuilder = new
                        CustomDialog.Builder(FragmentActivity.this);
                customBuilder
                        .setTitle("检查更新提示")
                        .setMessage("网络出现了问题")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                System.exit(0);
                            }
                        })
                ;

                dialog = customBuilder.create();
                dialog.setCancelable(false);
                dialog.show();
            }

        });
    }

}
