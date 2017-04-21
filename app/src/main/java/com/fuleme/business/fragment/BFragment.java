package com.fuleme.business.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuleme.business.R;
import com.fuleme.business.activity.OrderDetailsActivity;
import com.fuleme.business.adapter.OrderAdapter;
import com.fuleme.business.adapter.SinceMediaAdapter;
import com.fuleme.business.bean.OrderBean;
import com.fuleme.business.bean.SinceMediaBean;
import com.fuleme.business.bean.bannerBean;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BFragment extends Fragment {
    private static final String TAG = "BFragment";
    View view = null;
    private Context context;
    LinearLayoutManager linearLayoutManager;
    @Bind(R.id.m_recyclerview)
    RecyclerView mRecyclerview;
    SinceMediaAdapter mAdapter;
    private List<SinceMediaBean.DataBean> mDatas = new ArrayList<>();
//    private int[] mImageDatas = {R.mipmap.icon_zhifu, R.mipmap.icon_weixin_54, R.mipmap.icon_dazhong, R.mipmap.icon_jinri, R.mipmap.icon_koubei};
//    private String[] mTextDatas = {"支付宝服务窗口", "微信公众号管理", "大众点评", "今日头条", "口碑客"};
//    private String[] mTextContentDatas = {"每天帮我做促销", "每天帮我做促销", "每天帮我做促销", "每天帮我做促销", "每天帮我做促销"};
//    private String[] mUrlDatas = {"每天帮我做促销", "每天帮我做促销", "每天帮我做促销", "每天帮我做促销", "每天帮我做促销", "每天帮我做促销", "每天帮我做促销", "每天帮我做促销"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_administrator_b, container, false);
        context = getActivity();
        ButterKnife.bind(this, view);
        init();
        return view;
    }


    public void init() {
        selfmedia();
    }

    /**
     *
     */
    public void initView() {
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mAdapter = new SinceMediaAdapter(context, mDatas);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(new SinceMediaAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, String url) {

            }
        });
    }

    /**
     * 获取自媒体
     */
    private void selfmedia() {

        ((FragmentActivity) getActivity()).showLoading("加载中...");
        Call<SinceMediaBean> call = ((FragmentActivity) getActivity()).getApi().selfmedia();
        call.enqueue(new Callback<SinceMediaBean>() {
            @Override
            public void onResponse(Call<SinceMediaBean> call, Response<SinceMediaBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("登陆成功");
                        //TODO 初始化数据
                        mDatas = response.body().getData();
                        initView();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                    }

                } else {
                    LogUtil.i(response.message());
                }
                ((FragmentActivity) getActivity()).closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<SinceMediaBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                ((FragmentActivity) getActivity()).closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
