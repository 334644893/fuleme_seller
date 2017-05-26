package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */

public class ClerkOederBean {

    /**
     * error_code : 200
     * total : 0.02
     * data : [{"total_fee":"0.01","trade_type":"pay.weixin.jspay","out_trade_no":"201703251602188485413360","coupon_fee":"0","prime_cost":"0.01","time_end":"1490428945"},{"total_fee":"0.01","trade_type":"pay.weixin.jspay","out_trade_no":"201703251602188485413360","coupon_fee":"0","prime_cost":"0.01","time_end":"1490428945"}]
     */

    private String error_code;
    private double total;
    private List<DataBean> data;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * total_fee : 0.01
         * trade_type : pay.weixin.jspay
         * out_trade_no : 201703251602188485413360
         * coupon_fee : 0
         * prime_cost : 0.01
         * time_end : 1490428945
         */

        private double total_fee;
        private String trade_type;
        private String out_trade_no;
        private String coupon_fee;
        private String prime_cost;
        private String time_end;

        public double getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(double total_fee) {
            this.total_fee = total_fee;
        }

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getCoupon_fee() {
            return coupon_fee;
        }

        public void setCoupon_fee(String coupon_fee) {
            this.coupon_fee = coupon_fee;
        }

        public String getPrime_cost() {
            return prime_cost;
        }

        public void setPrime_cost(String prime_cost) {
            this.prime_cost = prime_cost;
        }

        public String getTime_end() {
            return time_end;
        }

        public void setTime_end(String time_end) {
            this.time_end = time_end;
        }
    }
}
