package com.example.demo.news.fragments.slidingmenu.left;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.demo.news.activity.MainActivity;
import com.example.demo.news.adapters.LawListAdapter;
import com.example.demo.news.databeans.ColumnEntity;
import com.example.demo.news.utils.Constants;
import com.example.demo.news.utils.NetworkRequest;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.TextHttpResponseHandler;

import net.xinhuamm.d0403.R;

import org.apache.http.Header;

import java.lang.reflect.Field;

import medusa.theone.waterdroplistview.view.WaterDropListView;

//党纪法规栏目内容
@SuppressLint("InflateParams")
public class FragmentLaw extends Fragment implements OnClickListener, WaterDropListView.IWaterDropListViewListener {
    //党纪法规栏的内容 就是一个只有标题的列表。
    private View root;
    private SlidingMenu menu;
    private MainActivity mainActivity;
    private WaterDropListView lv;
    private LawListAdapter adapter;
    private ColumnEntity entity;
    private ProgressBar bar;
    private Handler mHandler;

    private int pageCount = 0;
    private int page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mHandler = new Handler();
        mainActivity = (MainActivity) getActivity();
        menu = mainActivity.getSlidingMenu();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_law, container, false);
        initView();
        onRefresh();
        return root;
    }

    @Override
    public void onRefresh() {

        page = 1;
        if (NetworkRequest.isNetworkConnected(mainActivity)) {
            NetworkRequest.get(Constants.LAWURL + "&page=" + page, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, final String responseString) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            parseFirstJson(responseString);
                            bar.setVisibility(View.GONE);
                            lv.setVisibility(View.VISIBLE);
                            lv.stopRefresh();
                        }
                    }, 500);
                }
            });
        } else {
            Toast.makeText(mainActivity, "请检查您的网络", Toast.LENGTH_SHORT).show();
            lv.stopRefresh();
        }
    }


    @Override
    public void onLoadMore() {
        page++;
        Log.e("", page + "---------" + pageCount);
        if (page <= pageCount) {
            if (NetworkRequest.isNetworkConnected(mainActivity)) {
                NetworkRequest.get(Constants.LAWURL + "&page=" + page, new TextHttpResponseHandler() {
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
                Toast.makeText(mainActivity, "请检查您的网络", Toast.LENGTH_SHORT).show();
                lv.stopLoadMore();
            }

        } else {
            Toast.makeText(mainActivity, "没有更多了", Toast.LENGTH_SHORT).show();
            lv.stopLoadMore();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowLeft:
                menu.showMenu();
                break;
            case R.id.btnShowRight:
                menu.showSecondaryMenu();
                ;
                break;

            default:
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private void initView() {
        root.findViewById(R.id.btnShowLeft).setOnClickListener(this);
        root.findViewById(R.id.btnShowRight).setOnClickListener(this);
        lv = (WaterDropListView) root.findViewById(R.id.lvLaw);
        lv.setWaterDropListViewListener(this);
        adapter = new LawListAdapter(mainActivity);
        lv.setAdapter(adapter);
        lv.setPullLoadEnable(true);
        bar = (ProgressBar) root.findViewById(R.id.pb);
        bar.setVisibility(View.VISIBLE);
    }

    private void parseFirstJson(String response) {
        entity = new Gson().fromJson(response, ColumnEntity.class);
        adapter.setEntities(entity.getData().getList(), true);
        pageCount = entity.getData().getPagecount();
    }

    private void parseMoreJson(String response) {
        ColumnEntity entity = new Gson().fromJson(response, ColumnEntity.class);
        adapter.setEntities(entity.getData().getList(), false);
    }
}
