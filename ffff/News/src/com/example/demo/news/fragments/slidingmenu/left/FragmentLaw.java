package com.example.demo.news.fragments.slidingmenu.left;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import net.xinhuamm.d0403.R;

import com.example.demo.news.activity.NewsDetailsActivity;
import com.example.demo.news.activity.MainActivity;
import com.example.demo.news.databeans.importantnews.ImportantNewsData;
import com.example.demo.news.databeans.importantnews.ImportantNewsList;
import com.example.demo.news.dataloaders.ImportantNewsLoader;
import com.example.demo.news.xlistviewsource.XListView;
import com.example.demo.news.xlistviewsource.XListView.IXListViewListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

//党纪法规栏目内容
@SuppressLint("InflateParams")
public class FragmentLaw extends Fragment implements OnClickListener,
        IXListViewListener {
    private SlidingMenu slidingMenu1;
    private SlidingMenu slidingMenu2;
    private ImageButton showLeft;
    private ImageButton showRight;
    private View root;
    private XListView lv;
    private ProgressBar pb;
    private XListViewAdapter ListAdapter;
    private Handler mHandler = new Handler();
    private ImportantNewsLoader loader = new ImportantNewsLoader();
    private ImportantNewsData data;// Ҫ��ҳ��ȡ����������
    private AsyncTask<String, Void, ImportantNewsData> task;// ����Ҫ�ŵ�task
    private ArrayList<String> listTitles;// �б����title
    private int page = 1;// ��ǰ�б����ҳ��
    private ArrayList<ImportantNewsList> newsList = null;
    private Context context;
    private int pageCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_law, container, false);
        slidingMenu1 = ((MainActivity) getActivity()).getSlidingMenu1();
        slidingMenu2 = ((MainActivity) getActivity()).getSlidingMenu2();
        slidingMenu1.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        showLeft = (ImageButton) root.findViewById(R.id.btnShowLeft);
        showLeft.setOnClickListener(this);
        showRight = (ImageButton) root.findViewById(R.id.btnShowRight);
        showRight.setOnClickListener(this);

        lv = (XListView) root.findViewById(R.id.lvFirstPage);
        onRefresh();

        lv.setXListViewListener(this);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setVisibility(View.GONE);
        if (MainActivity.isNetworkConnected(getActivity())) {
            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String link = newsList.get(position - 1).getInfo_link();
                    Intent intent = new Intent(getActivity(),
                            NewsDetailsActivity.class);
                    int contentId = newsList.get(position - 1).getContent_id();
                    intent.putExtra("content_id", contentId);
                    intent.putExtra("link", link);
                    startActivity(intent);
                }
            });
        }
        return root;
    }

    private ImportantNewsData getData(int i) throws IOException {

        String JSON = loader
                .readURL("http://api.jjjc.yn.gov.cn//jwapp//?service=List.index&cid=43&page="
                        + i);
        return loader.getJSONDate(JSON);

    }

    @Override
    public void onRefresh() {

        if (MainActivity.isNetworkConnected(getActivity())) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    page = 1;
                    task = new AsyncTask<String, Void, ImportantNewsData>() {

                        @Override
                        protected ImportantNewsData doInBackground(String... params) {
                            ImportantNewsData data = null;
                            try {
                                data = getData(1);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            return data;
                        }
                    };
                    task.execute();
                    if (task != null) {
                        try {
                            data = task.get();
                            newsList = data.getData().getList();
                            pageCount = data.getData().getPagecount();
                        } catch (InterruptedException | ExecutionException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    listTitles = new ArrayList<>();
                    listTitles.clear();
                    for (int i = 0; i < data.getData().getList().size(); i++) {
                        listTitles.add(data.getData().getList().get(i).getTitle());
                    }
                    System.out.println("listsize=" + listTitles.size());
                    ListAdapter = new XListViewAdapter(context, listTitles);
                    lv.setAdapter(ListAdapter);
                    pb = (ProgressBar) root.findViewById(R.id.pb);
                    lv.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);
                    ListAdapter.notifyDataSetChanged();
                    onLoad();
                }
            }, 2000);
        } else {
            Toast.makeText(getActivity(), "请检查您的网络后继续", Toast.LENGTH_SHORT).show();
            onLoad();
        }

    }

    private void onLoad() {
        lv.stopRefresh();
        lv.stopLoadMore();
        lv.setRefreshTime("刚刚");
    }

    @Override
    public void onLoadMore() {

        page++;
        if (page >= pageCount) {
            Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
            onLoad();
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    AsyncTask<Integer, Void, ArrayList<String>> task = new AsyncTask<Integer, Void, ArrayList<String>>() {

                        @Override
                        protected ArrayList<String> doInBackground(
                                Integer... params) {

                            ArrayList<String> titleList = new ArrayList<>();
                            try {
                                ImportantNewsData data = getData(page);
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
                    listTitles.addAll(list);

                    AsyncTask<String, Void, ImportantNewsData> task2 = new AsyncTask<String, Void, ImportantNewsData>() {

                        @Override
                        protected ImportantNewsData doInBackground(String... params) {
                            ImportantNewsData data = null;
                            try {
                                data = getData(page);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            return data;
                        }
                    };
                    task2.execute();
                    ImportantNewsData data = null;
                    try {
                        data = task2.get();
                    } catch (InterruptedException | ExecutionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (data != null) {

                        newsList.addAll(data.getData().getList());
                    }
                    ListAdapter.notifyDataSetChanged();
                    onLoad();
                }
            }, 2000);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowLeft:
                slidingMenu1.toggle();
                break;
            case R.id.btnShowRight:
                slidingMenu2.toggle();
                break;

            default:
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public class XListViewAdapter extends BaseAdapter {
        // �б���ݵ��m����
        private LayoutInflater inflater;
        private ArrayList<String> listTitles;

        public LayoutInflater getInflater() {
            return inflater;
        }

        public void setInflater(LayoutInflater inflater) {
            this.inflater = inflater;
        }


        public XListViewAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);

        }

        public XListViewAdapter(Context context, ArrayList<String> listTitles) {
            this.inflater = LayoutInflater.from(context);
            this.listTitles = listTitles;

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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_law, null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tvTitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvTitle.setText(listTitles.get(position));
            return convertView;
        }
        public class ViewHolder {
            public ImageView iv;
            public TextView tvTitle;
        }

    }
}
