package com.example.demo.news.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.news.databeans.dynamic.detail.DynamicDetailData;
import com.example.demo.news.databeans.dynamic.detail.DynamicDetailList;
import com.example.demo.news.dataloaders.DynamicDetailLoader;
import com.example.demo.news.xlistviewsource.XListView;
import com.example.demo.news.xlistviewsource.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.xinhuamm.d0403.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DynamicDetailActivity extends Activity implements OnClickListener,
        IXListViewListener {

    private XListView listView;
    private ProgressBar pb;
    private AsyncTask<String, Void, DynamicDetailData> taskForGet = null;
    private AsyncTask<String, Void, DynamicDetailData> taskForRefresh = null;
    private Handler mHandler = new Handler();
    private DynamicDetailLoader loader;
    private DynamicDetailData data;
    private String link;
    private ArrayList<String> listTitle;//新闻列表的标题
    private int page = 1;
    private XListViewAdapter adapter;
    private ArrayList<DynamicDetailList> newsList = null;
    private int pageCount = 0;
    private TextView title;
    private String titleString;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.news_list_bg)
                .showImageForEmptyUri(R.drawable.news_list_bg)
                .showImageOnFail(R.drawable.news_list_bg).cacheInMemory(true)
                .cacheOnDisk(true).build();
        setContentView(R.layout.activity_dynamic_detail);
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        link = getIntent().getExtras().getString("link");
        if (link != null) {
            Log.e("link", link);
        }
        taskForGet = new AsyncTask<String, Void, DynamicDetailData>() {

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
        taskForGet.execute();
        listView = (XListView) findViewById(R.id.lv);
        pb = (ProgressBar) findViewById(R.id.pb);
        textView = (TextView) findViewById(R.id.noData);
        textView.setVisibility(View.GONE);
        loader = new DynamicDetailLoader();
        mHandler = new Handler();
        listView = (XListView) findViewById(R.id.lv);
        try {
            data = taskForGet.get();
            titleString = data.getData().getCate_name();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (data.getData().getList().size() == 0) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView.setVisibility(View.VISIBLE);
                }
            }, 1000);
        }
        title = (TextView) findViewById(R.id.tvTitle);
        title.setText(titleString);
        onRefresh();
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        if (data.getData().getList().size() < 10) {
            listView.setPullLoadEnable(false);
        }
        listView.setVisibility(View.GONE);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position >= 1) {
                    String link = newsList.get(position - 1).getInfo_link();
                    int content_id = newsList.get(position - 1).getContent_id();
                    Intent intent = new Intent(DynamicDetailActivity.this,
                            LooperViewDetailsActivity.class);
                    intent.putExtra("link", link);
                    intent.putExtra("content_id", content_id);
                    startActivity(intent);
                }
            }
        });

    }

    private DynamicDetailData getData(int i, String link) throws IOException {

        String JSON = loader
                .readURL("http://api.jjjc.yn.gov.cn/jwapp/?service=" + link
                        + "&page=" + i);

        return loader.getJSONDate(JSON);

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
                if (MainActivity.isNetworkConnected(DynamicDetailActivity.this)) {
                    taskForRefresh = new AsyncTask<String, Void, DynamicDetailData>() {

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
                    taskForRefresh.execute();
                    try {
                        data = taskForRefresh.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                page = 1;
                newsList = data.getData().getList();
                pageCount = data.getData().getPagecount();
                listTitle = new ArrayList<>();
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
        }, 1000);

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
                        ArrayList<String> titleList = new ArrayList<>();
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
                } catch (InterruptedException | ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (page <= pageCount) {

                    if (list != null) {
                        listTitle.addAll(list);
                    }
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
                } catch (InterruptedException | ExecutionException e) {
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
        }, 1000);
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
            imageLoader.displayImage(newsList.get(position).getImage(), holder.iv, options);
            holder.tvTitle.setText(listTitle.get(position));
            return convertView;
        }

        public class ViewHolder {
            public ImageView iv;
            public TextView tvTitle;
        }
    }
}
