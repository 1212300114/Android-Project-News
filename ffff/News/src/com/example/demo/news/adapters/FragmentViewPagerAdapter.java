package com.example.demo.news.adapters;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
	// 主fragment力放置的ViewPager的fragment型pageradapter
	// 可以将许多fragment放在一个ViewPager里面达到左右滑动进入不同的可操作界面的效果
	// 这里因为要用到ViewPagerIndicator所以必须要重写getpagetitle方法因为indicator是通过这个方法来获取内容的
	private ArrayList<Fragment> fragments;
	private String[] titles = new String[] { "首页", "要闻", "党风", "审查", "巡视",
			"图闻", "专题" };

	public ArrayList<Fragment> getFragments() {
		return fragments;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public void setFragments(ArrayList<Fragment> fragments) {
		this.fragments = fragments;
	}

	public FragmentViewPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public FragmentViewPagerAdapter(FragmentManager fm,
			ArrayList<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return titles[position % titles.length];
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

}
