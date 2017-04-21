package com.fuleme.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.activity.AggregationQueryActivity;
import com.fuleme.business.activity.StartActivity;
import com.fuleme.business.bean.AFragmentImageBean;
import com.fuleme.business.utils.ToastUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/7.
 */

public class AFragmentAdapter extends RecyclerView.Adapter<AFragmentAdapter.MyViewHolder> {


    private List<AFragmentImageBean> mDatas;
    private Context context;
    private onRecyclerViewItemClickListener itemClickListener = null;

    public AFragmentAdapter(Context context, List<AFragmentImageBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_a_fragment)
        ImageView ivAFragment;
        @Bind(R.id.tv_a_fragment)
        TextView tvAFragment;
        @Bind(R.id.tv_a_fragment_text)
        TextView tvAFragment_text;
        @Bind(R.id.ll_a_fragment)
        LinearLayout llAFragment;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_afragment_g, parent, false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.ivAFragment.setImageResource(mDatas.get(position).getmItemImage());
        holder.tvAFragment.setText(mDatas.get(position).getmItemText());
        holder.tvAFragment_text.setText(mDatas.get(position).getmItemTextContent());
        holder.llAFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onItemClick(v, mDatas.get(position).getmItemText());
            }
        });

    }

    public interface onRecyclerViewItemClickListener {
        //点击传递item信息接口
        void onItemClick(View v, String itemText);
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
