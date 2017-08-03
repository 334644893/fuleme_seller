package com.fuleme.business.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.bean.BalanceBean;
import com.fuleme.business.utils.DateUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.MyViewHolder> {

    private List<BalanceBean.DataBean.ListBean> mDatas;
    private Context context;
    private onRecyclerViewItemClickListener itemClickListener = null;

    public BalanceAdapter(Context context, List<BalanceBean.DataBean.ListBean> mDatas) {
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
        @Bind(R.id.tv_4)
        TextView tv4;
        @Bind(R.id.tv_5)
        TextView tv5;
        @Bind(R.id.tv_8)
        TextView tv8;
        @Bind(R.id.tv_11)
        TextView tv11;
        @Bind(R.id.tv_6)
        TextView tv6;
        @Bind(R.id.tv_7)
        TextView tv7;
        @Bind(R.id.tv_9)
        TextView tv9;
        @Bind(R.id.tv_10)
        TextView tv10;
        @Bind(R.id.tv_12)
        TextView tv12;
        @Bind(R.id.tv_13)
        TextView tv13;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_balance, parent, false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        if (mDatas.get(position).getState() == 1) {
            //完成
            holder.tv4.setText("已到账");
            holder.tv4.setTextColor(context.getResources().getColor(R.color.theme));
            holder.tv2.setText("提现成功");
            holder.tv10.setText(DateUtil.stampToDate(mDatas.get(position).getTime_end() + "", DateUtil.DATE_2));

        } else {
            //未完成
            holder.tv3.setText("提现到账发起");
            holder.tv4.setText("待审核");
            holder.tv4.setTextColor(context.getResources().getColor(R.color.progress_background));
            holder.tv2.setText("申请提现");
            holder.tv9.setVisibility(View.INVISIBLE);
            holder.tv10.setVisibility(View.INVISIBLE);
        }

        holder.tv1.setText(DateUtil.stampToDate(mDatas.get(position).getCreate_time() + "", DateUtil.DATE_2));
        holder.tv7.setText(DateUtil.stampToDate(mDatas.get(position).getCreate_time() + "", DateUtil.DATE_2));
        holder.tv11.setText("￥"+mDatas.get(position).getMoney());
        holder.tv13.setText(mDatas.get(position).getBankcard());
    }

    public interface onRecyclerViewItemClickListener {
        //点击传递item信息接口
        void onItemClick(View v, String url);
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
