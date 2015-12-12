package com.lr.animalhusbandry;

import java.util.ArrayList;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.app.MyApplication;
import com.example.huoyuyunshu.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lr.conURL.CommonServiceURL;
import com.lr.javaBean.DataALL;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogActivety extends Activity implements OnClickListener {
	protected static final Context MyApplication = null;
	private EditText nameEditText;
	private EditText passEditText;
	private Button login_btn;
	private String result;
	private TextView login;

	private ProgressDialog progressDialog;
	private RequestParams requestParams;
	private MyApplication myApplication2;

	private SharedPreferences sharedPreferences;
	private TextView noshow;
	private TextView version;
	private String version2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logi);

		myApplication2 = (MyApplication) getApplicationContext();
		myApplication2.pingpai = getPhoneInfo();

		sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
		initView();
		version.setText("当前版本号：" + getVersionName());
		if (sharedPreferences.getBoolean("ischeck", false)) {
			String name = sharedPreferences.getString("name", "");
			String pass = sharedPreferences.getString("pass", "");
			login.setVisibility(View.VISIBLE);
			noshow.setVisibility(View.GONE);
			nameEditText.setText(name);
			passEditText.setText(pass);

		}
	}

	private String getVersionName() {
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			version2 = packInfo.versionName;

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return version2;
	}

	public void initView() {
		nameEditText = (EditText) findViewById(R.id.name_text);
		passEditText = (EditText) findViewById(R.id.pass_text);
		login = (TextView) findViewById(R.id.show);
		noshow = (TextView) findViewById(R.id.noshow);
		login_btn = (Button) findViewById(R.id.login_btn);
		version = (TextView) findViewById(R.id.version);
		noshow.setOnClickListener(this);
		login.setOnClickListener(this);
		login_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.login_btn) {
			if ("".equals(nameEditText.getText().toString())
					|| "".equals(passEditText.getText().toString())) {
				Toast.makeText(getApplicationContext(), "用户名密码不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				JSONObject json = new JSONObject();
				json.put("username", nameEditText.getText().toString());
				json.put("pwd", passEditText.getText().toString());
				getData(CommonServiceURL.UserRegister, json);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} else if (id == R.id.noshow) {
			noshow.setVisibility(View.GONE);
			login.setVisibility(View.VISIBLE);

		} else if (id == R.id.show) {
			login.setVisibility(View.GONE);
			noshow.setVisibility(View.VISIBLE);

		}

	}

	public String getData(String url, JSONObject jsonObject) {
		HttpUtils httpUtils = new HttpUtils();

		try {
			requestParams = new RequestParams();
			requestParams.setContentType("application/json");
			StringEntity stringEntity = new StringEntity(jsonObject.toString(),
					"utf-8");
			requestParams.setBodyEntity(stringEntity);

		} catch (Exception exception) {
		}
		httpUtils.send(HttpMethod.POST, url, requestParams,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						progressDialog = new ProgressDialog(LogActivety.this);
						progressDialog
								.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progressDialog.setCancelable(false);
						progressDialog.setTitle("乐然温馨提示");
						progressDialog.setMessage("请稍等,正在登录......");
						progressDialog.show();

						super.onStart();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						result = responseInfo.result;
						if (!"".equals(result)) {
							try {
								JSONObject jsonObject = new JSONObject(result);
								String string = jsonObject
										.getString("GetLoginResult");

								if ("error".equalsIgnoreCase(string)) {
									progressDialog.dismiss();
									Toast.makeText(getApplicationContext(),
											"用户名或者密码错误", Toast.LENGTH_SHORT)
											.show();
								} else {
									Context applicationContext = getApplicationContext();
									MyApplication myApplication = (MyApplication) applicationContext;
									myApplication.setId(string);

									JSONObject jsonObject2 = new JSONObject();
									jsonObject2.put("customerNo", string);
									getData2(CommonServiceURL.GetDeviceInfo,
											jsonObject2);

								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "网络异常",
								Toast.LENGTH_SHORT).show();
						progressDialog.dismiss();
					}

				});

		return result;
	}

	public String getData2(String url, JSONObject jsonObject) {
		HttpUtils httpUtils = new HttpUtils();
		try {
			requestParams = new RequestParams();
			requestParams.setContentType("application/json");
			StringEntity stringEntity = new StringEntity(jsonObject.toString(),
					"utf-8");
			requestParams.setBodyEntity(stringEntity);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		httpUtils.send(HttpMethod.POST, url, requestParams,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// progressDialog.dismiss();
						ArrayList<DataALL> alls = new ArrayList<DataALL>();
						result = responseInfo.result;
						String cityName = null;
						Log.e("mnn", result);
						result = result.replace("\\", "");
						result = result.substring(result.indexOf('['),
								result.lastIndexOf(']') + 1);
						try {
							JSONArray jsonArray = new JSONArray(result);
							for (int i = 0; i < jsonArray.length(); i++) {

								DataALL dataALL = new DataALL();
								JSONObject jsonObject2 = null;
								if (jsonArray.getJSONObject(0)
										.getString("CustomerNo")
										.equals("01-00-00-34")) {
									if (i == 0) {
										jsonObject2 = jsonArray
												.getJSONObject(i + 1);
									} else if (i == 1) {
										jsonObject2 = jsonArray
												.getJSONObject(i - 1);

									} else if (i > 1) {
										jsonObject2 = jsonArray
												.getJSONObject(i);
									}
								} else {
									jsonObject2 = jsonArray.getJSONObject(i);
								}

								dataALL.setCustomerNo(jsonObject2
										.getString("CustomerNo"));
								String id = jsonObject2.getString("DeviceID")
										.toLowerCase();
								dataALL.setDeviceID(id);
								dataALL.setProvinceName(jsonObject2
										.getString("ProvinceName"));
								// CityName
								dataALL.setCityName(jsonObject2
										.getString("CityName"));
								Log.e("city", "s" + dataALL.getCityName());
								dataALL.setOrgName(jsonObject2
										.getString("OrgName"));
								dataALL.setPondName(jsonObject2
										.getString("PondName"));
								dataALL.setVedioName(jsonObject2
										.getString("VedioName"));
								dataALL.setElectrics(jsonObject2
										.getString("Electrics"));
								alls.add(dataALL);
							}
							Intent intent = new Intent(LogActivety.this,
									MainActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("name", alls);
							// intent.putExtra("cityname",cityName);
							intent.putExtras(bundle);
							startActivity(intent);
							finish();
							if (login.getVisibility() == View.VISIBLE) {
								Editor edit = sharedPreferences.edit();
								edit.putBoolean("ischeck", true);
								edit.putString("name", nameEditText.getText()
										.toString());
								edit.putString("pass", passEditText.getText()
										.toString());
								edit.commit();
							} else {
								Editor edit = sharedPreferences.edit();
								edit.putBoolean("ischeck", false);
								edit.putString("name", nameEditText.getText()
										.toString());
								edit.putString("pass", passEditText.getText()
										.toString());
								edit.commit();
							}
							progressDialog.dismiss();

							overridePendingTransition(android.R.anim.fade_in,
									android.R.anim.fade_out);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "网络异常",
								Toast.LENGTH_SHORT).show();
						progressDialog.dismiss();
					}

				});

		return result;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	public String getPhoneInfo() {
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(TELEPHONY_SERVICE);
		String mtyb = android.os.Build.BRAND;// 手机品牌
		return mtyb;
	}
}
