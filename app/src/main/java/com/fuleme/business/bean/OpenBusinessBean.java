package com.fuleme.business.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/2.
 * 开通业务
 */

public class OpenBusinessBean implements Serializable{
    /**
     * businessName 业务名
     * state 开通状态 0关1开·默认为关
     * rate 费率
     */

    private String businessName = "";
    private int state = 0;
    private double rate = 0;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
