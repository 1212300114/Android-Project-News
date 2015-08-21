package com.example.demo.news.activity;

import net.xinhuamm.d0403.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {
	// 程序启动界面通过一个延时启动主activity来实现效果，可以为他添加一些动画效果。
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent main = new Intent(Splash.this, MainActivity.class);
				startActivity(main);
				finish();
			}
		}, 3000);
	}

}
