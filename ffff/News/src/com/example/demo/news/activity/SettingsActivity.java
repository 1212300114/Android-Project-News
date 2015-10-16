package com.example.demo.news.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.demo.news.fragments.slidingmenu.right.FragmentSettingsMain;

import net.xinhuamm.d0403.R;

public class SettingsActivity extends FragmentActivity {
    //设置页面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_settings);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FragmentSettingsMain()).commit();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}
