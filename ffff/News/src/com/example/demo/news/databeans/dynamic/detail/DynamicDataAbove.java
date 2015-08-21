package com.example.demo.news.databeans.dynamic.detail;

import java.util.ArrayList;

public class DynamicDataAbove {
	private ArrayList<DynamicDetailBanner> banner;
	private String cate_name;
	private ArrayList<DynamicDetailList> list;
	private String next_link;
	private int page;
	private int pagecount;
	public ArrayList<DynamicDetailBanner> getBanner() {
		return banner;
	}
	public void setBanner(ArrayList<DynamicDetailBanner> banner) {
		this.banner = banner;
	}
	public String getCate_name() {
		return cate_name;
	}
	public void setCate_name(String cate_name) {
		this.cate_name = cate_name;
	}
	public ArrayList<DynamicDetailList> getList() {
		return list;
	}
	public void setList(ArrayList<DynamicDetailList> list) {
		this.list = list;
	}
	public String getNext_link() {
		return next_link;
	}
	public void setNext_link(String next_link) {
		this.next_link = next_link;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPagecount() {
		return pagecount;
	}
	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}
	

}
