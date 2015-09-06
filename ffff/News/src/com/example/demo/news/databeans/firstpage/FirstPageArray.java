package com.example.demo.news.databeans.firstpage;

import java.util.ArrayList;

public class FirstPageArray {
    private ArrayList<FirstpageLoopPager> banner;
    private ArrayList<FirstpageLoopPager> list;
    private ArrayList<FirstPageCate> cate;
    private int page;
    private String next_link;
    private int pagecount;

    public ArrayList<FirstpageLoopPager> getBanner() {
        return banner;
    }

    public void setBanner(ArrayList<FirstpageLoopPager> banner) {
        this.banner = banner;
    }

    public ArrayList<FirstpageLoopPager> getList() {
        return list;
    }

    public void setList(ArrayList<FirstpageLoopPager> list) {
        this.list = list;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getNext_link() {
        return next_link;
    }

    public void setNext_link(String next_link) {
        this.next_link = next_link;
    }

    public int getPagecount() {
        return pagecount;
    }

    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }

    public ArrayList<FirstPageCate> getCate() {
        return cate;
    }

    public void setCate(ArrayList<FirstPageCate> cate) {
        this.cate = cate;
    }
}
