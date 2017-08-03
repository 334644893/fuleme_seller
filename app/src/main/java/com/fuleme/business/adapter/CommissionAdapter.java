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
import com.fuleme.business.bean.MyCommissionBean;
import com.fuleme.business.utils.DateUtil;

import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class CommissionAdapter extends RecyclerView.Adapter<CommissionAdapter.MyViewHolder> {



    private onRecyclerViewItemClickListener itemClickListener = null;
    NumberFormat nf = NumberFormat.getInstance();
    private List<MyCommissionBean.DataBean.ListBean> mDatas;
    private Context context;

    public CommissionAdapter(Context context, List<MyCommissionBean.DataBean.ListBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_1)
        ImageView iv1;
        @Bind(R.id.tv_1)
        TextView tv1;
        @Bind(R.id.tv_2)
        TextView tv2;
        @Bind(R.id.tv_3)
        TextView tv3;
        @Bind(R.id.tv_4)
        TextView tv4;
        @Bind(R.id.tv_5)
        TextView tv5;
        @Bind(R.id.tv_6)
        TextView tv6;
        @Bind(R.id.ll_linlayout)
        LinearLayout llLinlayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            nf.setGroupingUsed(false);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_commission_list, parent, false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if ("weixin".equals(mDatas.get(position).getTrade_type())) {
            holder.iv1.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_weixin));
        } else if ("alipay".equals(mDatas.get(position).getTrade_type())) {
            holder.iv1.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_zhifubao));
        }
        holder.tv1.setText(mDatas.get(position).getShort_name());
        if(mDatas.get(position).getOrderid().length()>3){
            holder.tv2.setText("订单尾号("+mDatas.get(position).getOrderid().substring(mDatas.get(position).getOrderid().length()-4)+")");
        }
        holder.tv3.setText("¥ " + nf.format(mDatas.get(position).getTotal_fee()));
        holder.tv4.setText(DateUtil.stampToDate(mDatas.get(position).getCreate_time(), DateUtil.DATE_3));
        holder.tv5.setText("¥ " + nf.format(mDatas.get(position).getMoney()));
        holder.tv6.setText(Double.valueOf(mDatas.get(position).getRate())*100+"%");
        holder.llLinlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onItemClick(v, mDatas.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    public interface onRecyclerViewItemClickListener {
        //点击传递item信息接口
        void onItemClick(View v, MyCommissionBean.DataBean.ListBean bean);
    }

    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }

}
