package com.example.demo.news.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo.news.databeans.ColumnEntity;
import com.example.demo.news.utils.ImageLoaderInition;

import net.xinhuamm.d0403.R;

import java.util.ArrayList;

/**
 * Created by 123456 on 2015/9/16.
 */
public class CollectionListAdapter extends BaseAdapter {
    //收藏列表的适配器
    private ArrayList<String> times;//收藏时间
    private Context context;
    private ColumnEntity entity;//收藏的内容的数据

    public CollectionListAdapter(Context context) {
        this.context = context;
        this.times = new ArrayList<>();
    }

    public void setData(ArrayList<String> times, ColumnEntity entity) {
        this.times = times;
        this.entity = entity;
        notifyDataSetChanged();
    }

    //删除列表项的操作
    public void removeItem(int position) {
        if (0 != times.size()) {
            times.remove(position);
        }
        if (0 != entity.getData().getList().size()) {
            entity.getData().getList().remove(position);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return times.size();
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
        //初始化view
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_collection, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTime.setText(times.get(position));
        viewHolder.tvTitle.setText(entity.getData().getList().get(position).getTitle());
        ImageLoaderInition.imageLoader.displayImage(entity.getData().getList().get(position).getImage(), viewHolder.iv
                , ImageLoaderInition.options);
        //view被创建时就添加事件侦听。启动详情页
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, NewsDetailsActivity.class);
//                intent.putExtra("content_id", String.valueOf(entity.getData().getList().get(position).getContent_id()));
//                intent.putExtra("link", enti  ty.getData().getList().get(position).getInfo_link());
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }

    private static class ViewHolder {
        private ImageView iv;
        private TextView tvTitle;
        private TextView tvTime;
    }
}
