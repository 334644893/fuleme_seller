package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/5.
 */

public class MyBankBean {
    /**
     * error_code : 200
     * data : [{"bankcard":"250250250250","truename":"老毕","account_bank":"中国工商银行","account_bank_address":"武汉市藏龙岛分行"},{"bankcard":"250250250250","truename":"老毕","account_bank":"中国工商银行","account_bank_address":"武汉市藏龙岛分行"}]
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
         * bankcard : 250250250250
         * truename : 老毕
         * account_bank : 中国工商银行
         * account_bank_address : 武汉市藏龙岛分行
         */

        private String bankcard;
        private String truename;
        private String account_bank;
        private String account_bank_address;

        public String getBankcard() {
            return bankcard;
        }

        public void setBankcard(String bankcard) {
            this.bankcard = bankcard;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }

        public String getAccount_bank() {
            return account_bank;
        }

        public void setAccount_bank(String account_bank) {
            this.account_bank = account_bank;
        }

        public String getAccount_bank_address() {
            return account_bank_address;
        }

        public void setAccount_bank_address(String account_bank_address) {
            this.account_bank_address = account_bank_address;
        }
    }
}
