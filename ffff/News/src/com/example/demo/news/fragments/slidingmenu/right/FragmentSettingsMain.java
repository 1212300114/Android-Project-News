package com.example.demo.news.fragments.slidingmenu.right;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import net.xinhuamm.d0403.R;

public class FragmentSettingsMain extends Fragment {
	Dialog dialog;
	Dialog dialog2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root;
		root = inflater.inflate(R.layout.fragment_settings, container, false);
		ImageButton btnBack = (ImageButton) root.findViewById(R.id.btnBack);
		dialog = new Dialog(getActivity(), R.style.MyDialog1);
		dialog2 = new Dialog(getActivity(), R.style.MyDialog1);
		dialog2.setContentView(R.layout.check_update_dialog_style);
		dialog.setContentView(R.layout.settings_dialog_style);
		dialog.findViewById(R.id.dialogNo).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.cancel();
					}
				});
		dialog.findViewById(R.id.dialogYes).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.cancel();
					}
				});
		dialog2.findViewById(R.id.dialogNNo).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog2.cancel();
					}
				});
		dialog2.findViewById(R.id.dialogYYes).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog2.cancel();
					}
				});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});

		RelativeLayout checkUpdate = (RelativeLayout) root
				.findViewById(R.id.checkUpdate);
		checkUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog2.show();
			}
		});
		root.findViewById(R.id.clear).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});
		root.findViewById(R.id.reflect).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						getFragmentManager().beginTransaction()
								.replace(R.id.container, new FragmentReflect())
								.addToBackStack(null).commit();
					}
				});

		return root;
	}
}
