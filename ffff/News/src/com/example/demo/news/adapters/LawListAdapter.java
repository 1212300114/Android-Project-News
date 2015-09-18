package com.example.demo.news.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.demo.news.activity.NewsDetailsActivity;
import com.example.demo.news.databeans.ColumnEntity;

import net.xinhuamm.d0403.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123456 on 2015/9/16.
 */
public class LawListAdapter extends BaseAdapter {
    //当即法规列表的适配器-感觉可以写一个基类了。。。。。几个adapter 都长得很像
    private List<ColumnEntity.DataEntity.ListEntity> entities;
    private Context context;

    public LawListAdapter(Context context) {
        this.entities = new ArrayList<>();
        this.context = context;
    }

    public void setEntities(List<ColumnEntity.DataEntity.ListEntity> entities, boolean isRefresh) {
        if (isRefresh) {
            this.entities = entities;
        } else {
            this.entities.addAll(entities);
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_law, null);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(entities.get(position).getTitle());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra("content_id", String.valueOf(entities.get(position).getContent_id()));
                intent.putExtra("link", entities.get(position).getInfo_link());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView tv;
    }
}
