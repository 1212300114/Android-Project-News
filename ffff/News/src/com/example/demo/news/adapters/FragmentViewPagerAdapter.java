package com.example.demo.news.adapters;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
	// ��fragment�����õ�ViewPager��fragment��pageradapter
	// ���Խ����fragment����һ��ViewPager����ﵽ���һ������벻ͬ�Ŀɲ��������Ч��
	// ������ΪҪ�õ�ViewPagerIndicator���Ա���Ҫ��дgetpagetitle������Ϊindicator��ͨ�������������ȡ���ݵ�
	private ArrayList<Fragment> fragments;
	private String[] titles = new String[] { "��ҳ", "Ҫ��", "����", "���", "Ѳ��",
			"ͼ��", "ר��" };

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
