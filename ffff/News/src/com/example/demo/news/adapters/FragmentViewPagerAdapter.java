package com.example.demo.news.adapters;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {
    //栏目viewpager的适配器 里面的gettitle方法返回给viewpagerindicator导航栏标题
    private ArrayList<Fragment> fragments;
    private String[] titles = new String[]{"首页", "要闻", "审查", "党风", "巡视",
            "图闻", "专题"};
    private ArrayList<String> title;

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    public void setTitles(ArrayList<String> titles) {
        this.title = titles;
    }

    public void setFragments(ArrayList<Fragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
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
        if (title != null) {
            return title.get(position);
        }
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

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
