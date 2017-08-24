package com.fuleme.business.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuleme.business.R;
import com.fuleme.business.bean.MerchantListBean;
import com.fuleme.business.bean.PromoteBean;
import com.fuleme.business.bean.ServiceBusinessesBean;
import com.fuleme.business.utils.DateUtil;

import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class ServiceBusinessesAdapter extends RecyclerView.Adapter<ServiceBusinessesAdapter.MyViewHolder> {

    private onRecyclerViewItemClickListener itemClickListener = null;
    NumberFormat nf = NumberFormat.getInstance();
    private List<ServiceBusinessesBean.DataBean> mDatas;
    private Context context;

    public ServiceBusinessesAdapter(Context context, List<ServiceBusinessesBean.DataBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ll_1)
        LinearLayout ll1;
        @Bind(R.id.tv_1)
        TextView tv1;
        @Bind(R.id.tv_2)
        TextView tv2;
        @Bind(R.id.tv_3)
        TextView tv3;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            nf.setGroupingUsed(false);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_meichan, parent, false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyViewHolder hodler, final int position) {
        hodler.tv1.setText(mDatas.get(position).getShort_name());
        hodler.tv2.setText("开户时间:  " + DateUtil.stampToDate(mDatas.get(position).getCreate_time(), DateUtil.DATE_2));
        hodler.tv3.setText(mDatas.get(position).getMerchant_address());
        hodler.ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onItemClick(v, mDatas.get(position));
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
        void onItemClick(View v, ServiceBusinessesBean.DataBean bean);
    }

    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }

}
