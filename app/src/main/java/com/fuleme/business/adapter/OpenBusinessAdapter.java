package com.fuleme.business.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.bean.OpenBusinessBean;
import com.fuleme.business.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class OpenBusinessAdapter extends RecyclerView.Adapter<OpenBusinessAdapter.MyViewHolder> {


    private ArrayList<OpenBusinessBean> mDatas;
    private Context context;
    private onRecyclerViewItemClickListener itemClickListener = null;

    public OpenBusinessAdapter(Context context, ArrayList<OpenBusinessBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.iv_btm)
        ImageView ivBtm;
        @Bind(R.id.tv_rate)
        EditText tvRate;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_open_business, parent, false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvName.setText(mDatas.get(position).getBusinessName());
        switch (mDatas.get(position).getState()) {
            case 0://未开通
                holder.ivBtm.setImageResource(R.mipmap.icon_off);
                break;
            case 1://开通
                holder.ivBtm.setImageResource(R.mipmap.icon_on);
                break;
        }
        holder.tvRate.setText(mDatas.get(position).getRate() + "");
        holder.ivBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mDatas.get(position).getState()) {
                    case 0://点击开通
                        holder.ivBtm.setImageResource(R.mipmap.icon_on);
                        mDatas.get(position).setState(1);
                        ToastUtil.showMessage(mDatas.get(position).getBusinessName() + "已开通");
                        break;
                    case 1://点击关闭
                        holder.ivBtm.setImageResource(R.mipmap.icon_off);
                        mDatas.get(position).setState(0);
                        ToastUtil.showMessage(mDatas.get(position).getBusinessName() + "未开通");
                        break;
                }

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
