package com.fuleme.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.activity.OrderContentActivity;
import com.fuleme.business.activity.OrderDetailsActivity;
import com.fuleme.business.bean.OrderDetailsBean;
import com.fuleme.business.utils.DividerItemDecoration;

import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class OrderDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    NumberFormat nf = NumberFormat.getInstance();
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //底部FootView


    private List<OrderDetailsBean.DataBean> mDatas;
    private Context context;

    public OrderDetailsAdapter(Context context, List<OrderDetailsBean.DataBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_1)
        TextView tv1;
        @Bind(R.id.tv_2)
        TextView tv2;
        @Bind(R.id.tv_3)
        TextView tv3;
        @Bind(R.id.rv_m_recyclerview)
        RecyclerView mRecyclerView;
        LinearLayoutManager linearLayoutManager;
        OrderDetailsListAdapter mAdapter;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            nf.setGroupingUsed(false);
            /**
             * 设置列表
             */
            linearLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;

        public FootViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_order_details, parent, false));
            return hodler;
        } else if (viewType == TYPE_FOOTER) {
            FootViewHolder footViewHolder = new FootViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_textview, parent, false));
            return footViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.tv1.setText(mDatas.get(position).getDate());
            myViewHolder.tv2.setText("共" + mDatas.get(position).getCount() + "笔");
            myViewHolder.tv3.setText("¥ " + nf.format(mDatas.get(position).getTotal_fee()));
            /**
             * 设置列表
             */
            myViewHolder.mAdapter = new OrderDetailsListAdapter(context, mDatas.get(position).getDetails());
            myViewHolder.mRecyclerView.setAdapter(myViewHolder.mAdapter);
            //点击进入内容详情
            myViewHolder.mAdapter.setOnItemClickListener(new OrderDetailsListAdapter.onRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View v, OrderDetailsBean.DataBean.DetailsBean bean) {
                    OrderContentActivity.title = OrderDetailsActivity.short_name;
                    OrderContentActivity.out_trade_no = bean.getOut_trade_no();
                    context.startActivity(new Intent(context, OrderContentActivity.class));
                }
            });

        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            if (OrderDetailsActivity.textState) {
                footViewHolder.tvName.setText(R.string.load);
            } else {
                if (mDatas.size() == 0) {
                    footViewHolder.tvName.setVisibility(View.GONE);
                } else {
                    footViewHolder.tvName.setVisibility(View.VISIBLE);
                }
                footViewHolder.tvName.setText(R.string.nomore);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }

    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }
}
