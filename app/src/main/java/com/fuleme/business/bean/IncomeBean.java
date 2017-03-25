package com.fuleme.business.bean;

import java.util.List;

/**
 * 收入
 */

public class IncomeBean {

    /**
     * error_code : 1
     * message : 请求成功
     * balance : 281.93
     * prompt : 到账时间提示
     * data : [
     * {"resultTime":"20170115","arrivalAmount":1672.72,"resultStatus":1},
     * {"resultTime":"20170120","arrivalAmount":3322.72,"resultStatus":0}]
     */

    private int error_code;
    private String message;
    private double totalAmount;
//    private String prompt;
    private List<DataBean> data;

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


//    public String getPrompt() {
//        return prompt;
//    }
//
//    public void setPrompt(String prompt) {
//        this.prompt = prompt;
//    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * resultTime : 20170115
         * arrivalAmount : 1672.72
         * resultStatus : 1
         */

        private String trading_time;
        private double total_fee;
        private int count;

        public String getResultTime() {
            return trading_time;
        }

        public void setResultTime(String resultTime) {
            this.trading_time = resultTime;
        }

        public double getArrivalAmount() {
            return total_fee;
        }

        public void setArrivalAmount(double arrivalAmount) {
            this.total_fee = arrivalAmount;
        }

        public int getResultStatus() {
            return count;
        }

        public void setResultStatus(int resultStatus) {
            this.count = resultStatus;
        }
    }
}
