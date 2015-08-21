package com.example.demo.news.dataloaders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.demo.news.databeans.indicator.IndicatorData;
import com.google.gson.Gson;

public class IndicatorLoader {
	public String readURL(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream inputStream = connection.getInputStream();
		int length = 0;
		byte[] data = new byte[1024];
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		while ((length = inputStream.read(data)) != -1) {
			outputStream.write(data, 0, length);
		}
		inputStream.close();
		return new String(outputStream.toByteArray());

	}

	public IndicatorData getJSONDate(String JSON) {
		IndicatorData data = null;
		data = new Gson().fromJson(JSON, IndicatorData.class);
		return data;

	}

}
