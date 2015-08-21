package com.example.demo.news.adapters;

import net.xinhuamm.d0403.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class XListViewAdapter extends BaseAdapter {
	//ÁÐ±íƒÈÈÝµÄßmÅäÆ÷
	private LayoutInflater inflater;
	private int count = 10;

	public LayoutInflater getInflater() {
		return inflater;
	}

	public void setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public XListViewAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);

	}

	public void setCount(int countNumber,boolean isRefresh) {
		if (!isRefresh) {
			count = countNumber+count;
		}	
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
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
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.iv.setImageResource(R.drawable.jinping);

		return convertView;
	}

	public class ViewHolder {
		public ImageView iv;
		public TextView tvTitle;
		public TextView tvDescription;
	}

}
