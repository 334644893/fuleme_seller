package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/22.
 */

public class CouponsBean {


    /**
     * error_code : 200
     * data : [{"reduce":"1","id":"1","used":"4","type":"1","todayused":0,"name":"超级折扣券","term":"0.1","state":"1"},{"reduce":"20","id":"2","used":"0","type":"2","todayused":0,"name":"超级满级","term":"80","state":"1"}]
     */

    private String error_code;
    private List<DataBean> data;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
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
         * reduce : 1
         * id : 1
         * used : 4
         * type : 1
         * todayused : 0
         * name : 超级折扣券
         * term : 0.1
         * state : 1
         */
        private String reduce;
        private String id;
        private String used;
        private String type;
        private int todayused;
        private String name;
        private String term;
        private String state;

        public String getReduce() {
            return reduce;
        }

        public void setReduce(String reduce) {
            this.reduce = reduce;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getTodayused() {
            return todayused;
        }

        public void setTodayused(int todayused) {
            this.todayused = todayused;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
