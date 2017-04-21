package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */

public class MemberManagementBean {


    /**
     * data : [{"openid":"o-Rj7wC3nlA6uKoc6yC_dpj2zXwY","count":"3","last_end":"1491965616","total_fee":"0.03"},{"openid":"o-Rj7wIJ3uCpSpFLLpTc_lF_Km7w","count":"7","last_end":"1491981939","total_fee":"0.07"},{"openid":"o-Rj7wITrA1vWKMuSjAHw0HItfnQ","count":"1","last_end":"1491563770","total_fee":"6"},{"openid":"o-Rj7wJRK6f1iZxdpZOTa8l9kNyU","count":"2","last_end":"1491563711","total_fee":"9"},{"openid":"o-Rj7wNL6z5MFtrkcE0iTuKAeCc4","count":"1","last_end":"1491560772","total_fee":"0.01"}]
     * shopuser : 5
     * todayuser : 0
     * thirtybefore : 0
     * thismonth : 0
     * error_code : 200
     */

    private String shopuser;
    private String todayuser;
    private String thirtybefore;
    private String thismonth;
    private String error_code;
    private List<DataBean> data;

    public String getShopuser() {
        return shopuser;
    }

    public void setShopuser(String shopuser) {
        this.shopuser = shopuser;
    }

    public String getTodayuser() {
        return todayuser;
    }

    public void setTodayuser(String todayuser) {
        this.todayuser = todayuser;
    }

    public String getThirtybefore() {
        return thirtybefore;
    }

    public void setThirtybefore(String thirtybefore) {
        this.thirtybefore = thirtybefore;
    }

    public String getThismonth() {
        return thismonth;
    }

    public void setThismonth(String thismonth) {
        this.thismonth = thismonth;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * openid : o-Rj7wC3nlA6uKoc6yC_dpj2zXwY
         * count : 3
         * last_end : 1491965616
         * total_fee : 0.03
         */

        private String openid;
        private String count;
        private String last_end;
        private String total_fee;
        private String nick_name;
        private String head_img;

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getLast_end() {
            return last_end;
        }

        public void setLast_end(String last_end) {
            this.last_end = last_end;
        }

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }
    }
}
