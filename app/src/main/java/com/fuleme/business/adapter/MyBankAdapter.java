package com.fuleme.business.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.bean.MyBankBean;

import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class MyBankAdapter extends RecyclerView.Adapter<MyBankAdapter.MyViewHolder> {


    private onRecyclerViewItemClickListener itemClickListener = null;
    NumberFormat nf = NumberFormat.getInstance();
    private List<MyBankBean.DataBean> mDatas;
    private Context context;

    public MyBankAdapter(Context context, List<MyBankBean.DataBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_1)
        TextView tv1;
        @Bind(R.id.tv_2)
        TextView tv2;
        @Bind(R.id.ll_1)
        LinearLayout ll1;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            nf.setGroupingUsed(false);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_my_banks, parent, false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyViewHolder hodler, final int position) {
        hodler.tv1.setText(mDatas.get(position).getAccount_bank());
        if (mDatas.get(position).getBankcard().length() >= 4) {// 判断是否长度大于等于4
            hodler.tv2.setText("**** **** **** " + mDatas.get(position).getBankcard().substring(mDatas.get(position).getBankcard().length() - 4));
        }
        hodler.ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null){
                    itemClickListener.onItemClick(v, mDatas.get(position));
            }
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
        void onItemClick(View v, MyBankBean.DataBean bean);
    }

    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }

}
