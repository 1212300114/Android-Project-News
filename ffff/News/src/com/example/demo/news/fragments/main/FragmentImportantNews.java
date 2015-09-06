package com.example.demo.news.fragments.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import net.xinhuamm.d0403.R;

import com.example.demo.news.activity.LooperViewDetailsActivity;
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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("InflateParams")
public class FragmentImportantNews extends Fragment implements
        IXListViewListener {
    private View root;
    private XListView lv;
    private ProgressBar pb;
    private XListViewAdapter ListAdapter;
    private Handler mHandler = new Handler();
    private ImportantNewsLoader loader = new ImportantNewsLoader();
    private ImportantNewsData data;// Ҫ��ҳ��ȡ����������
    private AsyncTask<String, Void, ImportantNewsData> task;// ����Ҫ�ŵ�task
    private ArrayList<String> listTitles;// �б����title
    private LinearLayout layout;
    private ViewPager viewPager;// �ֲ�ͼ
    private int page = 1;// ��ǰ�б����ҳ��
    @SuppressWarnings("unused")
    private int currPage = 0;// ��ǰ��ʾ��ҳ
    private int oldPage = 0;// ��һ����ʾ��ҳ
    private ArrayList<View> dots = null;
    private TextView title = null;
    private ImageAdapter imageAdapter;
    private String[] titles = null;// ������û��ʱ�ֲ�ͼ�ı���
    private MyPageChangeListener listener;
    private ArrayList<ImageView> imageSource = null;// �ֲ�ͼ�õ�Imageview
    private String[] imageTitles;// �ֲ�ͼ�ı���
    private ImageView[] imageView = null;
    Bitmap[] bitmaps;
    private ArrayList<ImportantNewsList> newsList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        root = inflater.inflate(R.layout.fragment_important_news, container,
                false);
        imageView = new ImageView[20];
        for (int i = 0; i < data.getData().getBanner().size(); i++) {
            imageView[i] = (ImageView) inflater.inflate(R.layout.image_item,
                    null);
            imageView[i].setImageResource(R.drawable.fuqi);
        }
        listTitles = new ArrayList<String>();
        for (int i = 0; i < data.getData().getList().size(); i++) {
            listTitles.add(data.getData().getList().get(i).getTitle());
        }

        lv = (XListView) root.findViewById(R.id.lvFirstPage);
        ListAdapter = new XListViewAdapter(getActivity());
        lv.addHeaderView(initLayout(inflater));
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
                // String link = data.getData().getList().get(position-2)
                // .getInfo_link();
                String link = newsList.get(position - 2).getInfo_link();
                Intent intent = new Intent(getActivity(),
                        LooperViewDetailsActivity.class);
                intent.putExtra("link", link);
                startActivity(intent);
            }
        });

        return root;
    }

    private ImportantNewsData getData(int i) throws IOException {

        String JSON = loader
                .readURL("http://api.jjjc.yn.gov.cn/jwapp/?service=List.index&cid=30&page="
                        + i);
        ImportantNewsData data = loader.getJSONDate(JSON);

        return data;

    }

    public LinearLayout initLayout(LayoutInflater inflater) {

        titles = new String[]{"���ǵ�1��ͼƬ", "���ǵ�2��ͼƬ", "���ǵ�3��ͼƬ", "���ǵ�4��ͼƬ",
                "���ǵ�5��ͼƬ"};
        imageSource = new ArrayList<ImageView>();
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_viewpager,
                null);
        viewPager = (ViewPager) layout.findViewById(R.id.vp);
        listener = new MyPageChangeListener();
        viewPager.setOnPageChangeListener(listener);

        imageTitles = new String[20];
        int viewPagerSize = data.getData().getBanner().size();// �ֲ�ͼ��ҳ��
        for (int i = 0; i < viewPagerSize; i++) {
            imageTitles[i] = data.getData().getBanner().get(i).getTitle();
            // ��ȡ���ֲ�ͼ�ı���
        }
        dots = new ArrayList<View>();// ��ʼ������������Ҷ����������7����
        // --�����ٶ�Щ��-����Ҫ��layout��Ϊ�Ҳ���ͨ����̬��Ӷ���ֱ����layout���滭�ŵ�
        View dotView[] = new View[7];
        dotView[0] = layout.findViewById(R.id.dot1);
        dotView[1] = layout.findViewById(R.id.dot2);
        dotView[2] = layout.findViewById(R.id.dot3);
        dotView[3] = layout.findViewById(R.id.dot4);
        dotView[4] = layout.findViewById(R.id.dot5);
        dotView[5] = layout.findViewById(R.id.dot6);
        dotView[6] = layout.findViewById(R.id.dot7);
        Collections.addAll(dots, dotView);
        // �����ֲ�ͼ�ж���ҳ��̬�Ľ�����Ҫ�ĵ��gone��
        if (viewPagerSize < 7) {
            int div = 7 - viewPagerSize;
            int j = 6;

            for (int i = 0; i < div; i++) {
                dots.remove(j);
                dotView[j].setVisibility(View.GONE);
                j--;
            }

        }
        // �������ݵ����ã����Ƚ���������Ϊ���ݵĵ�һ������ٶ�̬�ĸı�
        title = (TextView) layout.findViewById(R.id.title);
        if (imageTitles != null) {

            title.setText(imageTitles[0]);
        } else {
            title.setText(titles[0]);
        }

        return layout;

    }

    private class MyPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            if (imageTitles != null) {

                title.setText(imageTitles[position]);
            } else {
                title.setText(titles[position]);
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

        @Override
        public int getCount() {
            System.out.println("size=" + imageSource.size());
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

            container.addView(imageSource.get(position));
            imageSource.get(position).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(getActivity(), LooperViewDetailsActivity.class);
                    intent.putExtra(
                            "link",
                            data.getData().getBanner()
                                    .get(viewPager.getCurrentItem())
                                    .getInfo_link());
                    startActivity(intent);
                }
            });
            return imageSource.get(position);

        }
    }

    // ��ȡ�ֲ�ͼ��ԴͼƬ�ķ���
    private Bitmap getBitmap(int position) throws InterruptedException,
            ExecutionException, IOException {
        if (task.get() != null) {

            data = task.get();
        }
        String urlString;
        urlString = data.getData().getBanner().get(position).getImage();
        URL url = new URL(urlString);
        InputStream is = url.openStream();

        Bitmap map = BitmapFactory.decodeStream(is);
        return map;
    }

    // ��ȡ�б���ͼƬ�ķ���
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
                if (data != null) {

                    int viewPagerSize = data.getData().getBanner().size();

                    AsyncTask<Integer, Void, Bitmap> taskBanner;
                    ArrayList<AsyncTask<Integer, Void, Bitmap>> bannerList = new ArrayList<AsyncTask<Integer, Void, Bitmap>>();
                    for (int i = 0; i < viewPagerSize; i++) {
                        taskBanner = new AsyncTask<Integer, Void, Bitmap>() {

                            @Override
                            protected Bitmap doInBackground(Integer... params) {
                                Bitmap bitmap = null;
                                try {
                                    bitmap = getBitmap(params[0]);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                return bitmap;
                            }
                        };
                        bannerList.add(taskBanner);

                    }
                    bitmaps = new Bitmap[50];
                    for (int i = 0; i < viewPagerSize; i++) {
                        try {
                            bitmaps[i] = bannerList.get(i).execute(i).get();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    for (int i = 0; i < data.getData().getBanner().size(); i++) {
                        if (bitmaps[i] != null) {
                            imageView[i].setImageBitmap(bitmaps[i]);
                        }
                        if (bitmaps[i] == null) {
                            imageView[i].setImageResource(R.drawable.fuqi);
                        }
                        imageSource.add(imageView[i]);
                    }
                }
                pb = (ProgressBar) root.findViewById(R.id.pb);
                imageAdapter = new ImageAdapter(imageSource);
                lv.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                viewPager.setAdapter(imageAdapter);
                ListAdapter.notifyDataSetChanged();
                imageAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);

    }

    private void onLoad() {
        lv.stopRefresh();
        lv.stopLoadMore();
        lv.setRefreshTime("�ո�");
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
