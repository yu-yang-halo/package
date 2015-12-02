package com.lr.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.Cipher;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import com.example.shuichang.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lr.javaBean.DataALL;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WeaFragment extends Fragment {
	private HttpUtils httpUtils;
	private TextView tem;
	private TextView data;
	private EditText city;
	private TextView weastatu;
	private String encode;
	private String cityName;
	private HashMap<String, String> hashMap;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			city.setText(hashMap.get("city"));
			weastatu.setText(hashMap.get("status1") + "转"
					+ hashMap.get("status2"));
			tem.setText(hashMap.get("temperature1") + "℃/"
					+ hashMap.get("temperature2") + "℃");
			data.setText(hashMap.get("udatetime"));
		};

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		Bundle arguments = getArguments();
		ArrayList<DataALL> list = (ArrayList<DataALL>) arguments
				.getSerializable("name");
		for (int i = 0; i < list.size(); i++) {
			cityName = list.get(i).getCityName();

		}
		try {
			if (cityName.endsWith("市") || cityName.endsWith("县")) {
				cityName = cityName.substring(0, cityName.length() - 1);

			}
			if (cityName.endsWith("市辖区")) {
				cityName = list.get(0).getProvinceName();
				if (cityName.endsWith("市") || cityName.endsWith("县")) {
					cityName = cityName.substring(0, cityName.length() - 1);

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		View view = inflater.inflate(R.layout.wea, null);
		tem = (TextView) view.findViewById(R.id.tem);
		data = (TextView) view.findViewById(R.id.data);
		city = (EditText) view.findViewById(R.id.city);
		city.setText(cityName);
		weastatu = (TextView) view.findViewById(R.id.weathstatu);
		Button button = (Button) view.findViewById(R.id.lookfor);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showView(city.getText().toString().trim());
			}
		});
		showView(city.getText().toString().trim());
		return view;
	}

	private void showView(String city1) {
		// httpUtils = new HttpUtils();
		// try {
		try {
			encode = java.net.URLEncoder.encode(city1, "gb2312");
			new Thread() {
				public void run() {
					HttpClient httpClient = new DefaultHttpClient();
					try {
						HttpResponse execute = httpClient.execute(new HttpGet(
								"http://php.weather.sina.com.cn/xml.php?city="
										+ encode
										+ "&password=DJOYnieT8234jlsK&day=0"));
						Log.e("ex", execute.getStatusLine().getStatusCode()
								+ "");
						if (execute.getStatusLine().getStatusCode() == 200) {
							HttpEntity entity = execute.getEntity();
							InputStream inputStream = entity.getContent();
							XmlPullParser xmlPullParser = Xml.newPullParser();
							xmlPullParser.setInput(inputStream, "utf-8");
							int eventType = xmlPullParser.getEventType();
							while (eventType != XmlPullParser.END_DOCUMENT) {
								String name = xmlPullParser.getName();
								switch (eventType) {
								case XmlPullParser.START_DOCUMENT:
									hashMap = new HashMap<String, String>();
									break;
								case XmlPullParser.START_TAG:
									if (name.equals("city")) {
										hashMap.put("city",
												xmlPullParser.nextText());
									} else if (name.equals("status1")) {

										hashMap.put("status1",
												xmlPullParser.nextText());

									} else if (name.equals("status2")) {
										hashMap.put("status2",
												xmlPullParser.nextText());
									} else if (name.equals("temperature1")) {
										hashMap.put("temperature1",
												xmlPullParser.nextText());

									} else if (name.equals("temperature2")) {
										hashMap.put("temperature2",
												xmlPullParser.nextText());
									} else if (name.equals("savedate_zhishu")) {
										hashMap.put("udatetime",
												xmlPullParser.nextText());
									}
									break;
								case XmlPullParser.END_TAG:
									break;

								default:
									break;
								}
								eventType = xmlPullParser.next();

							}
							//
							handler.sendEmptyMessage(0);
						}
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				};

			}.start();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// httpUtils.send(HttpMethod.GET,"http://php.weather.sina.com.cn/xml.php?city="+encode+"&password=DJOYnieT8234jlsK&day=0",new
	// RequestCallBack<String>() {

	// private HashMap<String,String> hashMap;
	// // private ProgressDialog progressDialog;
	// private InputStream content;
	// @Override
	// public void onStart() {
	// // TODO Auto-generated method stub
	// super.onStart();
	// // progressDialog = new ProgressDialog(getActivity());
	// // progressDialog
	// // .setProgressStyle(ProgressDialog.STYLE_SPINNER);
	// // progressDialog.setCancelable(false);
	// //
	// // progressDialog.setMessage("正在请求数据......");
	// // progressDialog.show();
	//
	//
	// }
	// @Override
	public void onSuccess(ResponseInfo<String> responseInfo) {
		// TODO Auto-generated method stub
		// progressDialog.dismiss();

		// XmlPullParser xmlPullParser=Xml.newPullParser();
		// try {
		// Log.e("nb", responseInfo.result);
		//
		// httpUtils.getHttpClient().getConnectionManager().shutdown();
		// xmlPullParser.setInput(new
		// StringReader(responseInfo.result),"utf-8");
		//
		// int eventType = xmlPullParser.getEventType();
		// while(eventType!=XmlPullParser.END_DOCUMENT){
		// String name=xmlPullParser.getName();
		// switch (eventType) {
		// case XmlPullParser.START_DOCUMENT:
		// hashMap = new HashMap<String, String>();
		// break;
		// case XmlPullParser.START_TAG:
		// if(name.equals("city")){
		// hashMap.put("city",xmlPullParser.nextText());
		// }
		// else if(name.equals("status1")){
		//
		// hashMap.put("status1",xmlPullParser.nextText());
		//
		// }
		// else if(name.equals("status2")){
		// hashMap.put("status2",xmlPullParser.nextText());
		// }
		// else if(name.equals("temperature1")){
		// hashMap.put("temperature1",xmlPullParser.nextText());
		//
		// }
		// else if(name.equals("temperature2")){
		// hashMap.put("temperature2",xmlPullParser.nextText());
		// }
		// else if(name.equals("savedate_zhishu")){
		// hashMap.put("udatetime",xmlPullParser.nextText());
		// }
		// break;
		// case XmlPullParser.END_TAG:
		// break;
		//
		// default:
		// break;
		// }
		// eventType=xmlPullParser.next();
		//
		// }
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// try {
		// content.close();
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// } catch (XmlPullParserException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// city.setText(hashMap.get("city"));
		// weastatu.setText(hashMap.get("status1")+"转"+hashMap.get("status2"));
		// tem.setText(hashMap.get("temperature1")+"℃/"+hashMap.get("temperature2")+"℃");
		// data.setText(hashMap.get("udatetime"));

	}

	// @Override
	// public void onFailure(HttpException error, String msg) {
	// // TODO Auto-generated method stub
	// // progressDialog.dismiss();
	//
	// }
	// });
}
