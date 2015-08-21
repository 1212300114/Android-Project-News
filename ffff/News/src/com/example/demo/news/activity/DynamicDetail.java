package com.example.demo.news.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.example.demo.news.databeans.dynamic.detail.DynamicDetailData;
import com.example.demo.news.databeans.dynamic.detail.DynamicDetailList;
import com.example.demo.news.dataloaders.DynamicDetailLoader;
import com.example.demo.news.xlistviewsource.XListView;
import com.example.demo.news.xlistviewsource.XListView.IXListViewListener;

import net.xinhuamm.d0403.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DynamicDetail extends Activity implements OnClickListener,
		IXListViewListener {

	private XListView listView;
	private ProgressBar pb;
	private AsyncTask<String, Void, DynamicDetailData> task = null;
	private Handler mHandler = new Handler();
	private DynamicDetailLoader loader;
	private DynamicDetailData data;
	private String link;
	private ArrayList<String> listTitle;
	private int page = 1;
	private XListViewAdapter adapter;
	private ArrayList<DynamicDetailList> newsList = null;
	private int pageCount = 0;
	private TextView title;
	private String titleString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamic_detail);
		ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		link = getIntent().getExtras().getString("link");
		if (link != null) {
			System.out.println("link = " + link);
		}
		task = new AsyncTask<String, Void, DynamicDetailData>() {

			@Override
			protected DynamicDetailData doInBackground(String... params) {
				DynamicDetailData data = null;
				try {
					data = getData(1, link);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return data;
			}
		};
		task.execute();
		listView = (XListView) findViewById(R.id.lv);
		pb = (ProgressBar) findViewById(R.id.pb);
		loader = new DynamicDetailLoader();
		mHandler = new Handler();
		listView = (XListView) findViewById(R.id.lv);
		try {
			data = task.get();
			titleString = data.getData().getCate_name();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		title = (TextView) findViewById(R.id.tvTitle);
		title.setText(titleString);
		onRefresh();
		listView.setXListViewListener(this);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setVisibility(View.GONE);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String link = newsList.get(position - 1).getInfo_link();
				int content_id = newsList.get(position - 1).getContent_id();
				Intent intent = new Intent(DynamicDetail.this,
						LooperViewDetails.class);
				intent.putExtra("link", link);
				intent.putExtra("content_id", content_id);
				startActivity(intent);
			}
		});

	}

	private DynamicDetailData getData(int i, String link) throws IOException {

		String JSON = loader
				.readURL("http://api.jjjc.yn.gov.cn/jwapp/?service=" + link
						+ "&page=" + i);
		DynamicDetailData data = loader.getJSONDate(JSON);

		return data;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				page = 1;
				newsList = data.getData().getList();
				pageCount = data.getData().getPagecount();
				listTitle = new ArrayList<String>();
				for (int i = 0; i < data.getData().getList().size(); i++) {
					listTitle.add(data.getData().getList().get(i).getTitle());
				}

				adapter = new XListViewAdapter(getApplicationContext());
				listView.setAdapter(adapter);

				listView.setVisibility(View.VISIBLE);
				pb.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);

	}

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("刚刚");
	}

	@Override
	public void onLoadMore() {

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				AsyncTask<Integer, Void, ArrayList<String>> task = new AsyncTask<Integer, Void, ArrayList<String>>() {

					@Override
					protected ArrayList<String> doInBackground(
							Integer... params) {
						page++;
						ArrayList<String> titleList = new ArrayList<String>();
						try {
							DynamicDetailData data = getData(page, link);
							for (int i = 0; i < data.getData().getList().size(); i++) {
								titleList.add(data.getData().getList().get(i)
										.getTitle());
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						return titleList;
					}
				};
				task.execute();
				ArrayList<String> list = null;
				try {
					list = task.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (page <= pageCount) {

					listTitle.addAll(list);
				}

				AsyncTask<String, Void, DynamicDetailData> task2 = new AsyncTask<String, Void, DynamicDetailData>() {

					@Override
					protected DynamicDetailData doInBackground(String... params) {
						DynamicDetailData data = null;
						try {
							data = getData(page, link);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return data;
					}
				};
				task2.execute();
				data = null;
				try {
					data = task2.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				if (data != null) {
					if (page <= pageCount) {

						newsList.addAll(data.getData().getList());
					}
				}
				if (page > pageCount) {
					Toast.makeText(getApplicationContext(), "没有更多了",
							Toast.LENGTH_SHORT).show();
				}
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	public class XListViewAdapter extends BaseAdapter {
		// �б���ݵ��m����
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

		public void setCount(int countNumber, boolean isRefresh) {
			if (!isRefresh) {
				count = countNumber + count;
			}

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
			holder.iv.setImageResource(R.drawable.news_list_bg);
			holder.tvTitle.setText(listTitle.get(position));
			return convertView;
		}

		public class ViewHolder {
			public ImageView iv;
			public TextView tvTitle;
			public TextView tvDescription;
		}
	}
}
