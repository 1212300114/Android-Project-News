package com.example.demo.news.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo.news.activity.NewsDetailsActivity;
import com.example.demo.news.activity.SubjectDetailsActivity;
import com.example.demo.news.databeans.ColumnEntity;
import com.example.demo.news.utils.ImageLoaderInition;

import net.xinhuamm.d0403.R;

import java.util.ArrayList;

/**
 * Created by 123456 on 2015/9/11.
 */
public class ColumnListAdapter extends BaseAdapter {
    //新闻列表的适配器
    private Context context;
    private boolean subject = false;
    private ArrayList<ColumnEntity.DataEntity.ListEntity> list;//对于当前栏是普通页取的数据
    private ArrayList<ColumnEntity.DataEntity.CateEntity> cate;//对于当前栏是专题页取的数据

    public ColumnListAdapter(Context context, boolean subject) {
        this.context = context;
        this.subject = subject;
        this.list = new ArrayList<>();
        this.cate = new ArrayList<>();
    }

    public void clearData() {
        //清楚列表内容的操作。。没用clear因为空指针╮(╯▽╰)╭
        this.cate = new ArrayList<>();
        this.list = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setData(ColumnEntity entity, boolean isRefresh) {
        //根据是否是刷新操作填充数据
        if (isRefresh) {
            this.cate = (ArrayList<ColumnEntity.DataEntity.CateEntity>) entity.getData().getCate();
            this.list = (ArrayList<ColumnEntity.DataEntity.ListEntity>) entity.getData().getList();
        } else {
            if (entity.getData().getCate() != null) {
                this.cate.addAll(entity.getData().getCate());
            }
            if (entity.getData().getList() != null) {
                this.list.addAll(entity.getData().getList());
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (subject) {
            return cate.size();
        } else {
            return list.size();
        }
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

    //根据是否为专题加载不同的内容
    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 初始化view根据是否为专题页呈现不同内容并且设置相对应的touch事件
        ViewHolder holder;
        if (!subject) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_list, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                holder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tvTitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoaderInition.imageLoader.displayImage(list.get(position).getImage(), holder.iv,
                    ImageLoaderInition.options);
            holder.tvTitle.setText(list.get(position).getTitle());
        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_subject, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoaderInition.imageLoader.displayImage(cate.get(position).getImage(), holder.iv, ImageLoaderInition.options);
            holder.tvTitle.setText(cate.get(position).getName());
        }
        if (!subject) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewsDetailsActivity.class);
                    intent.putExtra("content_id", String.valueOf(list.get(position).getContent_id()));
                    intent.putExtra("link", list.get(position).getInfo_link());
                    context.startActivity(intent);
                }
            });
        } else {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SubjectDetailsActivity.class);
                    intent.putExtra("name", cate.get(position).getName());
                    intent.putExtra("link", cate.get(position).getCate_link());
                    context.startActivity(intent);
                }
            });
        }

        return convertView;
    }


    public class ViewHolder {
        public ImageView iv;
        public TextView tvTitle;
    }

}

