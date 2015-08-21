package com.example.demo.news.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.news.databeans.search.SearchData;
import com.example.demo.news.databeans.search.SearchList;
import com.example.demo.news.dataloaders.SearchLoader;
import com.example.demo.news.xlistviewsource.XListView;
import com.example.demo.news.xlistviewsource.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.xinhuamm.d0403.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends Activity implements OnClickListener,
        IXListViewListener {

    // 搜索也的内容
    private ImageButton back;
    private Button cancle;
    private ImageButton delete;
    private EditText search;
    private XListView lv;
    private String searchText = null;
    private SearchLoader loader = new SearchLoader();
    private AsyncTask<Integer, Void, SearchData> task;
    private SearchData data;
    private XListViewAdapter adapter;
    private ArrayList<SearchList> newsList;
    private ArrayList<String> listTitles;
    private Handler mHandler;
    private int page = 1;
    @SuppressWarnings("unused")
    private int pageCount = 0;

    private ArrayList<String> ListImageURL;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    private Animation animation;
    private LayoutAnimationController controller;

    // �������ݻ�ûʵ�־ͷ���2����ť��������
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.news_list_bg)
                .showImageForEmptyUri(R.drawable.news_list_bg)
                .showImageOnFail(R.drawable.news_list_bg).cacheInMemory(true)
                .cacheOnDisk(true).build();

        mHandler = new Handler();
        back = (ImageButton) findViewById(R.id.btnBack);
        back.setOnClickListener(this);
        cancle = (Button) findViewById(R.id.btnSearch);
        cancle.setOnClickListener(this);
        delete = (ImageButton) findViewById(R.id.btnDelete);
        delete.setOnClickListener(this);
        search = (EditText) findViewById(R.id.etSearch);
        lv = (XListView) findViewById(R.id.lvSearch);
        lv.setXListViewListener(this);
        lv.setVisibility(View.GONE);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        animation = new TranslateAnimation(-1000f, 0f, 0f, 0f);
        animation.setDuration(500);
        // 1fΪ��ʱ
        controller = new LayoutAnimationController(animation, 1f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);

    }

    private SearchData getResource(String text, int i) throws IOException {
        String te = text + "";
        String urlString = "http://api.jjjc.yn.gov.cn/jwapp/?service=Search.index&title="
                + te + "&page=" + i;
        HttpGet get = new HttpGet(urlString);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);
        String resultString = EntityUtils.toString(response.getEntity());
        System.out.println(resultString);
        SearchData data = loader.getJSONDate(resultString);
        System.out.println(new Gson().toJson(data));
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnSearch:
                searchText = search.getText().toString().trim();
                System.out.println(searchText);
                if (searchText.equals("")) {
                    Toast.makeText(this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                }
                onRefresh();

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

        page = 1;
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                task = new AsyncTask<Integer, Void, SearchData>() {

                    @Override
                    protected SearchData doInBackground(Integer... params) {
                        SearchData data = null;
                        try {
                            data = getResource(searchText, params[0]);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        return data;
                    }
                };
                task.execute(1);
                try {
                    data = task.get();
                    if (data == null) {
                        Toast.makeText(SearchActivity.this, "查询无结果",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!searchText.equals("") && data != null) {
                    newsList = new ArrayList<>();
                    newsList.removeAll(newsList);
                    newsList = data.getData().getList();
                    ListImageURL = new ArrayList<>();
                    ListImageURL.removeAll(ListImageURL);
                    for (int i = 0; i < data.getData().getList().size(); i++) {
                        ListImageURL.add(data.getData().getList().get(i)
                                .getImage());
                    }

                    listTitles = new ArrayList<>();
                    listTitles.removeAll(listTitles);
                    for (int i = 0; i < data.getData().getList().size(); i++) {
                        listTitles.add(data.getData().getList().get(i)
                                .getTitle());
                    }
                    adapter = new XListViewAdapter(SearchActivity.this,
                            listTitles);
                    lv.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                    lv.setVisibility(View.VISIBLE);
                    lv.startAnimation(animation);
                    lv.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            String link;
                            Intent intent;

                            link = data.getData().getList().get(position - 1)
                                    .getInfo_link();
                            int content_id = data.getData().getList()
                                    .get(position - 1).getContent_id();
                            intent = new Intent(SearchActivity.this,
                                    LooperViewDetails.class);
                            intent.putExtra("link", link);
                            intent.putExtra("content_id", content_id);
                            startActivity(intent);
                        }
                    });

                }
                onLoad();

            }
        }, 1000);

    }

    @Override
    public void onLoadMore() {

        page++;
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                AsyncTask<Integer, Void, SearchData> task = new AsyncTask<Integer, Void, SearchData>() {

                    @Override
                    protected SearchData doInBackground(Integer... params) {
                        SearchData data = null;
                        try {
                            data = getResource(searchText, page);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        return data;
                    }
                };
                task.execute();
                try {
                    SearchData data = task.get();
                    newsList.addAll(data.getData().getList());
                    for (int i = 0; i < data.getData().getList().size(); i++) {
                        listTitles.add(data.getData().getList().get(i)
                                .getTitle());
                        ListImageURL.add(data.getData().getList().get(i)
                                .getImage());
                    }

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                lv.startAnimation(animation);
                adapter.notifyDataSetChanged();
                onLoad();

            }
        }, 1000);

    }

    private void onLoad() {
        lv.stopRefresh();
        lv.stopLoadMore();
        lv.setRefreshTime("刚刚");
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
            imageLoader.displayImage(ListImageURL.get(position), holder.iv,
                    options);

            if (listTitles.get(position).length() > 25) {
                char[] t = listTitles.get(position).toCharArray();
                char[] tt = new char[25];
                System.arraycopy(t, 0, tt, 0, 25);
                holder.tvTitle.setText(String.valueOf(tt) + "...");
            } else {

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
