package com.example.demo.news.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.demo.news.databasehelper.DataBaseHelper;
import com.example.demo.news.databasehelper.ListDataHelper;
import com.example.demo.news.databeans.content.ContentData;
import com.google.gson.Gson;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

//各页详情内容
public class LooperViewDetails extends Activity implements OnClickListener {

    final UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");//获取到友盟分享控制器
    String appID = "wx967daebe835fbeac";
    String appSecret = "5595ea1667e58e486200553a";
    private int contentId;//详情内容的id
    private CheckBox collection;//收藏按钮
    private ImageButton share;//分享按钮
    private ImageButton back;//返回按钮
    private AsyncTask<Void, Void, String> task;//获取详情内容的task
    private String urlString;//详情内容的url
    private ContentData dataGot;//详情内容的书
    private ProgressBar pb;
    private TextView tvLoad;

    @SuppressWarnings("static-access")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_PROGRESS);// 让进度条显示在标题栏上
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looper_view_details);
        //从Intent 获取到详情的id
        contentId = getIntent().getExtras().getInt("content_id");
        //详情内容的link构造
        urlString = "http://api.jjjc.yn.gov.cn/jwapp/?service=Info.index&cid=18&id="
                + contentId;

        //获取到详情内容的task定义
        task = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                URL url;
                String result = null;
                try {
                    url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    InputStream inputStream = connection.getInputStream();
                    int length = 0;
                    byte[] data = new byte[1024];
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    while ((length = inputStream.read(data)) != -1) {
                        outputStream.write(data, 0, length);
                    }
                    inputStream.close();
                    result = new String(outputStream.toByteArray());

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return result;
            }
        };

        task.execute();
        try {
            if (task.get() != null) {
                String jsString = task.get();
                System.out.println(jsString);
                dataGot = new Gson().fromJson(jsString, ContentData.class);
                //获取到详情页内容的数据
            }
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //获取到详情内容的link 以及图片资源作为分享的内容使用
        String shareLink = dataGot.getData().getShare_link();
        String shareImage = dataGot.getData().getShare_image();
        String title = dataGot.getData().getTitle();
        //判断分享内容标题的长度并且修改大于15个字的标题内容
        if (title.length() > 15) {
            char[] t = title.toCharArray();
            char[] tt = new char[15];
            System.arraycopy(t, 0, tt, 0, 15);
            title = new String().valueOf(tt) + "...";
        }
        // 设置分享内容
        mController.setShareContent("test----" + title + shareLink);
        // ���÷���ͼƬ, ����2ΪͼƬ��url��ַ
        mController.setShareMedia(new UMImage(LooperViewDetails.this,
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
        UMWXHandler wxHandler = new UMWXHandler(LooperViewDetails.this, appID,
                appSecret);
        wxHandler.addToSocialSDK();
        // ֧��΢������Ȧ
        UMWXHandler wxCircleHandler = new UMWXHandler(LooperViewDetails.this,
                appID, appSecret);
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
                LooperViewDetails.this, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.addToSocialSDK();
        // qq�ռ����
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
                LooperViewDetails.this, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();
        pb = (ProgressBar) findViewById(R.id.pb);
        WebView webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        if (getIntent().getExtras().get("link") != null) {
            System.out.println(getIntent().getExtras().get("link"));

        }
        System.out.println(getIntent().getExtras().get("content_id"));
        //详情内容
        tvLoad = (TextView) findViewById(R.id.tvLoad);
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
        back = (ImageButton) findViewById(R.id.btnBack);
        back.setOnClickListener(this);
        share = (ImageButton) findViewById(R.id.btnShare);
        share.setOnClickListener(this);
        collection = (CheckBox) findViewById(R.id.btnCollect);
        collection.setOnClickListener(this);
        //获取到本地数据库判断该条内容是否已经被收藏从而动态改变收藏按钮的状态

        DataBaseHelper db = new DataBaseHelper(this);
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Cursor c = dbRead.query("id", null, null, null, null, null, null);
        while (c.moveToNext()) {
            int contentID = c.getInt(c.getColumnIndex("contentId"));
            System.out.println("���ݿ������" + contentID);
            String time = c.getString(c.getColumnIndex("time"));
            System.out.println(time);
            if (contentID == contentId) {
                collection.setChecked(true);
            }
        }
        c.close();
        dbRead.close();
        ListDataHelper dataHelper = new ListDataHelper(this);
        SQLiteDatabase sqLiteDatabase = dataHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("listData", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String js = cursor.getString(cursor.getColumnIndex("name"));
            System.out.print("数据内容" + js + "\n\n");
        }
        cursor.close();
        sqLiteDatabase.close();

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

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:

                this.finish();
            case R.id.btnShare:
                mController.openShare(LooperViewDetails.this, false);
                // �����������
                break;
            case R.id.btnCollect:
                //收藏内容的按钮侦听设置 动态的修改本地数据库根据数据库是否存在该条数据决定是否添加数据根据按钮状态决定是添加数据还是删除数据
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "yyyy-MM-dd    HH:mm:ss     ");
                Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
                String str = formatter.format(curDate);
                DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
                SQLiteDatabase dbWriteDatabase = dataBaseHelper.getWritableDatabase();
                SQLiteDatabase dbReaDatabase = dataBaseHelper.getReadableDatabase();
                ContentValues values = new ContentValues();
                Cursor cc = dbReaDatabase.query("id", null, null, null, null, null,
                        null);
                values.put("contentId", contentId);
                values.put("time", str);
                if (collection.isChecked()) {
                    dbWriteDatabase.insert("id", null, values);
                }
                if (!collection.isChecked()) {
                    while (cc.moveToNext()) {
                        int contentID = cc.getInt(cc.getColumnIndex("contentId"));
                        if (contentID == contentId) {
                            String sql = "delete from id where contentId  = "
                                    + contentId;
                            dbWriteDatabase.execSQL(sql);
                        }
                    }
                }
                dbWriteDatabase.close();
                dbReaDatabase.close();

                break;

            default:
                break;
        }
    }

}
