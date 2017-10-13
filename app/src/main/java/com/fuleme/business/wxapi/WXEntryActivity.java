package com.fuleme.business.wxapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.utils.ToastUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        App.api.handleIntent(getIntent(), this); //处理微信传回的Intent,当然你也可以在别的地方处理
    }

    @Override
    public void onReq(BaseReq baseReq) {
//......这里是用来处理接收的请求,暂不做讨论

    }

    @Override
    public void onResp(BaseResp resp) {
//形参resp 有下面两个个属性比较重要 //1.resp.errCode //2.resp.transaction则是在分享数据的时候手动指定的字符创,用来分辨是那次分享(参照4.中req.transaction)
        switch (resp.errCode) {
            //根据需要的情况进行处理
            case BaseResp.ErrCode.ERR_OK:
                ToastUtil.showMessage("正确返回");
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ToastUtil.showMessage("用户取消");
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                ToastUtil.showMessage("认证被否决");
                finish();
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                ToastUtil.showMessage("发送失败");
                finish();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                ToastUtil.showMessage("不支持错误");
                finish();
                break;
            case BaseResp.ErrCode.ERR_COMM:
                ToastUtil.showMessage("一般错误");
                finish();
                break;
            default:
                //其他不可名状的情况 break;
                finish();
        }
    }
}
