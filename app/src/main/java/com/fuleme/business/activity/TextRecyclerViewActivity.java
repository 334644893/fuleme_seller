package com.fuleme.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fuleme.business.R;
import com.fuleme.business.adapter.TextAdapter;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.utils.DividerItemDecoration;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 普通文本列表
 */
public class TextRecyclerViewActivity extends BaseActivity {
    public static final int FROMSTORE = 200;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private String[] mDatas = {};
    private String Type = "";
    LinearLayoutManager linearLayoutManager;
    TextAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    public void initView() {


        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(TextRecyclerViewActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new TextAdapter(TextRecyclerViewActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(TextRecyclerViewActivity.this, LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(new TextAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent=null;
//                if ("ll_acct_typ".equals(Type)) {
//                    intent= new Intent(TextRecyclerViewActivity.this, RegistrationInformationActivity.class);
//
//                }
//                if(intent!=null){
//                    intent.putExtra("FromStore", position);
//                    setResult(FROMSTORE, intent);
//
//                }
//                finish();
            }
        });

    }

    protected void initData() {
//        Bundle b = this.getIntent().getExtras();
//        mDatas = b.getStringArray("data");
//        Type = this.getIntent().getStringExtra("type");
    }
}
