package com.example.demo.news.activity;

import java.util.ArrayList;

import net.xinhuamm.d0403.R;

import com.example.demo.news.fragments.slidingmenu.left.FragmentAboutUs;
import com.example.demo.news.fragments.slidingmenu.left.FragmentDynamic;
import com.example.demo.news.fragments.slidingmenu.left.FragmentLaw;
import com.example.demo.news.fragments.slidingmenu.left.FragmentMain;
import com.example.demo.news.fragments.slidingmenu.left.FragmentMessageOpen;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
    private SlidingMenu slidingMenu1, slidingMenu2;
    private boolean isInFragmentFirstPage = true;//判断是否在首页内
    private FragmentMain main;
    private FragmentMessageOpen fragmentMessageOpen;
    private FragmentDynamic fragmentDynamic;
    private FragmentLaw fragmentLaw;
    private FragmentAboutUs fragmentAboutUs;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private long firstTime;

    public SlidingMenu getSlidingMenu1() {
        return slidingMenu1;
    }

    public SlidingMenu getSlidingMenu2() {
        return slidingMenu2;
    }

    public boolean isInFragmentFirstPage() {
        return isInFragmentFirstPage;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (!isNetworkConnected(this)) {
            editor.putBoolean("network", false);
            Toast.makeText(this, "没有网络连接!", Toast.LENGTH_LONG).show();
        } else {
            editor.putBoolean("network", true);
        }
        editor.commit();
        setContentView(R.layout.main_activity);

        main = new FragmentMain();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, main).commit();
        }
        //初始化左侧栏的各个fragment
        fragmentMessageOpen = new FragmentMessageOpen();
        fragmentDynamic = new FragmentDynamic();
        fragmentLaw = new FragmentLaw();
        fragmentAboutUs = new FragmentAboutUs();

        // 首先设置为在首页内
        isInFragmentFirstPage = true;
        overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        //初始化两侧的menu
        slidingMenu1 = new SlidingMenu(this);
        slidingMenu2 = new SlidingMenu(this);
        slidingMenu1.setMenu(R.layout.slidingmenu_left);
        slidingMenu1.setMode(SlidingMenu.LEFT);
        slidingMenu1.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu1.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        slidingMenu1.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        slidingMenu2.setMenu(R.layout.slidingmenu_right);
        slidingMenu2.setMode(SlidingMenu.RIGHT);
        slidingMenu2.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu2.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        // 左侧menu的按钮设置
        slidingMenu1.findViewById(R.id.btnFP).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, main).commit();
                        slidingMenu1.toggle();
                        slidingMenu2
                                .setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                        isInFragmentFirstPage = true;
                    }
                });
        slidingMenu1.findViewById(R.id.btnMO).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragmentMessageOpen)
                                .commit();
                        slidingMenu2
                                .setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                        slidingMenu1.toggle();
                        isInFragmentFirstPage = false;
                    }
                });
        slidingMenu1.findViewById(R.id.btnDY).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragmentDynamic)
                                .commit();
                        slidingMenu2
                                .setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                        slidingMenu1.toggle();
                        isInFragmentFirstPage = false;
                    }
                });
        slidingMenu1.findViewById(R.id.btnLA).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragmentLaw).commit();
                        slidingMenu1.toggle();
                        isInFragmentFirstPage = false;
                    }
                });
        slidingMenu1.findViewById(R.id.btnAB).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragmentAboutUs)
                                .commit();
                        slidingMenu1.toggle();
                        isInFragmentFirstPage = false;
                    }
                });
        // 右侧menu按钮的设置
        slidingMenu2.findViewById(R.id.btnSA).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this,
                                SearchActivity.class));
                    }
                });
        slidingMenu2.findViewById(R.id.btnCO).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,
                                MyCollectionsActivity.class);
                        startActivity(intent);
                    }
                });
        slidingMenu2.findViewById(R.id.btnSE).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this,
                                SettingsActivity.class));
                    }
                });

    }

    // touch接口为fragment提供使fragment 可以获取到touch事件达到menu滑动TouchMode的动态改变
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
            10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {

        super.onActivityResult(arg0, arg1, arg2);
    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (slidingMenu1.isMenuShowing()) {
            slidingMenu1.toggle();
        } else if (slidingMenu2.isMenuShowing()) {
            slidingMenu2.toggle();
        } else {

            if (secondTime - firstTime > 2000) {
                //判断两次点击按钮时间间隔 大于2秒则弹出消息
                Toast.makeText(MainActivity.this, "再次按下退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
            } else {//小于2秒则推出activity也就推出了app
                finish();
            }
        }
    }

}
