package com.example.demo.news.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo.news.activity.DynamicDetailActivity;
import com.example.demo.news.databeans.dynamic.detail.DynamicDetailList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.xinhuamm.d0403.R;

import java.util.ArrayList;

/**
 * Created by 123456 on 2015/9/9.
 */
public class DynamicListAdapter extends BaseAdapter {
    // �б���ݵ��m����
    private LayoutInflater inflater;
    private int count = 10;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private ArrayList<String> listTitle;
    private ArrayList<DynamicDetailList> newsList = null;



    public DynamicListAdapter(Context context, ArrayList<String> listTitle, ArrayList<DynamicDetailList> newsList) {
        this.inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.news_list_bg)
                .showImageForEmptyUri(R.drawable.news_list_bg)
                .showImageOnFail(R.drawable.news_list_bg).cacheInMemory(true)
                .cacheOnDisk(true).build();
        this.listTitle = listTitle;
        this.newsList = newsList;
    }

    public void setListTitle(ArrayList<String> listTitle) {
        this.listTitle = listTitle;
    }

    public void setNewsList(ArrayList<DynamicDetailList> newsList) {
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listTitle.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.tvTitle = (TextView) convertView
                    .findViewById(R.id.tvTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        imageLoader.displayImage(newsList.get(position).getImage(), holder.iv, options);
        holder.tvTitle.setText(listTitle.get(position));
        return convertView;
    }

    public class ViewHolder {
        public ImageView iv;
        public TextView tvTitle;
    }
}
