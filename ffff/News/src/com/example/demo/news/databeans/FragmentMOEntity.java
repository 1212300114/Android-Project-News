package com.example.demo.news.databeans;

import java.util.List;

/**
 * Created by 123456 on 2015/9/15.
 */
public class FragmentMOEntity {
    //信息公开页的数据实体- 接口脑残非要改一个变量名称╮(╯▽╰)╭

    /**
     * ret : 200
     * data : {"cate":[{"class_id":2,"name":"领导机构","image":"http://www.jjjc.yn.gov.cn/upload/2014-09/05/636bf27266076750d128c5a695bac36b.jpg","show_child":0,"page_link":"http://www.jjjc.yn.gov.cn/mpage-2.html"},{"class_id":37,"name":"组织机构","image":"http://www.jjjc.yn.gov.cn/upload/2014-09/10/4a0a984911ed937f71b6f17283e45e30.jpg","show_child":0,"page_link":"http://www.jjjc.yn.gov.cn/mpage-37.html"},{"class_id":36,"name":"历史沿革","image":"http://www.jjjc.yn.gov.cn/upload/2014-09/10/a839612dfdd0a96b42f732ec2cf4e415.jpg","show_child":0,"page_link":"http://www.jjjc.yn.gov.cn/mpage-36.html"},{"class_id":74,"name":"工作程序","image":"","show_child":0,"page_link":"http://www.jjjc.yn.gov.cn/mpage-74.html"}]}
     * msg :
     */

    private int ret;
    private DataEntity data;
    private String msg;

    public void setRet(int ret) {
        this.ret = ret;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRet() {
        return ret;
    }

    public DataEntity getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public static class DataEntity {
        /**
         * cate : [{"class_id":2,"name":"领导机构","image":"http://www.jjjc.yn.gov.cn/upload/2014-09/05/636bf27266076750d128c5a695bac36b.jpg","show_child":0,"page_link":"http://www.jjjc.yn.gov.cn/mpage-2.html"},{"class_id":37,"name":"组织机构","image":"http://www.jjjc.yn.gov.cn/upload/2014-09/10/4a0a984911ed937f71b6f17283e45e30.jpg","show_child":0,"page_link":"http://www.jjjc.yn.gov.cn/mpage-37.html"},{"class_id":36,"name":"历史沿革","image":"http://www.jjjc.yn.gov.cn/upload/2014-09/10/a839612dfdd0a96b42f732ec2cf4e415.jpg","show_child":0,"page_link":"http://www.jjjc.yn.gov.cn/mpage-36.html"},{"class_id":74,"name":"工作程序","image":"","show_child":0,"page_link":"http://www.jjjc.yn.gov.cn/mpage-74.html"}]
         */

        private List<CateEntity> cate;

        public void setCate(List<CateEntity> cate) {
            this.cate = cate;
        }

        public List<CateEntity> getCate() {
            return cate;
        }

        public static class CateEntity {
            /**
             * class_id : 2
             * name : 领导机构
             * image : http://www.jjjc.yn.gov.cn/upload/2014-09/05/636bf27266076750d128c5a695bac36b.jpg
             * show_child : 0
             * page_link : http://www.jjjc.yn.gov.cn/mpage-2.html
             */

            private int class_id;
            private String name;
            private String image;
            private int show_child;
            private String page_link;

            public void setClass_id(int class_id) {
                this.class_id = class_id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public void setShow_child(int show_child) {
                this.show_child = show_child;
            }

            public void setPage_link(String page_link) {
                this.page_link = page_link;
            }

            public int getClass_id() {
                return class_id;
            }

            public String getName() {
                return name;
            }

            public String getImage() {
                return image;
            }

            public int getShow_child() {
                return show_child;
            }

            public String getPage_link() {
                return page_link;
            }
        }
    }
}
