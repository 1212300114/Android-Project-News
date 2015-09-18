package com.example.demo.news.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.demo.news.activity.SubjectDetailsActivity;
import com.example.demo.news.databeans.ColumnEntity;

import net.xinhuamm.d0403.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123456 on 2015/9/16.
 */
public class DynamicListAdapter extends BaseAdapter {
    //州市动态列表的适配器
    private List<ColumnEntity.DataEntity.CateEntity> entities;//数据
    private Context context;

    public DynamicListAdapter(Context context) {
        this.context = context;
        entities = new ArrayList<>();
    }

    public void setEntities(List<ColumnEntity.DataEntity.CateEntity> entities) {
        this.entities = entities;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //根据奇数偶数count
        if (entities.size() % 2 == 0) {
            return entities.size() / 2;
        } else {
            return (entities.size() + 1) / 2;
        }
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
        //初始化view同时设置侦听- 对于数据为奇数的gone掉最后一个BUTTON
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dynamic, null);
            viewHolder = new ViewHolder();
            viewHolder.btnLeft = (Button) convertView.findViewById(R.id.btnLeft);
            viewHolder.btnRight = (Button) convertView.findViewById(R.id.btnRight);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.btnLeft.setText(entities.get(position * 2).getName());
        viewHolder.btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SubjectDetailsActivity.class);
                intent.putExtra("name", entities.get(position * 2).getName());
                intent.putExtra("link", entities.get(position * 2).getCate_link());
                context.startActivity(intent);

            }
        });
        if (null != entities.get(position * 2 + 1).getName()) {
            viewHolder.btnRight.setText(entities.get(position * 2 + 1).getName());
            viewHolder.btnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SubjectDetailsActivity.class);
                    intent.putExtra("name", entities.get(position * 2 + 1).getName());
                    intent.putExtra("link", entities.get(position * 2 + 1).getCate_link());
                    context.startActivity(intent);

                }
            });
        } else {
            viewHolder.btnRight.setVisibility(View.GONE);
        }
        return convertView;
    }


    private static class ViewHolder {
        private Button btnLeft;
        private Button btnRight;
    }
}
