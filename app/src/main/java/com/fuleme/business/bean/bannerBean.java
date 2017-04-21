package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class bannerBean {

    /**
     * error_code : 200
     * data : [{"img":"/res/upload/banner-4.jpg","url":""},{"img":"/res/upload/banner-3.jpg","url":""},{"img":"/res/upload/banner-2.jpg","url":""},{"img":"/res/upload/banner-1.jpg","url":""}]
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
         * img : /res/upload/banner-4.jpg
         * url :
         */

        private String img;
        private String url;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
