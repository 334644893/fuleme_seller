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
    private String balance;
    private String prompt;
    private List<DataBean> data;

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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

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

        private String resultTime;
        private double arrivalAmount;
        private int resultStatus;

        public String getResultTime() {
            return resultTime;
        }

        public void setResultTime(String resultTime) {
            this.resultTime = resultTime;
        }

        public double getArrivalAmount() {
            return arrivalAmount;
        }

        public void setArrivalAmount(double arrivalAmount) {
            this.arrivalAmount = arrivalAmount;
        }

        public int getResultStatus() {
            return resultStatus;
        }

        public void setResultStatus(int resultStatus) {
            this.resultStatus = resultStatus;
        }
    }
}
