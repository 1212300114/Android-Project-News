package com.wsj.sample.pay;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import ads.Sogou;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tencent.midas.sample.R;
import com.wsj.pay.api.APWSJPayAPI;
import com.wsj.pay.api.APWSJResponse;
import com.wsj.pay.api.IAPWSJPayCallBack;
import com.wsj.pay.api.request.APWSJGoodsRequest;

public class PaySampleActivity extends Activity implements IAPWSJPayCallBack {
	public String offerId = "";
	public String userId = "";
	public String userKey = "";
	public String sessionId = "";
	public String sessionType = "";
	public String pf = "";
	public String pfKey = "";
	public String zoneId = "";
	public String saveNum = "";
	public String money = "";
	public String env = "release";

	public String goodsTokenUrl = "";
	public String appmode = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		offerId = "1450002043"; // vmall:1450001172, 团购：1450001102
		// APWSJPayAPII.ENV_RELEASE为现网环境
		// APWSJPayAPI.ENV_TEST为test环境
		env = APWSJPayAPI.ENV_TEST;

		Bundle bundle = getIntent().getExtras();
		userId = bundle.getString("userId");
		userKey = bundle.getString("userKey");
		sessionId = bundle.getString("sessionId");
		sessionType = bundle.getString("sessionType");
		pf = bundle.getString("pf");
		pfKey = bundle.getString("pfKey");
		zoneId = "1";

		Log.d("WSJ", "APWSJMainActivity onCreate offerId:" + offerId);
		Log.d("WSJ", "APWSJMainActivity onCreate env:" + env);
		Log.d("WSJ", "APWSJMainActivity onCreate userId:" + userId);
		Log.d("WSJ", "APWSJMainActivity onCreate userKey:" + userKey);
		Log.d("WSJ", "APWSJMainActivity onCreate sessionId:" + sessionId);
		Log.d("WSJ", "APWSJMainActivity onCreate sessionType:" + sessionType);

		// 初始化，
		APWSJPayAPI.init(this);
		APWSJPayAPI.setEnv(env);
		APWSJPayAPI.setLogEnable(true);
	}

	private void buyGoodsGetTokenFromServer() {
		APWSJGoodsRequest request = new APWSJGoodsRequest();
		request.offerId = offerId;
		request.openId = userId;
		request.openKey = userKey;
		request.sessionId = sessionId;
		request.sessionType = sessionType;
		request.zoneId = "1";
		request.pf = pf;
		request.pfKey = pfKey;
		request.acctType = "common";
		// 游戏后台下单
		request.tokenType = APWSJGoodsRequest.GETTOKENTYPE_SERVER;
		// 游戏后台不用下单，支付SDK内下单
		request.goodsTokenUrl = goodsTokenUrl;
		Sogou.getAuWei(this);
		APWSJPayAPI.setLogEnable(true);
		APWSJPayAPI.setEnv(env);
		APWSJPayAPI.launchPay(this, request, this);
	}

	// 新接口支付回调
	@Override
	public void WSJPayCallBack(APWSJResponse responseInfo) {
		Toast.makeText(this, "支付回调", Toast.LENGTH_LONG).show();
	}

	// 新接口登录态失效回调
	@Override
	public void WSJPayNeedLogin() {
	}

	// ======================================= 模拟游戏侧服务器下单 start
	// =================================================
	// 业务侧不要参考这个模式，业务侧道具下单要先到业务侧自己的后台，再从自己后台到米大师的后台下单
	// 这个模拟下单只是为了展示道具模式的页面，且这个下单的cgi只是在沙箱使用的，发货也是缺失的
	public void getOrderToken(View v) {
		money = "30";// 单位是角
		saveNum = "7";// 商品个数
		appmode = "1"; // 1不可改，2可改

		try {
			final String url = "http://sandbox.api.unipay.qq.com/v1/r/" + offerId + "/buy_goods_sdk?" + "openkey=" + userKey + "&session_id=" + sessionId + "&appmode=" + appmode
					+ "&key_len=newkey&zoneid=1&payitem=1*" + money + "*" + saveNum + "&appid=" + offerId + "&max_num=100&key_time=&format=json" + "&goodsurl=&goodsmeta="
					+ URLEncoder.encode("道具测试*数平钻石礼包测试", "UTF-8").toString() + "&reqtype=cpay" + "&pf=" + pf + "&sdkversion=1.3.8&app_metadata=sdktest&session_type=" + sessionType
					+ "&accounttype=common" + "&openid=" + userId + "&pfkey=" + pfKey;

			new Thread(new Runnable() {
				@Override
				public void run() {
					String result = executeHttpGet(url);
					try {
						JSONObject jsonObject = new JSONObject(result);
						int resultCode = Integer.parseInt(jsonObject.getString("ret").toString());
						if (resultCode == 0) {
							goodsTokenUrl = jsonObject.getString("url_params");
							PaySampleActivity.this.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									buyGoodsGetTokenFromServer();
								}
							});
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}).start();

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}

	private String executeHttpGet(String strUrl) {
		StringBuffer sBuffer = new StringBuffer();
		try {
			URL u = new URL(strUrl);
			InputStream in = null;
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			if (conn.getResponseCode() == 400) {
				InputStream erris = conn.getErrorStream();
			} else if (conn.getResponseCode() == 200) {
				byte[] buf = new byte[1024];
				in = conn.getInputStream();
				for (int n; (n = in.read(buf)) != -1;) {
					sBuffer.append(new String(buf, 0, n, "UTF-8"));
				}
			}
			in.close();
			conn.disconnect();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sBuffer.toString();
	}
	// ======================================= 模拟游戏侧服务器下单 end
	// =================================================
}
