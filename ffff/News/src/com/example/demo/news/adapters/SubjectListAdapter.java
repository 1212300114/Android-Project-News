package com.example.demo.news.adapters;

import net.xinhuamm.d0403.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SubjectListAdapter extends BaseAdapter {
	// 列表內容的適配器
	private LayoutInflater inflater;
	private int count = 10;

	public SubjectListAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);

	}

	public void setCount(int number, boolean isRefresh) {
		if (isRefresh) {
		} else {
			count = count + number;
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

	// 返回你有多少个不同的布局
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	// 由position返回view type id
	@Override
	public int getItemViewType(int position) {
		if (position % 3 == 0) {
			return 1;

		} else {
			return 0;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			holder = new ViewHolder();
			if (type == 1) {
				convertView = inflater.inflate(R.layout.item_title, null);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.lanMu);

			} else {

				convertView = inflater.inflate(R.layout.item_list, null);
				holder.iv = (ImageView) convertView.findViewById(R.id.iv);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tvTitle);
				holder.iv.setImageResource(R.drawable.jinping);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView iv;
		public TextView tvTitle;
		public TextView tvDescription;
	}

}
