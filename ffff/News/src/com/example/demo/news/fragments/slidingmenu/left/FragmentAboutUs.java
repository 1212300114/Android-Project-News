package com.example.demo.news.fragments.slidingmenu.left;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.demo.news.activity.MainActivity;
import com.example.demo.news.utils.Constants;
import com.example.demo.news.utils.NetworkRequest;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import net.xinhuamm.d0403.R;

public class FragmentAboutUs extends Fragment implements OnClickListener {
    //关于我们栏的内容 就是载一个web
    private TextView tvLoad;
    private ProgressBar pb;
    private SlidingMenu slidingMenu;
    private View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        slidingMenu = activity.getSlidingMenu1();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_about_us, container, false);
        initView();
        return root;
    }

    private void initView() {
        pb = (ProgressBar) root.findViewById(R.id.pb);
        tvLoad = (TextView) root.findViewById(R.id.tvLoad);
        root.findViewById(R.id.btnShowLeft).setOnClickListener(this);
        root.findViewById(R.id.btnShowRight).setOnClickListener(this);
        WebView webView = (WebView) root.findViewById(R.id.wb);
        if (NetworkRequest.isNetworkConnected(getActivity())) {
            webView.loadUrl(Constants.ABURL);
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
                    view.loadUrl(url);
                    return true;
                }
            });
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowLeft:
                slidingMenu.showMenu();
                break;
            case R.id.btnShowRight:
                slidingMenu.showSecondaryMenu();
                break;
            default:
                break;
        }
    }

}
