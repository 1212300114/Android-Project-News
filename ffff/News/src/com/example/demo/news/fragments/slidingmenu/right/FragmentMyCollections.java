package com.example.demo.news.fragments.slidingmenu.right;

import java.util.ArrayList;
import java.util.HashMap;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import net.xinhuamm.d0403.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentMyCollections extends Fragment {

	private ArrayList<HashMap<String, Object>> list = null;
	private SwipeMenuListView mListView;
	private ListViewAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root;
		root = inflater.inflate(R.layout.activity_my_collections, container,
				false);
		ImageButton back = (ImageButton) root.findViewById(R.id.btnBack);
		mListView = (SwipeMenuListView) root.findViewById(R.id.list_view);
		list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 5; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("title", "news");
			map.put("icon", R.drawable.about_us_logo);
			list.add(map);
		}
		adapter = new ListViewAdapter(getActivity());
		mListView.setAdapter(adapter);
		initSlideMenu();
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFragmentManager().popBackStack();
			}
		});
		return root;

	}

	private void initSlideMenu() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				SwipeMenuItem delete = new SwipeMenuItem(getActivity());
				delete.setBackground(new ColorDrawable(Color.rgb(0xff, 0x00,
						0x00)));
				delete.setWidth(dp2px(80));
				delete.setIcon(R.drawable.mycollection_delete);
				menu.addMenuItem(delete);

			}
		};
		mListView.setMenuCreator(creator);

		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {

				switch (index) {
				case 0:
					list.remove(position);
					adapter.notifyDataSetChanged();
					break;

				default:
					break;
				}
				return false;
			}
		});
	}

	public class ListViewAdapter extends BaseAdapter {

		private Context context;

		public ListViewAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_title);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.icon.setImageResource((Integer) list.get(position).get(
					"icon"));
			holder.title
					.setText((CharSequence) list.get(position).get("title"));

			return convertView;
		}

		public class ViewHolder {
			private ImageView icon;
			private TextView title;

		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}
