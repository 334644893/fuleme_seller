package com.fuleme.business.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.ClerkManagementAdapter;
import com.fuleme.business.bean.CMBean;
import com.fuleme.business.bean.ClerkInfoBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.AddClerkDialog;
import com.fuleme.business.widget.CustomDialog;
import com.fuleme.business.widget.LoadingDialogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 店员管理
 */
public class ClerkManagementActivity extends BaseActivity {
    private static final String TAG = "ClerkManagementActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_store_name)
    TextView tvStoreName;
    @Bind(R.id.m_recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.none)
    TextView none;
    LinearLayoutManager linearLayoutManager;
    ClerkManagementAdapter mAdapter;
    private List<CMBean> mDatas = new ArrayList<CMBean>();//列表集合
    private Dialog mLoading, mLoading_2, mLoading_3;
    public static String storeID = "";
    public static String storeName = "";
    public static String short_state = "";
    final int TOSTORE = 998;
    List<ClerkInfoBean.DataBean> dataList = new ArrayList<ClerkInfoBean.DataBean>();//数据集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk_management);
        ButterKnife.bind(this);
        initView();
        getmerchantclerkinfo();

    }

    private CustomDialog deleteDialog;

    public void initView() {
        tvTitle.setText("店员管理");
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(ClerkManagementActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ClerkManagementAdapter(ClerkManagementActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(ClerkManagementActivity.this, LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(new ClerkManagementAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, final CMBean bean) {
                ClerkDetailsActivity.clerkId = bean.getId();
                ClerkDetailsActivity.storeID = bean.getStoreid();
                startActivity(new Intent(ClerkManagementActivity.this, ClerkDetailsActivity.class));
            }
        });
        mAdapter.setDeleteClickListener(new ClerkManagementAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, final CMBean bean) {
                CustomDialog.Builder customBuilder = new
                        CustomDialog.Builder(ClerkManagementActivity.this);
                customBuilder
                        .setTitle("将员工 " + bean.getName() + " 移除?")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                delclerk(App.token, bean.getStoreid(), bean.getId());


                            }
                        })
                        .setPositiveButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                deleteDialog = customBuilder.create();
                deleteDialog.show();
            }
        });
    }

    protected void initData() {
        mDatas.clear();

        //店名
        if ("".equals(storeID)) {
            //全部店铺
            storeName = "全部店铺";
            //列表数据
            for (ClerkInfoBean.DataBean db : dataList) {
                if (db.getClerk() != null) {
                    for (ClerkInfoBean.DataBean.ClerkBean cb : db.getClerk()) {
                        CMBean cmBean = new CMBean();
                        cmBean.setName(cb.getUsername());
                        cmBean.setType(cb.getRole() + "");
                        cmBean.setPhone(cb.getPhone());
                        cmBean.setId(cb.getId() + "");
                        cmBean.setStoreid(db.getId());
                        mDatas.add(cmBean);
                    }
                }
            }
        } else {
            //单一店铺
            for (ClerkInfoBean.DataBean db : dataList) {
                if (db.getId().equals(storeID)) {
                    //根据ID显示店名
                    storeName = db.getName();
                    //根据ID显示店员
                    if (db.getClerk() != null) {
                        for (ClerkInfoBean.DataBean.ClerkBean cb : db.getClerk()) {
                            CMBean cmBean = new CMBean();
                            cmBean.setName(cb.getUsername());
                            cmBean.setType(cb.getRole() + "");
                            cmBean.setPhone(cb.getPhone());
                            cmBean.setId(cb.getId() + "");
                            cmBean.setStoreid(db.getId());
                            mDatas.add(cmBean);
                        }
                    }
                }
            }
        }
        //刷新数据列表
        tvStoreName.setText(storeName);
        if (mDatas.size() > 0) {
            none.setVisibility(View.GONE);
        } else {
            none.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    private AddClerkDialog addDialog;

    @OnClick({R.id.tv_left, R.id.ll_to_store, R.id.rl_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_to_store:
                StoreAggregationQueryActivity.intentType = StoreAggregationQueryActivity.CLERKMANAGEMENTACTIVITY;
                Intent intent = new Intent(ClerkManagementActivity.this, StoreAggregationQueryActivity.class);

                startActivityForResult(intent, TOSTORE);
                break;
            case R.id.rl_add:

                if ("".equals(storeID)) {
                    ToastUtil.showMessage("请选择店铺");
                } else {
                    if ("0".equals(short_state)) {
                        ToastUtil.showMessage("审核中店铺无法添加店员");
                    } else if ("1".equals(short_state)) {
                        //添加员工
                        final AddClerkDialog.Builder customBuilder = new
                                AddClerkDialog.Builder(ClerkManagementActivity.this);
                        customBuilder.setPositiveButton(new AddClerkDialog.Builder.OnClickListener() {
                            @Override
                            public void onClick(String etPhone, String etPassword, String etName, int state) {
                                //添加店员
                                if (TextUtils.isEmpty(etPhone)) {
                                    ToastUtil.showMessage("请填写手机号");
                                } else if (TextUtils.isEmpty(etPassword)) {
                                    ToastUtil.showMessage("请填写密码");
                                } else if (TextUtils.isEmpty(etName)) {
                                    ToastUtil.showMessage("请填写员工姓名");
                                } else {
                                    addclerk(App.token, storeID, etName, etPassword, etPhone, state + "");
                                    addDialog.dismiss();
                                }

                            }
                        });
                        addDialog = customBuilder.create();
                        addDialog.show();
                    }

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOSTORE) {
            tvStoreName.setText(storeName);
            LogUtil.i("选择店铺ID：" + storeID);
            // 刷新
            getmerchantclerkinfo();
        }
    }

    /**
     * 店员管理接口
     */
    private void getmerchantclerkinfo() {
        mLoading = LoadingDialogUtils.createLoadingDialog(ClerkManagementActivity.this, "加载中...", true);//添加等待框
        Call<ClerkInfoBean> call = getApi().getmerchantclerkinfo(App.token);

        call.enqueue(new Callback<ClerkInfoBean>() {
            @Override
            public void onResponse(Call<ClerkInfoBean> call, Response<ClerkInfoBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");


                        dataList = response.body().getData();
                        initData();


                    } else {
                        ToastUtil.showMessage("失败");
                    }

                } else {

                    LogUtil.i("失败response.message():" + response.message());

                }
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
            }

            @Override
            public void onFailure(Call<ClerkInfoBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }

    /**
     * 添加店铺员工接口
     */
    private void addclerk(String token,
                          String shopid,
                          String username,
                          String password,
                          String phone,
                          String role) {
        mLoading_2 = LoadingDialogUtils.createLoadingDialog(ClerkManagementActivity.this, "加载中...", true);//添加等待框
        Call<Object> call = getApi().addclerk(token, shopid, username, password, phone, role);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        ToastUtil.showMessage("添加成功");

                        getmerchantclerkinfo();
                    } else {
                        ToastUtil.showMessage("失败");
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }

                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }

                LoadingDialogUtils.closeDialog(mLoading_2);//取消等待框
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading_2);//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }

    /**
     * 删除店铺店员接口
     *
     * @param token
     * @param shopid
     * @param id
     */
    private void delclerk(
            String token,
            String shopid,
            String id) {
        mLoading_3 = LoadingDialogUtils.createLoadingDialog(ClerkManagementActivity.this, "删除中...", true);//添加等待框
        Call<Object> call = getApi().delclerk(token, shopid, id);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        ToastUtil.showMessage("删除成功");
                        getmerchantclerkinfo();

                    } else {
                        ToastUtil.showMessage("失败");
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }

                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }
                LoadingDialogUtils.closeDialog(mLoading_3);//取消等待框
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading_3);//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }

}
