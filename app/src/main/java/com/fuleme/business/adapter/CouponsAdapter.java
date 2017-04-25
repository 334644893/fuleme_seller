package com.fuleme.business.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.bean.CouponsBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.MyViewHolder> {


    private List<CouponsBean.DataBean> mDatas;
    private Context context;
    private onRecyclerViewItemClickListener itemClickListener = null;

    public CouponsAdapter(Context context, List<CouponsBean.DataBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_type)
        ImageView ivType;
        @Bind(R.id.tv_store)
        TextView tvStore;
        @Bind(R.id.tv_0)
        TextView tv0;
        @Bind(R.id.tv_1)
        TextView tv1;
        @Bind(R.id.tv_number)
        TextView tvNumber;
        @Bind(R.id.ll_number)
        LinearLayout llNumber;
        @Bind(R.id.tv_2)
        TextView tv2;
        @Bind(R.id.tv_area)
        TextView tvArea;
        @Bind(R.id.ll_area)
        LinearLayout llArea;
        @Bind(R.id.card_view)
        CardView cardView;
        @Bind(R.id.ll_store)
        LinearLayout llStore;
        @Bind(R.id.im_r)
        ImageView imR;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_coup, parent, false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if ("1".equals(mDatas.get(position).getType())) {
            holder.tvStore.setText("折扣券");
            holder.tv0.setVisibility(View.GONE);
            holder.tv1.setText(mDatas.get(position).getReduce() + " 折");
            holder.tvNumber.setText("满" + mDatas.get(position).getTerm() + "元使用");
            holder.tv2.setText("累计使用" + mDatas.get(position).getUsed() + "张");
            holder.tvArea.setText("今日使用" + mDatas.get(position).getTodayused() + "张");
        } else if ("2".equals(mDatas.get(position).getType())) {
            holder.tvStore.setText("满减券");
            holder.tv0.setVisibility(View.VISIBLE);
            holder.tv1.setText(mDatas.get(position).getReduce());
            holder.tvNumber.setText("满" + mDatas.get(position).getTerm() + "元使用");
            holder.tv2.setText("累计使用" + mDatas.get(position).getUsed() + "张");
            holder.tvArea.setText("今日使用" + mDatas.get(position).getTodayused() + "张");
        } else {
            holder.tvStore.setText("其他券");
            holder.tv0.setVisibility(View.GONE);
            holder.tv1.setText("0");
        }
        if ("0".equals(mDatas.get(position).getState())) {
            holder.imR.setImageResource(R.mipmap.icon_zanting);
        } else if ("1".equals(mDatas.get(position).getState())) {
            holder.imR.setImageResource(R.mipmap.icon_fafang);
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
        void onItemClick(View v, CouponsBean.DataBean bean);
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
