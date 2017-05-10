package com.fuleme.business.bean;

/**
 * Created by Administrator on 2017/5/5.
 */

public class OrderContentBean {

    /**
     * error_code : 200
     * data : {"out_trade_no":"201703241009259739736936","prime_cost":0.01,"total_fee":0.01,"coupon_id":"0","time_end":"1490321371","trade_type":"pay.weixin.jspay","state":"1","openid":"o-Rj7wIJ3uCpSpFLLpTc_lF_Km7w","nick_name":"","head_img":"","discount":0}
     */

    private String error_code;
    private DataBean data;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * out_trade_no : 201703241009259739736936
         * prime_cost : 0.01
         * total_fee : 0.01
         * coupon_id : 0
         * time_end : 1490321371
         * trade_type : pay.weixin.jspay
         * state : 1
         * openid : o-Rj7wIJ3uCpSpFLLpTc_lF_Km7w
         * nick_name :
         * head_img :
         * discount : 0.0
         */

        private String out_trade_no;
        private double prime_cost;
        private double total_fee;
        private String coupon_id;
        private String time_end;
        private String trade_type;
        private String state;
        private String openid;
        private String nick_name;
        private String head_img;
        private double discount;
        private String refund_state;
        private String refund_fee;

        public String getRefund_state() {
            return refund_state;
        }

        public void setRefund_state(String refund_state) {
            this.refund_state = refund_state;
        }

        public String getRefund_fee() {
            return refund_fee;
        }

        public void setRefund_fee(String refund_fee) {
            this.refund_fee = refund_fee;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public double getPrime_cost() {
            return prime_cost;
        }

        public void setPrime_cost(double prime_cost) {
            this.prime_cost = prime_cost;
        }

        public double getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(double total_fee) {
            this.total_fee = total_fee;
        }

        public String getCoupon_id() {
            return coupon_id;
        }

        public void setCoupon_id(String coupon_id) {
            this.coupon_id = coupon_id;
        }

        public String getTime_end() {
            return time_end;
        }

        public void setTime_end(String time_end) {
            this.time_end = time_end;
        }

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

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

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }
    }
}
