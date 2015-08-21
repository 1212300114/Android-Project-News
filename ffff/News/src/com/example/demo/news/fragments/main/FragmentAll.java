package com.example.demo.news.fragments.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.example.demo.news.activity.LooperViewDetails;
import com.example.demo.news.databeans.firstpage.FirstPageData;
import com.example.demo.news.databeans.firstpage.FirstpageLoopPager;
import com.example.demo.news.dataloaders.FirstPageContentLoader;
import com.example.demo.news.xlistviewsource.XListView;
import com.example.demo.news.xlistviewsource.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.xinhuamm.d0403.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class FragmentAll extends Fragment implements IXListViewListener {
	private View root;// fragment�������view
	private XListView lv;// ����ˢ���б�
	private String link;// �洢��main��ȡ����link
	private ProgressBar bar;// ������Ϊ�������ʱ��ʾ�Ľ�����
	private ImageView[] imageViews = new ImageView[20];// ViewPager��������
	private LayoutInflater layoutInflater = null;
	private AsyncTask<String, Void, FirstPageData> task;// ���ص�ǰҳ�����ݵ�task
	private FirstPageContentLoader loader = new FirstPageContentLoader();// ������
	private FirstPageData data = null;// ����ʵ��
	private int pageCount = 0;// ��ǰ��Ŀ�б������ҳ��
	private int page = 1;// ��ǰҳ��
	private boolean viewPagerCreated = false;// �ж��ֲ�ͼ�Ƿ��Ѿ�����
	private int viewPagerSize = 0;// �ֲ�ͼ��size �ж��Ƿ�Ҫ����ֲ�ͼ��Ϊ0�����
	private XListViewAdapter adapter;// �б����������
	private Handler mHandler = null;// handler����ui
	private ArrayList<String> listTitles;// �б����title
	private ArrayList<FirstpageLoopPager> newsList = null;// �����б��ڼ��ظ���ʱ��Ҫ������������ݲ�Ȼ�����ݵ��������������޷���ȡ
	private LinearLayout layout;// ����ViewPager��layout
	private ViewPager viewPager;// �ֲ�ͼ
	private ArrayList<View> dots;// �ֲ�ͼ�ĵ�����
	private ArrayList<ImageView> imageSource = new ArrayList<ImageView>();// �ֲ�ͼ��������
	private String[] titles;// �ֲ�ͼ�ı�������
	private String[] titles1;
	private TextView viewPagerTitle;// �ֲ�ͼ�ı���view
	private int oldPage = 0;
	@SuppressWarnings("unused")
	private int currPage = 0;
	ImageAdapter imageAdapter;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ArrayList<String> listImageURL;
	private ArrayList<String> bannerImageURL;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.news_list_bg)
				.showImageForEmptyUri(R.drawable.news_list_bg)
				.showImageOnFail(R.drawable.news_list_bg).cacheInMemory(true)
				.cacheOnDisk(true).build();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layoutInflater = inflater;
		root = inflater.inflate(R.layout.fragment_firstpage, container, false);
		lv = (XListView) root.findViewById(R.id.lvFirstPage);
		bar = (ProgressBar) root.findViewById(R.id.pb);
		Bundle bundle = getArguments();
		oldPage = 0;
		link = bundle.getString("link");// ��ȡ��link�Ӷ���ȡ����ǰ��Ŀ������
		viewPagerCreated = false;// ����ʱ�Ƚ��ֲ�ͼ����Ϊδ����
		onRefresh();
		lv.setXListViewListener(this);
		lv.setPullLoadEnable(true);
		lv.setPullRefreshEnable(true);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String link = null;
				Intent intent = null;
				Integer contentId = 0;
				if (viewPagerSize > 0) {

					link = data.getData().getList().get(position - 2)
							.getInfo_link();
					contentId = data.getData().getList().get(position - 2)
							.getContent_id();
				} else {
					link = data.getData().getList().get(position - 1)
							.getInfo_link();
					contentId = data.getData().getList().get(position - 1)
							.getContent_id();
				}
				intent = new Intent(getActivity(), LooperViewDetails.class);
				intent.putExtra("link", link);
				intent.putExtra("content_id", contentId);
				startActivityForResult(intent, 1);
			}
		});
		return root;
	}

	// ��ȡjson����ķ���
	private FirstPageData getResource(int i) throws IOException {
		String JSON = loader
				.readURL("http://api.jjjc.yn.gov.cn/jwapp/?service=" + link
						+ "&page=" + i);
		FirstPageData data = loader.getJSONDate(JSON);
		return data;

	}

	@Override
	public void onRefresh() {
		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {

			@SuppressLint("InflateParams") @Override
			public void run() {
				// TODO Auto-generated method stub

				page = 1;
				task = new AsyncTask<String, Void, FirstPageData>() {

					@Override
					protected FirstPageData doInBackground(String... params) {
						FirstPageData data = null;
						try {
							data = getResource(page);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return data;
					}
				};
				task.execute();
				try {
					if (task.get() != null) {
						data = task.get();
						pageCount = data.getData().getPagecount();
						viewPagerSize = data.getData().getBanner().size();
						newsList = data.getData().getList();
						listImageURL = new ArrayList<String>();
						bannerImageURL = new ArrayList<String>();
						for (int i = 0; i < data.getData().getList().size(); i++) {
							listImageURL.add(data.getData().getList().get(i)
									.getImage());
						}
						for (int j = 0; j < data.getData().getBanner().size(); j++) {

							bannerImageURL.add(data.getData().getBanner()
									.get(j).getImage());
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				imageSource = new ArrayList<ImageView>();
				if (viewPagerSize > 0 && !viewPagerCreated) {
					viewPagerCreated = true;
					for (int i = 0; i < viewPagerSize; i++) {
						imageViews[i] = (ImageView) layoutInflater.inflate(
								R.layout.image_item, null);
						imageSource.add(imageViews[i]);
					}

					try {
						ImageAdapter adapter = new ImageAdapter(imageSource);
						lv.addHeaderView(init(layoutInflater));
						viewPager.setAdapter(adapter);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				listTitles = new ArrayList<String>();
				for (int i = 0; i < data.getData().getList().size(); i++) {
					listTitles.add(data.getData().getList().get(i).getTitle());
				}

				bar.setVisibility(View.GONE);

				adapter = new XListViewAdapter(getActivity(), listTitles);
				// firstPageListAdapter.setBitmaps(listBitmaps);
				lv.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				lv.setVisibility(View.VISIBLE);
				onLoad();
			}

		}, 1000);
	}

	@Override
	public void onLoadMore() {

		page++;
		if (page >= pageCount) {
			Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
			onLoad();
		} else {

			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {

					final AsyncTask<String, Void, FirstPageData> task2 = new AsyncTask<String, Void, FirstPageData>() {

						@Override
						protected FirstPageData doInBackground(String... params) {
							FirstPageData data = null;
							try {
								data = getResource(page);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return data;
						}
					};
					task2.execute();
					FirstPageData data = null;
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
						for (int i = 0; i < data.getData().getList().size(); i++) {
							listTitles.add(data.getData().getList().get(i)
									.getTitle());
							listImageURL.add(data.getData().getList().get(i)
									.getImage());
						}
					}

					adapter.notifyDataSetChanged();
					onLoad();
				}
			}, 1000);
		}
	}

	private void onLoad() {
		lv.stopRefresh();
		lv.stopLoadMore();
		lv.setRefreshTime("刚刚");
	}

	// ��ʼ���ֲ�ͼ������
	@SuppressLint("InflateParams") public LinearLayout init(LayoutInflater inflater) throws Exception {
		if (task != null) {

			data = task.get();
		}
		layout = (LinearLayout) inflater.inflate(R.layout.fragment_viewpager,
				null);
		viewPager = (ViewPager) layout.findViewById(R.id.vp);
		MyPageChangeListener listener = new MyPageChangeListener();
		viewPager.setOnPageChangeListener(listener);
		// �����õ��ֲ�ͼ������
		titles = new String[] { "���ǵ�1��ͼƬ", "���ǵ�2��ͼƬ", "���ǵ�3��ͼƬ", "���ǵ�4��ͼƬ",
				"���ǵ�5��ͼƬ" };
		titles1 = new String[100];
		for (int i = 0; i < viewPagerSize; i++) {
			titles1[i] = data.getData().getBanner().get(i).getTitle();
		}
		// ��ʼ��������ʾ��ǰ�ֲ�ͼλ�õĵ�����������7��
		dots = new ArrayList<View>();
		View dotView[] = new View[7];
		dotView[0] = layout.findViewById(R.id.dot1);
		dotView[1] = layout.findViewById(R.id.dot2);
		dotView[2] = layout.findViewById(R.id.dot3);
		dotView[3] = layout.findViewById(R.id.dot4);
		dotView[4] = layout.findViewById(R.id.dot5);
		dotView[5] = layout.findViewById(R.id.dot6);
		dotView[6] = layout.findViewById(R.id.dot7);
		for (int i = 0; i < dotView.length; i++) {
			dots.add(dotView[i]);
		}
		// �����ֲ�ͼ�ж���ҳ��̬�Ľ�����Ҫ�ĵ��gone��
		if (viewPagerSize < 7) {
			int div = 7 - viewPagerSize;
			int j = 6;

			for (int i = 0; i < div; i++) {
				dots.remove(j);
				dotView[j].setVisibility(View.GONE);
				j--;
			}
			if(dots.size()==1){
				dotView[0].setVisibility(View.GONE);
			}

		}
		// �������ݵ����ã����Ƚ���������Ϊ���ݵĵ�һ������ٶ�̬�ĸı�
		viewPagerTitle = (TextView) layout.findViewById(R.id.title);
		if (titles1 != null) {

			if (titles1 != null) {

				if (titles1[0].length() > 13) {
					char[] t = titles1[0].toCharArray();
					char[] tt = new char[13];
					for (int i = 0; i < 13; i++) {
						tt[i] = t[i];
					}

					viewPagerTitle.setText(String.valueOf(tt) + "...");
				} else {
					viewPagerTitle.setText(titles1[0]);
				}

			} else {
				viewPagerTitle.setText(titles[0]);
			}
		} else {
			viewPagerTitle.setText(titles[0]);
		}

		return layout;
	}

	// �ֲ�ͼ��ҳ�仯��������
	private class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int position, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			// ����ʾ��ͼƬ�����仯֮��
			// ���ñ���

			if (titles1 != null) {

				if (titles1[position].length() > 13) {
					char[] t = titles1[position].toCharArray();
					char[] tt = new char[13];
					for (int i = 0; i < 13; i++) {
						tt[i] = t[i];
					}

					viewPagerTitle.setText(String.valueOf(tt) + "...");
				} else {
					viewPagerTitle.setText(titles1[position]);
				}

			} else {
				viewPagerTitle.setText(titles[position]);
			}
			// �ı���״̬
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			dots.get(oldPage).setBackgroundResource(R.drawable.dot_normal);
			// ��¼��ҳ��
			oldPage = position;
			currPage = position;

		}

	}

	public class ImageAdapter extends PagerAdapter {
		// ��ҳ�ֲ�ͼ�������������������Ҫʵ�������ֲ���getcount�����Ż�һ���ܴ������Ȼ�󽫵��ڵ�λ�����ó��м��һ�����־Ϳ������߻����ˡ�����
		// ������instantiateItem����Ҫһ���Ĳ���
		private ArrayList<ImageView> imageSource = null;

		public ImageAdapter(ArrayList<ImageView> imageSource) {
			this.imageSource = imageSource;

		}

		public ArrayList<ImageView> getImageSource() {
			return imageSource;
		}

		public void setImageSource(ArrayList<ImageView> imageSource) {
			this.imageSource = imageSource;
		}

		@Override
		public int getCount() {
			return imageSource.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// �жϽ�Ҫ��ʾ��ͼƬ�Ƿ��������ʾ��ͼƬ��ͬһ��
			// arg0Ϊ��ǰ��ʾ��ͼƬ��arg1�ǽ�Ҫ��ʾ��ͼƬ
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imageSource.get(position));
			// ���ٸ�ͼƬ
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = imageSource.get(position);
			ViewGroup parent = (ViewGroup) imageView.getParent();
			if (parent != null) {
				parent.removeAllViews();
			}
			imageLoader.displayImage(bannerImageURL.get(position), imageView,
					options);
			container.addView(imageView);
			imageSource.get(position).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String infoLink = null;
					infoLink = data.getData().getBanner()
							.get(viewPager.getCurrentItem()).getInfo_link();
					Intent intent = new Intent(getActivity(),
							LooperViewDetails.class);
					intent.putExtra("link", infoLink);
					intent.putExtra("content_id", data.getData().getBanner()
							.get(viewPager.getCurrentItem()).getContent_id());
					startActivity(intent);
				}
			});
			return imageSource.get(position);

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
			imageLoader.displayImage(listImageURL.get(position), holder.iv,
					options);
			if (listTitles.get(position).length() > 25) {
				char[] t = listTitles.get(position).toCharArray();
				char[] tt = new char[25];
				for (int i = 0; i < 25; i++) {
					tt[i] = t[i];
				}
				holder.tvTitle.setText(String.valueOf(tt) + "...");
			} else {
				holder.tvTitle.setText(listTitles.get(position));
			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView iv;
			public TextView tvTitle;
			public TextView tvDescription;
		}

	}

}
