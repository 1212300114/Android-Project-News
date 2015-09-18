package com.example.demo.news.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.news.adapters.ColumnListAdapter;
import com.example.demo.news.databeans.ColumnEntity;
import com.example.demo.news.utils.Constants;
import com.example.demo.news.utils.NetworkRequest;
import com.example.demo.news.xlistviewsource.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import net.xinhuamm.d0403.R;

import org.apache.http.Header;

import medusa.theone.waterdroplistview.view.WaterDropListView;

public class SubjectDetailsActivity extends Activity implements IXListViewListener,
        OnClickListener, WaterDropListView.IWaterDropListViewListener {
    //专题的列表页--- 州市动态列表页，公用
    private WaterDropListView lv;
    private ColumnListAdapter adapter;

    private Handler mHandler;
    private String urlString;
    private int pageCount = 0;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        mHandler = new Handler();
        //获取到传递过来的数据
        String link = getIntent().getExtras().getString("link");
        String name = getIntent().getExtras().getString("name");
        //生成link
        urlString = Constants.COLUMNLISTURL + link + "&page=";
        setContentView(R.layout.activity_subject_details);
        //页标题设置
        TextView tv = (TextView) findViewById(R.id.tv);
        if (name != null)
            tv.setText(name);
        initView();
        onRefresh();
    }

    @Override
    public void onRefresh() {

        //刷新操作
        page = 1;
        if (NetworkRequest.isNetworkConnected(this)) {
            NetworkRequest.get(urlString + page, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, final String responseString) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            parseFirstJson(responseString);
                            lv.stopRefresh();
                        }
                    }, 500);

                }
            });
        } else {
            Toast.makeText(this, "请检查您的网络", Toast.LENGTH_SHORT).show();
            lv.stopRefresh();
        }

    }

    @Override
    public void onLoadMore() {
        //加载更多的操作
        page++;
        if (page <= pageCount) {
            if (NetworkRequest.isNetworkConnected(this)) {
                NetworkRequest.get(urlString + page, new TextHttpResponseHandler() {
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

                Toast.makeText(this, "请检查您的网络", Toast.LENGTH_SHORT).show();
                lv.stopLoadMore();
            }

        } else {
            Toast.makeText(this, "没有更多了", Toast.LENGTH_SHORT).show();
            lv.stopLoadMore();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            default:
                break;
        }
    }


    private void initView() {

        //初始化view
        lv = (WaterDropListView) findViewById(R.id.lv);
        lv.setPullLoadEnable(false);
        lv.setWaterDropListViewListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        adapter = new ColumnListAdapter(this, false);
        lv.setAdapter(adapter);

    }

    private void parseFirstJson(String response) {
        //加载第一次数据方法
        ColumnEntity entity = new Gson().fromJson(response, ColumnEntity.class);
        adapter.setData(entity, true);
        if (entity.getData().getList().size() < 10) {
            lv.setPullLoadEnable(false);
        } else {
            lv.setPullLoadEnable(true);
        }
        pageCount = entity.getData().getPagecount();
    }

    private void parseMoreJson(String response) {
        //加载更多数据的操作
        ColumnEntity entity = new Gson().fromJson(response, ColumnEntity.class);
        adapter.setData(entity, false);
    }
}
