package com.example.demo.news.databeans.search;

import java.util.ArrayList;

public class SearchDataDetail {
	private ArrayList<SearchList> list;
	private int page;
	private int pagecount;
	private String next_link;

	public ArrayList<SearchList> getList() {
		return list;
	}

	public void setList(ArrayList<SearchList> list) {
		this.list = list;
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

	public String getNext_link() {
		return next_link;
	}

	public void setNext_link(String next_link) {
		this.next_link = next_link;
	}
}
