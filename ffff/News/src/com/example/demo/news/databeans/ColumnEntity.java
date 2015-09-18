package com.example.demo.news.databeans;

import java.io.Serializable;
import java.util.List;

public class ColumnEntity implements Serializable {
    //各栏目的实体
    /**
     * ret : 200
     * data : {"cate":[{"class_id":17,"name":"时政头条","image":"","show_child":0,"cate_link":"List.index&cid=17"},{"class_id":18,"name":"八面来风","image":"","show_child":0,"cate_link":"List.index&cid=18"},{"class_id":23,"name":"基层情况","image":"","show_child":0,"cate_link":"List.index&cid=23"},{"class_id":31,"name":"州市动态","image":"","show_child":0,"cate_link":"List.index&cid=31"},{"class_id":62,"name":"云岭要闻","image":"","show_child":0,"cate_link":"List.index&cid=62"}],"banner":[{"content_id":12077,"class_id":17,"title":"张硕辅看望慰问云南纪检监察学院师生","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/5d91b6f8d1e5021ca1886e695d683ed4_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12077.html"},{"content_id":12038,"class_id":17,"title":"王岐山会见出席\"2015中国共产党与世界对话会\"外方代表","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/2c647f134ea0339879ec18e125ea4550_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12038.html"},{"content_id":12000,"class_id":17,"title":"张硕辅到楚雄市调研时强调\r\n认真落实\u201c挂包帮\u201d全力做好精准扶贫","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/09/3baada577d94feba052030988241e29a_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12000.html"},{"content_id":11627,"class_id":62,"title":"全省各级纪检监察机关认真学习传达贯彻省委全会精神","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-08/27/05e7824d536ae2794b52a1dbafd40fb5_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-11627.html"},{"content_id":11626,"class_id":62,"title":"云南严查干部涉\u201c地\u201d涉\u201c矿\u201d案","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-08/27/848181796207ce200898f73c170ad2e5_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-11626.html"}],"list":[{"content_id":12089,"class_id":62,"title":"张硕辅专题教育党课在德宏引发强烈反响","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/11/654a39a3385983806c4ba3b0cd942c71_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12089.html"},{"content_id":12088,"class_id":62,"title":"保山：召开常委会议专题学习张硕辅专题党课精神","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/11/7fc8b7d622632f7ffd3aa96b04f1ae94_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12088.html"},{"content_id":12059,"class_id":17,"title":"张硕辅：清正廉洁正身直行 坚守为官干净从政底线","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/16508b60db5260bb1b2c941a638ebf3d_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12059.html"},{"content_id":12058,"class_id":17,"title":"我省2015年第二轮专项巡视全面启动","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/04eff2a6c53d2a8661d926d275a88192_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12058.html"},{"content_id":12057,"class_id":62,"title":"省委第十巡视组进驻云南文化产业投资控股集团有限责任公司开展专项巡视\r\n","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/f3d3008087d71aa72e797a41d0c2be91_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12057.html"},{"content_id":12053,"class_id":23,"title":"会泽：\u201c有章、有序、有效\u201d运行灾后恢复重建资金确保高效安全","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/e314de8ace8c91d2cfd1cdde59d6b5df_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12053.html"},{"content_id":12052,"class_id":62,"title":"省委第一巡视组进驻云天化集团开展专项巡视","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/fa2147e20baa0e2d6805a661afec0d7c_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12052.html"},{"content_id":12036,"class_id":62,"title":"云南省国税局切实加强对权力运行的监督制约","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/3a4e558db760aa789ec6b395d03d2587_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12036.html"},{"content_id":11954,"class_id":62,"title":"德宏：\u201c五个突出\u201d聚焦主业","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/08/6deeac2a2219f399ada73c0f87d2a406_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-11954.html"},{"content_id":11945,"class_id":62,"title":"昆明：8月问责领导干部24名","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/07/15ecf434d65fd2bbdc4f20038bb2a9be_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-11945.html"}],"page":1,"next_link":"List.index&cid=30&page=2","pagecount":5}
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

    public static class DataEntity implements Serializable {
        /**
         * cate : [{"class_id":17,"name":"时政头条","image":"","show_child":0,"cate_link":"List.index&cid=17"},{"class_id":18,"name":"八面来风","image":"","show_child":0,"cate_link":"List.index&cid=18"},{"class_id":23,"name":"基层情况","image":"","show_child":0,"cate_link":"List.index&cid=23"},{"class_id":31,"name":"州市动态","image":"","show_child":0,"cate_link":"List.index&cid=31"},{"class_id":62,"name":"云岭要闻","image":"","show_child":0,"cate_link":"List.index&cid=62"}]
         * banner : [{"content_id":12077,"class_id":17,"title":"张硕辅看望慰问云南纪检监察学院师生","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/5d91b6f8d1e5021ca1886e695d683ed4_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12077.html"},{"content_id":12038,"class_id":17,"title":"王岐山会见出席\"2015中国共产党与世界对话会\"外方代表","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/2c647f134ea0339879ec18e125ea4550_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12038.html"},{"content_id":12000,"class_id":17,"title":"张硕辅到楚雄市调研时强调\r\n认真落实\u201c挂包帮\u201d全力做好精准扶贫","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/09/3baada577d94feba052030988241e29a_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12000.html"},{"content_id":11627,"class_id":62,"title":"全省各级纪检监察机关认真学习传达贯彻省委全会精神","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-08/27/05e7824d536ae2794b52a1dbafd40fb5_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-11627.html"},{"content_id":11626,"class_id":62,"title":"云南严查干部涉\u201c地\u201d涉\u201c矿\u201d案","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-08/27/848181796207ce200898f73c170ad2e5_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-11626.html"}]
         * list : [{"content_id":12089,"class_id":62,"title":"张硕辅专题教育党课在德宏引发强烈反响","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/11/654a39a3385983806c4ba3b0cd942c71_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12089.html"},{"content_id":12088,"class_id":62,"title":"保山：召开常委会议专题学习张硕辅专题党课精神","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/11/7fc8b7d622632f7ffd3aa96b04f1ae94_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12088.html"},{"content_id":12059,"class_id":17,"title":"张硕辅：清正廉洁正身直行 坚守为官干净从政底线","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/16508b60db5260bb1b2c941a638ebf3d_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12059.html"},{"content_id":12058,"class_id":17,"title":"我省2015年第二轮专项巡视全面启动","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/04eff2a6c53d2a8661d926d275a88192_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12058.html"},{"content_id":12057,"class_id":62,"title":"省委第十巡视组进驻云南文化产业投资控股集团有限责任公司开展专项巡视\r\n","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/f3d3008087d71aa72e797a41d0c2be91_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12057.html"},{"content_id":12053,"class_id":23,"title":"会泽：\u201c有章、有序、有效\u201d运行灾后恢复重建资金确保高效安全","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/e314de8ace8c91d2cfd1cdde59d6b5df_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12053.html"},{"content_id":12052,"class_id":62,"title":"省委第一巡视组进驻云天化集团开展专项巡视","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/fa2147e20baa0e2d6805a661afec0d7c_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12052.html"},{"content_id":12036,"class_id":62,"title":"云南省国税局切实加强对权力运行的监督制约","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/3a4e558db760aa789ec6b395d03d2587_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-12036.html"},{"content_id":11954,"class_id":62,"title":"德宏：\u201c五个突出\u201d聚焦主业","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/08/6deeac2a2219f399ada73c0f87d2a406_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-11954.html"},{"content_id":11945,"class_id":62,"title":"昆明：8月问责领导干部24名","image":"http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/07/15ecf434d65fd2bbdc4f20038bb2a9be_thumb.jpg","app_label":"","info_link":"http://www.jjjc.yn.gov.cn/mobile-view-11945.html"}]
         * page : 1
         * next_link : List.index&cid=30&page=2
         * pagecount : 5
         */

        private int page;
        private String next_link;
        private int pagecount;
        private List<CateEntity> cate;
        private List<BannerEntity> banner;
        private List<ListEntity> list;

        public void setPage(int page) {
            this.page = page;
        }

        public void setNext_link(String next_link) {
            this.next_link = next_link;
        }

        public void setPagecount(int pagecount) {
            this.pagecount = pagecount;
        }

        public void setCate(List<CateEntity> cate) {
            this.cate = cate;
        }

        public void setBanner(List<BannerEntity> banner) {
            this.banner = banner;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public int getPage() {
            return page;
        }

        public String getNext_link() {
            return next_link;
        }

        public int getPagecount() {
            return pagecount;
        }

        public List<CateEntity> getCate() {
            return cate;
        }

        public List<BannerEntity> getBanner() {
            return banner;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public static class CateEntity implements Serializable {
            /**
             * class_id : 17
             * name : 时政头条
             * image :
             * show_child : 0
             * cate_link : List.index&cid=17
             */

            private int class_id;
            private String name;
            private String image;
            private int show_child;
            private String cate_link;

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

            public void setCate_link(String cate_link) {
                this.cate_link = cate_link;
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

            public String getCate_link() {
                return cate_link;
            }
        }

        public static class BannerEntity implements Serializable {
            /**
             * content_id : 12077
             * class_id : 17
             * title : 张硕辅看望慰问云南纪检监察学院师生
             * image : http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/10/5d91b6f8d1e5021ca1886e695d683ed4_thumb.jpg
             * app_label :
             * info_link : http://www.jjjc.yn.gov.cn/mobile-view-12077.html
             */

            private int content_id;
            private int class_id;
            private String title;
            private String image;
            private String app_label;
            private String info_link;

            public void setContent_id(int content_id) {
                this.content_id = content_id;
            }

            public void setClass_id(int class_id) {
                this.class_id = class_id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public void setApp_label(String app_label) {
                this.app_label = app_label;
            }

            public void setInfo_link(String info_link) {
                this.info_link = info_link;
            }

            public int getContent_id() {
                return content_id;
            }

            public int getClass_id() {
                return class_id;
            }

            public String getTitle() {
                return title;
            }

            public String getImage() {
                return image;
            }

            public String getApp_label() {
                return app_label;
            }

            public String getInfo_link() {
                return info_link;
            }
        }

        public static class ListEntity implements Serializable {
            /**
             * content_id : 12089
             * class_id : 62
             * title : 张硕辅专题教育党课在德宏引发强烈反响
             * image : http://www.jjjc.yn.gov.cn/getimg.php?file=/upload/2015-09/11/654a39a3385983806c4ba3b0cd942c71_thumb.jpg
             * app_label :
             * info_link : http://www.jjjc.yn.gov.cn/mobile-view-12089.html
             */

            private int content_id;
            private int class_id;
            private String title;
            private String image;
            private String app_label;
            private String info_link;

            public void setContent_id(int content_id) {
                this.content_id = content_id;
            }

            public void setClass_id(int class_id) {
                this.class_id = class_id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public void setApp_label(String app_label) {
                this.app_label = app_label;
            }

            public void setInfo_link(String info_link) {
                this.info_link = info_link;
            }

            public int getContent_id() {
                return content_id;
            }

            public int getClass_id() {
                return class_id;
            }

            public String getTitle() {
                return title;
            }

            public String getImage() {
                return image;
            }

            public String getApp_label() {
                return app_label;
            }

            public String getInfo_link() {
                return info_link;
            }
        }
    }


}
