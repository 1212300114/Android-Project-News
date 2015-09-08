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
import com.google.gson.Gson;
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
    private AsyncTask<String, Void, IndicatorData> taskForSelecting;// 加载用户自选择导航栏的task
    private AsyncTask<String, Void, IndicatorData> taskForUser;// 加载用户自选择导航栏的task
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
    private static final String FIRST = "首页";
    private static final String IMPORTANT = "要闻";
    private static final String WIND = "党风";
    private static final String CHECK = "审查";
    private static final String WATCH = "巡视";
    private static final String IMAGE = "图闻";
    private static final String SUBJECT = "专题";
    private static final String EXPOSE = "曝光";
    private StringBuilder builder = new StringBuilder();
    boolean flag1 = false;
    private SharedPreferences.Editor editor;
    private ArrayList<Fragment> fragmentStored;
    private ArrayList<String> titleStored;
    private StringBuilder stringBuilder = new StringBuilder();
    private String[] idArray = new String[10];
    private String l;
    private Context context;
    private ArrayList<String> names;
    private boolean indicatorIsTouched = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listDataHelper = new ListDataHelper(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        SQLiteDatabase dbRead = listDataHelper.getReadableDatabase();
        context = getActivity();
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
        ArrayList<Fragment> selectedFragments = new ArrayList<>();
        firstPage = new FragmentFirstPage();//初始化栏目数组为其添加内容首先将首页固定
        fragmentStored = new ArrayList<>();
        fragmentStored.add(firstPage);
        titleStored = new ArrayList<>();
        titleStored.add(FIRST);
        selectedTitle.add(FIRST);
        fragments = new ArrayList<>();
        fragments.clear();
        names = new ArrayList<>();
        names.clear();


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

        l = "http://api.jjjc.yn.gov.cn/jwapp/?service=Category.index&class_id=";
        ArrayList<String> names = new ArrayList<>();
        if (indicatorData != null) {

            for (int i = 0; i < indicatorData.getData().getCate().size(); i++) {
                String name = indicatorData.getData().getCate().get(i)
                        .getName();
                String link = indicatorData.getData().getCate().get(i)
                        .getCate_link();
                Integer id = indicatorData.getData().getCate().get(i).getClass_id();
                idArray[i] = String.valueOf(id);
                builder.append(name);
                builder.append(",");
                if (id == null) {
                    stringBuilder.append("");
                } else {
                    stringBuilder.append(id);
                    stringBuilder.append(",");
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

        String str = sharedPreferences.getString("id", "然并卵");
        Log.e("str", str);
        if (!str.equals("然并卵")) {
            final String selectedLink = l + str;
            l = selectedLink;
            if (MainActivity.isNetworkConnected(context)) {
                taskForUser = new AsyncTask<String, Void, IndicatorData>() {
                    @Override
                    protected IndicatorData doInBackground(String... params) {
                        String json;
                        IndicatorData data = null;
                        try {
                            json = loader.readURL(selectedLink);
                            data = loader.getJSONDate(json);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        return data;
                    }
                };
                taskForUser.execute();
            }
            IndicatorData data = null;
            try {
                data = taskForUser.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            names.clear();
            fragments.clear();
            assert data != null;
            for (int i = 0; i < data.getData().getCate().size(); i++) {
                String name = data.getData().getCate().get(i).getName();
                String link = data.getData().getCate().get(i).getCate_link();
                names.add(name);
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("link", link);
                FragmentAll all = new FragmentAll();
                all.setArguments(bundle);
                fragments.add(all);
            }


        } else {

            l = l + stringBuilder.toString();
        }
//        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
//        String id = sharedPreferences.getString("id", "hehe");
//        idArray = id.split(",");
        Log.e("", l);
        for (int i = 0; i < names.size(); i++) {
            switch (names.get(i)) {
                case IMPORTANT:
                    importantNews.setChecked(true);
                    importantNews.setTextColor(getResources().getColor(R.color.dialog));
                    break;
                case WIND:
                    wind.setChecked(true);
                    wind.setTextColor(getResources().getColor(R.color.dialog));
                    break;
                case CHECK:
                    check.setChecked(true);
                    check.setTextColor(getResources().getColor(R.color.dialog));
                    break;
                case WATCH:
                    watch.setChecked(true);
                    watch.setTextColor(getResources().getColor(R.color.dialog));
                    break;
                case IMAGE:
                    imageNews.setChecked(true);
                    imageNews.setTextColor(getResources().getColor(R.color.dialog));
                    break;
                case SUBJECT:
                    subject.setChecked(true);
                    subject.setTextColor(getResources().getColor(R.color.dialog));
                    break;
                case EXPOSE:
                    expose.setChecked(true);
                    expose.setTextColor(getResources().getColor(R.color.dialog));
                    break;
            }

        }
        adapter = new FragmentViewPagerAdapter(getChildFragmentManager(),
                fragments);
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
                ViewPager vp = ((FragmentAll) fragments.get(0)).getViewPager();
                layout = ((FragmentAll) fragments.get(0)).getLayout();
                LinearLayout linearLayout = null;
                ViewPager viewPager1 = null;
                int viewPageSize = 0;
                if (((FragmentAll) fragments.get(fragments.size() - 1)).isViewPagerCreated()) {
                    linearLayout = ((FragmentAll) fragments.get(fragments.size() - 1)).getLayout();
                    viewPager1 = ((FragmentAll) fragments.get(fragments.size() - 1)).getViewPager();
                    viewPageSize = ((FragmentAll) fragments.get(fragments.size() - 1)).getViewPagerSize();
                }

                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    indicatorIsTouched = isInChangeImageZone(indicator, (int) ev.getX(),
                            (int) ev.getY());
                    if (layout != null) {

                        viewpagerIsTouched = isInChangeImageZone(layout,
                                (int) ev.getX(), (int) ev.getY());
                    }
                    if (linearLayout != null) {
                        flag1 = isInChangeImageZone(linearLayout, (int) ev.getX(), (int) ev.getY());
                    }
                }
                if (indicatorIsTouched) {
                    slidingMenu1.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                } else {
                    if (viewPager.getCurrentItem() == 0 && !viewpagerIsTouched) {
                        slidingMenu1.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    } else if (viewPager.getCurrentItem() == 0 && vp.getCurrentItem() == 0) {
                        slidingMenu1.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    } else {
                        slidingMenu1.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    }
                    if (linearLayout != null) {
                        if (viewPager.getCurrentItem() == fragments.size() - 1 && !flag1) {
                            slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                        } else if (viewPager1.getCurrentItem() == viewPageSize - 1 && flag1) {
                            slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                        } else {
                            slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                        }
                    } else {
                        if (viewPager.getCurrentItem() == fragments.size() - 1) {
                            slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                        } else {
                            slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                        }
                    }
                }
                return true;
            }
        };
        ((MainActivity) getActivity())
                .registerMyOnTouchListener(myOnTouchListener);

        return root;
    }

    ArrayList<String> selectedTitle = new ArrayList<>();

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
                if (MainActivity.isNetworkConnected(context)) {
                    taskForSelecting = new AsyncTask<String, Void, IndicatorData>() {
                        @Override
                        protected IndicatorData doInBackground(String... params) {
                            String json;
                            IndicatorData data = null;
                            try {
                                json = loader.readURL(l);
                                data = loader.getJSONDate(json);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            return data;
                        }
                    };
                    taskForSelecting.execute();
                }
                IndicatorData data = null;
                try {
                    data = taskForSelecting.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if (data != null) {
                    names.clear();
                    fragments.clear();
                    for (int i = 0; i < data.getData().getCate().size(); i++) {
                        String name = data.getData().getCate().get(i).getName();
                        String link = data.getData().getCate().get(i).getCate_link();
                        names.add(name);
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        bundle.putString("link", link);
                        FragmentAll all = new FragmentAll();
                        all.setArguments(bundle);
                        fragments.add(all);
                    }
                }
                String s = l.substring(65);
                editor.putString("id", s);
                editor.commit();
                adapter.setFragments(fragments);
                adapter.setTitles(names);
                adapter.notifyDataSetChanged();
                indicator.notifyDataSetChanged();
                Log.e("", new Gson().toJson(data));
                Log.e("data", sharedPreferences.getString("id", "id!!!"));
                Log.e("sub", l.substring(65));
                dialog.cancel();
                break;
            case R.id.btnImportantNews:
                if (importantNews.isChecked()) {
                    importantNews.setTextColor(getActivity().getResources().getColor(R.color.dialog));
                    l = l + idArray[1] + ",";
                } else {
                    importantNews.setTextColor(getActivity().getResources().getColor(R.color.black));
                    l = l.replace(idArray[1] + ",", "");
                }
                Log.e("~~", l);
                break;
            case R.id.btnWind:
                if (wind.isChecked()) {
                    wind.setTextColor(getActivity().getResources().getColor(R.color.dialog));
                    l = l + idArray[4] + ",";
                } else {
                    wind.setTextColor(getActivity().getResources().getColor(R.color.black));
                    l = l.replace(idArray[4] + ",", "");
                }
                Log.e("~~", l);
                break;
            case R.id.btnWatch:
                if (watch.isChecked()) {
                    watch.setTextColor(getActivity().getResources().getColor(R.color.dialog));
                    l = l + idArray[5] + ",";

                } else {
                    watch.setTextColor(getActivity().getResources().getColor(R.color.black));
                    l = l.replace(idArray[5] + ",", "");
                }
                Log.e("~~", l);
                break;
            case R.id.btnCheck:
                if (check.isChecked()) {
                    check.setTextColor(getActivity().getResources().getColor(R.color.dialog));
                    l = l + idArray[2] + ",";

                } else {
                    check.setTextColor(getActivity().getResources().getColor(R.color.black));
                    l = l.replace(idArray[2] + ",", "");
                }
                Log.e("~~", l);
                break;
            case R.id.btnImageNews:
                if (imageNews.isChecked()) {
                    imageNews.setTextColor(getActivity().getResources().getColor(R.color.dialog));
                    l = l + idArray[6] + ",";
                } else {
                    imageNews.setTextColor(getActivity().getResources().getColor(R.color.black));
                    l = l.replace(idArray[6] + ",", "");
                }
                Log.e("~~", l);
                break;
            case R.id.btnSubject:
                if (subject.isChecked()) {
                    subject.setTextColor(getActivity().getResources().getColor(R.color.dialog));
                    l = l + idArray[7] + ",";

                } else {
                    subject.setTextColor(getActivity().getResources().getColor(R.color.black));
                    l = l.replace(idArray[7] + ",", "");
                }
                Log.e("~~", l);
                break;
            case R.id.btnBaoGuang:
                if (expose.isChecked()) {
                    expose.setTextColor(getActivity().getResources().getColor(R.color.dialog));
                    l = l + idArray[3] + ",";

                } else {
                    expose.setTextColor(getActivity().getResources().getColor(R.color.black));
                    l = l.replace(idArray[3] + ",", "");
                }
                Log.e("~~", l);
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
