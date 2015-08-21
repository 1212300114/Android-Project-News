package com.example.demo.news.fragments.slidingmenu.left;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import net.xinhuamm.d0403.R;
import com.example.demo.news.activity.MainActivity;
import com.example.demo.news.databeans.message.MessageOpenData;
import com.example.demo.news.dataloaders.MessageOpenLoader;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class FragmentMessageOpen extends Fragment implements OnClickListener,
		OnCheckedChangeListener {
	private SlidingMenu slidingMenu1;
	private SlidingMenu slidingMenu2;
	private ImageButton showLeft;
	private ImageButton showRight;
	private RadioButton button1, button2, button3, button4;
	private RadioGroup group;
	private AsyncTask<String, Void, MessageOpenData> task;
	private MessageOpenData data;
	private MessageOpenLoader loader;
	private ArrayList<String> cateName;
	private ArrayList<String> link;

	private WebView webView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		loader = new MessageOpenLoader();
		View root = inflater.inflate(R.layout.fragment_message_open, container,
				false);
		slidingMenu1 = ((MainActivity) getActivity()).getSlidingMenu1();
		slidingMenu2 = ((MainActivity) getActivity()).getSlidingMenu2();
		slidingMenu1.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		showLeft = (ImageButton) root.findViewById(R.id.btnShowLeft);
		showLeft.setOnClickListener(this);
		showRight = (ImageButton) root.findViewById(R.id.btnShowRight);
		showRight.setOnClickListener(this);
		group = (RadioGroup) root.findViewById(R.id.radioGroup1);
		group.setOnCheckedChangeListener(this);
		button1 = (RadioButton) root.findViewById(R.id.radio0);
		button2 = (RadioButton) root.findViewById(R.id.radio1);
		button3 = (RadioButton) root.findViewById(R.id.radio2);
		button4 = (RadioButton) root.findViewById(R.id.radio3);

		task = new AsyncTask<String, Void, MessageOpenData>() {

			@Override
			protected MessageOpenData doInBackground(String... params) {
				MessageOpenData data = null;
				try {
					data = getData();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return data;
			}
		};
		task.execute();
		try {
			if (task.get() != null) {
				data = task.get();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(new Gson().toJson(data));
		cateName = new ArrayList<String>();
		link = new ArrayList<String>();
		for (int i = 0; i < data.getData().getCate().size(); i++) {
			cateName.add(data.getData().getCate().get(i).getName());
			link.add(data.getData().getCate().get(i).getPage_link());
		}
		button1.setText(cateName.get(0));
		button2.setText(cateName.get(1));
		button3.setText(cateName.get(2));
		button4.setText(cateName.get(3));

		webView = (WebView) root.findViewById(R.id.webView1);
		webView.loadUrl(link.get(0));
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});
		return root;

	}

	private MessageOpenData getData() throws IOException {

		String JSON = loader
				.readURL("http://api.jjjc.yn.gov.cn//jwapp//?service=Page.index&pid=1");
		MessageOpenData data = loader.getJSONDate(JSON);

		return data;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnShowLeft:
			slidingMenu1.toggle();
			break;
		case R.id.btnShowRight:
			slidingMenu2.toggle();
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		button1.setChecked(false);
		button2.setChecked(false);
		button3.setChecked(false);
		button4.setChecked(false);
		button1.setTextColor(getActivity().getResources().getColor(
				R.color.black));
		button2.setTextColor(getActivity().getResources().getColor(
				R.color.black));
		button3.setTextColor(getActivity().getResources().getColor(
				R.color.black));
		button4.setTextColor(getActivity().getResources().getColor(
				R.color.black));
		switch (checkedId) {
		case R.id.radio0:
			button1.setChecked(true);
			button1.setTextColor(getActivity().getResources().getColor(
					R.color.white));
			webView.loadUrl(link.get(0));
			break;

		case R.id.radio1:
			button2.setChecked(true);
			button2.setTextColor(getActivity().getResources().getColor(
					R.color.white));
			webView.loadUrl(link.get(1));
			break;
		case R.id.radio2:
			button3.setChecked(true);
			button3.setTextColor(getActivity().getResources().getColor(
					R.color.white));
			webView.loadUrl(link.get(2));
			break;
		case R.id.radio3:
			button4.setChecked(true);
			button4.setTextColor(getActivity().getResources().getColor(
					R.color.white));
			webView.loadUrl(link.get(3));
			break;

		default:
			break;
		}
	}

}
