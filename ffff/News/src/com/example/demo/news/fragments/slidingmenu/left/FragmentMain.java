package com.example.demo.news.fragments.slidingmenu.left;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.demo.news.activity.MainActivity;
import com.example.demo.news.adapters.ColumnSelectDialogAdapter;
import com.example.demo.news.adapters.FragmentViewPagerAdapter;
import com.example.demo.news.databeans.ColumnEntity;
import com.example.demo.news.fragments.FragmentAll;
import com.example.demo.news.myviews.MyDialog;
import com.example.demo.news.utils.Constants;
import com.example.demo.news.utils.NetworkRequest;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.TextHttpResponseHandler;
import com.viewpagerindicator.TabPageIndicator;

import net.xinhuamm.d0403.R;

import org.apache.http.Header;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FragmentMain extends Fragment implements OnClickListener {
    //主view 各个栏目放置在viewpager里面
    /*
     view
     */
    private View root;
    private ViewPager viewPager;//显示各个栏目内容的viewpager
    private TabPageIndicator indicator;// 栏目导航栏
    private SlidingMenu slidingMenu1;// 获取到MainActivity的2个menu
    private MyDialog dialog;// 栏目选择的dialog
    private MainActivity mainActivity;
    private FragmentViewPagerAdapter adapter;// 栏目内容viewpager的适配器
    private ColumnSelectDialogAdapter selectAdapter;
    private ProgressBar barMain;
    private TextView textView;
    /*
       data saving abouts
     */
    private ImageButton add;
    private SharedPreferences sharedPreferences;

    private ArrayList<String> names;//栏目标题的数组
    private ArrayList<Fragment> fragments; // 各个栏目内容的数组
    private Context context;//上下文

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        context = getActivity().getApplicationContext();
        mainActivity = (MainActivity) getActivity();
        slidingMenu1 = mainActivity.getSlidingMenu1();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main, container, false);
        initViews();
        initDialog();
        fragments = new ArrayList<>();
        names = new ArrayList<>();

        if (NetworkRequest.isNetworkConnected(context)) {
            NetworkRequest.get(Constants.COLUMNINDICATORURL, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    parseData(responseString);
                    barMain.setVisibility(View.GONE);
                }
            });
        } else {
            barMain.setVisibility(View.GONE);
        }
        return root;

    }

    //加载导航栏数据并且形成各个栏目的fragment装入adapter
    private void parseData(String result) {
        ColumnEntity entity = new Gson().fromJson(result, ColumnEntity.class);
        selectAdapter.setData(entity.getData().getCate());
        String storedNames[];
        String storedColumn = sharedPreferences.getString("id", "");
        Log.e("", storedColumn + "~~~");
        //根据是否有保存的栏目选择来添加fragment数组
        if (storedColumn.length() > 2) {
            storedNames = storedColumn.split(",");
            for (String storedName : storedNames) {
                for (int j = 0; j < entity.getData().getCate().size(); j++) {
                    if (storedName.equals(entity.getData().getCate().get(j).getName())) {
                        FragmentAll fragmentAll = new FragmentAll();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", entity.getData().getCate().get(j));
                        fragmentAll.setArguments(bundle);
                        fragments.add(fragmentAll);
                        names.add(entity.getData().getCate().get(j).getName());
                    }
                }
            }
            selectAdapter.setChoice(names);
        } else {
            for (int i = 0; i < entity.getData().getCate().size(); i++) {
                Bundle bundle = new Bundle();
                FragmentAll all = new FragmentAll();// 动态添加栏目
                bundle.putSerializable("data", entity.getData().getCate().get(i));
                all.setArguments(bundle);
                fragments.add(all);
                names.add(entity.getData().getCate().get(i).getName());
            }
        }
        add.setVisibility(View.VISIBLE);
        adapter.setData(fragments, names);
        indicator.setViewPager(viewPager);
        indicator.notifyDataSetChanged();
    }


    private void initViews() {
        //初始化view
        viewPager = (ViewPager) root.findViewById(R.id.pager);
        indicator = (TabPageIndicator) root.findViewById(R.id.indicator);
        indicator.setOnPageChangeListener(new MyPageChangeListener());//设置栏目内容切换侦听器
        ImageButton showLeft = (ImageButton) root.findViewById(R.id.btnShowLeft);
        showLeft.setOnClickListener(this);
        ImageButton showRight = (ImageButton) root.findViewById(R.id.btnShowRight);
        showRight.setOnClickListener(this);
        add = (ImageButton) root.findViewById(R.id.btnAdd);
        add.setVisibility(View.GONE);
        add.setOnClickListener(this);
        textView = (TextView) root.findViewById(R.id.tvNo);
        if (!NetworkRequest.isNetworkConnected(context)) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
        adapter = new FragmentViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        barMain = (ProgressBar) root.findViewById(R.id.pbMain);
        barMain.setVisibility(View.VISIBLE);
        textView.setOnClickListener(this);
    }

    /*
    initDialog
     */
    private void initDialog() {
        dialog = new MyDialog(mainActivity, R.style.MyDialog);
        dialog.setContentView(R.layout.column_select_diaolog_layout);
        ListView listView = (ListView) dialog.findViewById(R.id.lvColumnSelect);
        selectAdapter = new ColumnSelectDialogAdapter(mainActivity);
        listView.setAdapter(selectAdapter);
        dialog.findViewById(R.id.btnDialogCancle).setOnClickListener(this);
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
            case R.id.btnAdd:
                dialog.show();
                break;
            case R.id.btnDialogCancle:
                dialog.cancel();
                for (int i = 0; i < selectAdapter.getEntitiesResponse().size(); i++) {
                    Log.e("", selectAdapter.getEntitiesResponse().get(i).getName());
                }
                columnSetChanged();
                break;
            case R.id.tvNo:
                textView.setVisibility(View.GONE);
                barMain.setVisibility(View.VISIBLE);
                if (NetworkRequest.isNetworkConnected(context)) {
                    NetworkRequest.get(Constants.COLUMNINDICATORURL, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            barMain.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            parseData(responseString);
                            barMain.setVisibility(View.GONE);
                        }

                    });
                } else {
                    textView.setVisibility(View.VISIBLE);
                    barMain.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    //栏目选择完毕 改变view 的方法
    private void columnSetChanged() {
        ArrayList<Fragment> fragmentAlls = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        List<ColumnEntity.DataEntity.CateEntity> entities = selectAdapter.getEntitiesResponse();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        String store = "";
        for (int i = 0; i < entities.size(); i++) {
            FragmentAll all = new FragmentAll();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", entities.get(i));
            all.setArguments(bundle);
            fragmentAlls.add(all);
            names.add(entities.get(i).getName());
            store = store + entities.get(i).getName() + ",";
        }
        edit.putString("id", store);
//        edit.clear();
        edit.apply();
        adapter.setData(fragmentAlls, names);
        indicator.notifyDataSetChanged();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onDetach() {
        super.onDetach();
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

    //viewpager变化侦听
    private class MyPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
        }

    }


}
