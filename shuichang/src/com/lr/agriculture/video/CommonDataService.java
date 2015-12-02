package com.lr.agriculture.video;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;

public final class CommonDataService {

	private static final int REQUEST_TIMEOUT = 10 * 1000;// 设置请求超时10秒钟
	private static final int SO_TIMEOUT = 20 * 1000; // 设置等待数据超时时间10秒钟

	// 取json数据通用方法
	public static String getJsonData(String serviceName,
			HashMap<Object, Object> conditions) {
		try {
			HttpClient httpClient = null;
			if (httpClient == null) {
				// BasicHttpParams 是保存设置请求参数的类
				BasicHttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams,
						REQUEST_TIMEOUT);// 设置请求超时10秒钟
				HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);// 设置等待数据超时时间10秒钟
				httpClient = new DefaultHttpClient(httpParams);
			}

			HttpPost request = new HttpPost(serviceName);
			Gson gson = new Gson();
			Type type = null;
			if (conditions.get("type__") != null) {
				type = (Type) conditions.get("type__");
				conditions.remove("type__");
			}
			System.out.println("requset:" + gson.toJson(conditions));

			StringEntity se = new StringEntity(gson.toJson(conditions),
					HTTP.UTF_8);
			se.setContentType("application/json");
			request.setEntity(se);
			// 根据条件查询结果
			HttpResponse response = httpClient.execute(request);
			int code = response.getStatusLine().getStatusCode();
			// 响应的code

			// if (HttpStatus.SC_OK == code) {
			InputStream content = response.getEntity().getContent();

			return convertStreamToString(content);

			// }

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			String exmessage = e.getMessage();

		}
		return null;
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
