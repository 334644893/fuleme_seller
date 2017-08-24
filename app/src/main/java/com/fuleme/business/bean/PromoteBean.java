package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/4.
 */

public class PromoteBean {

    /**
     * error_code : 200
     * data : [{"id":5,"head_img":"","username":"付鹏","phone":"18827079068","shopnum":5},{"id":5,"head_img":"","username":"高飞","phone":"15516619656","shopnum":5}]
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
         * id : 5
         * head_img :
         * username : 付鹏
         * phone : 18827079068
         * shopnum : 5
         */

        private String id;
        private String head_img;
        private String username;
        private String phone;
        private String shopnum;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getShopnum() {
            return shopnum;
        }

        public void setShopnum(String shopnum) {
            this.shopnum = shopnum;
        }
    }
}
