package com.example.demo.news.activity;

import android.annotation.SuppressLint;
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
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.news.databeans.firstpage.FirstPageData;
import com.example.demo.news.databeans.firstpage.FirstpageLoopPager;
import com.example.demo.news.dataloaders.FirstPageContentLoader;
import com.example.demo.news.xlistviewsource.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.xinhuamm.d0403.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import medusa.theone.waterdroplistview.view.WaterDropListView;

public class SubjectDetails extends Activity implements IXListViewListener,
        OnClickListener, WaterDropListView.IWaterDropListViewListener {

    private WaterDropListView lv;
    private ProgressBar pb;
    private XListViewAdapter adapter;
    private Handler mHandler = new Handler();
    private ImageButton back;
    private String link;
    private String name;
    private FirstPageContentLoader loader = new FirstPageContentLoader();
    private AsyncTask<Integer, Void, FirstPageData> taskRefresh;
    private AsyncTask<Integer, Void, FirstPageData> taskLoadMore;
    private int page = 1;
    private FirstPageData data;
    private FirstPageData dataMore;
    private ArrayList<String> listTitle;
    private ArrayList<String> listImageUrl;
    private ArrayList<FirstpageLoopPager> newsList;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private String urlString;
    private int pageCount = 0;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.news_list_bg)
                .showImageForEmptyUri(R.drawable.news_list_bg)
                .showImageOnFail(R.drawable.news_list_bg).cacheInMemory(true)
                .cacheOnDisk(true).build();
        link = getIntent().getExtras().getString("link");
        name = getIntent().getExtras().getString("name");
        urlString = "http://api.jjjc.yn.gov.cn/jwapp/?service=" + link + "&page=";
        setContentView(R.layout.activity_subject_details);
        lv = (WaterDropListView) findViewById(R.id.lv);
        lv.setPullLoadEnable(true);
        lv.setWaterDropListViewListener(this);
        lv.setVisibility(View.GONE);
        onRefresh();
        back = (ImageButton) findViewById(R.id.btnBack);
        back.setOnClickListener(this);
        tv = (TextView) findViewById(R.id.tv);
        tv.setText(name);

    }

    private FirstPageData getResource(int page) throws IOException {
        FirstPageData data = null;
        String Json = loader.readURL(urlString + page);
        data = loader.getJSONDate(Json);
        Log.e("data", new Gson().toJson(data));
        return data;
    }

    @Override
    public void onRefresh() {
        page = 1;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (MainActivity.isNetworkConnected(SubjectDetails.this)) {
                    taskRefresh = new AsyncTask<Integer, Void, FirstPageData>() {
                        @Override
                        protected FirstPageData doInBackground(Integer... params) {
                            FirstPageData data = null;
                            try {
                                data = getResource(page);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return data;
                        }
                    };
                    taskRefresh.execute();
                }
                try {
                    data = taskRefresh.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                pageCount = data.getData().getPagecount();
                Log.e("pageCount", String.valueOf(pageCount));
                listImageUrl = new ArrayList<>();
                listTitle = new ArrayList<>();
                newsList = new ArrayList<>();
                newsList = data.getData().getList();
                for (int i = 0; i < newsList.size(); i++) {
                    listTitle.add(data.getData().getList().get(i).getTitle());
                    listImageUrl.add(data.getData().getList().get(i).getImage());
                }
                adapter = new XListViewAdapter(SubjectDetails.this, listTitle);
                lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                pb = (ProgressBar) findViewById(R.id.pb);
                lv.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                onLoad();
            }
        }, 2000);

    }

    private void onLoad() {
        lv.stopRefresh();
        lv.stopLoadMore();
    }

    @Override
    public void onLoadMore() {

        if (page >= pageCount) {
            onLoad();
            Toast.makeText(SubjectDetails.this, "没有更多了", Toast.LENGTH_SHORT).show();
        } else {
            page++;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (MainActivity.isNetworkConnected(SubjectDetails.this)) {
                        taskLoadMore = new AsyncTask<Integer, Void, FirstPageData>() {
                            @Override
                            protected FirstPageData doInBackground(Integer... params) {
                                FirstPageData data = null;
                                try {
                                    data = getResource(page);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return data;
                            }
                        };
                        taskLoadMore.execute();
                        try {
                            dataMore = taskLoadMore.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        newsList.addAll(dataMore.getData().getList());
                        for (int i = 0; i < dataMore.getData().getList().size(); i++) {
                            listImageUrl.add(dataMore.getData().getList().get(i).getImage());
                            listTitle.add(dataMore.getData().getList().get(i).getTitle());
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(SubjectDetails.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                    onLoad();
                }
            }, 2000);
        }
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

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
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
            imageLoader.displayImage(listImageUrl.get(position), holder.iv,
                    options);
            if (listTitles.get(position).length() > 25) {
                char[] t = listTitles.get(position).toCharArray();
                char[] tt = new char[25];
                System.arraycopy(t, 0, tt, 0, 25);
                holder.tvTitle.setText(String.valueOf(tt) + "...");
            } else {
                holder.tvTitle.setText(listTitles.get(position));
            }
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SubjectDetails.this, NewsDetailsActivity.class);
                    int id = newsList.get(position).getContent_id();
                    String link = newsList.get(position).getInfo_link();
                    i.putExtra("content_id", id);
                    i.putExtra("link", link);
                    startActivity(i);

                }
            });
            return convertView;
        }

        public class ViewHolder {
            public ImageView iv;
            public TextView tvTitle;
            public TextView tvDescription;
        }

    }


}
