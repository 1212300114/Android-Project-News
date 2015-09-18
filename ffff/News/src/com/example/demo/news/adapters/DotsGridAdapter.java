package com.example.demo.news.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.demo.news.databeans.ColumnEntity;

import net.xinhuamm.d0403.R;

import java.util.List;

/**
 * Created by 123456 on 2015/9/14.
 */
public class DotsGridAdapter extends BaseAdapter {
    //轮播图点的GridView适配器设置成了一行并且需要根据item数量动态设置gridview的width
    private List<ColumnEntity.DataEntity.BannerEntity> entities;
    private Context context;
    private int current = 0;

    public DotsGridAdapter(List<ColumnEntity.DataEntity.BannerEntity> entities, Context context) {
        this.entities = entities;
        this.context = context;
    }

    public void setEntities(List<ColumnEntity.DataEntity.BannerEntity> entities) {
        this.entities = entities;
        notifyDataSetChanged();
    }

    public void setCurrent(int current) {
        this.current = current;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view;
        //这边这个item 很坑如果只是一个imageview 设置background为shape 不能显示view 需要给他套上一个父view 例如relativelayout
        convertView = LayoutInflater.from(context).inflate(R.layout.item_dots, null);
        view = (ImageView) convertView.findViewById(R.id.dot1);
        if (current == position) {
            view.setImageResource(R.drawable.dot_focused);
        } else {
            view.setImageResource(R.drawable.dot_normal);
        }
        view.getLayoutParams().height = 25;
        view.getLayoutParams().width = 25;
        view.requestLayout();
        return convertView;
    }
}
