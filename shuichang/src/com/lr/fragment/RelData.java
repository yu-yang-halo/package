package com.lr.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.MyApplication;
import com.bean.Fram;
import com.bean.SavaFra;
import com.example.huoyuyunshu.R;
import com.jauker.widget.BadgeView;
import com.lr.animalhusbandry.MessageActivety;
import com.lr.javaBean.DataALL;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import map.GpsMapAcitivty;

/**
 * 
 * 
 * @author 胡欢欢 这里通过广播来更新数据当然也用观察者模式
 * 
 */
public class RelData extends Fragment {
	private static final String Mybrodcast = null;
	private ArrayList<DataALL> list;
	private LinearLayout ll;
	private ListView listView;
	public HashMap<Integer, View> hashMap2 = new HashMap<Integer, View>();
	private MyRelData myRelData;
	public HashMap<String, String> hashMap;
	private HashMap<String, Fram> haMap;
	HashMap<String, Fram> hashMapf = new HashMap<String, Fram>();
	private MyApplication myApplication;
	private Mybrodcast myBro;
	@SuppressWarnings("unused")
	private HashMap<String, Fram> hashMapFram;
	private String deviceID;
	private Editor edit;
	public int num = 2;
	LinearLayout contr;
	SharedPreferences sharedPreferences;
	private HashMap<Integer, Integer> flaghashMap2;

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		list = (ArrayList<DataALL>) arguments.getSerializable("name");

		myApplication = (MyApplication) getActivity().getApplicationContext();
		flaghashMap2 = myApplication.flaghashMap2;

		try {
			sharedPreferences = getActivity().getSharedPreferences("hu",
					getActivity().MODE_PRIVATE);
			edit = sharedPreferences.edit();
			for (int i = 0; i < list.size(); i++) {
				DataALL dataALL = list.get(i);
				deviceID = dataALL.getDeviceID();
				Fram fram = SavaFra.getFram(deviceID);
				
				String string = sharedPreferences.getString(deviceID, "无");
				if (string.equals("无")) {
					continue;
				}
				System.out.println("FRAM_DATA: "+string);
				String[] c = string.split("\\|");
				fram.setO1(Float.parseFloat(c[0]));
				fram.setO2o(Float.parseFloat(c[1]));
				fram.setPH3(Float.parseFloat(c[2]));
				fram.setnH4N(Float.parseFloat(c[3]));
				fram.setTem5(Float.parseFloat(c[4]));
				fram.setT6(Float.parseFloat(c[5]));
				fram.setTime(c[6]);
				fram.setY7(Float.parseFloat(c[7]));// 盐度
				fram.setH2S8(Float.parseFloat(c[8]));
				fram.setZd9(Float.parseFloat(c[9]));
				fram.setYan10(Float.parseFloat(c[10]));
				fram.setDian11(Float.parseFloat(c[11]));
				fram.setHua12(Float.parseFloat(c[12]));
				fram.setQi13(Float.parseFloat(c[13]));
				fram.setFengSu14(Float.parseFloat(c[14]));
				fram.setFengXiang15(Float.parseFloat(c[15]));
				fram.setYe16(Float.parseFloat(c[16]));
				fram.setDQW17(Float.parseFloat(c[17]));
				fram.setDQS18(Float.parseFloat(c[18]));
				fram.setLat19(Float.parseFloat(c[19]));
				fram.setLgt20(Float.parseFloat(c[20]));
				fram.setSpd21(Float.parseFloat(c[21]));
				hashMapf.put(deviceID, fram);
			}
			if (hashMapf.keySet().size() == 0) {
				haMap = null;
			} else {
				if (haMap == null) {
					haMap = hashMapf;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		View view = inflater.inflate(R.layout.reldata, null);
		listView = (ListView) view.findViewById(R.id.rel);
		myRelData = new MyRelData();
		listView.setAdapter(myRelData);
		return view;

	}

	class MyRelData extends BaseAdapter {
		private Drawable background;
		private BadgeView badgeView;
		private TextView datatime;
		private float zd;
		private float yan10;
		private float dian11;
		private float hua;
		private float daqi;
		private float fengshu;
		private float fengxiang;
		private float ye;
		private float dQS;
		private float dQw;
		private float o1;
		private float o2o;
		private float ph3;
		private float nh4;
		private float tem;
		private float t6;
		private float y7;
		private double latitude;
		private double longitude;
		private float speed;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		public void setHashMap(HashMap<String, String> hashMap1) {
			hashMap = hashMap1;
			notifyDataSetChanged();

		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressWarnings({ "static-access", "deprecation" })
		// 此处因业务少 就不处理了
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			View view = getActivity().getLayoutInflater().from(getActivity())
					.inflate(R.layout.relcontent, null);
			TextView textView = (TextView) view.findViewById(R.id.reltitle);
			TextView textView2 = (TextView) view.findViewById(R.id.state);
			TextView remind = (TextView) view.findViewById(R.id.remind);
			datatime = (TextView) view.findViewById(R.id.datatime);
			final ImageView tag = (ImageView) view.findViewById(R.id.tag);
			View im = view.findViewById(R.id.image);
			if (myApplication.getHashMap(list.get(position).getDeviceID())) {
				remind.setText("【播报】");
			} else {
				remind.setText("【静音】");
			}
			try {
				badgeView = new BadgeView(getActivity());
				badgeView.setTargetView(im);
				badgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
				Integer temHash = myApplication.getTemHash(list.get(position)
						.getDeviceID());
				badgeView.setBadgeCount(temHash);
			} catch (Exception e) {
				// TODO: handle exception
				badgeView.setBadgeCount(0);
			}
			ll = (LinearLayout) view.findViewById(R.id.o2);
			background = ll.getBackground();
			if (flaghashMap2 != null && flaghashMap2.get(position) != null) {
				ll.setVisibility(flaghashMap2.get(position));
			}
			contr = (LinearLayout) view.findViewById(R.id.contr);// 点击按钮哈
			if (flaghashMap2 != null && flaghashMap2.get(position) != null
					&& flaghashMap2.get(position) == View.VISIBLE) {
				contr.setBackgroundColor(Color.rgb(246, 246, 246));
				tag.setImageResource(R.drawable.top);
			} else if (flaghashMap2 != null
					&& flaghashMap2.get(position) != null
					&& flaghashMap2.get(position) == View.GONE) {
				contr.setBackgroundDrawable(background);
				tag.setImageResource(R.drawable.blo);
			}
			// contr.setClickable(true);

			remind.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					View view1 = listView.getChildAt(position
							- listView.getFirstVisiblePosition());// 取得每个view
					TextView textView = (TextView) view1
							.findViewById(R.id.remind);
					String dvId = list.get(position).getDeviceID();
					if (textView.getText().equals("【播报】")) {
						textView.setText("【静音】");
						if (myApplication.getHashMap(dvId)) {
							myApplication.putHashMap(dvId, false);

						} else {
							myApplication.putHashMap(dvId, true);
						}

					} else {
						textView.setText("【播报】");
						myApplication.putHashMap(dvId, true);
					}

				}
			});
			im.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myApplication.setTemHashmap(list.get(position)
							.getDeviceID(), 0);
					Intent intent = new Intent(getActivity(),
							MessageActivety.class);
					intent.putExtra("devId", list.get(position).getDeviceID());
					startActivity(intent);
				}
			});
			textView.setText(list.get(position).getPondName());
			if (hashMap != null) {
				hashMap = myApplication.hashMap;
				String state = hashMap.get(list.get(position).getDeviceID()) != "在线" ? "离线"
						: "在线";
				if (state.equals("在线")) {
					textView2.setTextColor(Color.RED);// 设置颜色
				}
				textView2.setText("【" + state + "】");
			}
			if (haMap != null) {
				if (haMap.get(list.get(position).getDeviceID()) != null) {
					Fram fram = haMap.get(list.get(position).getDeviceID());
					edit.putString(
							list.get(position).getDeviceID(),
							fram.getO1() + "|" + fram.getO2o() + "|"
									+ fram.getPH3() + "|" + fram.getnH4N()
									+ "|" + fram.getTem5() + "|" + fram.getT6()
									+ "|" + fram.getTime() + "|" + fram.getY7()
									+ "|" + fram.getH2S8() + "|"
									+ fram.getZd9() + "|" + fram.getYan10()
									+ "|" + fram.getDian11() + "|"
									+ fram.getHua12() + "|" + fram.getQi13()
									+ "|" + fram.getFengSu14() + "|"
									+ fram.getFengXiang15() + "|"
									+ fram.getYe16() + "|" + fram.getDQW17()
									+ "|" + fram.getDQS18()+ "|" + fram.getLat19()
									+ "|" + fram.getLgt20()+ "|" + fram.getSpd21());
					edit.commit();// 假如本地种便于下次取值
					datatime.setVisibility(View.VISIBLE);
					float o1 = fram.getO1();
					String time = fram.getTime();
					datatime.setText(time);
					double lat19=fram.getLat19();
					double lgt20=fram.getLgt20();
					float spd21=fram.getSpd21();
					
					
					
					float max = 15;
					setvalue(max, o1, "溶氧", Color.RED, R.drawable.red);
					float O2 = fram.getO2o();
					setvalue(100, O2, "溶氧饱和", Color.YELLOW, R.drawable.tou);
					float ph3 = fram.getPH3();
					setvalue(14, ph3, "PH", Color.RED, R.drawable.red);
					float Nh4 = fram.getnH4N();
					setvalue(0.1, Nh4, "氨氮", Color.YELLOW, R.drawable.tou);
					float tem5 = fram.getTem5();
					setvalue(50, tem5, "温度", Color.YELLOW, R.drawable.tou);
					float t6 = fram.getT6();
					setvalue(0.1, t6, "亚硝酸盐", Color.YELLOW, R.drawable.tou);
					float Y7 = fram.getY7();
					setvalue(100, Y7, "液位", Color.RED, R.drawable.red);
					float H2S8 = fram.getH2S8();
					setvalue(0.1, H2S8, "H2S", Color.RED, R.drawable.red);
					float zd9 = fram.getZd9();// 浊度
					setvalue(1000, zd9, "浊度", Color.RED, R.drawable.red);
					float yan10 = fram.getYan10();
					setvalue(10, yan10, "盐度", Color.YELLOW, R.drawable.tou);
					float dian11 = fram.getDian11();
					setvalue(10, dian11, "电导率", Color.YELLOW, R.drawable.tou);
					float hua = fram.getHua12();
					setvalue(10, hua, "化学需养", Color.YELLOW, R.drawable.tou);
					setvalue(2000, fram.getQi13(), "大气压", Color.RED,
							R.drawable.red);
					setvalue(10, fram.getFengSu14(), "风速", Color.RED,
							R.drawable.red);
					setvalue(1000, fram.getFengXiang15(), "风向", Color.RED,
							R.drawable.red);
					setvalue(15, fram.getYe16(), "叶绿素", Color.YELLOW,
							R.drawable.tou);
					setvalue(50, fram.getDQW17(), "大气温度", Color.YELLOW,
							R.drawable.tou);
					setvalue(120, fram.getDQS18(), "大气湿度", Color.RED,
							R.drawable.red);
					setvalue(lat19, lgt20, "经度/纬度");

				} else {
					String string = sharedPreferences.getString(
							list.get(position).getDeviceID(), "无");
					if (!string.equals("无")) {
						String[] c = string.split("\\|");

						try {

							o1 = Float.parseFloat(c[0]);
						} catch (Exception e) {
							// TODO: handle exception
							o1 = 0;
						}
						try {

							o2o = Float.parseFloat(c[1]);
						} catch (Exception e) {
							// TODO: handle exception
							o2o = 0;
						}
						try {
							ph3 = Float.parseFloat(c[2]);

						} catch (Exception e) {
							// TODO: handle exception
							ph3 = 0;
						}
						try {

							nh4 = Float.parseFloat(c[3]);
						} catch (Exception e) {
							// TODO: handle exception
							nh4 = 0;
						}
						try {

							tem = Float.parseFloat(c[4]);
						} catch (Exception e) {
							// TODO: handle exception
							tem = 0;
						}
						try {
							t6 = Float.parseFloat(c[5]);

						} catch (Exception e) {
							// TODO: handle exception
							t6 = 0;
						}
						try {

							y7 = Float.parseFloat(c[7]);
						} catch (Exception e) {
							// TODO: handle exception
							y7 = 0;
						}
						float H2S;
						try {
							H2S = Float.parseFloat(c[8]);
						} catch (Exception exception) {
							H2S = 0;
						}
						try {
							zd = Float.parseFloat(c[9]);
						} catch (Exception e) {
							// TODO: handle exception
							zd = 0;
						}
						try {
							yan10 = Float.parseFloat(c[10]);
						} catch (Exception e) {
							// TODO: handle exception
							yan10 = 0;
						}
						try {
							dian11 = Float.parseFloat(c[11]);
						} catch (Exception e) {
							// TODO: handle exception
							dian11 = 0;
						}

						try {
							hua = Float.parseFloat(c[12]);
						} catch (Exception e) {
							// TODO: handle exception
							hua = 0;
						}
						try {
							daqi = Float.parseFloat(c[13]);
						} catch (Exception e) {
							// TODO: handle exception
							daqi = 0;
						}
						try {
							fengshu = Float.parseFloat(c[14]);
						} catch (Exception e) {
							// TODO: handle exception
							fengshu = 0;
						}
						try {
							fengxiang = Float.parseFloat(c[15]);
						} catch (Exception e) {
							// TODO: handle exception
							fengxiang = 0;
						}

						try {
							ye = Float.parseFloat(c[16]);
						} catch (Exception e) {
							// TODO: handle exception
							ye = 0;
						}
						try {
							dQS = Float.parseFloat(c[17]);
						} catch (Exception e) {
							// TODO: handle exception
							dQS = 0;
						}
						try {
							dQw = Float.parseFloat(c[18]);
						} catch (Exception e) {
							// TODO: handle exception
							dQw = 0;
						}
						try {
							latitude = Float.parseFloat(c[19]);
						} catch (Exception e) {
							// TODO: handle exception
							latitude = 0;
						}
						try {
							longitude = Float.parseFloat(c[20]);
						} catch (Exception e) {
							// TODO: handle exception
							longitude = 0;
						}


						
						String time = c[6];

						datatime.setVisibility(View.VISIBLE);
						datatime.setText(time);
						setvalue(15, o1, "溶氧", Color.RED, R.drawable.red);
						setvalue(100, o2o, "溶氧饱和度", Color.YELLOW,
								R.drawable.tou);
						setvalue(14, ph3, "PH", Color.RED, R.drawable.red);
						setvalue(0.1, nh4, "氨氮", Color.YELLOW, R.drawable.tou);
						setvalue(50, tem, "温度", Color.YELLOW, R.drawable.tou);
						setvalue(0.1, t6, "亚硝酸盐", Color.YELLOW, R.drawable.tou);
						setvalue(100, y7, "液位", Color.RED, R.drawable.red);
						setvalue(0.1, H2S, "H2S", Color.RED, R.drawable.red);
						setvalue(1000, zd, "浊度", Color.RED, R.drawable.red);
						setvalue(1000, yan10, "盐度", Color.RED, R.drawable.red);
						setvalue(10, dian11, "电导率", Color.YELLOW,
								R.drawable.tou);
						setvalue(10, hua, "化学需养", Color.YELLOW, R.drawable.tou);
						setvalue(2000, daqi, "大气压", Color.RED, R.drawable.red);
						setvalue(10, fengshu, "风速", Color.RED, R.drawable.red);
						setvalue(1000, fengxiang, "风向", Color.RED,
								R.drawable.red);
						setvalue(15, ye, "叶绿素", Color.YELLOW, R.drawable.tou);
						setvalue(50, dQS, "大气温度", Color.YELLOW, R.drawable.tou);
						setvalue(120, dQw, "大气湿度", Color.RED, R.drawable.red);
						setvalue(latitude, longitude, "经度/纬度");

					}
				}

			}
			contr.setOnClickListener(new OnClickListener() {
				private ImageView tag1;
				private View view1;

				@Override
				public void onClick(View v) {

					view1 = listView.getChildAt(position
							- listView.getFirstVisiblePosition());// 取得每个view
					try {

						tag1 = (ImageView) view1.findViewById(R.id.tag);
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getActivity(), position + "", 1).show();
						return;

					}

					LinearLayout vLayout = (LinearLayout) view1
							.findViewById(R.id.o2);
					if (vLayout.getChildCount() > 0
							&& (vLayout.getVisibility() == View.VISIBLE)) {
						vLayout.setVisibility(View.GONE);
						if (tag1 != null) {

							tag1.setImageResource(R.drawable.blo);
						}
						flaghashMap2.put(position, View.GONE);// 标志位
					} else if (vLayout.getChildCount() > 0
							&& (vLayout.getVisibility() == View.GONE)) {
						vLayout.setVisibility(View.VISIBLE);
						if (tag1 != null) {
							tag1.setImageResource(R.drawable.top);
						}
						flaghashMap2.put(position, View.VISIBLE);// 保存相应的状态
					}

				}
			});
			return view;
		}
		
		//加入经度纬度速度
		public void setvalue(double lat19, double lgt20,String text) {
			if (lat19==0 || lgt20==0) {
				return;

			}
			final View gpsView = getActivity().getLayoutInflater().from(getActivity())
					.inflate(R.layout.gpsdata, null);
			
			TextView name = (TextView) gpsView.findViewById(R.id.tv);
			TextView latText = (TextView) gpsView.findViewById(R.id.lat);
			TextView lgtText = (TextView) gpsView.findViewById(R.id.lgt);
			
			name.setText(text);
			String latStr=String.format("%.6f", lat19);
			String lgtStr=String.format("%.6f", lgt20);
			latText.setText(latStr);
			lgtText.setText(lgtStr);
			gpsView.setTag(lgtStr+","+latStr);
			ll.addView(gpsView);
			gpsView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(RelData.this.getActivity(),GpsMapAcitivty.class);
					String gpsDataArr=(String) gpsView.getTag();
					intent.putExtra("gpsData", gpsDataArr);
					RelData.this.getActivity().startActivity(intent);
				}
			});

	}

		// 根据不同的刻度画线 此处最大值以后添加暂时为个人意见
		public void setvalue(double max, float value1, String text, int color,
				int dra) {
			if (value1 == 0) {
				return;

			}
			int with = getResources().getDrawable(R.drawable.jing)
					.getIntrinsicWidth();
			@SuppressWarnings("static-access")
			View webdu = getActivity().getLayoutInflater().from(getActivity())
					.inflate(R.layout.wendu, null);
			TextView name = (TextView) webdu.findViewById(R.id.tv);
			TextView value = (TextView) webdu.findViewById(R.id.value);
			ImageView imageView = (ImageView) webdu.findViewById(R.id.tou);
			imageView.setImageResource(dra);
			View huangjing = webdu.findViewById(R.id.huanjing);
			huangjing.setBackgroundColor(color);
			double d = with / max * value1;
			LayoutParams layoutParams = huangjing.getLayoutParams();
			int height = layoutParams.height;
			layoutParams.width = (int) d;
			layoutParams.height = height;
			value.setText(value1 + "");
			huangjing.setLayoutParams(layoutParams);
			name.setText(text);
			ll.addView(webdu);

		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

		super.onStart();

	}

	@Override
	public void onResume() {

		// TODO Auto-generated method stub
		myRelData.setHashMap(myApplication.hashMap);
		myBro = new Mybrodcast();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("hu");
		getActivity().registerReceiver(myBro, intentFilter);// 处理广播 不然会报错
		// 在程序退出之后
		// listView.setAdapter(myRelData);
		myRelData.notifyDataSetChanged();
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		try {

			getActivity().unregisterReceiver(myBro);
		} catch (Exception e) {
			// TODO: handle exception
			super.onDestroy();
		}

		super.onDestroy();
	}

	class Mybrodcast extends BroadcastReceiver {// 注册广播
		@Override
		public void onReceive(Context context, Intent intent) {

			hashMap = myApplication.hashMap;

			haMap = myApplication.hashMapFram;
			if (haMap == null) {
				haMap = hashMapf;
			}
			Log.e("sta", list.get(0).getDeviceID());
			myRelData.notifyDataSetChanged();
			//
		}

	}

}
