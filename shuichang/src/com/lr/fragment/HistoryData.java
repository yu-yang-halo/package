package com.lr.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.huoyuyunshu.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lr.javaBean.DataALL;
import com.util.MyCharActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class HistoryData extends Fragment implements OnCheckedChangeListener,
		OnClickListener {
	private static boolean isresum = false;
	ArrayList<String> tem = new ArrayList<String>();
	HashMap<String, ArrayList<ArrayList<String>>> hashMap = new HashMap<String, ArrayList<ArrayList<String>>>();
	private String url = "http://60.173.247.137:9010/ClientService.svc/GetSensorData";
	ArrayList<DataALL> list;
	private String deviceid;
	private View view;
	private LinearLayout layout;
	private String yes;
	private String today;
	private LinearLayout layout2;
	private String after;
	private View succfulorError;
	HashMap<String, HttpUtils> httphashmap = new HashMap<String, HttpUtils>();
	private HashMap<String, Boolean> booleanHashMap = new HashMap<String, Boolean>();
	private String oneString;

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		todayToafter();
		Bundle bundle = getArguments();
		list = (ArrayList<DataALL>) bundle.getSerializable("name");
		view = inflater.inflate(R.layout.listchar, null);
		layout = (LinearLayout) view.findViewById(R.id.listcharcontent);

		for (int i = 0; i < list.size(); i++) {
			View view1 = inflater.inflate(R.layout.contentchar, null);

			TextView title = (TextView) view1.findViewById(R.id.titlechar);
			LinearLayout titleclick = (LinearLayout) view1
					.findViewById(R.id.titlehistory);// 操作标题
			titleclick.setTag("" + i);
			titleclick.setOnClickListener(this);
			RadioGroup check = (RadioGroup) view1
					.findViewById(R.id.historycheck);// 操作日期
			check.setTag("" + i);// 标记哪个
			check.setOnCheckedChangeListener(this);
			title.setText(list.get(i).getVedioName());
			layout2 = (LinearLayout) view1.findViewById(R.id.charl);
			getDataJson(today, list.get(i).getDeviceID(), layout2);// 加载数据图表
			layout.addView(view1);// 添加view
			View charview = view1.findViewById(R.id.mess);
			if (list.size() > 3) {
				charview.setVisibility(View.GONE);
				ImageView imageView = (ImageView) view1
						.findViewById(R.id.imageflag);
				imageView.setImageResource(R.drawable.top);
			}

		}

		return view;
	}

	private void todayToafter() {
		Calendar cal11 = Calendar.getInstance();
		cal11.add(Calendar.DATE, -2);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		after = sdf1.format(cal11.getTime());
		Calendar cal1 = Calendar.getInstance();
		cal1.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		yes = sdf.format(cal1.getTime());
		today = sdf.format(Calendar.getInstance().getTime());
		booleanHashMap.put(today, true);
		booleanHashMap.put(yes, false);
		booleanHashMap.put(after, false);
	}

	@SuppressWarnings("unused")
	private void getDataJson(String time, String id, LinearLayout layout) {

		JSONObject jsonObject1 = dataExchageJson(time, id);
		getData(url, jsonObject1, layout, time, id);

	}

	private JSONObject dataExchageJson(String time, String devID) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("deviceId", devID);
			jsonObject.put("dateTime", time);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

	public String getData(String url, JSONObject jsonObject,
			final LinearLayout layout, final String time, final String id) {
		if (!httphashmap.isEmpty() && !httphashmap.keySet().isEmpty()) {
			if (httphashmap.get(id) != null) {
				HttpUtils httpUtils = httphashmap.get(id);
				httpUtils.getHttpClient().getConnectionManager().shutdown();
			}

		}
		HttpUtils httpUtils = new HttpUtils();
		httphashmap.put(id, httpUtils);

		RequestParams requestParams = null;
		try {
			deviceid = jsonObject.getString("deviceId");
			requestParams = new RequestParams();
			requestParams.setContentType("application/json");
			StringEntity stringEntity = new StringEntity(jsonObject.toString(),
					"utf-8");
			requestParams.setBodyEntity(stringEntity);
			layout.setLayoutParams(new LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.FILL_PARENT, 500));
			layout.removeAllViews();
			// if (isresum && getActivity() != null
			// && layout.getChildCount() == 0) {
			succfulorError = getActivity().getLayoutInflater().inflate(
					R.layout.proshow, null);
			layout.addView(succfulorError, 0);
		} catch (Exception exception) {
		}
		httpUtils.send(HttpMethod.POST, url, requestParams,
				new RequestCallBack<String>() {
					private String oneString;

					private View err;

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();

						// }
					}

					@SuppressWarnings("deprecation")
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						layout.removeAllViews();
						android.widget.LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
								android.widget.LinearLayout.LayoutParams.FILL_PARENT,
								android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
						layoutParams.topMargin = 30;

						layout.setLayoutParams(layoutParams);
						String result = responseInfo.result;
						SouleToString(layout, time, result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub

						if (getActivity() != null) {
							err = getActivity().getLayoutInflater().inflate(
									R.layout.errorload, null);
						}
						layout.removeView(succfulorError);

						if (isresum) {
							if (error.getMessage().contains(
									"java.net.SocketTimeoutException")
									&& getActivity() != null) {

								if (layout.getChildCount() == 0) {
									layout.addView(err);
								}

							}
						}

					}

				});
		return id;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		isresum = true;
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

		if (!httphashmap.isEmpty() && !httphashmap.keySet().isEmpty()) {
			Iterator<String> iterator = httphashmap.keySet().iterator();
			while (iterator.hasNext()) {
				String next = iterator.next();
				httphashmap.get(next).getHttpClient().getConnectionManager()
						.shutdown();

			}
		}
		super.onPause();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		String tag = group.getTag().toString();
		int position = Integer.parseInt(tag);
		if (checkedId == R.id.yesterday) {
			View childAt = layout.getChildAt(position);
			LinearLayout layout = (LinearLayout) childAt
					.findViewById(R.id.charl);
			setDateFlage(yes);
			getDataJson(yes, list.get(position).getDeviceID(), layout);
		} else if (checkedId == R.id.today) {

			View childAt = layout.getChildAt(position);
			LinearLayout layout = (LinearLayout) childAt
					.findViewById(R.id.charl);

			setDateFlage(today);
			// layout.removeAllViews();
			getDataJson(today, list.get(position).getDeviceID(), layout);
		} else if (checkedId == R.id.afterday) {

			View childAt = layout.getChildAt(position);
			LinearLayout layout = (LinearLayout) childAt
					.findViewById(R.id.charl);
			// layout.removeAllViews();
			setDateFlage(after);
			getDataJson(after, list.get(position).getDeviceID(), layout);

		}
	}

	public void setDateFlage(String time) {
		Set<String> keySet = booleanHashMap.keySet();
		for (String string : keySet) {
			if (time.equals(string)) {
				booleanHashMap.put(string, true);
			} else {
				booleanHashMap.put(string, false);
			}

		}
	}

	public synchronized void SouleToString(final LinearLayout layout,
			final String time, String result) {
		View view2 = null;
		ArrayList<ArrayList<String>> all = new ArrayList<ArrayList<String>>();
		ArrayList<String> ph = new ArrayList<String>();
		ArrayList<String> O2 = new ArrayList<String>();
		ArrayList<String> co2 = new ArrayList<String>();
		ArrayList<String> sd = new ArrayList<String>();
		ArrayList<String> Hno3 = new ArrayList<String>();// 亚硝酸盐
		ArrayList<String> weaterPosition = new ArrayList<String>();// 液位
		ArrayList<String> h2s = new ArrayList<String>();// 硫化氢
		ArrayList<String> tur = new ArrayList<String>();// 浑浊度
		ArrayList<String> atuPressed = new ArrayList<String>();// 大气压
		ArrayList<String> fs = new ArrayList<String>();// 风速
		ArrayList<String> fx = new ArrayList<String>();// 风向
		ArrayList<String> yls = new ArrayList<String>();// 叶绿色
		ArrayList<String> at = new ArrayList<String>();// 大气温度
		ArrayList<String> as = new ArrayList<String>();// 大气湿度

		ArrayList<String> title = new ArrayList<String>();
		ArrayList<Integer> pointArrayList = new ArrayList<Integer>();
		ArrayList<Integer> coloList = new ArrayList<Integer>();
		// int[] color = new int[] { Color.RED, Color.GREEN, Color.BLUE,
		// Color.YELLOW };
		Log.e("bwm", result);
		if (!"".equals(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				String messgae = jsonObject.getString("GetSensorDataResult");
				if (messgae.equals("[]")) {

					layout.addView(getActivity().getLayoutInflater().inflate(
							R.layout.nodatachar, null));
					return;

				}
				JSONArray array = new JSONArray(messgae);

				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject2 = array.getJSONObject(i);
					String point = jsonObject2.getString("ReceivedTime");
					pointArrayList.add(Integer.parseInt(point));
					String o2String = jsonObject2.getString("Param1");// 溶氧
					String phString = jsonObject2.getString("Param3");// ph
					String co2strString = jsonObject2.getString("Param4");// 氨氮

					String wendu = jsonObject2// 温度
							.getString("Param5");
					String HNo3 = jsonObject2.getString("Param6");// 亚硝酸盐
					// String WaterPosition=jsonObject2.getString("Param7");//液位
					String H2s = jsonObject2.getString("Param8");// 硫化氢
					String turbidity = jsonObject2.getString("Param9");// 浑浊度
					String Atmpre = jsonObject2.getString("Param13");// 大气压
					String fengsu = jsonObject2.getString("Param14");// 风速
					String fengxiang = jsonObject2.getString("Param15");// 风向
					String yelvsu = jsonObject2.getString("Param16");// 叶绿素
					String atmTem = jsonObject2.getString("Param17");// 大气温度
					String atmHum = jsonObject2.getString("Param18");// 大气湿度
					O2.add(o2String);// 光照
					ph.add(phString);
					co2.add(co2strString);
					// pointArrayList.add(point);
					sd.add(wendu);
					Hno3.add(HNo3);
					// weaterPosition.add(WaterPosition);
					h2s.add(H2s);
					tur.add(turbidity);
					atuPressed.add(Atmpre);
					fs.add(fengsu);
					fx.add(fengxiang);
					yls.add(yelvsu);
					at.add(atmTem);
					as.add(atmHum);
				}
				if (!O2.contains("null")) {

					all.add(O2);
					title.add("溶氧");
					coloList.add(Color.RED);
				}
				if (!ph.contains("null") && getIsAllZero(ph)) {
					all.add(ph);
					title.add("PH  ");
					coloList.add(Color.GREEN);
				}
				if (!co2.contains("null") && getIsAllZero(co2)) {
					Log.e("co2", co2.get(0));
					title.add("氨氮");
					all.add(co2);
					coloList.add(Color.LTGRAY);
				}
				if (!sd.contains("null")) {
					all.add(sd);
					title.add("温度");
					coloList.add(Color.YELLOW);
				}
				if (!Hno3.contains("null") && getIsAllZero(Hno3)) {
					all.add(Hno3);
					title.add("亚硝酸盐");
					coloList.add(Color.rgb(0, 129, 188));

				}
				if (!h2s.contains("null") && getIsAllZero(h2s)) {
					all.add(h2s);
					title.add("硫化氢");
					coloList.add(Color.GREEN);
				}
				if (!tur.contains("null") && getIsAllZero(tur)) {
					all.add(tur);
					title.add("浊度");
					coloList.add(Color.GREEN);

				}
				if (!atuPressed.contains("null") && getIsAllZero(atuPressed)) {
					all.add(atuPressed);
					title.add("大气压");
					coloList.add(Color.YELLOW);
				}
				if (!fs.contains("null") && getIsAllZero(fs)) {
					all.add(fs);
					title.add("风速");
					coloList.add(Color.WHITE);
				}
				if (!fx.contains("null") && getIsAllZero(fx)) {
					all.add(fx);
					title.add("风向");
					coloList.add(Color.RED);
				}
				if (!yls.contains("null") && getIsAllZero(yls)) {
					all.add(yls);
					title.add("叶绿素");
					coloList.add(Color.GREEN);
				}
				if (!at.contains("null") && getIsAllZero(at)) {
					all.add(at);
					title.add("大气温度");
					coloList.add(Color.RED);

				}
				if (!as.contains("null") && getIsAllZero(as)) {
					all.add(as);
					title.add("大气湿度");
					coloList.add(Color.YELLOW);
				}

				hashMap.put(deviceid, all);

				// if (booleanHashMap.get(time) == true) {
				layout.removeAllViews();
				if (layout.getChildCount() < 1) {
					for (int i = 0; i < all.size(); i++) {
						View view1 = new MyCharActivity(getActivity(),
								all.get(i), pointArrayList, title.get(i),
								coloList.get(i)).getDataview();
						layout.addView(view1, i, new LayoutParams(
								LayoutParams.FILL_PARENT, 500));
					}

				}
				// }

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int position = Integer.parseInt(v.getTag().toString());
		View view = layout.getChildAt(position);
		ImageView image = (ImageView) view.findViewById(R.id.imageflag);

		View viewmessage = view.findViewById(R.id.mess);
		if (View.VISIBLE == viewmessage.getVisibility()) {
			viewmessage.setVisibility(View.GONE);
			image.setImageResource(R.drawable.top);

		} else if (View.GONE == viewmessage.getVisibility()) {
			viewmessage.setVisibility(View.VISIBLE);
			image.setImageResource(R.drawable.blo);
		}
	}

	public boolean getIsAllZero(ArrayList<String> parms) {
		for (int i = 0; i < parms.size(); i++) {
			if (parms.get(i) != "0.0") {
				return true;
			}
		}
		return false;

	}

}
