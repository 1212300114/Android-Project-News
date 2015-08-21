package com.example.demo.news.activity;

import net.xinhuamm.d0403.R;

import com.example.demo.news.adapters.SubjectListAdapter;
import com.example.demo.news.xlistviewsource.XListView;
import com.example.demo.news.xlistviewsource.XListView.IXListViewListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class SubjectDetails extends Activity implements IXListViewListener,
		OnClickListener {

	private XListView lv;
	private ProgressBar pb;
	private SubjectListAdapter adapter;
	private Handler mHandler = new Handler();
	private ImageButton back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subject_details);
		lv = (XListView) findViewById(R.id.lvFirstPage);
		adapter = new SubjectListAdapter(this);
		lv.setAdapter(adapter);
		lv.setPullLoadEnable(true);
		lv.setXListViewListener(this);
		lv.setPullRefreshEnable(true);
		lv.setVisibility(View.GONE);
		back = (ImageButton) findViewById(R.id.btnBack);
		back.setOnClickListener(this);
		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this)
				.inflate(R.layout.subject_header, null);
		lv.addHeaderView(layout);
		onRefresh();
	}

	@Override
	public void onRefresh() {

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				pb = (ProgressBar) findViewById(R.id.pb);
				lv.setVisibility(View.VISIBLE);
				pb.setVisibility(View.INVISIBLE);
				adapter.setCount(10, true);
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);

	}

	private void onLoad() {
		lv.stopRefresh();
		lv.stopLoadMore();
		lv.setRefreshTime("刚刚");
	}

	@Override
	public void onLoadMore() {

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				onLoad();

				adapter.setCount(10, false);
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();

			break;

		default:
			break;
		}
	}

}
