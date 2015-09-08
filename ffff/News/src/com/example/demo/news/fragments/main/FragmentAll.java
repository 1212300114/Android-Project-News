package com.example.demo.news.fragments.main;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.news.activity.LooperViewDetailsActivity;
import com.example.demo.news.activity.MainActivity;
import com.example.demo.news.activity.SubjectDetails;
import com.example.demo.news.databasehelper.ListDataHelper;
import com.example.demo.news.databeans.firstpage.FirstPageCate;
import com.example.demo.news.databeans.firstpage.FirstPageData;
import com.example.demo.news.databeans.firstpage.FirstpageLoopPager;
import com.example.demo.news.dataloaders.FirstPageContentLoader;
import com.example.demo.news.myviews.MyViewPager;
import com.example.demo.news.xlistviewsource.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.xinhuamm.d0403.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import medusa.theone.waterdroplistview.view.WaterDropListView;

public class FragmentAll extends Fragment implements IXListViewListener, WaterDropListView.IWaterDropListViewListener {
    private View root;// fragment的主view
    private WaterDropListView lv;// 新闻列表 使用的是水滴状下拉刷新的控件
    private String link;// 从argument获取到的link用来获取拼接接口地址
    private ProgressBar bar;// ������Ϊ�������ʱ��ʾ�Ľ�����
    private ImageView[] imageViews = new ImageView[20];//imageview数组用来给轮播图放置内容
    private LayoutInflater layoutInflater = null;
    private AsyncTask<String, Void, FirstPageData> taskForSaving;// 获取网络数据用于保存数据字符串
    private AsyncTask<String, Void, FirstPageData> taskForRefresh;// 用来加载数据的task 不用同一个的原因是？？我忘记了好像可以只用一个。
    private FirstPageContentLoader loader = new FirstPageContentLoader();// 加载数据的loader也就是把加载的过程封装在一个类里面了
    private FirstPageData data = null;// 获取到的数据实体
    private int pageCount = 0;//  新闻列表的页数
    private int page = 1;//  当前新闻页码
    private boolean viewPagerCreated = false;// 判断viewpager即轮播图是否已经创建的flag
    private int viewPagerSize = 0;// 轮播图的size 当为0的时候不添加轮播图当为1的时候不添加显示位置的点
    private XListViewAdapter adapter;// 新闻列表的listview的适配器
    private Handler mHandler = null;
    private ArrayList<String> listTitles;// 新闻列表的标题字符串 数组
    private ArrayList<FirstpageLoopPager> newsList = new ArrayList<>();//新闻列表数据数组
    private ArrayList<FirstPageCate> cateList;//栏目数据数组主要是为了专题页 的分栏设计是获取数据
    private LinearLayout layout;// 轮播图
    private MyViewPager viewPager;// 轮播图的viewpager部分
    private ArrayList<View> dots;// 显示图片位置的点
    private ArrayList<ImageView> imageSource = new ArrayList<>();//为填充viewpager的图片数组
    private String[] titles;//轮播图的标题
    private String[] titles1;
    private TextView viewPagerTitle;// 显示轮播图标题的textview
    private int oldPage = 0;
    private ImageLoader imageLoader;//imageloader加载图片··贼好使
    private DisplayImageOptions options;//加载图片的选项
    private ArrayList<String> listImageURL;//新闻列表的图片地址字符串数组
    private ArrayList<String> bannerImageURL;//轮播图的图片地址字符串数组
    private String name;//当前栏目的标题
    private ListDataHelper listDataHelper;//数据库建立的helper
    private SharedPreferences sharedPreferences;//其实没用到的 app保存数据
    private boolean network = false;//判断网络状态的flag
    private String storedJson;//从数据库获取到的字符串
    private boolean subject = false;//判断当前栏目是否为专题的flag
    private Context context;

    public int getViewPagerSize() {
        return viewPagerSize;
    }

    public boolean isViewPagerCreated() {
        return viewPagerCreated;
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bannerImageURL = new ArrayList<>();
        cateList = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        network = sharedPreferences.getBoolean("network", false);
        Bundle bundle = getArguments();
        name = bundle.getString("name");
        context = getActivity();
        link = bundle.getString("link");// ��ȡ��link�Ӷ���ȡ����ǰ��Ŀ������
        Log.e("link", link);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.news_list_bg)
                .showImageForEmptyUri(R.drawable.news_list_bg)
                .showImageOnFail(R.drawable.news_list_bg).cacheInMemory(true)
                .cacheOnDisk(true).build();
        //初始化各种数据以及图片加载选项
        listDataHelper = new ListDataHelper(context);
        if (network) {
            taskForSaving = new AsyncTask<String, Void, FirstPageData>() {

                @Override
                protected FirstPageData doInBackground(String... params) {
                    FirstPageData data = null;
                    try {
                        data = getResource(1);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return data;
                }
            };
            taskForSaving.execute();
        }
        SQLiteDatabase dbRead = listDataHelper.getReadableDatabase();
        Cursor cursor = dbRead.query("listData", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String nameInside = cursor.getString(cursor.getColumnIndex("name"));
            if (nameInside.equals(name)) {
                storedJson = cursor.getString(cursor.getColumnIndex("json"));
            }
        }
        //从数据库获取到当前栏目的数据
        dbRead.close();
        cursor.close();
        System.out.print("\n" + storedJson + "\n");
        if (!network) {
            System.out.print("--------------------");
            data = loader.getJSONDate(storedJson);//如果没用网络则调用数据库的数据
            System.out.print(new Gson().toJson(data));
        }
        if (name.equals("专题")) {
            subject = true;//栏目是专题flag = true；
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layoutInflater = inflater;
        root = inflater.inflate(R.layout.fragment_firstpage, container, false);
        lv = (WaterDropListView) root.findViewById(R.id.lvFirstPage);
        bar = (ProgressBar) root.findViewById(R.id.pb);
        oldPage = 0;
        viewPagerCreated = false;// ����ʱ�Ƚ��ֲ�ͼ����Ϊδ����
        onRefresh();
        lv.setWaterDropListViewListener(this);
        lv.setPullLoadEnable(true);
        if (newsList.size() < 9 || subject) {
            Log.e("aaaaaaaaaa", "????????????????????????????");
            lv.setPullLoadEnable(false);
        }
        if (!subject) {
            //如果不知专题时新闻列表的点击事件
            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String link;
                    Intent intent;
                    Integer contentId;
                    if (position >= 1) {
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
                        intent = new Intent(context, LooperViewDetailsActivity.class);
                        intent.putExtra("link", link);
                        intent.putExtra("content_id", contentId);
                        startActivityForResult(intent, 1);
                    }
                }
            });
        } else {
            lv.setOnItemClickListener(new OnItemClickListener() {
                //如果是专题时的设置
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= 1) {
                        String link;
                        String name;
                        Intent intent;
                        name = cateList.get(position - 1).getName();
                        link = cateList.get(position - 1).getCate_link();
                        intent = new Intent(context, SubjectDetails.class);
                        intent.putExtra("link", link);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                }
            });
        }
        return root;

    }

    // ��ȡjson����ķ���
    private FirstPageData getResource(int i) throws IOException {
        //获取数据的方法同时执行方法的同时回去更新数据库的内容
        String JSON = loader
                .readURL("http://api.jjjc.yn.gov.cn/jwapp/?service=" + link
                        + "&page=" + i);
        SQLiteDatabase dbWrite = listDataHelper.getWritableDatabase();
        SQLiteDatabase dbRead = listDataHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("json", JSON);
        Cursor cursor =
                dbRead.query("listData", null, null, null, null, null, null);
        boolean had = false;
        while (cursor.moveToNext()) {
            String nameInside = cursor.getString(cursor.getColumnIndex("name"));
            if (nameInside.equals(name)) {
                had = true;
            }
        }
        if (had) {
            dbWrite.update("listData", values, "name = ?", new String[]{name});
        } else {
            dbWrite.insert("listData", null, values);
        }
        cursor.close();
        dbWrite.close();
        dbRead.close();
        return loader.getJSONDate(JSON);

    }

    @Override
    public void onRefresh() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @SuppressLint("InflateParams")
            @Override
            public void run() {

                // TODO Auto-generated method stub
                //刷新时的操作包含了第一次获取数据时候的操作
                page = 1;
                if (MainActivity.isNetworkConnected(context)) {
                    taskForRefresh = new AsyncTask<String, Void, FirstPageData>() {

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
                    taskForRefresh.execute();
                    try {
                        data = taskForRefresh.get();//获取到数据
                    } catch (InterruptedException | ExecutionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (!MainActivity.isNetworkConnected(context)) {
                    Toast.makeText(context, "请检查您的网络后再试", Toast.LENGTH_SHORT).show();

                }
                pageCount = data.getData().getPagecount();
                viewPagerSize = data.getData().getBanner().size();
                newsList = data.getData().getList();
                cateList = data.getData().getCate();
                listImageURL = new ArrayList<>();

                listTitles = new ArrayList<>();
                if (!subject) {//针对是否为专题获取不同的数据来呈现不同的列表
                    for (int i = 0; i < data.getData().getList().size(); i++) {
                        listImageURL.add(data.getData().getList().get(i)
                                .getImage());
                    }
                } else {
                    for (int i = 0; i < data.getData().getCate().size(); i++) {
                        listImageURL.add(data.getData().getCate().get(i).getImage());
                    }
                }
                imageSource = new ArrayList<>();
                //轮播图数据的设置
                if (viewPagerSize > 0 && !viewPagerCreated) {
                    for (int j = 0; j < data.getData().getBanner().size(); j++) {
                        bannerImageURL.add(data.getData().getBanner()
                                .get(j).getImage());
                    }
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
                if (!subject) {
                    for (int i = 0; i < data.getData().getList().size(); i++) {
                        listTitles.add(data.getData().getList().get(i).getTitle());
                    }
                } else {
                    for (int i = 0; i < data.getData().getCate().size(); i++) {
                        listTitles.add(data.getData().getCate().get(i).getName());
                    }
                }
                bar.setVisibility(View.GONE);
                adapter = new XListViewAdapter(context, listTitles);
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
        //加载更多的操作
        if (page >= pageCount) {
            Toast.makeText(context, "没有更多了", Toast.LENGTH_SHORT).show();
            onLoad();
        } else if (!MainActivity.isNetworkConnected(context)) {
            Toast.makeText(context, "请检查您的网络后再试", Toast.LENGTH_SHORT).show();
            onLoad();

        } else {
            page++;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //加载更多数据的task
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
                    } catch (InterruptedException | ExecutionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (data != null) {
                        //向title 和image 数组添加内容
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
    }

    // ��ʼ���ֲ�ͼ������
    @SuppressLint("InflateParams")
    public LinearLayout init(LayoutInflater inflater) throws Exception {

        //初始化轮播图内容的方法
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_viewpager,
                null);
        viewPager = (MyViewPager) layout.findViewById(R.id.vp);
        MyPageChangeListener listener = new MyPageChangeListener();
        viewPager.setOnPageChangeListener(listener);
        // �����õ��ֲ�ͼ������
        titles = new String[]{"���ǵ�1��ͼƬ", "���ǵ�2��ͼƬ", "���ǵ�3��ͼƬ", "���ǵ�4��ͼƬ",
                "���ǵ�5��ͼƬ"};
        titles1 = new String[100];
        for (int i = 0; i < viewPagerSize; i++) {
            titles1[i] = data.getData().getBanner().get(i).getTitle();
        }
        // ��ʼ��������ʾ��ǰ�ֲ�ͼλ�õĵ�����������7��
        dots = new ArrayList<>();
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
            if (dots.size() == 1) {
                dotView[0].setVisibility(View.GONE);
            }

        }
        //缩短title的做法
        viewPagerTitle = (TextView) layout.findViewById(R.id.title);
        if (titles1 != null) {

            if (titles1 != null) {

                if (titles1[0].length() > 13) {
                    char[] t = titles1[0].toCharArray();
                    char[] tt = new char[13];
                    System.arraycopy(t, 0, tt, 0, 13);

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

    //  轮播图变化的侦听器主要为了更新显示点的状态以及轮播图标题
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
                    System.arraycopy(t, 0, tt, 0, 13);

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

        }

    }

    public class ImageAdapter extends PagerAdapter {
        //轮播图viewpager的适配器
        private ArrayList<ImageView> imageSource = null;

        public ImageAdapter(ArrayList<ImageView> imageSource) {
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
                    String infoLink;
                    infoLink = data.getData().getBanner()
                            .get(viewPager.getCurrentItem()).getInfo_link();
                    Intent intent = new Intent(context,
                            LooperViewDetailsActivity.class);
                    intent.putExtra("link", infoLink);
                    intent.putExtra("content_id", data.getData().getBanner()
                            .get(viewPager.getCurrentItem()).getContent_id());
                    startActivity(intent);
                }
            });
            //当图片被载入到viewpager的时候就为他设置侦听
            return imageSource.get(position);

        }
    }

    public class XListViewAdapter extends BaseAdapter {
        //新闻列表的适配器
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

        //根据是否为专题加载不同的内容
        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (!subject) {
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
                    System.arraycopy(t, 0, tt, 0, 25);
                    holder.tvTitle.setText(String.valueOf(tt) + "...");
                } else {
                    holder.tvTitle.setText(listTitles.get(position));
                }
            } else {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_subject, null);
                    holder = new ViewHolder();
                    holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                    holder.tvTitle = (TextView) convertView.findViewById(R.id.tv);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                imageLoader.displayImage(listImageURL.get(position), holder.iv, options);
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
