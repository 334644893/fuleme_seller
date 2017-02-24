package com.fuleme.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fuleme.business.R;
import com.fuleme.business.adapter.StoreAQAdapter;
import com.fuleme.business.bean.StoreAQBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 汇总查询-查询店铺
 */
public class StoreAggregationQueryActivity extends BaseActivity {
    final int FROMSTORE = 888;
    @Bind(R.id.rv_store_agg)
    RecyclerView mRecyclerView;
    private List<String> mDatas = new ArrayList<String>();
    //    private List<StoreAQBean> mDatas = new ArrayList<StoreAQBean>();
    LinearLayoutManager linearLayoutManager;
    StoreAQAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_aggregation_query);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    public void initView() {


        /**
         * 设置列表
         */
        linearLayoutManager = new LinearLayoutManager(StoreAggregationQueryActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new StoreAQAdapter(StoreAggregationQueryActivity.this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(StoreAggregationQueryActivity.this, LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(new StoreAQAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, String itemText) {
                Intent intent = new Intent(StoreAggregationQueryActivity.this, AggregationQueryActivity.class);
                intent.putExtra("FromStore", itemText);
                setResult(FROMSTORE, intent);
                finish();

            }
        });

    }

    protected void initData() {

//        TODO 假数据
//        StoreAQBean storeAQBean = new StoreAQBean();
//        storeAQBean.setmItemText("全部店铺");
//        storeAQBean.setmState(0);
//        StoreAQBean storeAQBean_1 = new StoreAQBean();
//        storeAQBean.setmItemText("周黑鸭");
//        storeAQBean.setmState(1);

        mDatas.add("全部店铺");
        mDatas.add("周黑鸭");
        mDatas.add("周黑鸭1212");
        mDatas.add("周黑鸭31313");
        mDatas.add("周黑鸭441414");
    }

    @OnClick({R.id.tv_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;

        }
    }
}
