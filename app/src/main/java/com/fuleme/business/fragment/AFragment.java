package com.fuleme.business.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuleme.business.R;
import com.fuleme.business.activity.AggregationQueryActivity;
import com.fuleme.business.activity.IncomeActivity;
import com.fuleme.business.adapter.AFragmentAdapter;
import com.fuleme.business.bean.AFragmentImageBean;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 首页页面
 */
public class AFragment extends Fragment {

    GridLayoutManager mGridLayoutManager;
    @Bind(R.id.rv_af)
    RecyclerView mRecyclerView;
    AFragmentAdapter mAdapter;
    private List<AFragmentImageBean> mDatas = new ArrayList<AFragmentImageBean>();
    private int[] mImageDatas = {R.mipmap.icon35, R.mipmap.icon36};
    private String[] mTextDatas = {"我的账户", "汇总查询"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_administrator_a, container, false);
        ButterKnife.bind(this, view);
        init();


        return view;
    }

    public void init() {
        initView();
        initData();
    }

    public void initView() {


        /**
         * 设置列表
         */
        for (int i = 0; i < mImageDatas.length; i++) {
            AFragmentImageBean aFragmentImageBean = new AFragmentImageBean();
            aFragmentImageBean.setmItemImage(mImageDatas[i]);
            aFragmentImageBean.setmItemText(mTextDatas[i]);
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
                    ToastUtil.showMessage("点击了" + itemText + ",mTextDatas[0]:" + mTextDatas[0]);
                    //我的账户
                    startActivity(new Intent(getActivity(), IncomeActivity.class));
                } else if (mTextDatas[1].equals(itemText)) {
                    ToastUtil.showMessage("点击了" + itemText);
                    //汇总查询
                    startActivity(new Intent(getActivity(), AggregationQueryActivity.class));
                }
            }
        });
    }

    public void initData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
