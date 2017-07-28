package com.fuleme.business.activity.Version2;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.activity.RegistrationStoreActivity;
import com.fuleme.business.adapter.TextAdapter;
import com.fuleme.business.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private List<String> mDatas = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    TextAdapter mAdapter;
    public static int INTENTTYPE = 0;//0银行

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        tvTitle.setText("选择银行");
        initView();
    }

    public void initView() {
        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(ListActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new TextAdapter(ListActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new TextAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, String bank) {
                switch (INTENTTYPE) {
                    case 0:
                        RegistrationStoreActivity.BANK = bank;
                        finish();
                        break;
                }
            }
        });
    }

    @OnClick(R.id.tv_left)
    public void onClick() {
        finish();
    }
}
