package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 * ADD行业
 */

public class IndustryBean {

    /**
     * error_code : 0
     * data : [{"id":"001001","name":"餐饮","data":[{"id":"001001","name":"餐饮"},{"id":"001001","name":"餐饮"}]},{"id":"002001","name":"泛行业","data":[{"id":"002001","name":"第一个行业"},{"id":"002001","name":"第二个行业"}]}]
     */

    private int error_code;
    private List<DataBeanX> data;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * id : 001001  后台将id改为code
         * name : 餐饮
         * data : [{"id":"001001","name":"餐饮"},{"id":"001001","name":"餐饮"}]
         */

//        private String id;
        private String code;
        private String name;
        private List<DataBean> data;

        public String getId() {
            return code;
        }

        public void setId(String id) {
            this.code = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 001001 后台将id改为code
             * name : 餐饮
             */

            private String code;
            private String name;

            public String getId() {
                return code;
            }

            public void setId(String id) {
                this.code = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
