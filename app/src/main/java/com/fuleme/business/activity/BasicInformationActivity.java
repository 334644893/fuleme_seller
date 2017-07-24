package com.fuleme.business.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.adapter.LRTextAdapter;
import com.fuleme.business.bean.ContractBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 店铺基本资料
 */
public class BasicInformationActivity extends BaseActivity {
    private static final String TAG = "ContractrateActivity";
    @Bind(R.id.tv_dianyuan)
    TextView tvDianyuan;
    @Bind(R.id.tv_dianyuan_l)
    View tvDianyuanL;
    @Bind(R.id.tv_admin)
    TextView tvAdmin;
    @Bind(R.id.tv_admin_l)
    View tvAdminL;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.m_recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.ll_bot)
    LinearLayout llBot;
    @Bind(R.id.ll_0)
    LinearLayout ll0;
    private Context context;
    LinearLayoutManager linearLayoutManager;
    LRTextAdapter mAdapter;
    private List<ContractBean> mDatas = new ArrayList<>();
    public static String short_id = "";
    public static String name = "";
    public static String state = "";
    public static String qrcode = "";
    int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_information);
        ButterKnife.bind(this);
        context = this;

        init();
    }

    public void init() {
        tvTitle.setText("店铺基本资料");
        initView();
        setState(type);
    }

    public void initView() {
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mAdapter = new LRTextAdapter(context, mDatas);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
    }

    /**
     * 获取店铺基本资料
     */
    private void basicinfo() {

        showLoading("加载中...");
        Call<Object> call = getApi().basicinfo(App.token, short_id);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        mDatas.clear();
                        JSONObject data = GsonUtils.getResultData(response.body());
                        if (type == 1) {
                            ll0.setVisibility(View.GONE);
                            llBot.setVisibility(View.VISIBLE);
                            //基本资料
                            ContractBean ContractBean = new ContractBean();
                            ContractBean.setName("商户号:");
                            ContractBean.setRate(data.optString("id"));
                            mDatas.add(ContractBean);
                            ContractBean ContractBean1 = new ContractBean();
                            ContractBean1.setName("店铺名称:");
                            ContractBean1.setRate(data.optString("short_name"));
                            mDatas.add(ContractBean1);
                            ContractBean ContractBean2 = new ContractBean();
                            ContractBean2.setName("所在地区:");
                            ContractBean2.setRate(data.optString("merchant_area"));
                            mDatas.add(ContractBean2);
                            ContractBean ContractBean3 = new ContractBean();
                            ContractBean3.setName("详细地址:");
                            ContractBean3.setRate(data.optString("merchant_address"));
                            mDatas.add(ContractBean3);
                            ContractBean ContractBean4 = new ContractBean();
                            ContractBean4.setName("联系人:");
                            ContractBean4.setRate(data.optString("contact_person_name"));
                            mDatas.add(ContractBean4);
                            ContractBean ContractBean5 = new ContractBean();
                            ContractBean5.setName("联系电话:");
                            ContractBean5.setRate(data.optString("contact_person_mobile"));
                            mDatas.add(ContractBean5);
                        } else {
                            ll0.setVisibility(View.VISIBLE);
                            llBot.setVisibility(View.GONE);
                            //开户信息
                            //TODO 接口信息未添加
                            ContractBean ContractBean = new ContractBean();
                            ContractBean.setName("法人:");
                            ContractBean.setRate("法人");
                            mDatas.add(ContractBean);
                            ContractBean ContractBean1 = new ContractBean();
                            ContractBean1.setName("银行账户:");
                            ContractBean1.setRate("收款人");
                            mDatas.add(ContractBean1);
                            ContractBean ContractBean2 = new ContractBean();
                            ContractBean2.setName("银行账号:");
                            ContractBean2.setRate("收款卡号");
                            mDatas.add(ContractBean2);
                            ContractBean ContractBean3 = new ContractBean();
                            ContractBean3.setName("银行:");
                            ContractBean3.setRate("中国建设银行");
                            mDatas.add(ContractBean3);
                            ContractBean ContractBean4 = new ContractBean();
                            ContractBean4.setName("支行:");
                            ContractBean4.setRate("武汉支行:");
                            mDatas.add(ContractBean4);
                            ContractBean ContractBean5 = new ContractBean();
                            ContractBean5.setName("手机号:");
                            ContractBean5.setRate("18672359600");
                            mDatas.add(ContractBean5);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }
                } else {
                    ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    finish();
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
     * 改变登录类型
     *
     * @param types
     */
    private void setState(int types) {
        type = types;
        switch (type) {
            case 0:

                tvAdmin.setTextColor(getResources().getColor(R.color.red_1));
                tvAdminL.setBackgroundColor(getResources().getColor(R.color.red_1));
                tvDianyuan.setTextColor(getResources().getColor(R.color.progress_background));
                tvDianyuanL.setBackgroundColor(getResources().getColor(R.color.app_back_color));
                break;
            case 1:

                tvDianyuan.setTextColor(getResources().getColor(R.color.red_1));
                tvDianyuanL.setBackgroundColor(getResources().getColor(R.color.red_1));
                tvAdmin.setTextColor(getResources().getColor(R.color.progress_background));
                tvAdminL.setBackgroundColor(getResources().getColor(R.color.app_back_color));
                break;
        }
        basicinfo();
    }

    @OnClick({R.id.ll_0, R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.ll_4, R.id.ll_5, R.id.tv_left, R.id.ll_dianyuan, R.id.ll_admin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_dianyuan:
                setState(1);
                break;
            case R.id.ll_admin:
                setState(0);
                break;
            case R.id.ll_0:
                //TODO 营业执照
                break;
            case R.id.ll_1:
                //TODO 设定店员
                ClerkManagementActivity.storeName =name;
                ClerkManagementActivity.storeID = short_id;
                ClerkManagementActivity.short_state = state;
                startActivity(new Intent(context, ClerkManagementActivity.class));
                break;
            case R.id.ll_2:
                //TODO 店铺活动
                CouponsActivity.short_id=short_id;
                startActivity(new Intent(context, CouponsActivity.class));
                break;
            case R.id.ll_3:
                //TODO 查看账单
                OrderDetailsActivity.activity_type = 0;
                OrderDetailsActivity.shopid = short_id;
                OrderDetailsActivity.short_name = name;
                startActivity(new Intent(context, OrderDetailsActivity.class));
                break;
            case R.id.ll_4:
                //TODO 查看报表
                ReportActivity.short_id=short_id;
                ReportActivity.short_name=name;
                startActivity(new Intent(context, ReportActivity.class));
                break;
            case R.id.ll_5:
                //TODO 下载收款码
                EmployeeCollectionActivity.short_id = short_id;
                EmployeeCollectionActivity.name = name;
                EmployeeCollectionActivity.short_state = state;
                EmployeeCollectionActivity.qrcode = qrcode;
                startActivity(new Intent(context, EmployeeCollectionActivity.class));
                break;
        }
    }
}
