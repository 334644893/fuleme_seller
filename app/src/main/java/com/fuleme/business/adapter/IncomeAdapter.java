package com.fuleme.business.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.bean.IncomeBean;
import com.fuleme.business.utils.DateUtil;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.MyViewHolder> {
    java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
    final private int Y_STATE = 1;
    final private String Y_STATE_S = "已划款";
    final private int N_STATE = 0;
    final private String N_STATE_S = "未划款";
    private List<IncomeBean.DataBean> mDatas;
    private Context context;

    public IncomeAdapter(Context context, List<IncomeBean.DataBean> mDatas) {
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
            nf.setGroupingUsed(false);
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
        holder.tvITime.setText(DateUtil.stampToDate(mDatas.get(position).getResultTime(),DateUtil.DATE_2));
        holder.tvIAmount.setText("¥ "+nf.format(mDatas.get(position).getArrivalAmount()));

        if (mDatas.get(position).getResultStatus() == Y_STATE) {
            holder.tvIState.setText(Y_STATE_S);
            holder.tvIState.setTextColor(context.getResources().getColor(R.color.online_1));
        } else if (mDatas.get(position).getResultStatus() == N_STATE) {
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
