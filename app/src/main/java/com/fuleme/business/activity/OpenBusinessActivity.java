//package com.fuleme.business.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.widget.TextView;
//
//import com.fuleme.business.R;
//import com.fuleme.business.adapter.OpenBusinessAdapter;
//import com.fuleme.business.bean.OpenBusinessBean;
//import com.fuleme.business.utils.DividerItemDecoration;
//
//import java.util.ArrayList;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class OpenBusinessActivity extends AppCompatActivity {
//    private static final String TAG = "OpenBusinessActivity";
//    public static int TAGRESULT = 200;
//    private ArrayList<OpenBusinessBean> mDatas = new ArrayList<OpenBusinessBean>();
//    LinearLayoutManager linearLayoutManager;
//    OpenBusinessAdapter mAdapter;
//    @Bind(R.id.tv_title)
//    TextView tvTitle;
//    @Bind(R.id.recyclerview)
//    RecyclerView mRecyclerView;
//    private String[] name = {"微信","支付宝"};
//    private double[] rate = {0.0038, 0.0037};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_open_business);
//        ButterKnife.bind(this);
//        initData();
//        initView();
//    }
//
//    public void initView() {
//        tvTitle.setText("开通业务");
//
//        /**
//         * 设置列表
//         */
//        linearLayoutManager = new LinearLayoutManager(OpenBusinessActivity.this);
//        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mAdapter = new OpenBusinessAdapter(OpenBusinessActivity.this, mDatas);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(OpenBusinessActivity.this, LinearLayoutManager.VERTICAL));
//
//
//    }
//
//    protected void initData() {
//            for (int i = 0; i < name.length; i++) {
//                OpenBusinessBean obBean = new OpenBusinessBean();
//                obBean.setBusinessName(name[i]);
//                obBean.setRate(rate[i]);
//                mDatas.add(obBean);
//            }
//
//    }
//
//    @OnClick(R.id.tv_left)
//    public void onClick() {
//
//        backResult();
//    }
//    @Override
//    public void onBackPressed() {
//        backResult();
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//    }
//    private void backResult(){
//        Intent intent = new Intent(OpenBusinessActivity.this, RegistrationInformationActivity.class);
//        Bundle b=new Bundle();
//        b.putSerializable("OpenBusinessBeanList",mDatas);
//        intent.putExtras(b);
//        setResult(TAGRESULT, intent);
//        finish();
//    }
//
//}
