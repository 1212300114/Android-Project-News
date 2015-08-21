package com.example.demo.news.fragments.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import net.xinhuamm.d0403.R;
import com.example.demo.news.activity.LooperViewDetails;
import com.example.demo.news.databeans.importantnews.ImportantNewsData;
import com.example.demo.news.databeans.importantnews.ImportantNewsList;
import com.example.demo.news.dataloaders.ImportantNewsLoader;
import com.example.demo.news.xlistviewsource.XListView;
import com.example.demo.news.xlistviewsource.XListView.IXListViewListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

public class FragmentCheck extends Fragment implements IXListViewListener {
	private View root;
	private XListView lv;
	private ProgressBar pb;
	private XListViewAdapter ListAdapter;
	private Handler mHandler = new Handler();
	private ImportantNewsLoader loader = new ImportantNewsLoader();
	private ImportantNewsData data;// 要闻页获取的网络数据
	private AsyncTask<String, Void, ImportantNewsData> task;// 加载要闻的task
	private ArrayList<String> listTitles;// 列表项的title
	private int page = 1;// 当前列表项的页数
	private ArrayList<ImportantNewsList> newsList = null;
	private int pageCount = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		root = inflater.inflate(R.layout.fragment_important_news, container,
				false);
		listTitles = new ArrayList<String>();

		lv = (XListView) root.findViewById(R.id.lvFirstPage);
		ListAdapter = new XListViewAdapter(getActivity());
		onRefresh();
		lv.setAdapter(ListAdapter);
		lv.setXListViewListener(this);
		lv.setPullLoadEnable(true);
		lv.setPullRefreshEnable(true);
		lv.setVisibility(View.GONE);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String link = newsList.get(position - 1).getInfo_link();
				Intent intent = new Intent(getActivity(),
						LooperViewDetails.class);
				intent.putExtra("link", link);
				startActivity(intent);
			}
		});

		return root;
	}

	private ImportantNewsData getData(int i) throws IOException {

		String JSON = loader
				.readURL("http://api.jjjc.yn.gov.cn/jwapp/?service=List.index&cid=5&page="
						+ i);
		ImportantNewsData data = loader.getJSONDate(JSON);

		return data;

	}

	// 获取列表项图片的方法
	@SuppressWarnings("unused")
	private Bitmap getListBitmap(int position) throws InterruptedException,
			ExecutionException, IOException {
		if (task.get() != null) {

			data = task.get();
		}
		String urlString;
		urlString = data.getData().getList().get(position).getImage();
		URL url = null;
		if (urlString != null) {

			url = new URL(urlString);
		}
		InputStream is = url.openStream();
		Bitmap map = BitmapFactory.decodeStream(is);
		return map;

	}

	@Override
	public void onRefresh() {

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				page = 1;

				task = new AsyncTask<String, Void, ImportantNewsData>() {

					@Override
					protected ImportantNewsData doInBackground(String... params) {
						ImportantNewsData data = null;
						try {
							data = getData(1);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return data;
					}
				};
				task.execute();
				if (task != null) {
					try {
						data = task.get();
						newsList = data.getData().getList();
						pageCount = data.getData().getPagecount();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				listTitles.removeAll(listTitles);
				for (int i = 0; i < data.getData().getList().size(); i++) {
					listTitles.add(data.getData().getList().get(i).getTitle());
				}
				pb = (ProgressBar) root.findViewById(R.id.pb);
				lv.setVisibility(View.VISIBLE);
				pb.setVisibility(View.GONE);
				ListAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);

	}

	private void onLoad() {
		lv.stopRefresh();
		lv.stopLoadMore();
		lv.setRefreshTime("刚刚");
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
							ImportantNewsData data = getData(page);
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

					listTitles.addAll(list);
				}

				AsyncTask<String, Void, ImportantNewsData> task2 = new AsyncTask<String, Void, ImportantNewsData>() {

					@Override
					protected ImportantNewsData doInBackground(String... params) {
						ImportantNewsData data = null;
						try {
							data = getData(page);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return data;
					}
				};
				task2.execute();
				ImportantNewsData data = null;
				try {
					data = task2.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (data != null) {
					if (page <= pageCount) {

						newsList.addAll(data.getData().getList());
					}
				}
				if (page > pageCount) {
					Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT)
							.show();
				}
				ListAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	public class XListViewAdapter extends BaseAdapter {
		// 列表內容的適配器
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
			return listTitles.size();
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

		@SuppressLint("InflateParams") @Override
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
			holder.tvTitle.setText(listTitles.get(position));
			return convertView;
		}

		public class ViewHolder {
			public ImageView iv;
			public TextView tvTitle;
			public TextView tvDescription;
		}

	}
}
