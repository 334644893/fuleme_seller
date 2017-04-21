package com.fuleme.business.bean;

import java.util.List;

/**
 * 累计收入（报表）
 */

public class IncomeBean {

    /**
     * error_code : 200
     * message : 请求成功
     * data : {"number":"36","total_fee":"0.41","EveryDay":[{"time_end":"17-03-23","number":"5","total_fee":"0.05"},{"time_end":"17-03-31","number":"10","total_fee":"0.11"},{"time_end":"17-04-01","number":"9","total_fee":"0.09"},{"time_end":"17-04-05","number":"1","total_fee":"0.01"},{"time_end":"17-04-11","number":"1","total_fee":"0.01"},{"time_end":"17-04-18","number":"1","total_fee":"0.01"},{"time_end":"17-04-20","number":"6","total_fee":"0.1"},{"time_end":"17-04-21","number":"3","total_fee":"0.03"}],"weixin":{"number":"19","total_fee":"0.24"},"alipay":{"number":"17","total_fee":"0.17"}}
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
         * number : 36
         * total_fee : 0.41
         * EveryDay : [{"time_end":"17-03-23","number":"5","total_fee":"0.05"},{"time_end":"17-03-31","number":"10","total_fee":"0.11"},{"time_end":"17-04-01","number":"9","total_fee":"0.09"},{"time_end":"17-04-05","number":"1","total_fee":"0.01"},{"time_end":"17-04-11","number":"1","total_fee":"0.01"},{"time_end":"17-04-18","number":"1","total_fee":"0.01"},{"time_end":"17-04-20","number":"6","total_fee":"0.1"},{"time_end":"17-04-21","number":"3","total_fee":"0.03"}]
         * weixin : {"number":"19","total_fee":"0.24"}
         * alipay : {"number":"17","total_fee":"0.17"}
         */

        private String number;
        private String total_fee;
        private WeixinBean weixin;
        private AlipayBean alipay;
        private List<EveryDayBean> EveryDay;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public WeixinBean getWeixin() {
            return weixin;
        }

        public void setWeixin(WeixinBean weixin) {
            this.weixin = weixin;
        }

        public AlipayBean getAlipay() {
            return alipay;
        }

        public void setAlipay(AlipayBean alipay) {
            this.alipay = alipay;
        }

        public List<EveryDayBean> getEveryDay() {
            return EveryDay;
        }

        public void setEveryDay(List<EveryDayBean> EveryDay) {
            this.EveryDay = EveryDay;
        }

        public static class WeixinBean {
            /**
             * number : 19
             * total_fee : 0.24
             */

            private String number;
            private double total_fee;

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public double getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(double total_fee) {
                this.total_fee = total_fee;
            }
        }

        public static class AlipayBean {
            /**
             * number : 17
             * total_fee : 0.17
             */

            private String number;
            private double total_fee;

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public double getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(double total_fee) {
                this.total_fee = total_fee;
            }
        }

        public static class EveryDayBean {
            /**
             * time_end : 17-03-23
             * number : 5
             * total_fee : 0.05
             */

            private String time_end;
            private String number;
            private double total_fee;

            public String getTime_end() {
                return time_end;
            }

            public void setTime_end(String time_end) {
                this.time_end = time_end;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public double getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(double total_fee) {
                this.total_fee = total_fee;
            }
        }
    }
}
