package com.fuleme.business.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.bean.OrderBean;

import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    NumberFormat nf = NumberFormat.getInstance();

    private List<OrderBean.DataBean> mDatas;
    private Context context;
    private onRecyclerViewItemClickListener itemClickListener = null;

    public OrderAdapter(Context context, List<OrderBean.DataBean> mDatas) {
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
        @Bind(R.id.ll_all)
        LinearLayout llAll;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            nf.setGroupingUsed(false);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_order_fragment, parent, false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (mDatas.get(position).getShort_name() != null) {
            holder.tv1.setText(mDatas.get(position).getShort_name());
        }
        holder.tv2.setText("¥ " + nf.format(mDatas.get(position).getY_amount()));
        holder.tv3.setText("¥ " + nf.format(mDatas.get(position).getT_amount()));
        holder.llAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null && mDatas.get(position).getShort_id() != null)
                    itemClickListener.onItemClick(v, mDatas.get(position));
            }
        });
    }

    public interface onRecyclerViewItemClickListener {
        //点击传递item信息接口
        void onItemClick(View v, OrderBean.DataBean dataBean);
    }

    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }


}
