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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.demo.news.activity.MainActivity;
import com.example.demo.news.databeans.FragmentMOEntity;
import com.example.demo.news.utils.Constants;
import com.example.demo.news.utils.NetworkRequest;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.TextHttpResponseHandler;

import net.xinhuamm.d0403.R;

import org.apache.http.Header;


public class FragmentMessageOpen extends Fragment implements OnClickListener,
        OnCheckedChangeListener {
    //信息公开栏的页面
    private SlidingMenu slidingMenu1;
    private RadioButton button1, button2, button3, button4;//这个暂时是死的吧╮(╯▽╰)╭
    private ProgressBar progressBar;
    private TextView tvLoad;
    private WebView webView;
    private View root;
    private FragmentMOEntity entity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        slidingMenu1 = ((MainActivity) getActivity()).getSlidingMenu();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_message_open, container,
                false);

        initView();
        if (NetworkRequest.isNetworkConnected(getActivity())) {
            NetworkRequest.get(Constants.MOURL, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    parseJson(responseString);
                }
            });
        }
        return root;

    }

    private void parseJson(String response) {
        entity = new Gson().fromJson(response, FragmentMOEntity.class);

        button1.setText(entity.getData().getCate().get(0).getName());
        button2.setText(entity.getData().getCate().get(1).getName());
        button3.setText(entity.getData().getCate().get(2).getName());
        button4.setText(entity.getData().getCate().get(3).getName());
        webView.loadUrl(entity.getData().getCate().get(0).getPage_link());
    }

    private void initView() {
        ImageButton showLeft = (ImageButton) root.findViewById(R.id.btnShowLeft);
        showLeft.setOnClickListener(this);
        ImageButton showRight = (ImageButton) root.findViewById(R.id.btnShowRight);
        showRight.setOnClickListener(this);
        RadioGroup group = (RadioGroup) root.findViewById(R.id.radioGroup1);
        group.setOnCheckedChangeListener(this);

        button1 = (RadioButton) root.findViewById(R.id.radio0);
        button2 = (RadioButton) root.findViewById(R.id.radio1);
        button3 = (RadioButton) root.findViewById(R.id.radio2);
        button4 = (RadioButton) root.findViewById(R.id.radio3);
        tvLoad = (TextView) root.findViewById(R.id.tvLoad);
        progressBar = (ProgressBar) root.findViewById(R.id.pb);

        webView = (WebView) root.findViewById(R.id.webView1);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                tvLoad.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                tvLoad.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowLeft:
                slidingMenu1.toggle();
                break;
            case R.id.btnShowRight:
                break;

            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //改变RadioGroup的状态
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
                webView.loadUrl(entity.getData().getCate().get(0).getPage_link());
                break;

            case R.id.radio1:
                button2.setChecked(true);
                button2.setTextColor(getActivity().getResources().getColor(
                        R.color.white));
                webView.loadUrl(entity.getData().getCate().get(1).getPage_link());
                break;
            case R.id.radio2:
                button3.setChecked(true);
                button3.setTextColor(getActivity().getResources().getColor(
                        R.color.white));
                webView.loadUrl(entity.getData().getCate().get(2).getPage_link());
                break;
            case R.id.radio3:
                button4.setChecked(true);
                button4.setTextColor(getActivity().getResources().getColor(
                        R.color.white));
                webView.loadUrl(entity.getData().getCate().get(3).getPage_link());
                break;

            default:
                break;
        }
    }

}
