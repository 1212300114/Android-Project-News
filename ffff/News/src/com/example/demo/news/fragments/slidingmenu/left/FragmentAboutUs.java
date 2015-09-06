package com.example.demo.news.fragments.slidingmenu.left;

import net.xinhuamm.d0403.R;

import com.example.demo.news.activity.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FragmentAboutUs extends Fragment implements OnClickListener {

    private SlidingMenu slidingMenu1;
    private SlidingMenu slidingMenu2;
    private ImageButton showLeft;
    private ImageButton showRight;
    private TextView tvLoad;
    private ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root;
        root = inflater.inflate(R.layout.fragment_about_us, container, false);
        pb = (ProgressBar) root.findViewById(R.id.pb);
        tvLoad = (TextView) root.findViewById(R.id.tvLoad);
        slidingMenu1 = ((MainActivity) getActivity()).getSlidingMenu1();
        slidingMenu2 = ((MainActivity) getActivity()).getSlidingMenu2();
        slidingMenu1.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        showLeft = (ImageButton) root.findViewById(R.id.btnShowLeft);
        showLeft.setOnClickListener(this);
        showRight = (ImageButton) root.findViewById(R.id.btnShowRight);
        showRight.setOnClickListener(this);
        WebView webView = (WebView) root.findViewById(R.id.wb);
        if (MainActivity.isNetworkConnected(getActivity())) {
            webView.loadUrl("http://220.163.128.41/mpage-12.html");
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setBlockNetworkImage(false);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    pb.setVisibility(View.VISIBLE);
                    tvLoad.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    pb.setVisibility(View.GONE);
                    tvLoad.setVisibility(View.GONE);
                }


                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // ��д�˷������������ҳ��������ӻ����ڵ�ǰ��webview����ת��������������Ǳ�
                    view.loadUrl(url);
                    return true;
                }
            });
        }
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
