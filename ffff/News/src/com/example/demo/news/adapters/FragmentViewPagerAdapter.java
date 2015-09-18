package com.example.demo.news.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {
    //栏目viewpager的适配器 里面的gettitle方法返回给viewpagerindicator导航栏标题   这里用state的 可以做到fragment更新后viewpager更新否则viewpager无变化
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> title;


    public void setData(ArrayList<Fragment> fragments, ArrayList<String> title) {
        this.title = title;
        this.fragments = fragments;
        notifyDataSetChanged();
    }


    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.title = new ArrayList<>();
        // TODO Auto-generated constructor stub
    }

    public FragmentViewPagerAdapter(FragmentManager fm,
                                    ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.title = new ArrayList<>();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return title.get(position);
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
