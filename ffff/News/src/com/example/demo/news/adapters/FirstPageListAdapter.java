package com.example.demo.news.adapters;

import java.util.ArrayList;


import net.xinhuamm.d0403.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FirstPageListAdapter extends BaseAdapter {
	// 列表內容的適配器
	private LayoutInflater inflater;
	private String title = "这是标题这是标题这是标题————————";
	private ArrayList<String> titles = null;
	private Bitmap[] bitmaps = new Bitmap[20];
	
	public void setBitmaps(Bitmap[] bitmaps) {
		this.bitmaps = bitmaps;
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	public void setTitles(ArrayList<String> titles) {
		this.titles = titles;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
	}


	public FirstPageListAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.size();
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
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (bitmaps[position] == null) {

			holder.iv.setImageResource(R.drawable.news_list_bg);
		}
		if (bitmaps[position] != null) {
			holder.iv.setImageBitmap(bitmaps[position]);
		}
		holder.tvTitle.setText(titles.get(position));
		return convertView;
	}

	public class ViewHolder {
		public ImageView iv;
		public TextView tvTitle;
		public TextView tvDescription;
	}

}
