package com.fuleme.business.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.adapter.EAdapter;
import com.fuleme.business.bean.ProvBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.CustomViewPager;
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
 * 添加省市区
 */
public class RegionActivity extends BaseActivity {
    private static final String TAG = "RegionActivity";
    public static int TAGRESULT = 888;
    private String province = "";
    private String city = "";
    private String area = "";

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.viewpager)
    CustomViewPager viewpager;

    @OnClick(R.id.tv_left)
    public void onClick() {
        if (viewpager.getCurrentItem() == 0) {
            finish();
        } else {
            viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
        }
    }

    ProvBean provBean_1;
    private View view1, view2, view3;
    private List<View> viewList = new ArrayList<View>();
    RecyclerView mRecyclerView_1, mRecyclerView_2, mRecyclerView_3;
    LinearLayoutManager linearLayoutManager_1, linearLayoutManager_2, linearLayoutManager_3;
    EAdapter mAdapter_1, mAdapter_2, mAdapter_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);
        ButterKnife.bind(this);
        tvTitle.setText("选择省");
        getprov();

    }


    public void init() {
        initTitle();
        initViewPager();


    }

    public void initViewPager() {
        LayoutInflater lf = getLayoutInflater().from(this);
        view1 = lf.inflate(R.layout.activity_recyclerview, null);
        view2 = lf.inflate(R.layout.activity_recyclerview, null);
        view3 = lf.inflate(R.layout.activity_recyclerview, null);

        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setCurrentItem(0);
        tvTitle.setText("选择省");
/**
 * 初始化列表并设置省列表数据
 */

        mRecyclerView_1 = (RecyclerView) view1.findViewById(R.id.recyclerview);
        mRecyclerView_2 = (RecyclerView) view2.findViewById(R.id.recyclerview);
        mRecyclerView_3 = (RecyclerView) view3.findViewById(R.id.recyclerview);
        linearLayoutManager_1 = new LinearLayoutManager(RegionActivity.this);
        linearLayoutManager_2 = new LinearLayoutManager(RegionActivity.this);
        linearLayoutManager_3 = new LinearLayoutManager(RegionActivity.this);
        mRecyclerView_1.setLayoutManager(linearLayoutManager_1);
        mRecyclerView_2.setLayoutManager(linearLayoutManager_2);
        mRecyclerView_3.setLayoutManager(linearLayoutManager_3);
        if (provBean_1 != null) {
            mAdapter_1 = new EAdapter(RegionActivity.this, provBean_1.getData());
        }
        mRecyclerView_1.setAdapter(mAdapter_1);
        mRecyclerView_1.addItemDecoration(new DividerItemDecoration(RegionActivity.this, LinearLayoutManager.VERTICAL));
        mRecyclerView_2.addItemDecoration(new DividerItemDecoration(RegionActivity.this, LinearLayoutManager.VERTICAL));
        //选择省
        mAdapter_1.setOnItemClickListener(new EAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, ProvBean.DataBean dataBean) {
                province = dataBean.getName();
                geturbn(dataBean.getCode());
            }
        });


    }

    public void initTitle() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tvTitle.setText("选择省");
                        break;
                    case 1:
                        tvTitle.setText(province);
                        break;
                    case 2:
                        tvTitle.setText(province + "·" + city);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        // 实例化一个页卡
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        // 销毁一个页卡
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    };
    private Dialog mLoading;

    /**
     * 省接口
     */
    private void getprov() {
        mLoading = LoadingDialogUtils.createLoadingDialog(RegionActivity.this, "加载中...");//添加等待框
        Call<ProvBean> call = getApi().getprov();

        call.enqueue(new Callback<ProvBean>() {
            @Override
            public void onResponse(Call<ProvBean> call, Response<ProvBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                        provBean_1 = response.body();
                        //TODO 初始化数据
                        init();
                    } else {
                        ToastUtil.showMessage("获取数据失败");
                        LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                    }

                } else {
                    LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                    LogUtil.i("失败response.message():" + response.message());

                }
            }

            @Override
            public void onFailure(Call<ProvBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }

    /**
     * 市接口
     *
     * @param pcode 省ID
     */
    private void geturbn(String pcode) {
        mLoading = LoadingDialogUtils.createLoadingDialog(RegionActivity.this, "加载中...");//添加等待框
        Call<ProvBean> call = getApi().geturbn(pcode);

        call.enqueue(new Callback<ProvBean>() {
            @Override
            public void onResponse(Call<ProvBean> call, Response<ProvBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                        //TODO 初始化数据
                        mAdapter_2 = new EAdapter(RegionActivity.this, response.body().getData());
                        mRecyclerView_2.setAdapter(mAdapter_2);
                        mAdapter_2.setOnItemClickListener(new EAdapter.onRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View v, ProvBean.DataBean dataBean) {
                                city = dataBean.getName();
                                getarea(dataBean.getCode());

                            }
                        });
                        viewpager.setCurrentItem(1);
                    } else {
                        ToastUtil.showMessage("获取数据失败");
                        LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                    }

                } else {
                    LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                    LogUtil.i("失败response.message():" + response.message());

                }
            }

            @Override
            public void onFailure(Call<ProvBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }

    /**
     * 区接口
     *
     * @param pcode 市ID
     */
    private void getarea(String pcode) {
        mLoading = LoadingDialogUtils.createLoadingDialog(RegionActivity.this, "加载中...");//添加等待框
        Call<ProvBean> call = getApi().getarea(pcode);

        call.enqueue(new Callback<ProvBean>() {
            @Override
            public void onResponse(Call<ProvBean> call, Response<ProvBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                        //TODO 初始化数据
                        mAdapter_3 = new EAdapter(RegionActivity.this, response.body().getData());
                        mRecyclerView_3.setAdapter(mAdapter_3);
                        mAdapter_3.setOnItemClickListener(new EAdapter.onRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View v, ProvBean.DataBean dataBean) {
                                area = dataBean.getName();
                                Intent intent = new Intent(RegionActivity.this, RegistrationInformationActivity.class);
                                intent.putExtra("getarea", province + city + area);
                                intent.putExtra("getareaId", dataBean.getCode());
                                setResult(TAGRESULT, intent);
                                finish();

                            }
                        });
                        viewpager.setCurrentItem(2);
                    } else {
                        ToastUtil.showMessage("获取数据失败");
                        LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                    }

                } else {
                    LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                    LogUtil.i("失败response.message():" + response.message());

                }
            }

            @Override
            public void onFailure(Call<ProvBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }
    @Override
    public void onBackPressed() {
        if (viewpager.getCurrentItem() == 0) {
            finish();
        } else {
            viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
        }
    }
}
