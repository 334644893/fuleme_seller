//package com.fuleme.business.activity;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.fuleme.business.R;
//import com.fuleme.business.adapter.EAdapter;
//import com.fuleme.business.adapter.IndustryAdapter;
//import com.fuleme.business.adapter.IndustryNextAdapter;
//import com.fuleme.business.bean.IndustryBean;
//import com.fuleme.business.bean.ProvBean;
//import com.fuleme.business.common.BaseActivity;
//import com.fuleme.business.helper.GsonUtils;
//import com.fuleme.business.utils.DividerItemDecoration;
//import com.fuleme.business.utils.LogUtil;
//import com.fuleme.business.utils.ToastUtil;
//import com.fuleme.business.widget.CustomViewPager;
//import com.fuleme.business.widget.LoadingDialogUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * ADD行业
// */
//public class AddIndustryActivity extends BaseActivity {
//    private static final String TAG = "AddIndustryActivity";
//    public static int TAGRESULT = 777;
//    private String industry = "";//选择的一级行业
//
//    @Bind(R.id.tv_title)
//    TextView tvTitle;
//    @Bind(R.id.viewpager)
//    CustomViewPager viewpager;
//
//    @OnClick(R.id.tv_left)
//    public void onClick() {
//        if (viewpager.getCurrentItem() == 0) {
//            finish();
//        } else {
//            viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
//        }
//    }
//
//    IndustryBean industryBean;
//    private View view1, view2;
//    private List<View> viewList = new ArrayList<View>();
//    RecyclerView mRecyclerView_1, mRecyclerView_2;
//    LinearLayoutManager linearLayoutManager_1, linearLayoutManager_2;
//    IndustryAdapter mAdapter_1;
//    IndustryNextAdapter mAdapter_2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_region);
//        ButterKnife.bind(this);
//        tvTitle.setText("选择行业");
//        getoprcls();
//
//    }
//
//
//    public void init() {
//        initTitle();
//        initViewPager();
//
//
//    }
//
//    public void initViewPager() {
//        LayoutInflater lf = getLayoutInflater().from(this);
//        view1 = lf.inflate(R.layout.activity_recyclerview, null);
//        view2 = lf.inflate(R.layout.activity_recyclerview, null);
//
//        viewList.add(view1);
//        viewList.add(view2);
//        viewpager.setAdapter(pagerAdapter);
//        viewpager.setCurrentItem(0);
///**
// * 初始化列表并设置省列表数据
// */
//
//        mRecyclerView_1 = (RecyclerView) view1.findViewById(R.id.recyclerview);
//        mRecyclerView_2 = (RecyclerView) view2.findViewById(R.id.recyclerview);
//        linearLayoutManager_1 = new LinearLayoutManager(AddIndustryActivity.this);
//        linearLayoutManager_2 = new LinearLayoutManager(AddIndustryActivity.this);
//        mRecyclerView_1.setLayoutManager(linearLayoutManager_1);
//        mRecyclerView_2.setLayoutManager(linearLayoutManager_2);
//        if (industryBean != null) {
//            mAdapter_1 = new IndustryAdapter(AddIndustryActivity.this, industryBean.getData());
//        }
//        mRecyclerView_1.setAdapter(mAdapter_1);
//        mRecyclerView_1.addItemDecoration(new DividerItemDecoration(AddIndustryActivity.this, LinearLayoutManager.VERTICAL));
//        mRecyclerView_2.addItemDecoration(new DividerItemDecoration(AddIndustryActivity.this, LinearLayoutManager.VERTICAL));
//        //选择一级行业
//        mAdapter_1.setOnItemClickListener(new IndustryAdapter.onRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View v, IndustryBean.DataBeanX dataBean) {
//                industry = dataBean.getName();
//                industryNext(dataBean);
//            }
//        });
//
//
//    }
//
//    /**
//     * 初始化第二级目录
//     *
//     * @param dataBean
//     */
//    private void industryNext(IndustryBean.DataBeanX dataBean) {
//        //TODO 初始化数据
//        mAdapter_2 = new IndustryNextAdapter(AddIndustryActivity.this, dataBean.getData());
//        mRecyclerView_2.setAdapter(mAdapter_2);
//        viewpager.setCurrentItem(1);
//        mAdapter_2.setOnItemClickListener(new IndustryNextAdapter.onRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View v, IndustryBean.DataBeanX.DataBean dataBean) {
//                Intent intent = new Intent(AddIndustryActivity.this, RegistrationInformationActivity.class);
//                intent.putExtra("industryId", dataBean.getId());
//                intent.putExtra("industryName", industry+"-"+dataBean.getName());
//                setResult(TAGRESULT, intent);
//                finish();
//            }
//        });
//
//    }
//
//    /**
//     * 初始化标题
//     */
//    public void initTitle() {
//        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                switch (position) {
//                    case 0:
//                        tvTitle.setText("选择行业");
//                        break;
//                    case 1:
//                        tvTitle.setText(industry);
//                        break;
//                }
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//
//    }
//
//    PagerAdapter pagerAdapter = new PagerAdapter() {
//        // 实例化一个页卡
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            container.addView(viewList.get(position));
//            return viewList.get(position);
//        }
//
//        // 销毁一个页卡
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView(viewList.get(position));
//        }
//
//        @Override
//        public int getCount() {
//            return viewList.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//    };
//    private Dialog mLoading;
//
//    /**
//     * 第一级行业接口
//     */
//    private void getoprcls() {
//        mLoading = LoadingDialogUtils.createLoadingDialog(AddIndustryActivity.this, "加载中...");//添加等待框
//        Call<IndustryBean> call = getApi().getoprcls();
//
//        call.enqueue(new Callback<IndustryBean>() {
//            @Override
//            public void onResponse(Call<IndustryBean> call, Response<IndustryBean> response) {
//                if (response.isSuccessful()) {
//                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
//                        // do SomeThing
//                        LogUtil.i("成功");
//                        LoadingDialogUtils.closeDialog(mLoading);//取消等待框
//                        industryBean = response.body();
//                        //TODO 初始化数据
//                        init();
//                    } else {
//                        ToastUtil.showMessage("获取数据失败");
//                        LoadingDialogUtils.closeDialog(mLoading);//取消等待框
//                    }
//
//                } else {
//                    LoadingDialogUtils.closeDialog(mLoading);//取消等待框
//                    LogUtil.i("失败response.message():" + response.message());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<IndustryBean> call, Throwable t) {
//                LogUtil.e(TAG, t.toString());
//                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
//                ToastUtil.showMessage("超时");
//            }
//
//        });
//    }
//
//
//
//    @Override
//    public void onBackPressed() {
//        if (viewpager.getCurrentItem() == 0) {
//            finish();
//        } else {
//            viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
//        }
//    }
//}
