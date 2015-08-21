package com.example.demo.news.fragments.main;

import net.xinhuamm.d0403.R;

import com.example.demo.news.activity.SubjectDetails;
import com.example.demo.news.adapters.XListViewAdapter;
import com.example.demo.news.xlistviewsource.XListView;
import com.example.demo.news.xlistviewsource.XListView.IXListViewListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentSubject extends Fragment implements IXListViewListener {
	private View root;
	private XListView lv;
	private ProgressBar pb;
	private XListViewAdapter adapter;
	private Handler mHandler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_subject, container, false);
		lv = (XListView) root.findViewById(R.id.lvFirstPage);
		adapter = new XListViewAdapter(getActivity());
		lv.setAdapter(adapter);
		lv.setXListViewListener(this);
		lv.setPullLoadEnable(true);
		lv.setPullRefreshEnable(true);
		lv.setVisibility(View.GONE);
		onRefresh();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position < 5) {
					startActivity(new Intent(getActivity(),
							SubjectDetails.class));

				}
			}
		});
		return root;
	}

	@Override
	public void onRefresh() {

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				pb = (ProgressBar) root.findViewById(R.id.pb);
				lv.setVisibility(View.VISIBLE);
				pb.setVisibility(View.INVISIBLE);
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);

	}

	private void onLoad() {
		lv.stopRefresh();
		lv.stopLoadMore();
		lv.setRefreshTime("¸Õ¸Õ");
	}

	@Override
	public void onLoadMore() {

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				onLoad();

				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

}
