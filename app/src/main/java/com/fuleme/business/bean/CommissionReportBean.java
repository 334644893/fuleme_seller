package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/5.
 */

public class CommissionReportBean {
    /**
     * number : 2
     * money : 3.1
     * DayInfo : [{"time_end":"2017-07-01","number":"0","money":"0"},{"time_end":"2017-07-02","number":"0","money":"0"},{"time_end":"2017-07-03","number":"0","money":"0"},{"time_end":"2017-07-04","number":"0","money":"0"},{"time_end":"2017-07-05","number":"0","money":"0"},{"time_end":"2017-07-06","number":"0","money":"0"},{"time_end":"2017-07-07","number":"0","money":"0"},{"time_end":"2017-07-08","number":"0","money":"0"},{"time_end":"2017-07-09","number":"0","money":"0"},{"time_end":"2017-07-10","number":"0","money":"0"},{"time_end":"2017-07-11","number":"0","money":"0"},{"time_end":"2017-07-12","number":"0","money":"0"},{"time_end":"2017-07-13","number":"0","money":"0"},{"time_end":"2017-07-14","number":"0","money":"0"},{"time_end":"2017-07-15","number":"0","money":"0"},{"time_end":"2017-07-16","number":"0","money":"0"},{"time_end":"2017-07-17","number":"0","money":"0"},{"time_end":"2017-07-18","number":1,"money":3.6},{"time_end":"2017-07-19","number":"0","money":"0"},{"time_end":"2017-07-20","number":"0","money":"0"},{"time_end":"2017-07-21","number":"0","money":"0"},{"time_end":"2017-07-22","number":"0","money":"0"},{"time_end":"2017-07-23","number":"0","money":"0"},{"time_end":"2017-07-24","number":"0","money":"0"},{"time_end":"2017-07-25","number":2,"money":3.1},{"time_end":"2017-07-26","number":"0","money":"0"},{"time_end":"2017-07-27","number":"0","money":"0"},{"time_end":"2017-07-28","number":"0","money":"0"},{"time_end":"2017-07-29","number":"0","money":"0"},{"time_end":"2017-07-30","number":"0","money":"0"},{"time_end":"2017-07-31","number":"0","money":"0"}]
     */
    private int number;
    private double money;
    private List<DayInfoBean> DayInfo;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public List<DayInfoBean> getDayInfo() {
        return DayInfo;
    }

    public void setDayInfo(List<DayInfoBean> DayInfo) {
        this.DayInfo = DayInfo;
    }

    public static class DayInfoBean {
        /**
         * time_end : 2017-07-01
         * number : 0
         * money : 0
         */

        private String time_end;
        private String number;
        private float money;

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

        public float getMoney() {
            return money;
        }

        public void setMoney(float money) {
            this.money = money;
        }
    }
}
