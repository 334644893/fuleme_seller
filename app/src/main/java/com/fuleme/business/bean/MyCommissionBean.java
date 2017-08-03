package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public class MyCommissionBean {
    /**
     * error_code : 200
     * message : 请求成功
     * data : {"info":{"num":3,"money":0.0716},"list":[{"trade_type":"alipay","short_name":"合财购","orderid":"201707210823572474261613","total_fee":"2.46","create_time":"1501149137","money":"0.0246","pid":"1","rate":"0.001"},{"trade_type":"alipay","short_name":"合财购","orderid":"201707210721582553980502","total_fee":"2.38","create_time":"1501149137","money":"0.0238","pid":"1","rate":"0.001"},{"trade_type":"alipay","short_name":"合财购","orderid":"201707210720314247643649","total_fee":"2.32","create_time":"1501149137","money":"0.0232","pid":"1","rate":"0.001"}]}
     */

    private String error_code;
    private String message;
    private DataBean data;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * info : {"num":3,"money":0.0716}
         * list : [{"trade_type":"alipay","short_name":"合财购","orderid":"201707210823572474261613","total_fee":"2.46","create_time":"1501149137","money":"0.0246","pid":"1","rate":"0.001"},{"trade_type":"alipay","short_name":"合财购","orderid":"201707210721582553980502","total_fee":"2.38","create_time":"1501149137","money":"0.0238","pid":"1","rate":"0.001"},{"trade_type":"alipay","short_name":"合财购","orderid":"201707210720314247643649","total_fee":"2.32","create_time":"1501149137","money":"0.0232","pid":"1","rate":"0.001"}]
         */

        private InfoBean info;
        private List<ListBean> list;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class InfoBean {
            /**
             * num : 3
             * money : 0.0716
             */

            private String num;
            private double money;

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }
        }

        public static class ListBean {
            /**
             * trade_type : alipay
             * short_name : 合财购
             * orderid : 201707210823572474261613
             * total_fee : 2.46
             * create_time : 1501149137
             * money : 0.0246
             * pid : 1
             * rate : 0.001
             */

            private String trade_type;
            private String short_name;
            private String orderid;
            private double total_fee;
            private String create_time;
            private double money;
            private String pid;
            private String rate;

            public String getTrade_type() {
                return trade_type;
            }

            public void setTrade_type(String trade_type) {
                this.trade_type = trade_type;
            }

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public String getOrderid() {
                return orderid;
            }

            public void setOrderid(String orderid) {
                this.orderid = orderid;
            }

            public double getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(double total_fee) {
                this.total_fee = total_fee;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getRate() {
                return rate;
            }

            public void setRate(String rate) {
                this.rate = rate;
            }
        }
    }
}
