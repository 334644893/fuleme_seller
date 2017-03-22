package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/22.
 */

public class OrderDetailsBean {


    /**
     * error_code : 200
     * totalAmount : 768.99
     * totalOrder : 10
     * data : [{"date":"2017-03-21","total_fee":6.5,"count":2,"details":[{"trade_type":"pay.alipay.jspay","time_end":1490063524,"total_fee":1.5,"out_trade_no":"1489988559zelzPxkgJW5311945606"},{"trade_type":"546","time_end":1490063524,"total_fee":5,"out_trade_no":"14686466"}]}]
     */

    private String error_code;
    private double totalAmount;
    private int totalOrder;
    private List<DataBean> data;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(int totalOrder) {
        this.totalOrder = totalOrder;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * date : 2017-03-21
         * total_fee : 6.5
         * count : 2
         * details : [{"trade_type":"pay.alipay.jspay","time_end":1490063524,"total_fee":1.5,"out_trade_no":"1489988559zelzPxkgJW5311945606"},{"trade_type":"546","time_end":1490063524,"total_fee":5,"out_trade_no":"14686466"}]
         */

        private String date;
        private double total_fee;
        private int count;
        private List<DetailsBean> details;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(double total_fee) {
            this.total_fee = total_fee;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsBean> details) {
            this.details = details;
        }

        public static class DetailsBean {
            /**
             * trade_type : pay.alipay.jspay
             * time_end : 1490063524
             * total_fee : 1.5
             * out_trade_no : 1489988559zelzPxkgJW5311945606
             */

            private String trade_type;
            private String time_end;
            private double total_fee;
            private String out_trade_no;

            public String getTrade_type() {
                return trade_type;
            }

            public void setTrade_type(String trade_type) {
                this.trade_type = trade_type;
            }

            public String getTime_end() {
                return time_end;
            }

            public void setTime_end(String time_end) {
                this.time_end = time_end;
            }

            public double getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(double total_fee) {
                this.total_fee = total_fee;
            }

            public String getOut_trade_no() {
                return out_trade_no;
            }

            public void setOut_trade_no(String out_trade_no) {
                this.out_trade_no = out_trade_no;
            }
        }
    }
}
