package com.example.demo.news.myviews;

import net.xinhuamm.d0403.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class MyDialog extends Dialog {
	@SuppressWarnings("unused")
	private Context context;

	public MyDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		this.setContentView(R.layout.dialog_style);
	}

	public MyDialog(Context context) {
		super(context);
		this.context = context;
		this.setContentView(R.layout.dialog_style);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

}
