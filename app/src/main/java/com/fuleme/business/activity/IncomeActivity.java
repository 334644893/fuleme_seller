package com.fuleme.business.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.adapter.IncomeAdapter;
import com.fuleme.business.bean.IncomeBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的账户-收入
 */
public class IncomeActivity extends BaseActivity {
    int text_state = 0;//0：收缩状态 1：伸张状态
    @Bind(R.id.tv_tipstext)
    TextView tvTipstext;
    @Bind(R.id.rv_m_recyclerview)
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager;
    IncomeAdapter mAdapter;
    @Bind(R.id.iv_tubiao)
    ImageView ivTubiao;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    private List<IncomeBean> mDatas = new ArrayList<IncomeBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    public void initView() {

        tvTitle.setText("收入");
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(IncomeActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new IncomeAdapter(IncomeActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(IncomeActivity.this, LinearLayoutManager.VERTICAL));


    }

    protected void initData() {

//        TODO 假数据
        IncomeBean incomeBean = new IncomeBean();
        incomeBean.setmState(0);
        incomeBean.setmAmount(2020201);
        incomeBean.setmDate("2020-12-12");
        mDatas.add(incomeBean);
        IncomeBean incomeBean1 = new IncomeBean();
        incomeBean1.setmState(1);
        incomeBean1.setmAmount(2020201);
        incomeBean1.setmDate("2020-12-12");
        mDatas.add(incomeBean1);
        mDatas.add(incomeBean);
        mDatas.add(incomeBean1);
        mDatas.add(incomeBean1);
        mDatas.add(incomeBean);
        mDatas.add(incomeBean1);
        mDatas.add(incomeBean);
        mDatas.add(incomeBean1);
        mDatas.add(incomeBean1);
        mDatas.add(incomeBean);
    }


    @OnClick({R.id.tv_left, R.id.ll_tips})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_tips:
                if (text_state == 0) {
                    tvTipstext.setMaxLines(10);
                    text_state = 1;
                    ivTubiao.setImageDrawable(getResources().getDrawable(R.mipmap.icon31));
                } else if (text_state == 1) {
                    tvTipstext.setMaxLines(1);
                    text_state = 0;
                    ivTubiao.setImageDrawable(getResources().getDrawable(R.mipmap.icon32));
                }
                break;
        }
    }
}
