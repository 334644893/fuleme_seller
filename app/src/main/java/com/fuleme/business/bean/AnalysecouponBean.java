package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/22.
 */

public class AnalysecouponBean {
    /**
     * error_code : 200
     * data : {"name":"超级折扣券","type":"1","term":"0.1","reduce":"1","start":"1492617600","end":"1492804000","state":"1","addtime":"0","total":"-1","used":"1","list":[0,2,0,0,0,0,0]}
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
         * name : 超级折扣券
         * type : 1
         * term : 0.1
         * reduce : 1
         * start : 1492617600
         * end : 1492804000
         * state : 1
         * addtime : 0
         * total : -1
         * used : 1
         * list : [0,2,0,0,0,0,0]
         */

        private String name;
        private String type;
        private String term;
        private String reduce;
        private String start;
        private String end;
        private int state;
        private String addtime;
        private String total;
        private String used;
        private List<Integer> list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public String getReduce() {
            return reduce;
        }

        public void setReduce(String reduce) {
            this.reduce = reduce;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public List<Integer> getList() {
            return list;
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }
    }
}
