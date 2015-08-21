package com.example.demo.news.dataloaders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.demo.news.databeans.dynamic.DynamicData;
import com.google.gson.Gson;

public class DynamicLoader {
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

	public DynamicData getJSONDate(String JSON) {
		DynamicData data = null;
		data = new Gson().fromJson(JSON, DynamicData.class);
		return data;

	}

}
