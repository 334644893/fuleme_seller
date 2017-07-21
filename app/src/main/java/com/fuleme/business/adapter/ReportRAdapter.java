package com.fuleme.business.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.bean.OrderDetailsBean;
import com.fuleme.business.bean.ReportRBean;

import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class ReportRAdapter extends RecyclerView.Adapter<ReportRAdapter.MyViewHolder> {

    private onRecyclerViewItemClickListener itemClickListener = null;
    NumberFormat nf = NumberFormat.getInstance();
    private List<ReportRBean> mDatas;
    private Context context;

    public ReportRAdapter(Context context, List<ReportRBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ll_a_fragment)
        LinearLayout llAFragment;
        @Bind(R.id.iv_a_fragment)
        ImageView ivAFragment;
        @Bind(R.id.tv_1)
        TextView tv1;
        @Bind(R.id.tv_2)
        TextView tv2;
        @Bind(R.id.tv_title)
        TextView tvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            nf.setGroupingUsed(false);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_report_r, parent, false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.ivAFragment.setImageResource(mDatas.get(position).getmItemImage());
        holder.tvTitle.setText(mDatas.get(position).getTitle());
        holder.tv1.setText("金额: " + nf.format(mDatas.get(position).getTotal_fee()) + "元");
        holder.tv2.setText("笔数: " + mDatas.get(position).getNumber() + "笔");
        holder.llAFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onItemClick(v, position);
            }
        });
    }


    public interface onRecyclerViewItemClickListener {
        //点击传递item信息接口
        void onItemClick(View v, int position);
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
