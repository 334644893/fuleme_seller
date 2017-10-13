package com.fuleme.business.utils;

import com.fuleme.business.App;
import com.fuleme.business.helper.APIService;

/**
 * Created by Administrator on 2017/2/8.
 */

public class Constant {
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;
    /**
     * WX分享邀请码
     */
    public static final String URL = APIService.SERVER_IP+"api/system/share.html?code="+App.invitation_code;
    public static final String TITLE = "付了么，收款利器.赚钱神器.低费率.高返佣!!!";
    public static final String description = "推荐人手机号\n"+ App.phone+"邀请您开通付了么";


}
