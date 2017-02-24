package com.fuleme.business.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.bean.IncomeBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.MyViewHolder> {


    final private int Y_STATE = 0;
    final private String Y_STATE_S = "已划款";
    final private int N_STATE = 1;
    final private String N_STATE_S = "未划款";
    private List<IncomeBean> mDatas;
    private Context context;

    public IncomeAdapter(Context context, List<IncomeBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_i_time)
        TextView tvITime;
        @Bind(R.id.tv_i_amount)
        TextView tvIAmount;
        @Bind(R.id.tv_i_state)
        TextView tvIState;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_income_list, parent, false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvITime.setText(mDatas.get(position).getmDate());
        holder.tvIAmount.setText("¥ "+mDatas.get(position).getmAmount());
        if (mDatas.get(position).getmState() == Y_STATE) {
            holder.tvIState.setText(Y_STATE_S);
            holder.tvIState.setTextColor(context.getResources().getColor(R.color.online_1));
        } else if (mDatas.get(position).getmState() == N_STATE) {
            holder.tvIState.setText(N_STATE_S);
            holder.tvIState.setTextColor(context.getResources().getColor(R.color.red));
        }


    }


    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }


}
