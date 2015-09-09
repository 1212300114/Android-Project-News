package com.example.demo.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.news.adapters.DynamicListAdapter;
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
    //专题栏列表页
    /*
    view
     */
    private XListView listView;
    private TextView textView;
    private DynamicListAdapter adapter;
    private ProgressBar pb;

    /*
    task
     */
    private AsyncTask<String, Void, DynamicDetailData> taskForGet = null;
    private AsyncTask<String, Void, DynamicDetailData> taskForRefresh = null;
    private Handler mHandler = new Handler();
    /*
    network abouts
     */
    private DynamicDetailLoader loader;
    private DynamicDetailData data;
    /*
    param
     */
    private String link;
    private ArrayList<String> listTitle;//新闻列表的标题
    private ArrayList<DynamicDetailList> newsList = null;
    private int page = 1;
    private int pageCount = 0;
    private TextView title;
    private String titleString;
    /*
        imageLoader
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init imageLoader

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
                            NewsDetailsActivity.class);
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

                adapter = new DynamicListAdapter(DynamicDetailActivity.this, listTitle, newsList);
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


                AsyncTask<String, Void, DynamicDetailData> task = new AsyncTask<String, Void, DynamicDetailData>() {

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
                task.execute();
                data = null;
                try {
                    data = task.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if (data != null) {
                    if (page <= pageCount) {
                        newsList.addAll(data.getData().getList());
                        for (int i = 0; i < data.getData().getList().size(); i++) {
                            listTitle.add(data.getData().getList().get(i)
                                    .getTitle());
                        }
                    }
                }
                if (page > pageCount) {
                    Toast.makeText(getApplicationContext(), "没有更多了",
                            Toast.LENGTH_SHORT).show();
                }
                adapter.setNewsList(newsList);
                adapter.setListTitle(listTitle);
                adapter.notifyDataSetChanged();
                onLoad();
            }
        }, 1000);
    }

}
