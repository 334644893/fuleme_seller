package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */

public class ClerkOederBean {


    /**
     * error_code : 200
     * data : {"allTotal":"0.72","allNumber":"49","alipayTotal":"0.07","alipayNumber":"7","weixinTotal":"0.65","weixinNumber":"42"}
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
         * allTotal : 0.72
         * allNumber : 49
         * alipayTotal : 0.07
         * alipayNumber : 7
         * weixinTotal : 0.65
         * weixinNumber : 42
         */

        private double allTotal;
        private String allNumber;
        private double alipayTotal;
        private String alipayNumber;
        private double weixinTotal;
        private String weixinNumber;

        public double getAllTotal() {
            return allTotal;
        }

        public void setAllTotal(double allTotal) {
            this.allTotal = allTotal;
        }

        public String getAllNumber() {
            return allNumber;
        }

        public void setAllNumber(String allNumber) {
            this.allNumber = allNumber;
        }

        public double getAlipayTotal() {
            return alipayTotal;
        }

        public void setAlipayTotal(double alipayTotal) {
            this.alipayTotal = alipayTotal;
        }

        public String getAlipayNumber() {
            return alipayNumber;
        }

        public void setAlipayNumber(String alipayNumber) {
            this.alipayNumber = alipayNumber;
        }

        public double getWeixinTotal() {
            return weixinTotal;
        }

        public void setWeixinTotal(double weixinTotal) {
            this.weixinTotal = weixinTotal;
        }

        public String getWeixinNumber() {
            return weixinNumber;
        }

        public void setWeixinNumber(String weixinNumber) {
            this.weixinNumber = weixinNumber;
        }
    }
}
