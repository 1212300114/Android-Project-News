package com.example.demo.news.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 123456 on 2015/9/14.
 */
public class MyPagerAdapter extends PagerAdapter {
    private List<View> views;

    public MyPagerAdapter(List<View> views) {
        this.views = views;
    }

    //轮播图的内容适配器
    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
