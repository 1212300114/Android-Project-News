package com.example.demo.news.fragments.slidingmenu.left;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.demo.news.activity.MainActivity;
import com.example.demo.news.adapters.DynamicListAdapter;
import com.example.demo.news.databeans.ColumnEntity;
import com.example.demo.news.utils.Constants;
import com.example.demo.news.utils.NetworkRequest;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.TextHttpResponseHandler;

import net.xinhuamm.d0403.R;

import org.apache.http.Header;

public class FragmentDynamic extends Fragment implements OnClickListener {
    //州市动态也的内容 就是一个button列表
    private SlidingMenu slidingMenu1;
    private View root;
    private MainActivity activity;
    private DynamicListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        slidingMenu1 = activity.getSlidingMenu1();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dynamic, container, false);
        initView();
        if (NetworkRequest.isNetworkConnected(activity)) {
            NetworkRequest.get(Constants.DYURL, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    parseJson(responseString);
                }
            });
        }

        return root;
    }


    private void parseJson(String response) {
        ColumnEntity entity = new Gson().fromJson(response, ColumnEntity.class);
        adapter.setEntities(entity.getData().getCate());
    }


    private void initView() {
        root.findViewById(R.id.btnShowLeft).setOnClickListener(this);
        root.findViewById(R.id.btnShowRight).setOnClickListener(this);
        ListView listView = (ListView) root.findViewById(R.id.lvDynamic);
        adapter = new DynamicListAdapter(activity);
        listView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowLeft:
                slidingMenu1.showMenu();
                break;
            case R.id.btnShowRight:
                slidingMenu1.showSecondaryMenu();
                break;
            default:
                break;
        }
    }
}
