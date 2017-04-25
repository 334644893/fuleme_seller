package com.fuleme.business.activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.ToastUtil;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 修改密码
 */
public class ChangePasswordActivity extends BaseActivity {
    private static final String TAG = "ChangePasswordActivity";
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_f_p_phone)
    EditText etFPPhone;
    @Bind(R.id.et_f_p_ps)
    EditText etFPPs;
    @Bind(R.id.et_f_p_ep)
    EditText etFPEp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        initView();
    }
    public void initView() {
        tvTitle.setText("修改密码");
    }
    @OnClick({R.id.tv_left, R.id.btn_tj})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_tj:
                if (TextUtils.isEmpty(etFPPhone.getText().toString())) {
                    ToastUtil.showMessage("请填写旧密码");
                } else if (TextUtils.isEmpty(etFPPs.getText().toString())) {
                    ToastUtil.showMessage("请填写新密码");
                } else if (!etFPPs.getText().toString().equals(etFPEp.getText().toString())) {
                    ToastUtil.showMessage("新密码不一致");
                } else {
                    modifypwd();
                }
                break;
        }
    }
    /**
     * 修改密码接口
     */
    private void modifypwd() {
        showLoading("获取中...");
        Call<Object> call = getApi().modifypwd(App.token,
                etFPPhone.getText().toString(),
                etFPPs.getText().toString());

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    // do SomeThing
                    if (GsonUtils.getError_code(response.body())==GsonUtils.SUCCESSFUL) {
                        ToastUtil.showMessage("重置成功");
                        finish();
                    } else {
                        ToastUtil.showMessage(GsonUtils.getErrmsg(response.body()));
                        etFPPhone.setText("");
                    }


                } else {
                    ToastUtil.showMessage("重置失败");
                    LogUtil.i("重置失败response.message():" + response.message());
                }
                closeLoading();//取消等待框
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                closeLoading();//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }
}
