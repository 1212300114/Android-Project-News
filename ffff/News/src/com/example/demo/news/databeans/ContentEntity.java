package com.example.demo.news.databeans;

public class ContentEntity {
    //详情内容的实体

    private int ret;
    private String msg;
    private ContentDataDetail data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ContentDataDetail getData() {
        return data;
    }

    public void setData(ContentDataDetail data) {
        this.data = data;
    }

    public static class ContentDataDetail {

        private int class_id;
        private int content_id;
        private String copyfrom;
        private String share_image;
        private String share_link;
        private String time;
        private String title;
        private String content;

        public int getClass_id() {
            return class_id;
        }

        public void setClass_id(int class_id) {
            this.class_id = class_id;
        }

        public int getContent_id() {
            return content_id;
        }

        public void setContent_id(int content_id) {
            this.content_id = content_id;
        }

        public String getCopyfrom() {
            return copyfrom;
        }

        public void setCopyfrom(String copyfrom) {
            this.copyfrom = copyfrom;
        }

        public String getShare_image() {
            return share_image;
        }

        public void setShare_image(String share_image) {
            this.share_image = share_image;
        }

        public String getShare_link() {
            return share_link;
        }

        public void setShare_link(String share_link) {
            this.share_link = share_link;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }


}
