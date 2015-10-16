package com.example.demo.news.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.demo.news.adapters.ColumnListAdapter;
import com.example.demo.news.databeans.ColumnEntity;
import com.example.demo.news.utils.Constants;
import com.example.demo.news.utils.NetworkRequest;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import net.xinhuamm.d0403.R;

import org.apache.http.Header;

import medusa.theone.waterdroplistview.view.WaterDropListView;

public class SearchActivity extends Activity implements OnClickListener,
        WaterDropListView.IWaterDropListViewListener {
    // 搜索页的内容
    private EditText search;
    private ProgressBar pb;
    private WaterDropListView lv;
    private ColumnListAdapter adapter;
    private String link;
    private Handler mHandler;
    private int page = 1;
    private int pageCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mHandler = new Handler();
        initView();
    }

    private void initView() {
        //初始化view
        pb = (ProgressBar) findViewById(R.id.pb);
        this.findViewById(R.id.btnBack).setOnClickListener(this);
        this.findViewById(R.id.btnSearch).setOnClickListener(this);
        this.findViewById(R.id.btnDelete).setOnClickListener(this);
        search = (EditText) findViewById(R.id.etSearch);
        lv = (WaterDropListView) findViewById(R.id.lvSearch);
        lv.setVisibility(View.GONE);
        lv.setWaterDropListViewListener(this);
        lv.setPullLoadEnable(true);
        adapter = new ColumnListAdapter(this, false);
        lv.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnSearch:
                //搜索内容获取并显示到listview
                String searchText = search.getText().toString();
                System.out.println(searchText);
                if (searchText.equals("")) {
                    Toast.makeText(SearchActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                } else {
                    pb.setVisibility(View.VISIBLE);
                    link = Constants.SEARCH_URL + searchText;
                    onRefresh();
                }

                break;
            case R.id.btnDelete:
                search.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        //刷新操作
        page = 1;
        if (NetworkRequest.isNetworkConnected(this)) {
            NetworkRequest.get(link + "&page=" + page, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toast.makeText(SearchActivity.this, "查询无结果", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                    adapter.clearData();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, final String responseString) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            parseFirstJson(responseString);
                            Log.e("----------------", "+++++++++++++++++++++++++++");
                            lv.stopRefresh();
                        }
                    }, 500);
                }
            });
        }
    }

    private void parseFirstJson(String response) {
        //加载第一次获取到数据

        ColumnEntity entity;
        Log.e("", response);
        if (null != response) {
            entity = new Gson().fromJson(response, ColumnEntity.class);
            pageCount = entity.getData().getPagecount();
            adapter.setData(entity, true);
            pb.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoadMore() {
        //加载更多操作
        page++;
        if (NetworkRequest.isNetworkConnected(this)) {
            if (page <= pageCount) {
                NetworkRequest.get(link + "&page=" + page, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, final String responseString) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                parseMoreJson(responseString);
                                lv.stopLoadMore();
                            }
                        }, 500);
                    }
                });
            } else {
                Toast.makeText(this, "没有更多了", Toast.LENGTH_SHORT).show();
                lv.stopLoadMore();
            }
        } else {

            Toast.makeText(this, "请检查您的网络", Toast.LENGTH_SHORT).show();
            lv.stopLoadMore();
        }
    }

    private void parseMoreJson(String response) {
        //加载更多数据的方法
        if (null != response) {
            ColumnEntity entity = new Gson().fromJson(response, ColumnEntity.class);
            adapter.setData(entity, false);
        }
    }


}
