package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 */

public class BankBean {

    /**
     * error_code : 200
     * data : [{"bankCode":"102100099996","bankName":"中国工商银行"},{"bankCode":"103100000026","bankName":"中国农业银行"}]
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
         * bankCode : 102100099996
         * bankName : 中国工商银行
         */

        private String bankCode;
        private String bankName;

        public String getBankCode() {
            return bankCode;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }
    }
}
