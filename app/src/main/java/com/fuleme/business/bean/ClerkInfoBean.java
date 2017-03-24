package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 */

public class ClerkInfoBean {

    /**
     * error_code : 200
     * data : [{"id":1,"name":"芙蓉兴盛超市1","address":"湖北省武汉市江夏区武软","clerk":[{"id":19,"phone":"13419567555","username":"Bml","role":1},{"id":30,"phone":"13419567666","username":"Ming","role":2}]},{"id":2,"name":"芙蓉兴盛超市2","address":"湖北省武汉市江夏区武软","clerk":[{"id":15,"phone":"13035674287","username":"clerk01","role":1}]}]
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
         * id : 1
         * name : 芙蓉兴盛超市1
         * address : 湖北省武汉市江夏区武软
         * clerk : [
         * {"id":19,"phone":"13419567555","username":"Bml","role":1},
         * {"id":30,"phone":"13419567666","username":"Ming","role":2}
         * ]
         */

        private String id;
        private String name;
        private String address;
        private String state;
        private String qrcode;
        private List<ClerkBean> clerk;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<ClerkBean> getClerk() {
            return clerk;
        }

        public void setClerk(List<ClerkBean> clerk) {
            this.clerk = clerk;
        }

        public static class ClerkBean {
            /**
             * id : 19
             * phone : 13419567555
             * username : Bml
             * role : 1
             */

            private int id;
            private String phone;
            private String username;
            private int role;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public int getRole() {
                return role;
            }

            public void setRole(int role) {
                this.role = role;
            }
        }
    }
}
