package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class BalanceBean {

    /**
     * error_code : 200
     * message : 请求成功
     * data : {"money":250,"profit":560.01,"list":[{"id":1,"uid":1,"orderid":"201707160934411720722675","money":250,"type":"提现","create_time":1500861113,"time_end":1500947513,"state":1,"bankcard":"建设银行(2540)"},{"id":2,"uid":1,"orderid":"201707160934411720722675","money":160,"type":"提现","create_time":1500861113,"time_end":1500947513,"state":0,"bankcard":"建设银行(2540)"},{"id":8,"uid":1,"orderid":"201707251614431059468187","money":100,"type":"提现","create_time":1500970483,"time_end":0,"state":0,"bankcard":"建设银行(2540)"},{"id":9,"uid":1,"orderid":"201707251614451218789030","money":100,"type":"提现","create_time":1500970485,"time_end":0,"state":0,"bankcard":"建设银行(2540)"},{"id":10,"uid":1,"orderid":"201707251614471399760457","money":100,"type":"提现","create_time":1500970487,"time_end":0,"state":0,"bankcard":"建设银行(2540)"}]}
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
         * money : 250
         * profit : 560.01
         * list : [{"id":1,"uid":1,"orderid":"201707160934411720722675","money":250,"type":"提现","create_time":1500861113,"time_end":1500947513,"state":1,"bankcard":"建设银行(2540)"},{"id":2,"uid":1,"orderid":"201707160934411720722675","money":160,"type":"提现","create_time":1500861113,"time_end":1500947513,"state":0,"bankcard":"建设银行(2540)"},{"id":8,"uid":1,"orderid":"201707251614431059468187","money":100,"type":"提现","create_time":1500970483,"time_end":0,"state":0,"bankcard":"建设银行(2540)"},{"id":9,"uid":1,"orderid":"201707251614451218789030","money":100,"type":"提现","create_time":1500970485,"time_end":0,"state":0,"bankcard":"建设银行(2540)"},{"id":10,"uid":1,"orderid":"201707251614471399760457","money":100,"type":"提现","create_time":1500970487,"time_end":0,"state":0,"bankcard":"建设银行(2540)"}]
         */

        private String money;
        private String profit;
        private List<ListBean> list;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 1
             * uid : 1
             * orderid : 201707160934411720722675
             * money : 250
             * type : 提现
             * create_time : 1500861113
             * time_end : 1500947513
             * state : 1
             * bankcard : 建设银行(2540)
             */

            private int id;
            private int uid;
            private String orderid;
            private float money;
            private String type;
            private int create_time;
            private int time_end;
            private int state;
            private String bankcard;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getOrderid() {
                return orderid;
            }

            public void setOrderid(String orderid) {
                this.orderid = orderid;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(float money) {
                this.money = money;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public int getTime_end() {
                return time_end;
            }

            public void setTime_end(int time_end) {
                this.time_end = time_end;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public String getBankcard() {
                return bankcard;
            }

            public void setBankcard(String bankcard) {
                this.bankcard = bankcard;
            }
        }
    }
}
