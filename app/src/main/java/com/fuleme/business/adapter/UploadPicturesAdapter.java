package com.fuleme.business.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fuleme.business.R;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/7.
 */

public class UploadPicturesAdapter extends RecyclerView.Adapter<UploadPicturesAdapter.MyViewHolder> {


    private ArrayList<String> mDatas;
    private Context context;
    private onRecyclerViewItemClickListener itemClickListener = null;

    public UploadPicturesAdapter(Context context, ArrayList<String> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_avator)
        SimpleDraweeView userAvator;
        @Bind(R.id.iv_ppp)
        ImageView ivPpp;

        @Bind(R.id.ll_ppp)
        LinearLayout llPpp;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_registration_photo, parent, false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if ((position + 1) == mDatas.size()) {
            //最后一个视图为添加图
            holder.ivPpp.setVisibility(View.VISIBLE);
            holder.userAvator.setVisibility(View.GONE);

        } else {
            //其余为图片
            holder.ivPpp.setVisibility(View.GONE);
            holder.userAvator.setVisibility(View.VISIBLE);
            holder.userAvator.setImageURI(Uri.fromFile(new File(mDatas.get(position))));
        }
        holder.llPpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, position);

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
