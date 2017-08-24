package com.fuleme.business.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.bean.OrderDetailsBean;
import com.fuleme.business.utils.DateUtil;

import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class OrderDetailsListAdapter extends RecyclerView.Adapter<OrderDetailsListAdapter.MyViewHolder> {


    private onRecyclerViewItemClickListener itemClickListener = null;
    NumberFormat nf = NumberFormat.getInstance();
    private List<OrderDetailsBean.DataBean.DetailsBean> mDatas;
    private Context context;

    public OrderDetailsListAdapter(Context context, List<OrderDetailsBean.DataBean.DetailsBean> mDatas) {
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
                .inflate(R.layout.item_order_details_list, parent, false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (mDatas.get(position).getTrade_type().contains(".")) {
            if (App.weixin.equals(mDatas.get(position).getTrade_type().split("\\.")[1])) {
                holder.iv1.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon13));
            } else if (App.alipay.equals(mDatas.get(position).getTrade_type().split("\\.")[1])) {
                holder.iv1.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon12));
            }
        }
        //订单时间
        holder.tv1.setText(DateUtil.stampToDate(mDatas.get(position).getTime_end(), DateUtil.DATE_3));
        //订单金额
        holder.tv2.setText("¥ " + nf.format(mDatas.get(position).getTotal_fee()));
        //订单尾号
        String str = mDatas.get(position).getOut_trade_no();
        if (!TextUtils.isEmpty(str)) {
        holder.tv3.setText(str.substring(str.length() - 4, str.length()));
        }
        //点击进入内容页面
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
        void onItemClick(View v, OrderDetailsBean.DataBean.DetailsBean bean);
    }

    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }

}
