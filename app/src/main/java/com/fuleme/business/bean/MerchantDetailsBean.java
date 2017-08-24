package com.fuleme.business.bean;

/**
 * Created by Administrator on 2017/8/4.
 */

public class MerchantDetailsBean {

    /**
     * error_code : 200
     * message : 请求成功
     * data : {"id":1000002,"short_name":"付了么聚合支付","alipay_operate_category":"美食|中餐|湘菜","merchant_area":"湖北 武汉 洪山区","merchant_address":"关山大道555号","contact_person_name":"梁锐","contact_person_mobile":"13910070763"}
     */

    private String error_code;
    private String message;
    private DataBean data;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1000002
         * short_name : 付了么聚合支付
         * alipay_operate_category : 美食|中餐|湘菜
         * merchant_area : 湖北 武汉 洪山区
         * merchant_address : 关山大道555号
         * contact_person_name : 梁锐
         * contact_person_mobile : 13910070763
         */

        private int id;
        private String short_name;
        private String alipay_operate_category;
        private String merchant_area;
        private String merchant_address;
        private String contact_person_name;
        private String contact_person_mobile;

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

        public String getAlipay_operate_category() {
            return alipay_operate_category;
        }

        public void setAlipay_operate_category(String alipay_operate_category) {
            this.alipay_operate_category = alipay_operate_category;
        }

        public String getMerchant_area() {
            return merchant_area;
        }

        public void setMerchant_area(String merchant_area) {
            this.merchant_area = merchant_area;
        }

        public String getMerchant_address() {
            return merchant_address;
        }

        public void setMerchant_address(String merchant_address) {
            this.merchant_address = merchant_address;
        }

        public String getContact_person_name() {
            return contact_person_name;
        }

        public void setContact_person_name(String contact_person_name) {
            this.contact_person_name = contact_person_name;
        }

        public String getContact_person_mobile() {
            return contact_person_mobile;
        }

        public void setContact_person_mobile(String contact_person_mobile) {
            this.contact_person_mobile = contact_person_mobile;
        }
    }
}
