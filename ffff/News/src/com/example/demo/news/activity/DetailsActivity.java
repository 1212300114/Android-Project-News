package com.example.demo.news.activity;

import net.xinhuamm.d0403.R;

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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;

public class DetailsActivity extends Activity implements OnClickListener {
	// Ԕ���

	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");// ���������˵ķ��������
	String appID = "wx967daebe835fbeac";
	String appSecret = "5595ea1667e58e486200553a";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		// ���÷�������
		mController
				.setShareContent("������ữ�����SDK�����ƶ�Ӧ�ÿ��������罻�����ܣ�http://www.umeng.com/social");
		// ���÷���ͼƬ, ����2ΪͼƬ��url��ַ
		mController.setShareMedia(new UMImage(DetailsActivity.this,
				"http://www.baidu.com/img/bdlogo.png"));
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
		UMWXHandler wxHandler = new UMWXHandler(DetailsActivity.this, appID,
				appSecret);
		wxHandler.addToSocialSDK();
		// ֧��΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(DetailsActivity.this,
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
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(DetailsActivity.this,
				"100424468", "c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.addToSocialSDK();
		// qq�ռ����
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
				DetailsActivity.this, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();
		// ���ؼ�������
		ImageButton back = (ImageButton) findViewById(R.id.btnBack);
		back.setOnClickListener(this);
		// ��������򿪷������
		ImageButton share = (ImageButton) findViewById(R.id.btnShare);
		share.setOnClickListener(this);
		// �ղؼ�
		RadioButton collect = (RadioButton) findViewById(R.id.btnCollect);
		collect.setOnClickListener(this);

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
			finish();
			break;
		case R.id.btnShare:
			mController.openShare(DetailsActivity.this, false);
			// �����������
			break;
		case R.id.btnCollect:
			break;

		default:
			break;
		}
	}

}
