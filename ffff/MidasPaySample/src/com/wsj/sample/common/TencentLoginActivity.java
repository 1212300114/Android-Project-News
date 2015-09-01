package com.wsj.sample.common;

import com.tencent.midas.sample.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TencentLoginActivity extends Activity 
{
	private Button login_btn_qq;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wsj_login);
		// QQ号登陆按钮，应用接入支付可以忽略该处的登录，只关心支付即可，该登录仅用于演示支付使用
		//请忽略Login.java LoginHelper.java和该文件，只需要关注PaySampleActivity.java和H5PaySampleActivity.java
		login_btn_qq = (Button) findViewById(R.id.wsj_btn_qq);
		initUI();
	}

	private void initUI() 
	{
		login_btn_qq.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent in = new Intent();
				in.setClass(TencentLoginActivity.this, Login.class);
				startActivity(in);
			}
		});
	}
}
