package com.fuleme.business.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.MemberManagementActivity;
import com.fuleme.business.bean.MemberManagementBean;
import com.fuleme.business.utils.DateUtil;

import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class MemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //底部FootView


    private List<MemberManagementBean.DataBean> mDatas;
    private Context context;

    public MemberAdapter(Context context, List<MemberManagementBean.DataBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_avator)
        SimpleDraweeView ivHead;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_number)
        TextView tvNumber;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_amount)
        TextView tvAmount;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;

        public FootViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_member, parent, false));
            return hodler;
        } else if (viewType == TYPE_FOOTER) {
            FootViewHolder footViewHolder = new FootViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_textview, parent, false));
            return footViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            if (!TextUtils.isEmpty(mDatas.get(position).getHead_img())) {
                myViewHolder.ivHead.setImageURI(mDatas.get(position).getHead_img());
            }
            if (!TextUtils.isEmpty(mDatas.get(position).getNick_name())) {
                myViewHolder.tvName.setText(mDatas.get(position).getNick_name());
            } else {
                myViewHolder.tvName.setText(App.merchant + "用户");
            }
            if (!TextUtils.isEmpty(mDatas.get(position).getCount())) {
                myViewHolder.tvNumber.setText(mDatas.get(position).getCount() + "");
            }
            if (!TextUtils.isEmpty(mDatas.get(position).getLast_end())) {
                myViewHolder.tvTime.setText(DateUtil.stampToDate(mDatas.get(position).getLast_end(), DateUtil.DATE_1));
            }
            if (!TextUtils.isEmpty(mDatas.get(position).getTotal_fee())) {
                myViewHolder.tvAmount.setText(mDatas.get(position).getTotal_fee());
            }
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            if (MemberManagementActivity.textState) {
                footViewHolder.tvName.setText(R.string.load);
            } else {
                footViewHolder.tvName.setText(R.string.nomore);
            }
        }

    }


    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }

    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }
}
