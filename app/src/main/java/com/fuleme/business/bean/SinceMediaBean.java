package com.fuleme.business.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class SinceMediaBean {

    /**
     * error_code : 200
     * data : [{"id":5,"img":"/static/img/icon-koubei.png","title":"口碑客","abstract":"口碑服务窗口","url":"www.baidu.com"},{"id":4,"img":"/static/img/icon-jinri.png","title":"今日头条","abstract":"今日头条服务窗口","url":"www.baidu.com"},{"id":3,"img":"/static/img/icon-dazhong.png","title":"大众点评","abstract":"大众服务窗口","url":"www.baodu.com"},{"id":2,"img":"/static/img/icon-weixin.png","title":"微信公众号管理","abstract":"微信服务窗口","url":"www.baidu.com"}]
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
         * id : 5
         * img : /static/img/icon-koubei.png
         * title : 口碑客
         * abstract : 口碑服务窗口
         * url : www.baidu.com
         */

        private int id;
        private String img;
        private String title;
        @SerializedName("abstract")
        private String abstractX;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAbstractX() {
            return abstractX;
        }

        public void setAbstractX(String abstractX) {
            this.abstractX = abstractX;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
