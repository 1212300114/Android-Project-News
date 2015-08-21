package com.example.demo.news.fragments.slidingmenu.left;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import net.xinhuamm.d0403.R;

import com.example.demo.news.activity.LooperViewDetails;
import com.example.demo.news.activity.MainActivity;
import com.example.demo.news.databeans.importantnews.ImportantNewsData;
import com.example.demo.news.databeans.importantnews.ImportantNewsList;
import com.example.demo.news.dataloaders.ImportantNewsLoader;
import com.example.demo.news.xlistviewsource.XListView;
import com.example.demo.news.xlistviewsource.XListView.IXListViewListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

//党纪法规栏目内容
@SuppressLint("InflateParams")
public class FragmentLaw extends Fragment implements OnClickListener,
		IXListViewListener {
	private SlidingMenu slidingMenu1;
	private SlidingMenu slidingMenu2;
	private ImageButton showLeft;
	private ImageButton showRight;
	private View root;
	private XListView lv;
	private ProgressBar pb;
	private XListViewAdapter ListAdapter;
	private Handler mHandler = new Handler();
	private ImportantNewsLoader loader = new ImportantNewsLoader();
	private ImportantNewsData data;// Ҫ��ҳ��ȡ����������
	private AsyncTask<String, Void, ImportantNewsData> task;// ����Ҫ�ŵ�task
	private ArrayList<String> listTitles;// �б����title
	private int page = 1;// ��ǰ�б����ҳ��
	private ArrayList<ImportantNewsList> newsList = null;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = getActivity().getApplicationContext();

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_law, container, false);
		slidingMenu1 = ((MainActivity) getActivity()).getSlidingMenu1();
		slidingMenu2 = ((MainActivity) getActivity()).getSlidingMenu2();
		slidingMenu1.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		showLeft = (ImageButton) root.findViewById(R.id.btnShowLeft);
		showLeft.setOnClickListener(this);
		showRight = (ImageButton) root.findViewById(R.id.btnShowRight);
		showRight.setOnClickListener(this);

		lv = (XListView) root.findViewById(R.id.lvFirstPage);
		onRefresh();

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
				.readURL("http://api.jjjc.yn.gov.cn//jwapp//?service=List.index&cid=43&page="
						+ i);
		ImportantNewsData data = loader.getJSONDate(JSON);

		return data;

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
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				listTitles = new ArrayList<String>();
				listTitles.removeAll(listTitles);
				for (int i = 0; i < data.getData().getList().size(); i++) {
					listTitles.add(data.getData().getList().get(i).getTitle());
				}
				System.out.println("listsize=" + listTitles.size());
				ListAdapter = new XListViewAdapter(context, listTitles);
				lv.setAdapter(ListAdapter);
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
				listTitles.addAll(list);

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

					newsList.addAll(data.getData().getList());
				}
				ListAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnShowLeft:
			slidingMenu1.toggle();
			break;
		case R.id.btnShowRight:
			slidingMenu2.toggle();
			break;

		default:
			break;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

	public class XListViewAdapter extends BaseAdapter {
		// �б���ݵ��m����
		private LayoutInflater inflater;
		private int count = 10;
		private ArrayList<String> listTitles;

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

		public XListViewAdapter(Context context, ArrayList<String> listTitles) {
			this.inflater = LayoutInflater.from(context);
			this.listTitles = listTitles;

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
			holder.iv.setImageResource(R.drawable.jinping);
			holder.iv.setVisibility(View.GONE);
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
