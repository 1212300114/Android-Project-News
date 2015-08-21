package com.example.demo.news.fragments.slidingmenu.left;

import net.xinhuamm.d0403.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class FragmentWebsiteStatement extends Fragment {
	private ImageButton back;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root;
		root = inflater.inflate(R.layout.fragment_statement, container, false);
		back = (ImageButton) root.findViewById(R.id.btnBack);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getFragmentManager().popBackStack();
			}
		});
		return root;

	}
}
