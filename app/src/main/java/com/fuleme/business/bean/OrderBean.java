package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */

public class OrderBean {


    /**
     * error_code : 0
     * data : [{"short_id":"001","short_name":"ifyou咖啡家","y_amount":43,"t_amount":43}]
     */

    private int error_code;
    private List<DataBean> data;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
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
         * short_id : 001
         * short_name : ifyou咖啡家
         * y_amount : 43.0
         * t_amount : 43.0
         */

        private String short_id;
        private String short_name;
        private double y_amount;
        private double t_amount;

        public String getShort_id() {
            return short_id;
        }

        public void setShort_id(String short_id) {
            this.short_id = short_id;
        }

        public String getShort_name() {
            return short_name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        public double getY_amount() {
            return y_amount;
        }

        public void setY_amount(double y_amount) {
            this.y_amount = y_amount;
        }

        public double getT_amount() {
            return t_amount;
        }

        public void setT_amount(double t_amount) {
            this.t_amount = t_amount;
        }
    }
}
