//package com.fuleme.business.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.fuleme.business.R;
//import com.fuleme.business.bean.IncomeBean;
//import com.fuleme.business.utils.DateUtil;
//
//import java.text.NumberFormat;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
///**
// * Created by Administrator on 2017/2/7.
// */
//
//public class IncomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    NumberFormat nf = NumberFormat.getInstance();
////    private static final int TYPE_ITEM = 0;  //普通Item View
////
////    private static final int TYPE_FOOTER = 1;  //顶部FootView
////    final private int Y_STATE = 1;
////    final private String Y_STATE_S = "已划款";
////    final private int N_STATE = 0;
////    final private String N_STATE_S = "未划款";
//
//    private List<IncomeBean.DataBean> mDatas;
//    private Context context;
//
//    public IncomeAdapter(Context context, List<IncomeBean.DataBean> mDatas) {
//        this.context = context;
//        this.mDatas = mDatas;
//
//    }
//
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.tv_i_time)
//        TextView tvITime;
//        @Bind(R.id.tv_i_amount)
//        TextView tvIAmount;
//        @Bind(R.id.tv_i_state)
//        TextView tvIState;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//            nf.setGroupingUsed(false);
//        }
//    }
//
////    class FootViewHolder extends RecyclerView.ViewHolder {
////        @Bind(R.id.tv_name)
////        TextView tvName;
////
////        public FootViewHolder(View itemView) {
////            super(itemView);
////            ButterKnife.bind(this, itemView);
////        }
////    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        //进行判断显示类型，来创建返回不同的View
////        if (viewType == TYPE_ITEM) {
//            MyViewHolder hodler = new MyViewHolder(LayoutInflater.from(context)
//                    .inflate(R.layout.item_income_list, parent, false));
//            return hodler;
////        } else if (viewType == TYPE_FOOTER) {
////            FootViewHolder footViewHolder = new FootViewHolder(LayoutInflater.from(context)
////                    .inflate(R.layout.item_textview, parent, false));
////            return footViewHolder;
////        }
////        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//
////        if (holder instanceof MyViewHolder) {
//            MyViewHolder myViewHolder = (MyViewHolder) holder;
//            myViewHolder.tvITime.setText(DateUtil.stampToDate(mDatas.get(position).getResultTime(), DateUtil.DATE_2));
//            myViewHolder.tvIState.setText(mDatas.get(position).getResultStatus()+"");
//            myViewHolder.tvIAmount.setText("¥ " + nf.format(mDatas.get(position).getArrivalAmount()));
//
//
////        }
////        else if (holder instanceof FootViewHolder) {
////            FootViewHolder footViewHolder = (FootViewHolder) holder;
////            if (IncomeActivity.textState) {
////                footViewHolder.tvName.setText("正在加载更多数据...");
////            } else {
////                footViewHolder.tvName.setText("不用扯了，没有了...");
////            }
////        }
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return mDatas.size()
////                + 1
//                ;
//    }
//
////    public int getItemViewType(int position) {
////        // 最后一个item设置为footerView
////        if (position + 1 == getItemCount()) {
////            return TYPE_FOOTER;
////        } else {
////            return TYPE_ITEM;
////        }
////    }
//}
