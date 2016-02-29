package com.example.demo.news.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jijunjie on 15/10/16.
 */
@SuppressWarnings("ALL")
public class BaseListAdapter<T> extends BaseAdapter {

    protected List<T> listData = new ArrayList<>();

    private  BaseListAdapter sharedInstance;

    public BaseListAdapter getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new BaseListAdapter();
        }
        return sharedInstance;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    public void setData(List<T> listData, boolean isRefresh) {
        if (isRefresh) {
            this.listData = listData;
        } else {
            this.listData.addAll(listData);
        }
        notifyDataSetChanged();
    }

    public void addDataAtIndex(int index, T t) {
        this.listData.add(index, t);
    }

    public List<T> getListData() {
        return listData;
    }

    public void removeDataAtIndex(int index) {
        this.listData.remove(index);
        notifyDataSetChanged();
    }

    public void removeDataFromTo(int from, int to) {
        List<T> newList = new ArrayList<>();
        for (int i = from; i < to; i++) {
            newList.add(listData.get(i));
        }
        this.listData.remove(newList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.listData.clear();
        notifyDataSetChanged();
    }


    @Override
    public Object getItem(int position) {
        return listData == null ? null : listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listData == null ? null : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
