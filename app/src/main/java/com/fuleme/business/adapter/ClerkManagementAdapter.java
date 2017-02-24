package com.fuleme.business.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.bean.CMBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class ClerkManagementAdapter extends RecyclerView.Adapter<ClerkManagementAdapter.MyViewHolder> {


    private List<CMBean> mDatas;
    private Context context;
    private onRecyclerViewItemClickListener itemClickListener = null;

    public ClerkManagementAdapter(Context context, List<CMBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_type)
        TextView tvType;
        @Bind(R.id.tv_phone)
        TextView tvPhone;
        @Bind(R.id.ll_store)
        LinearLayout llStore;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_c_m, parent, false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvName.setText(mDatas.get(position).getName());
        holder.tvPhone.setText(mDatas.get(position).getPhone());
        if (mDatas.get(position).getType().equals("0")) {
            holder.tvType.setText("(店长)");
        } else if (mDatas.get(position).getType().equals("1")) {
            holder.tvType.setText("店员");
        } else {
            holder.tvType.setText("未知");
        }


        holder.llStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onItemClick(v, mDatas.get(position));
            }
        });

    }

    public interface onRecyclerViewItemClickListener {
        //点击传递item信息接口
        void onItemClick(View v, CMBean bean);
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
