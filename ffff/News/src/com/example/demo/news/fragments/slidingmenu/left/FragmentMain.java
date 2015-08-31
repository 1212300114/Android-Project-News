package com.example.demo.news.fragments.slidingmenu.left;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.demo.news.activity.MainActivity;
import com.example.demo.news.activity.MainActivity.MyOnTouchListener;
import com.example.demo.news.adapters.FragmentViewPagerAdapter;
import com.example.demo.news.databasehelper.ListDataHelper;
import com.example.demo.news.databeans.indicator.IndicatorData;
import com.example.demo.news.dataloaders.IndicatorLoader;
import com.example.demo.news.fragments.main.FragmentAll;
import com.example.demo.news.fragments.main.FragmentFirstPage;
import com.example.demo.news.myviews.MyDialog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import net.xinhuamm.d0403.R;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class FragmentMain extends Fragment implements OnClickListener {
    private ViewPager viewPager;//显示各个栏目内容的viewpager
    private TabPageIndicator indicator;// 栏目导航栏
    private ArrayList<Fragment> fragments; // 各个栏目内容的数组
    private SlidingMenu slidingMenu1, slidingMenu2;// 获取到MainActivity的2个menu
    private ImageButton showLeft; // 显示左侧栏的button
    private ImageButton showRight;// 显示右侧栏的button
    private ImageButton add;// 显示栏目选择dialog的button
    private MyDialog dialog;// 栏目选择的dialog
    private MainActivity mainActivity;// 或到Mainactivity从而获取到Slidingmenu
    private static boolean viewpagerIsTouched = false;// touch事件发生在首页轮播图内的flag
    private MyOnTouchListener myOnTouchListener;// touch事件获取
    private IndicatorLoader loader = new IndicatorLoader();// 导航栏数据加载器
    private IndicatorData indicatorData = null;// 导航栏的数据实体
    private FragmentViewPagerAdapter adapter;// 栏目内容viewpager的适配器
    private FragmentFirstPage firstPage; // 首页栏目内容
    private AsyncTask<String, Void, IndicatorData> task;// 加载导航栏数据的task
    private LinearLayout layout;
    private ListDataHelper listDataHelper;
    private static String TITLE = "栏目";
    private SharedPreferences sharedPreferences;
    private boolean network = false;
    private String storedJson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listDataHelper = new ListDataHelper(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        network = sharedPreferences.getBoolean("network", false);
        SQLiteDatabase dbRead = listDataHelper.getReadableDatabase();
        Cursor cursor = dbRead.query("listData", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            if (name.equals(TITLE)) {
                storedJson = cursor.getString(cursor.getColumnIndex("json"));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root;
        dialog = new MyDialog(getActivity(), R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_style);
        dialog.findViewById(R.id.btnDialogCancle).setOnClickListener(this);
        mainActivity = (MainActivity) getActivity();
        slidingMenu1 = mainActivity.getSlidingMenu1();
        slidingMenu2 = mainActivity.getSlidingMenu2();
        task = new AsyncTask<String, Void, IndicatorData>() {
            // ��ʼ��task���d����������
            @Override
            protected IndicatorData doInBackground(String... params) {
                try {
                    indicatorData = getIndicatorDataResource();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return indicatorData;
            }
        };
        task.execute();
        try {
            if (network) {
                indicatorData = task.get();//获取到数据
            } else {
            }
        } catch (InterruptedException | ExecutionException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (!network) {
            indicatorData = loader.getJSONDate(storedJson);
        }
        root = inflater.inflate(R.layout.fragment_main, container, false);
        viewPager = (ViewPager) root.findViewById(R.id.pager);
        fragments = new ArrayList<>();
        firstPage = new FragmentFirstPage();//初始化栏目数组为其添加内容首先将首页固定
        adapter = new FragmentViewPagerAdapter(getChildFragmentManager(),
                fragments);
        String[] names = new String[100];
        fragments.add(firstPage);//
        if (indicatorData != null) {
            names[0] = "首页";
            for (int i = 1; i < indicatorData.getData().getCate().size(); i++) {
                String name = indicatorData.getData().getCate().get(i)
                        .getName();
                String link = indicatorData.getData().getCate().get(i)
                        .getCate_link();
                int classId = indicatorData.getData().getCate().get(i).getClass_id();
                Bundle bundle = new Bundle();

                bundle.putInt("classId", classId);
                bundle.putString("name", name);
                bundle.putString("link", link);//获取到各个栏目的列表数据的数据接口以及栏目的名字并且将名字传给导航栏
                FragmentAll all = new FragmentAll();// 动态添加栏目
                all.setArguments(bundle);
                fragments.add(all);
                names[i] = indicatorData.getData().getCate().get(i).getName();
            }
        }
        adapter.setFragments(fragments);
        adapter.setTitles(names);
        viewPager.setAdapter(adapter);
        indicator = (TabPageIndicator) root.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);// 将导航栏与栏目内容绑定
        indicator.setCurrentItem(0);
        indicator.setOnPageChangeListener(new MyPageChangeListener());//设置栏目内容切换侦听器
        showLeft = (ImageButton) root.findViewById(R.id.btnShowLeft);//初始化各个按钮的内容
        showLeft.setOnClickListener(this);
        showRight = (ImageButton) root.findViewById(R.id.btnShowRight);
        showRight.setOnClickListener(this);
        add = (ImageButton) root.findViewById(R.id.btnAdd);
        add.setOnClickListener(this);
        //获取到touch事件动态判断左右侧栏的touchmode
        myOnTouchListener = new MyOnTouchListener() {
            // ��touch�¼����жϾ���slidingmenu��touchmode
            @Override
            public boolean onTouch(MotionEvent ev) {
                ViewPager vp = firstPage.getViewPager();
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    layout = firstPage.getLayout();
                    if (layout != null) {

                        viewpagerIsTouched = isInChangeImageZone(layout,
                                (int) ev.getX(), (int) ev.getY());
                    }
                }

                if (!mainActivity.isInFragmentFirstPage()
                        || viewPager.getCurrentItem() == (fragments.size() - 1)) {
                    slidingMenu2
                            .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                } else if (viewPager.getCurrentItem() != (fragments.size() - 1)) {
                    slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }
                if (viewPager.getCurrentItem() == 0 && !viewpagerIsTouched) {
                    slidingMenu1
                            .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }
                if (!mainActivity.isInFragmentFirstPage()) {
                    slidingMenu1
                            .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                } else if (viewPager.getCurrentItem() != 0) {
                    slidingMenu1.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }
                if (layout != null) {
                    if (vp.getCurrentItem() != 0
                            && viewPager.getCurrentItem() != 0) {
                        slidingMenu1
                                .setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    }
                    if (vp.getCurrentItem() != 0 && viewpagerIsTouched) {
                        slidingMenu1
                                .setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    }
                    if (vp.getCurrentItem() == 0
                            && viewPager.getCurrentItem() == 0) {
                        slidingMenu1
                                .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    }

                }
                return true;
            }
        };
        ((MainActivity) getActivity())
                .registerMyOnTouchListener(myOnTouchListener);

        return root;
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
            case R.id.btnAdd:
                dialog.show();
                break;
            case R.id.btnDialogCancle:
                dialog.cancel();
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

    // �Á���touch�¼����Ƿ����ֲ�ͼ�ڡ�������ΪҪ�ж�slidingmenu�Ƿ���Ի�����
    private Rect mChangeImageBackgroundRect = null;

    private boolean isInChangeImageZone(View view, int x, int y) {

        if (null == mChangeImageBackgroundRect) {
            mChangeImageBackgroundRect = new Rect();
        }
        view.getDrawingRect(mChangeImageBackgroundRect);

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        mChangeImageBackgroundRect.left = location[0];
        mChangeImageBackgroundRect.top = location[1];
        mChangeImageBackgroundRect.right = mChangeImageBackgroundRect.right
                + location[0];
        mChangeImageBackgroundRect.bottom = mChangeImageBackgroundRect.bottom
                + location[1];

        return mChangeImageBackgroundRect.contains(x, y);
    }

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

    private IndicatorData getIndicatorDataResource() throws IOException {
        String JSON = loader
                .readURL("http://api.jjjc.yn.gov.cn/jwapp/?service=Category.index");

        SQLiteDatabase database = listDataHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", TITLE);
        values.put("json", JSON);
        Cursor cursor = database.query("listData", null, null, null, null, null, null);
        boolean flag = false;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            if (name.equals(TITLE)) {
                flag = true;
            }
        }
        if (flag) {
            String sql = "DELETE FROM listData WHERE name ='" + TITLE + "'";
            database.execSQL(sql);
        }

        database.insert("listData", null, values);
        cursor.close();
        database.close();
        return loader.getJSONDate(JSON);

    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
