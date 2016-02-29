package com.example.demo.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.demo.news.fragments.slidingmenu.left.FragmentAboutUs;
import com.example.demo.news.fragments.slidingmenu.left.FragmentDynamic;
import com.example.demo.news.fragments.slidingmenu.left.FragmentLaw;
import com.example.demo.news.fragments.slidingmenu.left.FragmentMain;
import com.example.demo.news.fragments.slidingmenu.left.FragmentMessageOpen;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import net.xinhuamm.d0403.R;

public class MainActivity extends FragmentActivity implements OnClickListener {

    private SlidingMenu slidingMenu;//侧边栏
    private FragmentMain main;// 主页
    private FragmentMessageOpen fragmentMessageOpen;//信息公开栏
    private FragmentDynamic fragmentDynamic;//州市动态栏
    private FragmentLaw fragmentLaw;//党纪法规栏
    private FragmentAboutUs fragmentAboutUs;//关于我们栏
    private long firstTime;//记录第一次点下返回键的时间


    public SlidingMenu getSlidingMenu() {
        return slidingMenu;//返回slidingmenu 用于各页按钮操作
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        //初始化左侧栏的各个fragment
        main = new FragmentMain();
        fragmentMessageOpen = new FragmentMessageOpen();
        fragmentDynamic = new FragmentDynamic();
        fragmentLaw = new FragmentLaw();
        fragmentAboutUs = new FragmentAboutUs();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, main).commit();
        }

        // 首先设置为在首页内
        overridePendingTransition(0, 0);
        initSlidingMenu();

    }
    //初始化侧边栏
    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMenu(R.layout.slidingmenu_left);
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setSecondaryMenu(R.layout.slidingmenu_right);
        // 左侧menu的按钮设置
        slidingMenu.findViewById(R.id.btnFP).setOnClickListener(
                this);
        slidingMenu.findViewById(R.id.btnMO).setOnClickListener(
                this);
        slidingMenu.findViewById(R.id.btnDY).setOnClickListener(
                this);
        slidingMenu.findViewById(R.id.btnLA).setOnClickListener(
                this);
        slidingMenu.findViewById(R.id.btnAB).setOnClickListener(
                this);
        slidingMenu.findViewById(R.id.btnSA).setOnClickListener(
                this);
        slidingMenu.findViewById(R.id.btnCO).setOnClickListener(
                this);
        slidingMenu.findViewById(R.id.btnSE).setOnClickListener(
                this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //left menu
            case R.id.btnFP:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        main).addToBackStack(null).commit();
                slidingMenu.toggle();
                break;
            case R.id.btnMO:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        fragmentMessageOpen).addToBackStack(null).commit();
                slidingMenu.toggle();
                break;
            case R.id.btnDY:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        fragmentDynamic).addToBackStack(null).commit();
                slidingMenu.toggle();
                break;
            case R.id.btnLA:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        fragmentLaw).addToBackStack(null).commit();
                slidingMenu.toggle();
                break;
            case R.id.btnAB:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        fragmentAboutUs).addToBackStack(null).commit();
                slidingMenu.toggle();
                break;
            //right menu
            case R.id.btnSA:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
            case R.id.btnCO:
                startActivity(new Intent(MainActivity.this, MyCollectionsActivity.class));
                break;
            case R.id.btnSE:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
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
