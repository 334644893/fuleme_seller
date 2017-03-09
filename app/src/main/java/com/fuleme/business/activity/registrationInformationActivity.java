package com.fuleme.business.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fuleme.business.App;
import com.fuleme.business.R;
import com.fuleme.business.bean.BankBean;
import com.fuleme.business.bean.OpenBusinessBean;
import com.fuleme.business.common.BaseActivity;
import com.fuleme.business.fragment.FragmentActivity;
import com.fuleme.business.helper.GsonUtils;
import com.fuleme.business.utils.LogUtil;
import com.fuleme.business.utils.PictureUtil;
import com.fuleme.business.utils.ToastUtil;
import com.fuleme.business.widget.CustomViewPager;
import com.fuleme.business.widget.LoadingDialogUtils;
import com.yanzhenjie.album.Album;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 注册店铺
 */
public class RegistrationInformationActivity extends BaseActivity {
    private static final String TAG = "RegistrationInformation";

    @Bind(R.id.viewpager)
    CustomViewPager viewpager;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    private View view1, view2, view3, view4;
    private Button button1, button2, button3, button4;
    private List<View> viewList = new ArrayList<View>();
    private TextView area, opr_cls, acct_typ, et_yewu, tv_state, opn_bnk;
    private EditText mercht_full_nm, mercht_sht_nm, cust_serv_tel, contcr_nm, contcr_mobl_num, contcr_eml, acct_nm, acct_num,dtl_addr,contcr_tel,mercht_memo;
    private Store store = new Store();
    private String[] ll_acct_typ = {"对私账户", "对公账户", "内部账户"};
    private ArrayList<OpenBusinessBean> businessDatas = new ArrayList<OpenBusinessBean>();//返回业务数据
    String items[] = {"微信", "支付宝"};
    public static String url = "";//上传营业证地址

    boolean selected[] = {false, false};
    private Dialog mLoading;
    public int ACTIVITY_REQUEST_SELECT_PHOTO = 1000;

    @OnClick(R.id.tv_left)
    public void onClick() {
        if (viewpager.getCurrentItem() == 0) {
            finish();
        } else {
            viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        ToastUtil.showMessage(url);
        init();
    }

    public void init() {
        initTitle();
        initViewPager();
        nextOnclick();
    }

    public void initViewPager() {
        LayoutInflater lf = getLayoutInflater().from(this);
        view1 = lf.inflate(R.layout.activity_registration_information_1, null);
        view2 = lf.inflate(R.layout.activity_registration_information_2, null);
        view3 = lf.inflate(R.layout.activity_registration_information_3, null);
        view4 = lf.inflate(R.layout.activity_registration_information_4, null);

        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setCurrentItem(0);
        tvTitle.setText("注册信息(1/4)");
        //init view1
        mercht_full_nm = (EditText) view1.findViewById(R.id.mercht_full_nm);
        mercht_sht_nm = (EditText) view1.findViewById(R.id.mercht_sht_nm);
        cust_serv_tel = (EditText) view1.findViewById(R.id.cust_serv_tel);
        area = (TextView) view1.findViewById(R.id.area);
        opr_cls = (TextView) view1.findViewById(R.id.opr_cls);
        contcr_nm = (EditText) view1.findViewById(R.id.contcr_nm);
        contcr_mobl_num = (EditText) view1.findViewById(R.id.contcr_mobl_num);
        contcr_eml = (EditText) view1.findViewById(R.id.contcr_eml);
        //init view2
        acct_typ = (TextView) view2.findViewById(R.id.acct_typ);
        opn_bnk = (TextView) view2.findViewById(R.id.opn_bnk);
        acct_nm = (EditText) view2.findViewById(R.id.acct_nm);
        acct_num = (EditText) view2.findViewById(R.id.acct_num);
        et_yewu = (TextView) view2.findViewById(R.id.et_yewu);
        //init view3
        dtl_addr = (EditText) view3.findViewById(R.id.dtl_addr);
        contcr_tel = (EditText) view3.findViewById(R.id.contcr_tel);
        mercht_memo = (EditText) view3.findViewById(R.id.mercht_memo);
        //init view4
        tv_state = (TextView) view4.findViewById(R.id.tv_state);
        /**
         * view1点击事件
         */
        //添加地区
        view1.findViewById(R.id.ll_addprov).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(RegistrationInformationActivity.this, RegionActivity.class), 0);
            }
        });
        //添加行业
        view1.findViewById(R.id.ll_addoprcls).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(RegistrationInformationActivity.this, AddIndustryActivity.class), 1);
            }
        });
        /**
         * view2点击事件
         */
        //账户类型
        view2.findViewById(R.id.ll_acct_typ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationInformationActivity.this);  //先得到构造器
                builder.setTitle("选择类型"); //设置标题
                builder.setItems(ll_acct_typ, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        acct_typ.setText(ll_acct_typ[which]);
                        store.setAcct_typ(which + 1 + "");
                    }
                });
                builder.create().show();

            }
        });
        //开户行
        view2.findViewById(R.id.ll_opn_bnk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getbank();

            }
        });
        //开通业务
        view2.findViewById(R.id.ll_yewu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationInformationActivity.this);  //先得到构造器
                builder.setTitle("开通业务"); //设置标题
//                builder.setIcon(R.mipmap.logo);//设置图标，图片id即可
                builder.setMultiChoiceItems(items, selected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        Toast.makeText(RegistrationInformationActivity.this, items[which] + isChecked, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    StringBuffer s = new StringBuffer("");

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        //拼接选择的业务字符串用于显示
                        for (int i = 0; i < selected.length; i++) {
                            if (selected[i]) {
                                s.append(items[i] + ".");
                            }
                        }
                        if (!TextUtils.isEmpty(s)) {
                            et_yewu.setText(s.deleteCharAt(s.length() - 1));
                        } else {
                            et_yewu.setText("");
                        }
                        //根据selected中的boolen值来设置store中的业务开通状态
                        if (selected[0] == true) {
                            store.setOpen_wx("1");
                        } else {
                            store.setOpen_wx("0");
                        }
                        if (selected[1] == true) {
                            store.setOpen_zfb("1");
                        } else {
                            store.setOpen_zfb("0");
                        }
                        Toast.makeText(RegistrationInformationActivity.this, "store.getOpen_wx()" + store.getOpen_wx() + "store.getOpen_zfb()" + store.getOpen_zfb(), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();


            }


        });

        //上传营业证
        view4.findViewById(R.id.rl_biz_lics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationInformationActivity.this, UploadPicturesActivity.class);
                startActivityForResult(intent, 2);
            }


        });
    }

    public void initTitle() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tvTitle.setText("注册信息(1/4)");
                        break;
                    case 1:
                        tvTitle.setText("注册信息(2/4)");
                        break;
                    case 2:
                        tvTitle.setText("注册信息(3/4)");
                        break;
                    case 3:
                        tvTitle.setText("注册信息(4/4)");
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    public void nextOnclick() {
        button1 = (Button) view1.findViewById(R.id.btn_tj_1);
        button2 = (Button) view2.findViewById(R.id.btn_tj_2);
        button3 = (Button) view3.findViewById(R.id.btn_tj_3);
        button4 = (Button) view4.findViewById(R.id.btn_tj_4);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comitJudge(0)) {
                    pageSave(0);
                    viewpager.setCurrentItem(1);
                }

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comitJudge(1)) {
                    pageSave(1);
                    viewpager.setCurrentItem(2);
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageSave(2);
                viewpager.setCurrentItem(3);

            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (comitJudge(3)) {
                    pageSave(3);
                    //TODO 调用添加店铺接口,后台未改完
                    ToastUtil.showMessage("添加店铺接口未完成");
                    finish();

                }


            }
        });
    }

    /**
     * 判断是否符合提交
     *
     * @return
     */
    private boolean comitJudge(int i) {
        switch (i) {
            case 0:
                if (TextUtils.isEmpty(mercht_full_nm.getText().toString())) {
                    ToastUtil.showMessage("商户名称不能为空");
                    return false;
                } else if (TextUtils.isEmpty(area.getText().toString())) {
                    ToastUtil.showMessage("请选择所属地区");
                    return false;
                } else if (TextUtils.isEmpty(opr_cls.getText().toString())) {
                    ToastUtil.showMessage("请选择所属行业");
                    return false;
                } else if (TextUtils.isEmpty(contcr_nm.getText().toString())) {
                    ToastUtil.showMessage("请填写联系人");
                    return false;
                }
                return true;
            case 1:
                if (TextUtils.isEmpty(acct_typ.getText().toString())) {
                    ToastUtil.showMessage("请选择商户类型");
                    return false;
                } else if (TextUtils.isEmpty(opn_bnk.getText().toString())) {
                    ToastUtil.showMessage("请选择开户行");
                    return false;
                } else if (TextUtils.isEmpty(acct_nm.getText().toString())) {
                    ToastUtil.showMessage("请选择开户姓名");
                    return false;
                } else if (TextUtils.isEmpty(acct_num.getText().toString())) {
                    ToastUtil.showMessage("请填写开户行卡号");
                    return false;
                }
                return true;
            case 3:
                if (TextUtils.isEmpty(url)) {
                    ToastUtil.showMessage("请上传营业证");
                    return false;
                }
                return true;
        }
        ToastUtil.showMessage("参数错误");
        return false;
    }

    /**
     * 保存页面数据
     *
     * @param page
     */
    private void pageSave(int page) {
        switch (page) {
            case 0://保存第1页数据
                store.setMercht_full_nm(mercht_full_nm.getText().toString());
                store.setMercht_sht_nm(mercht_sht_nm.getText().toString());
                store.setCust_serv_tel(cust_serv_tel.getText().toString());
                store.setArea(area.getTag().toString());
                store.setOpr_cls(opr_cls.getTag().toString());
                store.setContcr_nm(contcr_nm.getText().toString());
                store.setContcr_mobl_num(contcr_mobl_num.getText().toString());
                store.setContcr_eml(contcr_eml.getText().toString());
                break;
            case 1://保存第2页数据,共5项，其余1项通过dialog设置
                store.setAcct_typ(acct_typ.getText().toString());
                store.setOpn_bnk(opn_bnk.getTag().toString());
                store.setAcct_nm(acct_nm.getText().toString());
                store.setAcct_num(acct_num.getText().toString());
                break;
            case 2://保存第3页数据
                store.setDtl_addr(dtl_addr.getText().toString());
                store.setContcr_tel(contcr_tel.getText().toString());
                store.setMercht_memo(mercht_memo.getText().toString());
                break;
            case 3://保存第4页数据
                store.setBiz_lics(url);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RegionActivity.TAGRESULT && data != null) {
            //地区
            area.setText(data.getExtras().getString("getarea"));
            area.setTag(data.getExtras().getString("getareaId"));
        } else if (requestCode == 1 && resultCode == AddIndustryActivity.TAGRESULT && data != null) {
            //行业
            opr_cls.setText(data.getExtras().getString("industryName"));
            opr_cls.setTag(data.getExtras().getString("industryId"));
        } else if (requestCode == 2) {
            //营业证
            if (!TextUtils.isEmpty(url)) {
                tv_state.setText("已上传");
                tv_state.setTextColor(getResources().getColor(R.color.theme));
            } else {
                tv_state.setText("未上传");
                tv_state.setTextColor(getResources().getColor(R.color.black_87));
            }
            ToastUtil.showMessage(url);

        }

    }

    /**
     * 获取开户行接口
     */
    private void getbank() {
        mLoading = LoadingDialogUtils.createLoadingDialog(RegistrationInformationActivity.this, "获取中...");//添加等待框
        Call<BankBean> call = getApi().getbank();

        call.enqueue(new Callback<BankBean>() {
            @Override
            public void onResponse(Call<BankBean> call, final Response<BankBean> response) {
                if (response.isSuccessful()) {
                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
                        // do SomeThing
                        LogUtil.i("银行接口成功");
                        //TODO 初始化数据
                        int size = response.body().getData().size();
                        final String[] opn_bnks = new String[size];
                        for (int i = 0; i < size; i++) {
                            opn_bnks[i] = response.body().getData().get(i).getBankName();
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationInformationActivity.this);  //先得到构造器
                        builder.setTitle("选择开户行"); //设置标题
                        builder.setItems(opn_bnks, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                opn_bnk.setText(opn_bnks[which]);
                                opn_bnk.setTag(response.body().getData().get(which).getBankCode());

                            }
                        });
                        builder.create().show();
                    } else {
                        ToastUtil.showMessage("获取失败");
                    }
                } else {
                    LogUtil.i("失败response.message():" + response.message());
                }
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
            }

            @Override
            public void onFailure(Call<BankBean> call, Throwable t) {
                LogUtil.e(TAG, t.toString());
                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
                ToastUtil.showMessage("超时");
            }

        });
    }
//    /**
//     * 添加店铺接口
//     */
//    private void addmerchant() {
//        mLoading = LoadingDialogUtils.createLoadingDialog(RegistrationInformationActivity.this, "获取中...");//添加等待框
//        Call<BankBean> call = getApi().addmerchant(
//                store.getMercht_full_nm(),
//                store.getMercht_sht_nm(),
//                store.getCust_serv_tel(),
//                store.getContcr_nm(),
//                store.getContcr_tel(),
//                store.getContcr_mobl_num(),
//                store.getContcr_eml(),
//                store.getMercht_memo(),
//                store.getArea(),
//                store.getDtl_addr(),
//                store.getAcct_nm(),
//                store.getOpn_bnk(),
//                store.getAcct_typ(),
//                store.getAcct_num(),
//                store.getBiz_lics(),
//                store.getOpen_wx(),
//                store.getWx_rate(),
//                store.getOpen_zfb(),
//                store.getZfb_rate(),
//                store.getAppl_typ(),
//                App.token
//                );
//
//        call.enqueue(new Callback<BankBean>() {
//            @Override
//            public void onResponse(Call<BankBean> call, final Response<BankBean> response) {
//                if (response.isSuccessful()) {
//                    if (GsonUtils.getError_code(response.body()) == GsonUtils.SUCCESSFUL) {
//                        // do SomeThing
//                        LogUtil.i("成功");
//                        //TODO 初始化数据
//                        int size = response.body().getData().size();
//                        final String[] opn_bnks = new String[size];
//                        for (int i = 0; i < size; i++) {
//                            opn_bnks[i] = response.body().getData().get(i).getBankName();
//                        }
//                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationInformationActivity.this);  //先得到构造器
//                        builder.setTitle("选择开户行"); //设置标题
//                        builder.setItems(opn_bnks, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                opn_bnk.setText(opn_bnks[which]);
//                                opn_bnk.setTag(response.body().getData().get(which).getBankCode());
//
//                            }
//                        });
//                        builder.create().show();
//                    } else {
//                        ToastUtil.showMessage("获取失败");
//                    }
//                } else {
//                    LogUtil.i("失败response.message():" + response.message());
//                }
//                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
//            }
//
//            @Override
//            public void onFailure(Call<BankBean> call, Throwable t) {
//                LogUtil.e(TAG, t.toString());
//                LoadingDialogUtils.closeDialog(mLoading);//取消等待框
//                ToastUtil.showMessage("超时");
//            }
//
//        });
//    }

    @Override
    public void onBackPressed() {
        if (viewpager.getCurrentItem() == 0) {
            finish();
        } else {
            viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UploadPicturesActivity.urlImg = "";
        url = "";
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        // 实例化一个页卡
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        // 销毁一个页卡
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    };
}

/**
 * 店铺
 */
class Store implements Serializable {
    String mercht_full_nm = "";// 	必填 	商户全名称
    String mercht_sht_nm = "";// 	选填 	string 	商户简称
    String cust_serv_tel = "";// 	选填 	string 	客户服务电话
    String contcr_nm = "";// 	必填 	string 	联系人名称
    String contcr_tel = "";// 	选填 	string 	联系人电话
    String contcr_mobl_num = "";// 	选填 	string 	联系人手机号
    String contcr_eml = "";// 	选填 	string 	联系人邮箱
    String opr_cls = "";// 	必填 	string 	经营类目
    String mercht_memo = "";// 	选填 	string 	商户备注
    String area = "";// 	必填 	string 	县区
    String dtl_addr = "";// 	选填 	string 	详细地址
    String acct_nm = "";// 	必填 	string 	账户名称
    String opn_bnk = "";// 	必填 	string 	开户行(商户结算账号)
    String acct_typ = "";// 	必填 	string 	帐号类型 1对私账户，2对公账户，3内部账户
    String acct_num = "";// 	必填 	string 	商户结算账号
    String is_nt_two_line = "";// 	选填 	string 	是否支持收支两条线
    String biz_lics = "";// 	必填 	string 	营业证
    String open_wx = ""; //	选填 	String 	0不开通1开通微信支付
    String wx_rate = "";//	选填 	String 	微信支付费率
    String open_zfb = "";//	选填 	String 	0不开通1开通支付宝支付
    String zfb_rate = "";//	选填 	String 	支付宝支付费率
    String appl_typ = "";// 	必填 	string 	申请类型 新增：0；变更：1；停用：2
    String token = "";// 	选填 	string 	用户token

    public String getMercht_full_nm() {
        return mercht_full_nm;
    }

    public void setMercht_full_nm(String mercht_full_nm) {
        this.mercht_full_nm = mercht_full_nm;
    }

    public String getMercht_sht_nm() {
        return mercht_sht_nm;
    }

    public void setMercht_sht_nm(String mercht_sht_nm) {
        this.mercht_sht_nm = mercht_sht_nm;
    }

    public String getCust_serv_tel() {
        return cust_serv_tel;
    }

    public void setCust_serv_tel(String cust_serv_tel) {
        this.cust_serv_tel = cust_serv_tel;
    }

    public String getContcr_nm() {
        return contcr_nm;
    }

    public void setContcr_nm(String contcr_nm) {
        this.contcr_nm = contcr_nm;
    }

    public String getContcr_tel() {
        return contcr_tel;
    }

    public void setContcr_tel(String contcr_tel) {
        this.contcr_tel = contcr_tel;
    }

    public String getContcr_mobl_num() {
        return contcr_mobl_num;
    }

    public void setContcr_mobl_num(String contcr_mobl_num) {
        this.contcr_mobl_num = contcr_mobl_num;
    }

    public String getContcr_eml() {
        return contcr_eml;
    }

    public void setContcr_eml(String contcr_eml) {
        this.contcr_eml = contcr_eml;
    }

    public String getOpr_cls() {
        return opr_cls;
    }

    public void setOpr_cls(String opr_cls) {
        this.opr_cls = opr_cls;
    }

    public String getMercht_memo() {
        return mercht_memo;
    }

    public void setMercht_memo(String mercht_memo) {
        this.mercht_memo = mercht_memo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDtl_addr() {
        return dtl_addr;
    }

    public void setDtl_addr(String dtl_addr) {
        this.dtl_addr = dtl_addr;
    }

    public String getAcct_nm() {
        return acct_nm;
    }

    public void setAcct_nm(String acct_nm) {
        this.acct_nm = acct_nm;
    }

    public String getOpn_bnk() {
        return opn_bnk;
    }

    public void setOpn_bnk(String opn_bnk) {
        this.opn_bnk = opn_bnk;
    }

    public String getAcct_typ() {
        return acct_typ;
    }

    public void setAcct_typ(String acct_typ) {
        this.acct_typ = acct_typ;
    }

    public String getAcct_num() {
        return acct_num;
    }

    public void setAcct_num(String acct_num) {
        this.acct_num = acct_num;
    }

    public String getIs_nt_two_line() {
        return is_nt_two_line;
    }

    public void setIs_nt_two_line(String is_nt_two_line) {
        this.is_nt_two_line = is_nt_two_line;
    }

    public String getBiz_lics() {
        return biz_lics;
    }

    public void setBiz_lics(String biz_lics) {
        this.biz_lics = biz_lics;
    }

    public String getOpen_wx() {
        return open_wx;
    }

    public void setOpen_wx(String open_wx) {
        this.open_wx = open_wx;
    }

    public String getWx_rate() {
        return wx_rate;
    }

    public void setWx_rate(String wx_rate) {
        this.wx_rate = wx_rate;
    }

    public String getOpen_zfb() {
        return open_zfb;
    }

    public void setOpen_zfb(String open_zfb) {
        this.open_zfb = open_zfb;
    }

    public String getZfb_rate() {
        return zfb_rate;
    }

    public void setZfb_rate(String zfb_rate) {
        this.zfb_rate = zfb_rate;
    }

    public String getAppl_typ() {
        return appl_typ;
    }

    public void setAppl_typ(String appl_typ) {
        this.appl_typ = appl_typ;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}