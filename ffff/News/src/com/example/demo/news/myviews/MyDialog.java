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
		this.setContentView(R.layout.column_select_diaolog_layout);
	}

	public MyDialog(Context context) {
		super(context);
		this.context = context;
		this.setContentView(R.layout.column_select_diaolog_layout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

}
