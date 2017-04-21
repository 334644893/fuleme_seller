package com.fuleme.business.bean;

/**
 * Created by Administrator on 2017/4/21.
 */

public class ReportRBean {
    private int mItemImage;
    private String title;
    private String number;
    private double total_fee;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getmItemImage() {
        return mItemImage;
    }

    public void setmItemImage(int mItemImage) {
        this.mItemImage = mItemImage;
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
