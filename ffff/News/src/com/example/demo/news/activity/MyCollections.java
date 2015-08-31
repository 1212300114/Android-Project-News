package com.example.demo.news.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.example.demo.news.databasehelper.DataBaseHelper;
import com.example.demo.news.databeans.search.SearchData;
import com.example.demo.news.dataloaders.SearchLoader;
import com.google.gson.Gson;

import net.xinhuamm.d0403.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MyCollections extends Activity {
	// �Ҳ��collection����
	private ArrayList<HashMap<String, Object>> list ;// �ղ��б���������
	private SwipeMenuListView mListView;// ���Ի���ɾ����Ŀ��listview
	private ListViewAdapter adapter;// ������
	ArrayList<Integer> collection;
	String urlString;
	private SearchLoader loader;
	ArrayList<String> titles = new ArrayList<>();
	SearchData data;
	private ArrayList<String> times = new ArrayList<>();
	DataBaseHelper db;
	ArrayList<Integer> ids = new ArrayList<>();
	String time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collections);
		ImageButton back = (ImageButton) findViewById(R.id.btnBack);
		mListView = (SwipeMenuListView) findViewById(R.id.list_view);
		list = new ArrayList<>();
		// �������������
		for (int i = 0; i < 5; i++) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("title", "news");
			map.put("icon", R.drawable.about_us_logo);
			list.add(map);
		}
		db = new DataBaseHelper(this);
		SQLiteDatabase dbRead = db.getReadableDatabase();
		Cursor c = dbRead.query("id", null, null, null, null, null, null);
		collection = new ArrayList<>();
		while (c.moveToNext()) {
			int contentID = c.getInt(c.getColumnIndex("contentId"));
			if (contentID > 0) {

				collection.add(contentID);
				time = c.getString(c.getColumnIndex("time"));
				times.add(time);
			}
			System.out.println("���ݿ������" + contentID);
			System.out.println("���ݵ��¼�" + time);
		}
		if (collection.size() != 0) {

			urlString = "http://api.jjjc.yn.gov.cn/jwapp/?service=Favorites.index&content_ids=";
			for (int i = 0; i < collection.size(); i++) {
				urlString = urlString + collection.get(i) + ",";
			}
			System.out.println(urlString);
			adapter = new ListViewAdapter(this);
			mListView.setAdapter(adapter);
			initSlideMenu();
			loader = new SearchLoader();
			AsyncTask<String, Void, SearchData> task = new AsyncTask<String, Void, SearchData>() {

				@Override
				protected SearchData doInBackground(String... params) {
					String json = null;
					try {
						json = loader.readURL(urlString);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SearchData data;
					data = loader.getJSONDate(json);
					return data;
				}
			};
			task.execute();

			try {
				data = task.get();
				System.out.println(new Gson().toJson(data));
				for (int i = 0; i < data.getData().getList().size(); i++) {
					titles.add(data.getData().getList().get(i).getTitle());
					ids.add(data.getData().getList().get(i).getContent_id());
				}
				System.out.println(ids);

			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String link;
				Intent intent;
				Integer contentId;

				link = data.getData().getList().get(position).getInfo_link();
				contentId = data.getData().getList().get(position)
						.getContent_id();
				intent = new Intent(MyCollections.this, LooperViewDetails.class);
				intent.putExtra("link", link);
				intent.putExtra("content_id", contentId);
				startActivityForResult(intent, 1);
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});
	}

	private void initSlideMenu() {
		// ��ʼ�ɻ���ɾ����Ŀlistview��������menu����
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// ����menu�����ÿ����������ֻ�����һ����ťɾ��
				SwipeMenuItem delete = new SwipeMenuItem(
						getApplicationContext());
				delete.setBackground(new ColorDrawable(Color.rgb(0xff, 0x00,
						0x00)));
				delete.setWidth(dp2px(80));
				delete.setIcon(R.drawable.mycollection_delete);
				menu.addMenuItem(delete);

			}
		};
		// ��menu�󶨵�listview
		mListView.setMenuCreator(creator);
		// ��������menu�����¼�����
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {

				switch (index) {
				case 0:
					titles.remove(position);// ����position���Ƴ�����
					adapter.notifyDataSetChanged();
					SQLiteDatabase dbWrite = db.getWritableDatabase();
					String sql = "delete from id where contentId  = "
							+ ids.get(position);
					dbWrite.execSQL(sql);

					break;

				default:
					break;
				}
				return false;
			}
		});
	}

	// ���������ݾ������ݲ�л��
	public class ListViewAdapter extends BaseAdapter {

		private Context context;

		public ListViewAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			// return list.size();
			return titles.size();
		}

		@Override
		public Object getItem(int position) {
			return titles.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams") @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.time = (TextView) convertView.findViewById(R.id.tvTime);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.time.setText(times.get(position));

			if (titles != null) {

				if (titles.get(position).length() > 20) {
					char[] t = titles.get(position).toCharArray();
					char[] tt = new char[20];
					System.arraycopy(t, 0, tt, 0, 20);
					holder.title.setText(String.valueOf(tt) + "...");
				} else {

					holder.title.setText(titles.get(position));
				}
			}

			return convertView;
		}

		public class ViewHolder {
			private TextView title;
			private TextView time;

		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

}
