package com.example.demo.news.fragments.slidingmenu.left;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import net.xinhuamm.d0403.R;

import com.example.demo.news.activity.DynamicDetail;
import com.example.demo.news.activity.MainActivity;
import com.example.demo.news.databeans.dynamic.DynamicData;
import com.example.demo.news.dataloaders.DynamicLoader;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class FragmentDynamic extends Fragment implements OnClickListener {

	SlidingMenu slidingMenu1;
	SlidingMenu slidingMenu2;
	private DynamicLoader loader;
	AsyncTask<String, Void, DynamicData> task;
	private DynamicData data;
	private Button km;
	private Button zt;
	private Button qj;
	private Button yx;
	private Button bs;
	private Button cx;
	private Button hh;
	private Button ws;
	private Button pe;
	private Button xsbn;
	private Button dl;
	private Button dh;
	private Button lj;
	private Button nj;
	private Button dq;
	private Button lc;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root;
		root = inflater.inflate(R.layout.fragment_changes, container, false);
		((MainActivity) getActivity()).getSlidingMenu2().setTouchModeAbove(
				SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu1 = ((MainActivity) getActivity()).getSlidingMenu1();
		slidingMenu2 = ((MainActivity) getActivity()).getSlidingMenu2();
		ImageButton showLeft;
		ImageButton showRight;
		showLeft = (ImageButton) root.findViewById(R.id.btnShowLeft);
		showLeft.setOnClickListener(this);
		showRight = (ImageButton) root.findViewById(R.id.btnShowRight);
		showRight.setOnClickListener(this);
		km = (Button) root.findViewById(R.id.btnKM);
		zt = (Button) root.findViewById(R.id.btnZT);
		qj = (Button) root.findViewById(R.id.btnQJ);
		yx = (Button) root.findViewById(R.id.btnYX);
		bs = (Button) root.findViewById(R.id.btnBS);
		cx = (Button) root.findViewById(R.id.btnCX);
		hh = (Button) root.findViewById(R.id.btnHH);
		ws = (Button) root.findViewById(R.id.btnWS);
		pe = (Button) root.findViewById(R.id.btnPE);
		xsbn = (Button) root.findViewById(R.id.btnXSPN);
		dl = (Button) root.findViewById(R.id.btnDL);
		dh = (Button) root.findViewById(R.id.btnDH);
		lj = (Button) root.findViewById(R.id.btnLJ);
		nj = (Button) root.findViewById(R.id.btnNJ);
		dq = (Button) root.findViewById(R.id.btnDQ);
		lc = (Button) root.findViewById(R.id.btnLC);
		km.setOnClickListener(this);
		zt.setOnClickListener(this);
		qj.setOnClickListener(this);
		yx.setOnClickListener(this);
		bs.setOnClickListener(this);
		cx.setOnClickListener(this);
		hh.setOnClickListener(this);
		ws.setOnClickListener(this);
		pe.setOnClickListener(this);
		xsbn.setOnClickListener(this);
		dl.setOnClickListener(this);
		dh.setOnClickListener(this);
		lj.setOnClickListener(this);
		nj.setOnClickListener(this);
		dq.setOnClickListener(this);
		lc.setOnClickListener(this);
		loader = new DynamicLoader();

		task = new AsyncTask<String, Void, DynamicData>() {

			@Override
			protected DynamicData doInBackground(String... params) {
				try {
					data = getResource();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return data;
			}
		};
		task.execute();
		try {
			data = task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("data = " + new Gson().toJson(data));
		return root;
	}

	private DynamicData getResource() throws IOException {
		String JSON = loader
				.readURL("http://api.jjjc.yn.gov.cn//jwapp//?service=Dynamic.index");
		DynamicData data = loader.getJSONDate(JSON);
		return data;

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getActivity(), DynamicDetail.class);
		switch (v.getId()) {
		case R.id.btnShowLeft:
			slidingMenu1.toggle();
			break;
		case R.id.btnShowRight:
			slidingMenu2.toggle();
			break;
		case R.id.btnKM:
			intent.putExtra("link", data.getData().getCate().get(0)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnZT:
			intent.putExtra("link", data.getData().getCate().get(1)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnQJ:
			intent.putExtra("link", data.getData().getCate().get(2)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnYX:
			intent.putExtra("link", data.getData().getCate().get(3)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnBS:
			intent.putExtra("link", data.getData().getCate().get(4)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnCX:
			intent.putExtra("link", data.getData().getCate().get(5)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnHH:
			intent.putExtra("link", data.getData().getCate().get(6)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnWS:
			intent.putExtra("link", data.getData().getCate().get(7)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnPE:
			intent.putExtra("link", data.getData().getCate().get(8)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnXSPN:
			intent.putExtra("link", data.getData().getCate().get(9)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnDL:
			intent.putExtra("link", data.getData().getCate().get(10)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnDH:
			intent.putExtra("link", data.getData().getCate().get(11)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnLJ:
			intent.putExtra("link", data.getData().getCate().get(12)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnNJ:
			intent.putExtra("link", data.getData().getCate().get(13)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnDQ:
			intent.putExtra("link", data.getData().getCate().get(14)
					.getCate_link());
			startActivity(intent);
			break;
		case R.id.btnLC:
			intent.putExtra("link", data.getData().getCate().get(15)
					.getCate_link());
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
