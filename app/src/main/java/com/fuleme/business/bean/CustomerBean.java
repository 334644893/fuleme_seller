package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 */

public class CustomerBean {

    /**
     * error_code : 200
     * data : [{"id":"1","merchant_id":"1000002","openid":"2088902702747495","score":"4","comment":"","addtime":"1494486584","nickname":"","headimg":""},{"id":"2","merchant_id":"1000002","openid":"2088902702747495","score":"3","comment":"","addtime":"1494486690","nickname":"xii","headimg":"https://tfs.alipayobjects.com/images/partner/T1gbXXXXXXXX"}]
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
         * merchant_id : 1000002
         * openid : 2088902702747495
         * score : 4
         * comment :
         * addtime : 1494486584
         * nickname :
         * headimg :
         */

        private String id;
        private String merchant_id;
        private String openid;
        private String score;
        private String comment;
        private String addtime;
        private String nickname;
        private String headimg;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMerchant_id() {
            return merchant_id;
        }

        public void setMerchant_id(String merchant_id) {
            this.merchant_id = merchant_id;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }
    }
}
