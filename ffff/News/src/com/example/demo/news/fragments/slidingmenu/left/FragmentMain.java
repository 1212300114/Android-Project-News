package com.example.demo.news.fragments.slidingmenu.left;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
    private String storedJson;
    private CheckBox firstPageBox;
    private CheckBox importantNews;
    private CheckBox wind;
    private CheckBox check;
    private CheckBox watch;
    private CheckBox imageNews;
    private CheckBox subject;
    private CheckBox expose;
    private ArrayList<Fragment> selectedFragments;
    private static final String FIRST = "首页";
    private static final String IMPORTANT = "要闻";
    private static final String WIND = "党风";
    private static final String CHECK = "审查";
    private static final String WATCH = "巡视";
    private static final String IMAGE = "图闻";
    private static final String SUBJECT = "专题";
    private static final String EXPOSE = "曝光";
    private String firstLink;
    private String importantLink;
    private String windLink;
    private String checkLink;
    private String watchLink;
    private String imageLink;
    private String subjectLink;
    private String exposeLink;
    private boolean flag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listDataHelper = new ListDataHelper(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        SQLiteDatabase dbRead = listDataHelper.getReadableDatabase();
        Cursor cursor = dbRead.query("listData", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            if (name.equals(TITLE)) {
                storedJson = cursor.getString(cursor.getColumnIndex("json"));
            }
        }
        cursor.close();
        dbRead.close();
        if (!MainActivity.isNetworkConnected(getActivity())) {
            indicatorData = loader.getJSONDate(storedJson);
        }
        selectedFragments = new ArrayList<>();
        FragmentFirstPage fragmentFirstPage = new FragmentFirstPage();
        selectedFragments.add(fragmentFirstPage);
        selectedTitle.add(FIRST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root;
        dialog = new MyDialog(getActivity(), R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_style);
        dialog.findViewById(R.id.btnDialogCancle).setOnClickListener(this);
        dialog.findViewById(R.id.btnFirstPage).setOnClickListener(this);
        firstPageBox = (CheckBox) dialog.findViewById(R.id.btnFirstPage);
        importantNews = (CheckBox) dialog.findViewById(R.id.btnImportantNews);
        wind = (CheckBox) dialog.findViewById(R.id.btnWind);
        check = (CheckBox) dialog.findViewById(R.id.btnCheck);
        watch = (CheckBox) dialog.findViewById(R.id.btnWatch);
        subject = (CheckBox) dialog.findViewById(R.id.btnSubject);
        imageNews = (CheckBox) dialog.findViewById(R.id.btnImageNews);
        expose = (CheckBox) dialog.findViewById(R.id.btnBaoGuang);

        firstPageBox.setClickable(false);
        importantNews.setOnClickListener(this);
        wind.setOnClickListener(this);
        check.setOnClickListener(this);
        watch.setOnClickListener(this);
        subject.setOnClickListener(this);
        imageNews.setOnClickListener(this);
        expose.setOnClickListener(this);

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
            if (MainActivity.isNetworkConnected(getActivity())) {
                indicatorData = task.get();//获取到数据
            }

        } catch (InterruptedException | ExecutionException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        root = inflater.inflate(R.layout.fragment_main, container, false);
        viewPager = (ViewPager) root.findViewById(R.id.pager);
        fragments = new ArrayList<>();
        firstPage = new FragmentFirstPage();//初始化栏目数组为其添加内容首先将首页固定
        adapter = new FragmentViewPagerAdapter(getChildFragmentManager(),
                fragments);
        ArrayList<String> names = new ArrayList<>();
        fragments.add(firstPage);//
        if (indicatorData != null) {
            names.add(FIRST);
            for (int i = 1; i < indicatorData.getData().getCate().size(); i++) {
                String name = indicatorData.getData().getCate().get(i)
                        .getName();
                String link = indicatorData.getData().getCate().get(i)
                        .getCate_link();
                if (name.equals(IMPORTANT)) {
                    importantLink = link;
//                    importantNews.setChecked(true);
//                    importantNews.setTextColor(getResources().getColor(R.color.dialog));
                } else if (name.equals(WIND)) {
                    windLink = link;
//                    wind.setChecked(true);
//                    wind.setTextColor(getResources().getColor(R.color.dialog));
                } else if (name.equals(CHECK)) {
                    checkLink = link;
//                    check.setChecked(true);
//                    check.setTextColor(getResources().getColor(R.color.dialog));
                } else if (name.equals(WATCH)) {
                    watchLink = link;
//                    watch.setChecked(true);
//                    watch.setTextColor(getResources().getColor(R.color.dialog));
                } else if (name.equals(IMAGE)) {
                    imageLink = link;
//                    imageNews.setChecked(true);
//                    imageNews.setTextColor(getResources().getColor(R.color.dialog));
                } else if (name.equals(SUBJECT)) {
                    subjectLink = link;
//                    subject.setChecked(true);
//                    subject.setTextColor(getResources().getColor(R.color.dialog));
                } else if (name.equals(EXPOSE)) {
                    exposeLink = link;
//                    expose.setChecked(true);
//                    expose.setTextColor(getResources().getColor(R.color.dialog));
                }
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("link", link);//获取到各个栏目的列表数据的数据接口以及栏目的名字并且将名字传给导航栏
                FragmentAll all = new FragmentAll();// 动态添加栏目
                all.setArguments(bundle);
                fragments.add(all);
                names.add(indicatorData.getData().getCate().get(i).getName());
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
                if (flag && viewPager.getCurrentItem() == selectedFragments.size() - 1) {
                    slidingMenu2
                            .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
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
                    } else if (vp.getCurrentItem() != 0 && viewpagerIsTouched) {
                        slidingMenu1
                                .setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    } else if (vp.getCurrentItem() == 0
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

    FragmentAll importantFragment = new FragmentAll();
    FragmentAll windFragment = new FragmentAll();
    FragmentAll watchFragment = new FragmentAll();
    FragmentAll checkFragment = new FragmentAll();
    FragmentAll imageFragment = new FragmentAll();
    FragmentAll subjectFragment = new FragmentAll();
    FragmentAll exposeFragment = new FragmentAll();
    ArrayList<String> selectedTitle = new ArrayList<>();

    @Override
    public void onClick(View v) {


        Bundle bundle = new Bundle();
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
                viewPager.removeAllViews();
                adapter.setFragments(selectedFragments);
                adapter.setTitles(selectedTitle);
                adapter.notifyDataSetChanged();
                indicator.setViewPager(viewPager);
                indicator.notifyDataSetChanged();
                dialog.cancel();
                flag = true;
                break;
            case R.id.btnImportantNews:
                bundle.clear();
                bundle.putString("name", IMPORTANT);
                bundle.putString("link", importantLink);
                if (importantNews.isChecked()) {
                    importantNews.setTextColor(getActivity().getResources().getColor(R.color.dialog));
                    if (!selectedFragments.contains(importantFragment)) {
                        if (importantFragment.getArguments() == null) {
                            importantFragment.setArguments(bundle);
                        }
                        selectedFragments.add(importantFragment);
                        selectedTitle.add(IMPORTANT);
                    }
                } else {
                    importantNews.setTextColor(getActivity().getResources().getColor(R.color.black));
                    if (selectedFragments.contains(importantFragment)) {
                        selectedFragments.remove(importantFragment);
                        selectedTitle.remove(IMPORTANT);
                    }
                }
                Log.e("size", String.valueOf(selectedFragments.size()));
                Log.e("~~", String.valueOf(selectedTitle.size()));
                break;
            case R.id.btnWind:
                bundle.clear();
                bundle.putString("name", WIND);
                bundle.putString("link", windLink);
                if (wind.isChecked()) {
                    wind.setTextColor(getActivity().getResources().getColor(R.color.dialog));
                    if (!selectedFragments.contains(windFragment)) {
                        if (windFragment.getArguments() == null) {

                            windFragment.setArguments(bundle);
                        }
                        selectedFragments.add(windFragment);
                        selectedTitle.add(WIND);
                    }
                } else {
                    wind.setTextColor(getActivity().getResources().getColor(R.color.black));
                    if (selectedFragments.contains(windFragment)) {
                        selectedFragments.remove(windFragment);
                        selectedTitle.remove(WIND);
                    }
                }
                Log.e("size", String.valueOf(selectedFragments.size()));
                Log.e("~~", String.valueOf(selectedTitle.size()));
                break;
            case R.id.btnWatch:
                bundle.clear();
                bundle.putString("name", WATCH);
                bundle.putString("link", watchLink);
                if (watch.isChecked()) {
                    watch.setTextColor(getActivity().getResources().getColor(R.color.dialog));
                    if (!selectedFragments.contains(watchFragment)) {
                        if (watchFragment.getArguments() == null) {
                            watchFragment.setArguments(bundle);
                        }
                        selectedFragments.add(watchFragment);
                        selectedTitle.add(WATCH);
                    }
                } else {
                    watch.setTextColor(getActivity().getResources().getColor(R.color.black));
                    if (selectedFragments.contains(watchFragment)) {
                        selectedFragments.remove(watchFragment);
                        selectedTitle.remove(WATCH);
                    }
                }
                Log.e("size", String.valueOf(selectedFragments.size()));
                Log.e("~~", String.valueOf(selectedTitle.size()));
                break;
            case R.id.btnCheck:
                bundle.clear();
                bundle.putString("name", CHECK);
                bundle.putString("link", checkLink);
                if (check.isChecked()) {
                    check.setTextColor(getActivity().getResources().getColor(R.color.dialog));
                    if (!selectedFragments.contains(checkFragment)) {
                        if (checkFragment.getArguments() == null) {
                            checkFragment.setArguments(bundle);
                        }
                        selectedFragments.add(checkFragment);
                        selectedTitle.add(CHECK);
                    }
                } else {
                    check.setTextColor(getActivity().getResources().getColor(R.color.black));
                    if (selectedFragments.contains(checkFragment)) {
                        selectedFragments.remove(checkFragment);
                        selectedTitle.remove(CHECK);
                    }
                }
                Log.e("size", String.valueOf(selectedFragments.size()));
                Log.e("~~", String.valueOf(selectedTitle.size()));
                break;
            case R.id.btnImageNews:
                bundle.clear();
                bundle.putString("name", IMAGE);
                bundle.putString("link", imageLink);
                if (imageNews.isChecked()) {
                    imageNews.setTextColor(getActivity().getResources().getColor(R.color.dialog));
                    if (!selectedFragments.contains(imageFragment)) {
                        if (imageFragment.getArguments() == null) {
                            imageFragment.setArguments(bundle);
                        }
                        selectedFragments.add(imageFragment);
                        selectedTitle.add(IMAGE);
                    }
                } else {
                    imageNews.setTextColor(getActivity().getResources().getColor(R.color.black));
                    if (selectedFragments.contains(imageFragment)) {
                        selectedFragments.remove(imageFragment);
                        selectedTitle.remove(IMAGE);
                    }
                }
                Log.e("size", String.valueOf(selectedFragments.size()));
                Log.e("~~", String.valueOf(selectedTitle.size()));
                break;
            case R.id.btnSubject:
                bundle.clear();
                bundle.putString("name", SUBJECT);
                bundle.putString("link", subjectLink);
                if (subject.isChecked()) {
                    subject.setTextColor(getActivity().getResources().getColor(R.color.dialog));
                    if (!selectedFragments.contains(subjectFragment)) {
                        if (subjectFragment.getArguments() == null) {
                            subjectFragment.setArguments(bundle);
                        }
                        selectedFragments.add(subjectFragment);
                        selectedTitle.add(SUBJECT);
                    }
                } else {
                    subject.setTextColor(getActivity().getResources().getColor(R.color.black));
                    if (selectedFragments.contains(subjectFragment)) {
                        selectedFragments.remove(subjectFragment);
                        selectedTitle.remove(SUBJECT);
                    }
                }
                Log.e("size", String.valueOf(selectedFragments.size()));
                Log.e("~~", String.valueOf(selectedTitle.size()));
                break;
            case R.id.btnBaoGuang:
                bundle.clear();
                bundle.putString("name", EXPOSE);
                bundle.putString("link", exposeLink);
                if (expose.isChecked()) {
                    expose.setTextColor(getActivity().getResources().getColor(R.color.dialog));
                    if (!selectedFragments.contains(exposeFragment)) {
                        if (exposeFragment.getArguments() == null) {
                            exposeFragment.setArguments(bundle);
                        }
                        selectedFragments.add(exposeFragment);
                        selectedTitle.add(EXPOSE);
                    }
                } else {
                    expose.setTextColor(getActivity().getResources().getColor(R.color.black));
                    if (selectedFragments.contains(exposeFragment)) {
                        selectedFragments.remove(exposeFragment);
                        selectedTitle.remove(EXPOSE);
                    }
                }
                Log.e("size", String.valueOf(selectedFragments.size()));
                Log.e("~~", String.valueOf(selectedTitle.size()));
                break;
            default:
                break;
        }
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
            database.update("listData", values, "name = ?", new String[]{TITLE});
        } else {
            database.insert("listData", null, values);
        }
        cursor.close();
        database.close();
        return loader.getJSONDate(JSON);

    }

}
