package com.wsj.sample.pay;

import com.wsj.pay.api.APWSJPayAPI;
import com.tencent.midas.sample.R;

import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.app.Activity;

public class H5PaySampleActivity extends Activity {
	private WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		mWebView = (WebView) findViewById(R.id.webView);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(webviewclient);
		mWebView.setWebChromeClient(webchromeclient);
		mWebView.loadUrl("file:///android_asset/click.html");
	}
	WebViewClient webviewclient = new WebViewClient(){
		@Override
		public void onPageFinished(WebView view, String url){
			super.onPageFinished(view, url);
			//1.初始化
	        APWSJPayAPI.h5PayInit(H5PaySampleActivity.this, view);
		}
	};
	WebChromeClient webchromeclient = new WebChromeClient() {
		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result){
			//2.调用原生支付转发
			if (APWSJPayAPI.h5PayHook(H5PaySampleActivity.this, view, url, message, result) == 0){
				result.cancel();
				return true;
			}
			//如果游戏的webview要接受JS的alert方法，直接return super的方法
			//如果不接受，直接return true
			return super.onJsAlert(view, url, message, result);
		}
	};
}
