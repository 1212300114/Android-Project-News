package com.example.demo.news.databeans.importantnews;

import java.util.ArrayList;

public class ImportantNewsDataDetail {
	private ArrayList<ImportantNewsCate> cate;
	private ArrayList<ImportantNewsBanner> banner;
	private ArrayList<ImportantNewsList> list;
	private String next_link;
	private int page;
	private int pagecount;
	public ArrayList<ImportantNewsCate> getCate() {
		return cate;
	}
	public void setCate(ArrayList<ImportantNewsCate> cate) {
		this.cate = cate;
	}
	public ArrayList<ImportantNewsBanner> getBanner() {
		return banner;
	}
	public void setBanner(ArrayList<ImportantNewsBanner> banner) {
		this.banner = banner;
	}
	public ArrayList<ImportantNewsList> getList() {
		return list;
	}
	public void setList(ArrayList<ImportantNewsList> list) {
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
