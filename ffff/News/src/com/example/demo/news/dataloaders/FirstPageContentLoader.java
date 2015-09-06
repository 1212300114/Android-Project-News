package com.example.demo.news.dataloaders;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.demo.news.databeans.firstpage.FirstPageData;
import com.google.gson.Gson;

public class FirstPageContentLoader {
	public String readURL(String urlString) throws
			IOException {
		HttpGet get = new HttpGet(urlString);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(get);
		String resultString = EntityUtils.toString(response.getEntity());
		return resultString;
	}

	public FirstPageData getJSONDate(String JSON) {
		FirstPageData data = null;
		data = new Gson().fromJson(JSON, FirstPageData.class);
		return data;

	}

}
