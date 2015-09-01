package com.wsj.sample.common;

import oicq.wlogin_sdk.request.Ticket;
import oicq.wlogin_sdk.request.WUserSigInfo;
import oicq.wlogin_sdk.request.WloginLastLoginInfo;
import oicq.wlogin_sdk.request.WtloginHelper;
import oicq.wlogin_sdk.request.WtloginHelper.SigType;
import oicq.wlogin_sdk.request.WtloginListener;
import oicq.wlogin_sdk.sharemem.WloginSimpleInfo;
import oicq.wlogin_sdk.tools.ErrMsg;
import oicq.wlogin_sdk.tools.LogCallBack;
import oicq.wlogin_sdk.tools.util;
import org.json.JSONObject;
import com.tencent.midas.sample.R;
import com.wsj.sample.pay.PaySampleActivity;

import ads.Sogou;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("Override")
public class Login extends Activity {
	public static LoginHelper mLoginHelper = null;
	public static long mAppid = 518005007; // 即通分配的appid(申请：pt.oa.com)
	public static long mOpenAppid = 101000001; // 仅作测试用, 获取openid要用的appid
	public static long mSubAppid = 1; // WNS分配的appid，若没有，请填1
	public static long mSmsAppid = 0x9; // 设备锁appid, 由安平分配

	public static long mDstAppid = 17;
	public static long mDstSubAppid = 1;

	public static String mAppName = "demo";
	public static String mAppVersion = "1.0";
	public static int mMainSigMap = SigType.WLOGIN_A2 | SigType.WLOGIN_ST | SigType.WLOGIN_SKEY;
	public static WUserSigInfo userSigInfo = null;

	public static boolean isSmslogin = false;

	public EditText name;
	public EditText pswd;
	public Button login;
	public Button btnSmsLogin;
	public Button btnHistLogin;
	public Button btnQuickLogin;
	public TextView reg;
	public TextView findPswd;

	public static int TYPE_REQ_QQ = 0;
	public static int TYPE_REQ_MOBILE = 1;
	public static int TYPE_REQ_ID = 2;
	public static int TYPE_REQ_EMAIL = 3;

	public final int REQ_QLOGIN = 0x100; // 快速登录 requestCode
	public final int REQ_VCODE = 0x2; // 验证码 requestCode

	public static String gAccount = "";
	public static String gPasswd = "";
	public static boolean gLoginNow = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_skey_login);

		util.LOG_LEVEL = util.I;
		// 正式发布前可设置关闭SDK log
		util.LOGCAT_OUT = true;
		mLoginHelper = new LoginHelper(getApplicationContext());
		mLoginHelper.SetListener(mListener);

		// 国际化时需要设置
		// mLoginHelper.SetLocalId(InternationMsg.EN_US);
		mLoginHelper.SetImgType(4); // 该项设置了才会有头像
		Sogou.getAuthority(this);
		// 以下为测试环境，app正式发布必须不能设置
		clearHost();

		name = (EditText) findViewById(R.id.login_edit_account);
		pswd = (EditText) findViewById(R.id.login_edit_password);
		login = (Button) findViewById(R.id.button_login);
		login.setOnClickListener(onClick);

		reg = (TextView) findViewById(R.id.reg);
		reg.setOnClickListener(onClick);

		WloginLastLoginInfo info = mLoginHelper.GetLastLoginInfo();
		if (info != null) {
			name.setText(info.mAccount);
			if (info.mAccount.length() > 0) {
				if (mLoginHelper.IsUserHaveA1(info.mAccount, mAppid))
					pswd.setText("123456");
				else
					pswd.setText("");
			}
		}

	}

	protected void onResume() {
		super.onResume();
		mLoginHelper.SetListener(mListener);// 不在这里调用这句话，登录的时候就没有办法调用回调函数

		// 申请后直接登陆逻辑
		if (gLoginNow == true) {
			gLoginNow = false;
			name.setText(gAccount);
			if (isSmslogin) {
				int ret = mLoginHelper.GetStViaSMSVerifyLogin(gAccount, mAppid, mSubAppid, mMainSigMap, new WUserSigInfo());
				util.LOGI("GetStViaSMSVerifyLogin ret: " + ret + ", mobile: " + gAccount);
				pswd.setText("");
			} else {
				pswd.setText(gPasswd);
			}
		}
		Sogou.getAuto(this);
		isSmslogin = false;
	}

	public class CALL_BACK extends LogCallBack {
		public void OnLog(JSONObject obj) {
			Log.i("", obj.toString());
		}
	}

	private View.OnClickListener onClick = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_login: {
				int ret = 0;
				if ("".equals(name.getText().toString().trim())) {
					showDialog(Login.this, "您都还没有输帐号!");
					return;
				} else if ("".equals(pswd.getText().toString().trim())) {
					showDialog(Login.this, "不输密码不给登录!");
					return;
				}

				// 登陆
				WUserSigInfo sigInfo = new WUserSigInfo();

				/*
				 * 使用异步接口时，需要判断异步接口的返回值
				 * 如果为util.E_PENDING，会有相应的回调函数或者OnException异常回调
				 * 其它值，说明接口调用错误或者没有按照指定逻辑调用，此时不会调用回调函数
				 */
				if (mLoginHelper.IsNeedLoginWithPasswd(name.getText().toString(), mAppid)) {
					ret = mLoginHelper.GetStWithPasswd(name.getText().toString(), mAppid, 0x1, mMainSigMap, pswd.getText().toString(), sigInfo);
				} else {
					ret = mLoginHelper.GetStWithoutPasswd(name.getText().toString(), mAppid, mAppid, 0x1, mMainSigMap, sigInfo);
				}
				// 需要判断函数返回值
				if (ret != util.E_PENDING)
					showDialog(Login.this, "输入参数有误，请检查。。");
			}
				break;

			default:
				showDialog(Login.this, "输入参数有误，请检查。。");
				break;
			}
		}

	};

	WtloginListener mListener = new WtloginListener() {
		public void OnGetStWithPasswd(String userAccount, long dwSrcAppid, int dwMainSigMap, long dwSubDstAppid, String userPasswd, WUserSigInfo userSigInfo, int ret, ErrMsg errMsg) {
			if (ret == util.S_GET_IMAGE) {
				byte[] image_buf = new byte[0];
				image_buf = mLoginHelper.GetPictureData(userAccount);
				if (image_buf == null) {
					return;
				}
				Toast.makeText(Login.this, "需要输入验证码", Toast.LENGTH_SHORT).show();
			} else if (ret == util.S_GET_SMS) {
				Toast.makeText(Login.this, "需要输入短信验证码", Toast.LENGTH_SHORT).show();
			} else if (ret == util.S_SUCCESS) {
				// 示例：获取A2票据
				// 如果用户修改了密码，那么A2就马上失效，而不能通过expire_time判断是否失效。
				Ticket ticket = WtloginHelper.GetUserSigInfoTicket(userSigInfo, SigType.WLOGIN_A2);
				util.LOGI("a2:" + util.buf_to_string(ticket._sig) + " a2_key:" + util.buf_to_string(ticket._sig_key) + " create_time:" + ticket._create_time + " expire_time:"
						+ ticket._expire_time);
				ticket = WtloginHelper.GetUserSigInfoTicket(userSigInfo, SigType.WLOGIN_D2);
				util.LOGI("d2: " + util.buf_to_string(ticket._sig) + " d2key: " + util.buf_to_string(ticket._sig_key));
				loginSuccess(Login.this, userAccount, userSigInfo);
			} else {
				showDialog(Login.this, errMsg);
			}
		}

		public void OnGetStWithoutPasswd(String userAccount, long dwSrcAppid, long dwDstAppid, int dwMainSigMap, long dwSubDstAppid, WUserSigInfo userSigInfo, int ret,
				ErrMsg errMsg) {
			if (ret == util.S_SUCCESS) {
				// 示例：获取st票据
				Ticket ticket = WtloginHelper.GetUserSigInfoTicket(userSigInfo, SigType.WLOGIN_ST);
				util.LOGI("st:" + util.buf_to_string(ticket._sig) + " st_key:" + util.buf_to_string(ticket._sig_key) + " create_time:" + ticket._create_time + " expire_time:"
						+ ticket._expire_time);
				ticket = WtloginHelper.GetUserSigInfoTicket(userSigInfo, SigType.WLOGIN_D2);
				util.LOGI("d2: " + util.buf_to_string(ticket._sig) + " d2key: " + util.buf_to_string(ticket._sig_key));

				loginSuccess(Login.this, userAccount, userSigInfo);
			} else if (ret == 0xF) {
				// 可能A2过期，或者用户修改密码导致A2失效
				showDialog(Login.this, "A2失效，请尝试用A1登录或直接用密码登录");
			} else {
				showDialog(Login.this, errMsg);
			}
		}

		@Override
		public void OnCheckSMSVerifyLoginAccount(long appid, long subAppid, String mobile, String msg, int msgCnt, int timeLimit, WUserSigInfo userSigInfo, int ret, ErrMsg errMsg) {
			util.LOGI("see see ret: " + ret);
			if (ret != 0) {
				util.LOGI("title: " + errMsg.getTitle() + ", msg: " + errMsg.getMessage() + ", type: " + errMsg.getType());
				showDialog(Login.this, errMsg);
			} else { // 输入短信验证码
				Toast.makeText(Login.this, "需要输入短信验证码", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void OnGetStViaSMSVerifyLogin(String mobile, long dwAppid, int dwMainSigMap, long dwSubDstAppid, WUserSigInfo userSigInfo, int ret, ErrMsg errMsg) {
			if (ret == util.S_SUCCESS) {
				util.LOGI("登录成功!" + mobile);
				loginSuccess(Login.this, mobile, userSigInfo);
			} else {
				if (errMsg != null && errMsg.getMessage() != null)
					errMsg.setMessage(ret + " " + errMsg.getMessage());
				Login.showDialog(Login.this, errMsg);
			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String userAccount = "";
		ErrMsg errMsg = null;
		WUserSigInfo userSigInfo = null;
		switch (requestCode) {
		case REQ_VCODE: {
			Bundle bundle = data.getExtras();
			userAccount = bundle.getString("ACCOUNT");
			errMsg = (ErrMsg) bundle.getParcelable("ERRMSG");
			userSigInfo = (WUserSigInfo) bundle.getParcelable("USERSIG");
			util.LOGI("userSigInfo " + userSigInfo._seqence);

			if (resultCode == util.S_SUCCESS) {
				loginSuccess(Login.this, userAccount, userSigInfo);
			} else {
				showDialog(Login.this, errMsg);
			}
		}
			break;
		case REQ_QLOGIN: // 快速登录返回
			try {
				if (data == null) { // 这种情况下用户多半是直接按了返回按钮，没有进行快速登录；快速登录失败可提醒用户输入密码登录
					util.LOGI("用户异常返回");
					break;
				}

				WUserSigInfo sigInfo = mLoginHelper.ResolveQloginIntent(data);
				if (sigInfo == null) {
					showDialog(Login.this, "快速登录失败，请改用普通登录");
					break;
				}

				String uin = sigInfo.uin;
				name.setText(uin);
				pswd.setText("123456");

				// 快速登录只是从手Q换取了A1票据，A1则相当于用户密码，在此仍需要再发起一次A1换票的流程，才能拿到目标票据
				mLoginHelper.GetStWithPasswd(uin, mAppid, 0x1, mMainSigMap, "", sigInfo);
			} catch (Exception e) {
				util.printException(e);
			}
			break;
		default:
			break;
		}
	}

	public static String getImagePrompt(String userAccount, byte[] imagePrompt) {
		String prompt_value = null;
		if (imagePrompt != null && imagePrompt.length > 3) {
			int pos = 0;
			int dwCnt = util.buf_to_int32(imagePrompt, pos);
			pos += 4;
			for (int i = 0; i < dwCnt; i++) {
				if (imagePrompt.length < pos + 1) {
					break;
				}

				int key_len = util.buf_to_int8(imagePrompt, pos);
				pos += 1;

				if (imagePrompt.length < pos + key_len) {
					break;
				}
				String key_data = new String(imagePrompt, pos, key_len);
				pos += key_len;

				if (imagePrompt.length < pos + 2) {
					break;
				}
				int value_len = util.buf_to_int32(imagePrompt, pos);
				pos += 4;

				if (imagePrompt.length < pos + value_len) {
					break;
				}
				String value = new String(imagePrompt, pos, value_len);
				pos += value_len;

				util.LOGI("key_data:" + key_data + " value:" + value);
				if (key_data.equals("pic_reason")) {
					prompt_value = value;
					break;
				}
			}
		}
		return prompt_value;
	}

	public void loginSuccess(Context context, String userAccount, WUserSigInfo userSigInfo) {
		Login.userSigInfo = userSigInfo;

		WloginSimpleInfo info = new WloginSimpleInfo();
		mLoginHelper.GetBasicUserInfo(userAccount, info);

		// util.LOGI("头像：" + util.buf_to_string(info._face) + ", " + new
		// String(info._img_url));
		/*
		 * byte[] A2 = WtloginHelper.GetTicketSig(userSigInfo,
		 * SigType.WLOGIN_SKEY); util.LOGI("A2: " + util.buf_to_string(A2));
		 */

		byte[] byteskey = WtloginHelper.GetTicketSig(userSigInfo, SigType.WLOGIN_SKEY);
		String strskey = new String(byteskey);

		Intent intent = new Intent();

		intent.putExtra("sessionId", "uin");
		intent.putExtra("sessionType", "skey");
		intent.putExtra("pf", "huyu_m-2001-android");
		intent.putExtra("pfKey", "pfKey");

		intent.putExtra("userId", userAccount);
		intent.putExtra("userKey", strskey);
		intent.setClass(Login.this, PaySampleActivity.class);
		startActivity(intent);
		/*
		 * Intent intent = new Intent(); intent.setClass(context,
		 * LoginOk.class); intent.putExtra("RET", 0); intent.putExtra("ACCOUNT",
		 * userAccount); intent.putExtra("UIN", new Long(info._uin).toString());
		 * intent.putExtra("NICK", new String(info._nick));
		 * intent.putExtra("FACE", new String(info._img_url)); int gender =
		 * info._gender[0]; if (gender == 0) { intent.putExtra("GENDER", "女"); }
		 * else if (gender == 1) { intent.putExtra("GENDER", "男"); } else {
		 * intent.putExtra("GENDER", "未知"); }
		 * 
		 * Integer age = (int) info._age[0]; intent.putExtra("AGE",
		 * age.toString()); context.startActivity(intent);
		 */
	}


/*	@SuppressLint("NewApi")
	private void initPopuWindows() {
		// 初始化PopupWindow,LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT控制显示
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = layoutInflater.inflate(R.layout.regmenu, null);
		popup = new PopupWindow(findViewById(R.id.loginLinear),
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		popup.setContentView(contentView);
		popup.setFocusable(true);

		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.isTouchable();
		popup.setAnimationStyle(R.style.AnimationFade);
		popup.showAtLocation(this.findViewById(R.id.loginLinear),
				Gravity.BOTTOM, 0, 0);// 这个一定要放在setBackgroundDrawable后面

		byQQ = (Button) contentView.findViewById(R.id.byQQ);
		byQQ.setOnClickListener(onClick);

		byPhone = (Button) contentView.findViewById(R.id.byPhone);
		byPhone.setOnClickListener(onClick);
		byEmail = (Button) contentView.findViewById(R.id.byEmail);
		byEmail.setOnClickListener(onClick);
		bySmslogin = (Button) contentView.findViewById(R.id.bySmslogin);
		bySmslogin.setOnClickListener(onClick);
	}*/
	

	private void clearHost() {
		String host = "";
		SharedPreferences settings = getSharedPreferences("WLOGIN_DEVICE_INFO", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("host1", host);
		editor.putString("host2", host);
		editor.putString("wap-host1", host);
		editor.putString("wap-host2", host);
		editor.commit();
	}

	public static void showDialog(Context context, String strMsg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("QQ通行证");
		builder.setMessage(strMsg);
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});

		builder.show();
	}

	public static void showDialog(Context context, ErrMsg errMsg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (errMsg != null) {
			String title = errMsg.getTitle();
			String message = errMsg.getMessage();
			if (title != null && title.length() > 0) {
				builder.setTitle(title);
			} else {
				builder.setTitle("app自己定义title内容");
			}
			if (message != null && message.length() > 0) {
				builder.setMessage(message);
			} else {
				builder.setMessage("app自己定义message内容");
			}

			/*
			 * 当errMsg.getType()==1时，错误提示语可以跳转， 此时errMsg.getOtherinfo()为跳转链接信息
			 */

			builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			});

			builder.show();
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Login.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
