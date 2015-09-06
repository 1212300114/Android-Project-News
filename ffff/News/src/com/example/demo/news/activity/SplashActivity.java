package com.example.demo.news.activity;

import net.xinhuamm.d0403.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	// ������������ͨ��һ����ʱ������activity��ʵ��Ч��������Ϊ�����һЩ����Ч����
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent main = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(main);
				finish();
			}
		}, 3000);
	}
}
