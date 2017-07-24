package com.fuleme.business.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.bean.ClerkInfoBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class StoreAQAdapter extends RecyclerView.Adapter<StoreAQAdapter.MyViewHolder> {
    private List<ClerkInfoBean.DataBean> mDatas;
    private Context context;
    private onRecyclerViewItemClickListener itemClickListener = null;

    public StoreAQAdapter(Context context, List<ClerkInfoBean.DataBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_store)
        TextView tvStore;
        @Bind(R.id.ll_store)
        LinearLayout llStore;
        @Bind(R.id.tv_number)
        TextView tvNumber;
        @Bind(R.id.tv_area)
        TextView tvArea;
        @Bind(R.id.tv_state)
        TextView tvState;
        @Bind(R.id.ll_number)
        LinearLayout llNumber;
        @Bind(R.id.ll_area)
        LinearLayout llArea;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.ll_name)
        LinearLayout llName;
        StringBuffer sb = new StringBuffer();

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_store_a_g_q, parent, false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.sb.setLength(0);
        if (App.PLACEHOLDER.equals(mDatas.get(position).getId())
                ) {
            holder.tvStore.setText(mDatas.get(position).getName());
            holder.llNumber.setVisibility(View.GONE);
            holder.llArea.setVisibility(View.GONE);
            holder.tvState.setVisibility(View.GONE);
        } else {
            if (mDatas.get(position).getClerk() != null && mDatas.size() > 0) {
                for (ClerkInfoBean.DataBean.ClerkBean cb : mDatas.get(position).getClerk()) {
                    holder.sb.append(cb.getUsername() + "、");
                }
                holder.sb.deleteCharAt(holder.sb.length() - 1);
                holder.tvNumber.setText("店员:" + mDatas.get(position).getClerk().size() + "人");
                holder.tvName.setText(holder.sb);
            } else {
                holder.tvNumber.setText("请设定店员");
            }
            holder.tvArea.setText("地址:" + mDatas.get(position).getAddress());
            holder.tvStore.setText(mDatas.get(position).getName());
            if ("0".equals(mDatas.get(position).getState())) {
                holder.tvState.setText("原因:不可抗逆因素");
                holder.tvState.setVisibility(View.VISIBLE);
                holder.tvNumber.setVisibility(View.GONE);
                holder.llName.setVisibility(View.GONE);
            } else if ("1".equals(mDatas.get(position).getState())) {
                holder.tvNumber.setVisibility(View.VISIBLE);
                holder.llName.setVisibility(View.VISIBLE);
                holder.tvState.setVisibility(View.GONE);
            }
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
        void onItemClick(View v, ClerkInfoBean.DataBean bean);
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
