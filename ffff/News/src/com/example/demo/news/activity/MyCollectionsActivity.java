package com.example.demo.news.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.example.demo.news.adapters.CollectionListAdapter;
import com.example.demo.news.databasehelper.DataBaseHelper;
import com.example.demo.news.databeans.ColumnEntity;
import com.example.demo.news.utils.Constants;
import com.example.demo.news.utils.NetworkRequest;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import net.xinhuamm.d0403.R;

import org.apache.http.Header;

import java.util.ArrayList;

public class MyCollectionsActivity extends Activity implements OnClickListener {
    //收藏页
    private SwipeMenuListView mListView;// 可横向滑动删除item的listview
    private CollectionListAdapter adapter;//  listview的适配器
    private ArrayList<Integer> collection;//收藏的news的id的数组
    private String urlString = Constants.CLTNURL;// 收藏接口地址
    private ArrayList<String> times = new ArrayList<>();//收藏内容时间
    private DataBaseHelper db;//数据库helper
    private TextView textView;// 显示无收藏内容时的tv

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collections);
        db = new DataBaseHelper(this);
        getIds();
        initView();
        initSlideMenu();
        overridePendingTransition(0, 0);
        if (collection.size() != 0) {
            for (int i = 0; i < collection.size(); i++) {
                //生成收藏内容接口地址
                urlString = urlString + collection.get(i) + ",";
            }
            System.out.println(urlString);
            if (NetworkRequest.isNetworkConnected(this)) {
                NetworkRequest.get(urlString, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        parseJson(responseString);//调用方法项view填充数据
                    }
                });
            }
        } else {
            //无数据显示提示tv
            textView.setVisibility(View.VISIBLE);
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

    private void getIds() {
        //从数据库取出收藏内容的id以及time
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Cursor c = dbRead.query("id", null, null, null, null, null, null);
        collection = new ArrayList<>();
        while (c.moveToNext()) {
            //从数据库取出数据
            int contentID = c.getInt(c.getColumnIndex("contentId"));
            if (contentID > 0) {
                collection.add(contentID);
                String time = c.getString(c.getColumnIndex("time"));
                times.add(time);
            }
        }
        c.close();
        dbRead.close();
    }

    private void parseJson(String response) {
        //填充数据的回调方法
        ColumnEntity entity = new Gson().fromJson(response, ColumnEntity.class);
        if (entity.getData().getList() != null) {
            adapter.setData(times, entity);
        }
    }

    private void initView() {

        //初始化view-未填充数据
        findViewById(R.id.btnBack).setOnClickListener(this);
        mListView = (SwipeMenuListView) findViewById(R.id.list_view);
        adapter = new CollectionListAdapter(this);
        mListView.setAdapter(adapter);
        textView = (TextView) findViewById(R.id.tvNoFile);

    }

    private void initSlideMenu() {
        //初始化swipeMenu的menu
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // ����menu�����ÿ����������ֻ�����һ����ťɾ��
                SwipeMenuItem delete = new SwipeMenuItem(
                        getApplicationContext());
                delete.setBackground(new ColorDrawable(Color.rgb(0xff, 0x00,
                        0x00)));
                delete.setWidth(dp2px(80));
                delete.setIcon(R.drawable.mycollection_delete);
                menu.addMenuItem(delete);

            }
        };
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            //menu的click侦听 执行删除数据库内容操作以及删除列表项的操作
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu,
                                           int index) {

                switch (index) {
                    case 0:
                        Log.e("---------------------", String.valueOf(times.size()));
                        adapter.removeItem(position);

                        SQLiteDatabase dbWrite = db.getWritableDatabase();
                        String sql = "delete from id where contentId  = "
                                + collection.get(position);
                        dbWrite.execSQL(sql);
                        dbWrite.close();
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
