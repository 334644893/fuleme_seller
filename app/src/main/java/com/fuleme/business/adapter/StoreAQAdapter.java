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
import com.fuleme.business.bean.StoreAQBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class StoreAQAdapter extends RecyclerView.Adapter<StoreAQAdapter.MyViewHolder> {


//    final int IVSTATE_VISIBLE = 0;
//    private List<StoreAQBean> mDatas;
    private List<String> mDatas;
    private Context context;
    private onRecyclerViewItemClickListener itemClickListener = null;

    public StoreAQAdapter(Context context, List<String> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_store)
        TextView tvStore;
        @Bind(R.id.ll_store)
        LinearLayout llStore;
//        @Bind(R.id.iv_store)
//        ImageView ivStore;
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
//        if (mDatas.get(position).getmState() == IVSTATE_VISIBLE) {
//            holder.ivStore.setVisibility(View.VISIBLE);
//        } else {
//            holder.ivStore.setVisibility(View.INVISIBLE);
//        }
        holder.tvStore.setText(mDatas.get(position));
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
