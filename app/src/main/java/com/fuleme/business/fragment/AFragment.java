package com.fuleme.business.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.CouponsActivity;
import com.fuleme.business.activity.EmployeeCollectionActivity;
import com.fuleme.business.activity.MemberManagementActivity;
import com.fuleme.business.activity.OrdersActivity;
import com.fuleme.business.activity.ReportActivity;
import com.fuleme.business.activity.ScanReceiptActivity;
import com.fuleme.business.activity.WebActivity;
import com.fuleme.business.adapter.AFragmentAdapter;
import com.fuleme.business.bean.AFragmentImageBean;
import com.fuleme.business.bean.bannerBean;
import com.fuleme.business.helper.APIService;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 首页页面
 */
public class AFragment extends Fragment {
    private static final String TAG = "AFragment";
    GridLayoutManager mGridLayoutManager;
    @Bind(R.id.rv_af)
    RecyclerView mRecyclerView;
    AFragmentAdapter mAdapter;
    @Bind(R.id.iv_1)
    ImageView iv1;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.iv_2)
    ImageView iv2;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.iv_3)
    ImageView iv3;
    @Bind(R.id.tv_3)
    TextView tv3;
    @Bind(R.id.iv_4)
    ImageView iv4;
    @Bind(R.id.tv_4)
    TextView tv4;
    @Bind(R.id.m_convenient_banner)
    ConvenientBanner mConvenientBanner;
    @Bind(R.id.ll_title_1)
    LinearLayout llTitle1;
    @Bind(R.id.ll_title_2)
    LinearLayout llTitle2;
    @Bind(R.id.ll_title_3)
    LinearLayout llTitle3;
    @Bind(R.id.ll_title_4)
    LinearLayout llTitle4;
    private List<bannerBean.DataBean> bannerImageList = new ArrayList<>();
    private List<AFragmentImageBean> mDatas = new ArrayList<AFragmentImageBean>();
    private int[] mImageDatas = {R.mipmap.icon_n_5, R.mipmap.icon_n_6, R.mipmap.icon_n_7, R.mipmap.icon_n_9, R.mipmap.icon_pinjia, R.mipmap.icon_n_10};
    private String[] mTextDatas = {"商家活动", "官方活动", "会员管理", "积分商城", "顾客评价", "供应链"};
    private String[] mTextContentDatas = {"每天帮我做促销", "客户引流不用愁", "大大提高复购率", "收款越多礼更多", "回头客就在这里", "一站式商品采购"};
    private int[] mTitleImageDatas = {R.mipmap.icon_n_1, R.mipmap.icon_n_2, R.mipmap.icon_n_3, R.mipmap.icon_n_4};
    private String[] mTitleTextDatas = {"收款", "收款码", "账本", "报表"};
    private static final int BANNERTIME = 5000;//banner轮播时间间隔

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_administrator_a, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init() {
        banner();
        initView();

    }


    public void initView() {
        /**
         * 设置上部导航
         *
         */
        initTopView();
        /**
         * 设置RecyclerView
         *
         */
        initRecyclerView();

    }


    public void initTopView() {
        //根据登录类型显示隐藏员工管理
        if ("0".equals(App.role)) {
            llTitle3.setVisibility(View.VISIBLE);
            llTitle4.setVisibility(View.VISIBLE);
        } else {
            llTitle3.setVisibility(View.GONE);
            llTitle4.setVisibility(View.GONE);
        }
        iv1.setImageResource(mTitleImageDatas[0]);
        iv2.setImageResource(mTitleImageDatas[1]);
        iv3.setImageResource(mTitleImageDatas[2]);
        iv4.setImageResource(mTitleImageDatas[3]);
        tv1.setText(mTitleTextDatas[0]);
        tv2.setText(mTitleTextDatas[1]);
        tv3.setText(mTitleTextDatas[2]);
        tv4.setText(mTitleTextDatas[3]);

    }

    public void initRecyclerView() {
        for (int i = 0; i < mImageDatas.length; i++) {
            AFragmentImageBean aFragmentImageBean = new AFragmentImageBean();
            aFragmentImageBean.setmItemImage(mImageDatas[i]);
            aFragmentImageBean.setmItemText(mTextDatas[i]);
            aFragmentImageBean.setmItemTextContent(mTextContentDatas[i]);
            mDatas.add(aFragmentImageBean);
        }
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new AFragmentAdapter(getActivity(), mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), GridLayoutManager.HORIZONTAL));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), GridLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(new AFragmentAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, String itemText) {

                if (mTextDatas[0].equals(itemText)) {
                    //商家活动
                    if (!TextUtils.isEmpty(App.short_id)) {
                        startActivity(new Intent(getActivity(), CouponsActivity.class));
                    } else {
                        ToastUtil.showMessage(R.string.nostore);
                    }

                } else if (mTextDatas[1].equals(itemText)) {
                    //官方活动
                    ToastUtil.showMessage("新功能正在开发");
                } else if (mTextDatas[2].equals(itemText)) {
                    //会员管理
                    if (!TextUtils.isEmpty(App.short_id)) {
                        startActivity(new Intent(getActivity(), MemberManagementActivity.class));
                    } else {
                        ToastUtil.showMessage(R.string.nostore);
                    }

                } else if (mTextDatas[3].equals(itemText)) {
                    //积分商城
                    ((FragmentActivity) getActivity()).select(1);

                }
//                else if (mTextDatas[4].equals(itemText)) {
//                    //汇总查询
//                    if (!TextUtils.isEmpty(App.short_id)) {
//                        startActivity(new Intent(getActivity(), AggregationQueryActivity.class));
//                    } else {
//                        ToastUtil.showMessage("您还没有店铺，快去添加一个吧");
//                    }
//
//                }
                else if (mTextDatas[4].equals(itemText)) {
                    //顾客评价
//                    if(!TextUtils.isEmpty(App.short_id)){
//                    }else{
//                        ToastUtil.showMessage("您还没有店铺，快去添加一个吧");
//                    }
                    ToastUtil.showMessage("新功能正在开发");
                } else if (mTextDatas[5].equals(itemText)) {
                    //供应链
                    ToastUtil.showMessage("新功能正在开发");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll_title_1, R.id.ll_title_2, R.id.ll_title_3, R.id.ll_title_4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title_1:
                //扫一扫
                if (!TextUtils.isEmpty(App.short_id)) {
                    startActivity(new Intent(getActivity(), ScanReceiptActivity.class));
                } else {
                    ToastUtil.showMessage(R.string.nostore);
                }

                break;
            case R.id.ll_title_2:
                //付款码
                startActivity(new Intent(getActivity(), EmployeeCollectionActivity.class));
                break;
            case R.id.ll_title_3:
                //账本
                startActivity(new Intent(getActivity(), OrdersActivity.class));
                break;
            case R.id.ll_title_4:
                //报表
                if (!TextUtils.isEmpty(App.short_id)) {
                    startActivity(new Intent(getActivity(), ReportActivity.class));
                } else {
                    ToastUtil.showMessage(R.string.nostore);
                }

                break;
        }
    }
    // 开始自动翻页

    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        mConvenientBanner.startTurning(BANNERTIME);
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        mConvenientBanner.stopTurning();
    }

    public void initConvenientBanner() {
        mConvenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, bannerImageList)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.icon_yuan, R.mipmap.icon_yuana})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

        mConvenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                WebActivity.url = bannerImageList.get(position).getUrl();
                WebActivity.title = App.merchant;
                if (!TextUtils.isEmpty(WebActivity.url))
                    startActivity(new Intent(getActivity(), WebActivity.class));
            }
        });
    }

    public class LocalImageHolderView implements Holder<bannerBean.DataBean> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int i, bannerBean.DataBean dataBean) {
            //glide加载出图片，data是传过来的图片地址，
            Glide.with(context).load(APIService.SERVER_IP + dataBean.getImg()).into(imageView);
        }

    }

    /**
     * 获取banner图
     */
    private void banner() {

        ((FragmentActivity) getActivity()).showLoading("加载中...");
        Call<bannerBean> call = ((FragmentActivity) getActivity()).getApi().banner();
        call.enqueue(new Callback<bannerBean>() {
            @Override
            public void onResponse(Call<bannerBean> call, Response<bannerBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("成功");
                        bannerImageList = response.body().getData();
                        initConvenientBanner();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }

                } else {
                    LogUtil.i(response.message());
                }
                ((FragmentActivity) getActivity()).closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<bannerBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                ((FragmentActivity) getActivity()).closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }

}
