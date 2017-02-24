package com.fuleme.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.adapter.ClerkManagementAdapter;
import com.fuleme.business.adapter.StoreAQAdapter;
import com.fuleme.business.bean.CMBean;
import com.fuleme.business.utils.DividerItemDecoration;
import com.fuleme.business.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 店员管理
 */
public class ClerkManagementActivity extends AppCompatActivity {
    final int TO = 997;
    final int FROM = 887;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_store_name)
    TextView tvStoreName;
    @Bind(R.id.m_recyclerView)
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager;
    ClerkManagementAdapter mAdapter;
    private List<CMBean> mDatas = new ArrayList<CMBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk_management);
        ButterKnife.bind(this);
        initData();
        initView();
    }

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
            public void onItemClick(View v, CMBean bean) {
            }
        });

    }

    protected void initData() {

//        TODO 假数据

        CMBean cmBean = new CMBean();
        cmBean.setName("汪晓龙");
        cmBean.setType("0");
        cmBean.setPhone("110110110110");
        CMBean cmBean1 = new CMBean();
        cmBean1.setName("报名量");
        cmBean1.setType("1");
        cmBean1.setPhone("120212110110");
        mDatas.add(cmBean);
        mDatas.add(cmBean1);
        mDatas.add(cmBean1);
        mDatas.add(cmBean1);
        mDatas.add(cmBean);
        mDatas.add(cmBean);
        mDatas.add(cmBean1);
        mDatas.add(cmBean1);
        mDatas.add(cmBean1);
        mDatas.add(cmBean1);
    }

    @OnClick({R.id.tv_left, R.id.ll_to_store, R.id.rl_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_to_store:
                break;
            case R.id.rl_add:
                //添加员工
                Intent intent = new Intent(ClerkManagementActivity.this, AddEmployeesActivity.class);
                startActivityForResult(intent, TO);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO && resultCode == FROM && data != null) {
            mDatas.add((CMBean) data.getExtras().get("cmbean"));
            mAdapter.notifyDataSetChanged();
        }
    }
}
