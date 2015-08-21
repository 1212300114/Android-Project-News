package com.example.demo.news.fragments.slidingmenu.left;

import net.xinhuamm.d0403.R;
import com.example.demo.news.activity.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class FragmentAboutUs extends Fragment implements OnClickListener {

	private SlidingMenu slidingMenu1;
	private SlidingMenu slidingMenu2;
	private ImageButton showLeft;
	private ImageButton showRight;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root;
		root = inflater.inflate(R.layout.fragment_about_us, container, false);
		slidingMenu1 = ((MainActivity) getActivity()).getSlidingMenu1();
		slidingMenu2 = ((MainActivity) getActivity()).getSlidingMenu2();
		slidingMenu1.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		showLeft = (ImageButton) root.findViewById(R.id.btnShowLeft);
		showLeft.setOnClickListener(this);
		showRight = (ImageButton) root.findViewById(R.id.btnShowRight);
		showRight.setOnClickListener(this);
		WebView webView = (WebView) root.findViewById(R.id.wb);
		webView.loadUrl("http://220.163.128.41/mpage-12.html");
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setBlockNetworkImage(false);
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});
		return root;
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

}
