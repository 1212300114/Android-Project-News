package com.example.demo.news.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.demo.news.Debug;
import com.example.demo.news.activity.MainActivity;
import com.example.demo.news.activity.NewsDetailsActivity;
import com.example.demo.news.adapters.ColumnListAdapter;
import com.example.demo.news.databeans.ColumnEntity;
import com.example.demo.news.myviews.Banner;
import com.example.demo.news.utils.Constants;
import com.example.demo.news.utils.NetworkRequest;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import net.xinhuamm.d0403.R;

import org.apache.http.Header;

import medusa.theone.waterdroplistview.view.WaterDropListView;

public class FragmentAll extends Fragment implements Banner.OnItemClickListener, WaterDropListView.IWaterDropListViewListener {
    //各栏目列表内容
    /*
    views
     */
    private View root;
    private ProgressBar bar;// ������Ϊ�������ʱ��ʾ�Ľ�����
    private WaterDropListView lv;// 新闻列表 使用的是水滴状下拉刷新的控件
    private ColumnListAdapter listAdapter;
    private Banner banner;//轮播图
    private View header;
    private boolean isSubject = false;
    private String link;
    private Handler mHandler;
    private MainActivity activity;
    private int page = 1;
    private int pageCount = 0;
    private boolean vpOn = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        activity = (MainActivity) getActivity();
        Bundle bundle = getArguments();
        ColumnEntity.DataEntity.CateEntity entity = (ColumnEntity.DataEntity.CateEntity) bundle.getSerializable("data");
        String name = null;
        if (entity != null) {
            link = entity.getCate_link();
            name = entity.getName();
        }
        //获取到传入的数据
        Log.e("name = ", name);
        Log.e("link", link);
        //初始化各种数据以及图片加载选项
        if (name != null) {
            if (name.equals(Constants.SUBJECT)) {
                isSubject = true;
            }
        }
        if (link != null) {
            link = Constants.COLUMNLISTURL + link;
        }

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_firstpage, container, false);
        initView();
        vpOn = false;
        onRefresh();
        return root;
    }

    //初始化view
    private void initView() {
        lv = (WaterDropListView) root.findViewById(R.id.lvFirstPage);
        bar = (ProgressBar) root.findViewById(R.id.pb);
        bar.setVisibility(View.VISIBLE);
        lv.setWaterDropListViewListener(this);
        lv.setPullLoadEnable(false);
        listAdapter = new ColumnListAdapter(activity, isSubject);
        lv.setAdapter(listAdapter);
        header = LayoutInflater.from(activity).inflate(R.layout.fragment_viewpager, lv, false);
        banner = (Banner) header.findViewById(R.id.banner);
    }

    @Override
    public void onRefresh() {
        //刷新操作
        page = 1;
        if (NetworkRequest.isNetworkConnected(activity)) {
            NetworkRequest.get(link + "&page=" + page, new TextHttpResponseHandler() {
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
        }
    }

    @Override
    public void onLoadMore() {
        //加载更多的操作
        page++;
        if (NetworkRequest.isNetworkConnected(activity)) {
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
                Toast.makeText(activity, "没有更多了", Toast.LENGTH_SHORT).show();
                lv.stopLoadMore();
            }
        } else {
            Toast.makeText(activity, "请检查您的网络", Toast.LENGTH_SHORT).show();
            lv.stopLoadMore();
        }
    }


    @Override
    public void click(View v, ColumnEntity.DataEntity.BannerEntity entity) {
        String link = entity.getInfo_link();
        String id = String.valueOf(entity.getContent_id());
        Intent intent = new Intent(activity, NewsDetailsActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("content_id", id);
        startActivity(intent);
        //轮播图点击事件处理
    }

    //加载第一次数据的方法
    private void parseFirstJson(String responseString) {
        if (null != responseString) {
            ColumnEntity entity = new Gson().fromJson(responseString, ColumnEntity.class);
            Debug.show(new Gson().toJson(entity));
            listAdapter.setData(entity, true);
            if (entity.getData().getBanner().size() != 0 && !vpOn) {
                banner.setTopEntities(entity.getData().getBanner());
                lv.addHeaderView(header);
                activity.getSlidingMenu().addIgnoredView(banner);
                vpOn = true;
            }
            if (entity.getData().getList().size() < 10) {
                lv.setPullLoadEnable(false);
            } else {
                lv.setPullLoadEnable(true);
            }
            if (isSubject) {
                lv.setPullLoadEnable(false);
            }
            pageCount = entity.getData().getPagecount();
            banner.setOnItemClickListener(this);
            bar.setVisibility(View.GONE);
        }
    }

    //加载更多数据的操作
    private void parseMoreJson(String response) {
        if (null != response) {
            ColumnEntity entity = new Gson().fromJson(response, ColumnEntity.class);
            listAdapter.setData(entity, false);
        }
    }

}
