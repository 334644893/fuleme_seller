package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/4.
 */

public class ServiceBusinessesBean {
    /**
     * error_code : 200
     * data : [{"id":1,"short_name":"","merchant_address":"付鹏","create_time":"18827079068","type":1},{"id":2,"short_name":"","merchant_address":"付鹏","create_time":"18827079068","type":2}]
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
         * id : 1
         * short_name :
         * merchant_address : 付鹏
         * create_time : 18827079068
         * type : 1
         */

        private int id;
        private String short_name;
        private String merchant_address;
        private String create_time;
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShort_name() {
            return short_name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        public String getMerchant_address() {
            return merchant_address;
        }

        public void setMerchant_address(String merchant_address) {
            this.merchant_address = merchant_address;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
