package com.example.demo.news.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.demo.news.databasehelper.DataBaseHelper;
import com.example.demo.news.databeans.ContentEntity;
import com.example.demo.news.utils.Constants;
import com.example.demo.news.utils.NetworkRequest;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import net.xinhuamm.d0403.R;

import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.Date;


public class NewsDetailsActivity extends Activity implements OnClickListener {
    //新闻详情内容
    final UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");//获取到友盟分享控制器
    private String contentId;//详情内容的id
    private CheckBox collection;//收藏按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_PROGRESS);// 让进度条显示在标题栏上
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        //从Intent 获取到详情的id
        contentId = getIntent().getExtras().getString("content_id");
        //详情内容的link构造
        Log.e("", contentId);
        final StringBuilder builder = new StringBuilder();
        builder.append(Constants.CONTENT_URL);
        builder.append(contentId);
        overridePendingTransition(0, 0);

        if (NetworkRequest.isNetworkConnected(this)) {
            NetworkRequest.get(builder.toString(), new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    parseJson(responseString);
                }
            });
        }
        initView();
        initCollectedState();

    }

    private void parseJson(String response) {
        ContentEntity data = new Gson().fromJson(response, ContentEntity.class);
        initShareOptions(data.getData().getTitle(), data.getData().getShare_link(), data.getData().getShare_image());
    }

    private void initView() {
        final ProgressBar pb;
        final TextView tvLoad;
        pb = (ProgressBar) findViewById(R.id.pb);
        tvLoad = (TextView) findViewById(R.id.tvLoad);
        WebView webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //详情内容
        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pb.setVisibility(View.VISIBLE);
                tvLoad.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pb.setVisibility(View.GONE);
                tvLoad.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        };
        webView.setWebViewClient(client);
        webView.loadUrl(getIntent().getExtras().getString("link"));
        //初始化各个按钮
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnShare).setOnClickListener(this);
        collection = (CheckBox) findViewById(R.id.btnCollect);
        collection.setOnClickListener(this);

        //获取到本地数据库判断该条内容是否已经被收藏从而改变收藏按钮的状态
    }

    private void initCollectedState() {
        //获取到本地数据库判断该条内容是否已经被收藏从而改变收藏按钮的状态
        DataBaseHelper db = new DataBaseHelper(this);
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Cursor c = dbRead.query("id", null, null, null, null, null, null);
        while (c.moveToNext()) {
            int contentID = c.getInt(c.getColumnIndex("contentId"));
            String time = c.getString(c.getColumnIndex("time"));
            System.out.println(time);
            if (String.valueOf(contentID).equals(contentId)) {
                collection.setChecked(true);
            }
        }
        c.close();
        dbRead.close();

    }

    private void initShareOptions(String title, String shareLink, String shareImage) {
        // 设置分享内容
        mController.setShareContent("test----" + title + shareLink);
        // ���÷���ͼƬ, ����2ΪͼƬ��url��ַ
        mController.setShareMedia(new UMImage(NewsDetailsActivity.this,
                shareImage));
        // �ӷ��������ȥ���㲻��Ҫ�Ĳ���
        // mController.getConfig().removePlatform( SHARE_MEDIA.RENREN,
        // SHARE_MEDIA.DOUBAN);
        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
        // ���÷���ƽ̨��˳��
        // mController.getConfig().setPlatformOrder(SHARE_MEDIA.RENREN,
        // SHARE_MEDIA.DOUBAN,
        // SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA);
        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.SINA, SHARE_MEDIA.EMAIL, SHARE_MEDIA.SMS);
        // ���΢��ƽ̨
        UMWXHandler wxHandler = new UMWXHandler(NewsDetailsActivity.this, Constants.APPID,
                Constants.APPSECRET);
        wxHandler.addToSocialSDK();
        // ֧��΢������Ȧ
        UMWXHandler wxCircleHandler = new UMWXHandler(NewsDetailsActivity.this,
                Constants.APPID, Constants.APPSECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        // ���email
        EmailHandler emailHandler = new EmailHandler();
        emailHandler.addToSocialSDK();
        // ��Ӷ���
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();
        // ����qq����
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(
                NewsDetailsActivity.this, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.addToSocialSDK();
        // qq�ռ����
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
                NewsDetailsActivity.this, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** ʹ��SSO��Ȩ����������´��� */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
                requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:

                this.finish();
            case R.id.btnShare:
                mController.openShare(NewsDetailsActivity.this, false);
                // �����������
                break;
            case R.id.btnCollect:
                doSql();
                break;

            default:
                break;
        }
    }

    private void doSql() {
        //更新收藏内容
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd    HH:mm:ss     ");
        Date curDate = new Date(System.currentTimeMillis());
        //获取到收藏被按下的时间
        String str = formatter.format(curDate);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        SQLiteDatabase dbWriteDatabase = dataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cc = dbWriteDatabase.query("id", null, null, null, null, null,
                null);
        values.put("contentId", contentId);
        values.put("time", str);
        if (collection.isChecked()) {
            dbWriteDatabase.insert("id", null, values);
        } else {
            while (cc.moveToNext()) {
                int contentID = cc.getInt(cc.getColumnIndex("contentId"));
                if (String.valueOf(contentID).equals(contentId)) {
                    String sql = "delete from id where contentId  = "
                            + contentId;
                    dbWriteDatabase.execSQL(sql);
                }
            }
        }
        cc.close();
        dbWriteDatabase.close();
    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }

}
