package com.fuleme.business.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.activity.LoginActivity;
import com.fuleme.business.activity.Version2.BalanceActivity;
import com.fuleme.business.activity.Version2.InviteCodeActivity;
import com.fuleme.business.activity.Version2.MyBankActivity;
import com.fuleme.business.activity.Version2.MyCommissionActivity;
import com.fuleme.business.activity.Version2.PromoteTeamActivity;
import com.fuleme.business.activity.Version2.ServiceBusinessesActivity;
import com.fuleme.business.activity.Version2.SignPromoteActivity;
import com.fuleme.business.helper.APIService;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.SharedPreferencesUtils;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.ScrollWebView;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 推广
 */
public class BFragment extends Fragment {
    private static final String TAG = "BFragment";
    View view = null;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.tv_3)
    TextView tv3;
    @Bind(R.id.tv_4)
    TextView tv4;
    @Bind(R.id.tv_5)
    TextView tv5;
    @Bind(R.id.ll_top)
    LinearLayout llTop;
    @Bind(R.id.ll_scl)
    ScrollView llScl;
    boolean flag = false;
    @Bind(R.id.tv_content)
    LinearLayout tvContent;
    @Bind(R.id.iv_gougou)
    ImageView ivGougou;
    @Bind(R.id.btn_tj_1)
    Button btnTj1;
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    private Context context;
    ScrollWebView webView1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_administrator_b, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();


        //刷新控件
        demoSwiperefreshlayout.setColorSchemeResources(R.color.white);
        demoSwiperefreshlayout.setProgressBackgroundColorSchemeResource(R.color.theme);
        demoSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉重置数据
                promotion();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        if ("1".equals(App.is_agent)) {
            //        代理
            llTop.setVisibility(View.GONE);
            llScl.setVisibility(View.VISIBLE);
            promotion();
        } else {
            //非代理
            webView1 = new ScrollWebView(context.getApplicationContext());
            tvContent.addView(webView1);
            webView1.setVerticalScrollBarEnabled(false);
            webView1.setHorizontalScrollBarEnabled(false);
            webView1.getSettings().setJavaScriptEnabled(true); //加上这句话才能使用JavaScript方法
            webView1.loadUrl(APIService.SERVER_IP + "api/system/clause");
            llTop.setVisibility(View.VISIBLE);
            llScl.setVisibility(View.GONE);
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll_1, R.id.btn_tj_1, R.id.ll_yes, R.id.ll_2, R.id.ll_3, R.id.ll_since_1, R.id.ll_since_2, R.id.ll_since_3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_yes:
                if (flag) {
                    ivGougou.setImageResource(R.mipmap.icon_xuanze);
                    btnTj1.setBackgroundResource(R.drawable.shape_corner_hui_p);
                    flag = false;
                } else {
                    flag = true;
                    ivGougou.setImageResource(R.mipmap.icon_xuanzhong);
                    btnTj1.setBackgroundResource(R.drawable.shape_corner_0);
                }
                break;
            case R.id.btn_tj_1:
                if (flag) {
                    // 签约推广
                    startActivity(new Intent(getActivity(), SignPromoteActivity.class));
                } else {
                    ToastUtil.showMessage("请同意推广条款");
                }
                break;
            case R.id.ll_1:
                //余额
                startActivity(new Intent(getActivity(), BalanceActivity.class));
                break;
            case R.id.ll_2:
                //我的返佣
                MyCommissionActivity.mid = App.PLACEHOLDER;
                startActivity(new Intent(getActivity(), MyCommissionActivity.class));
                break;
            case R.id.ll_3:
                //推广团队
                startActivity(new Intent(getActivity(), PromoteTeamActivity.class));
                break;
            case R.id.ll_since_1:
                startActivity(new Intent(getActivity(), ServiceBusinessesActivity.class));
                break;
            case R.id.ll_since_2:
                //提现记录--余额页面
                startActivity(new Intent(getActivity(), BalanceActivity.class));
                break;
            case R.id.ll_since_3:
                startActivity(new Intent(getActivity(), InviteCodeActivity.class));
                break;
        }
    }

    /**
     * 我的推广接口
     */

    private void promotion() {
        ((FragmentActivity) getActivity()).showLoading("加载中...");
        Call<Object> call = ((FragmentActivity) getActivity()).getApi().promotion(App.token);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        JSONObject data = GsonUtils.getResultData(response.body());
                        tv1.setText(data.optString("money"));
                        tv2.setText(data.optString("withdrawals"));
                        tv3.setText(data.optString("today_withdrawals"));
                        tv4.setText(data.optString("rebate"));
                        tv5.setText(data.optString("today_rebate"));
                        ((FragmentActivity) getActivity()).closeLoading();//取消等待框
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                        ((FragmentActivity) getActivity()).closeLoading();//取消等待框
                    }

                } else {
                    LogUtil.i(response.message());
                    ((FragmentActivity) getActivity()).closeLoading();//取消等待框
                }
                demoSwiperefreshlayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                ((FragmentActivity) getActivity()).closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
                demoSwiperefreshlayout.setRefreshing(false);
            }

        });
    }
}
