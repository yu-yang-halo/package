package com.lr.animalhusbandry;

import interfaceDao.Myinterface;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.app.MyApplication;
import com.bean.Fram;
import com.bean.SavaFra;
import com.example.huoyuyunshu.R;
import com.lr.adapter.FragmentViewPagerAdapter;
import com.lr.fragment.DataFragement;
import com.lr.fragment.DeviConFragment;
import com.lr.fragment.MonitiorContr;
import com.lr.fragment.SetFragment;
import com.lr.fragment.WeaFragment;
import com.lr.javaBean.DataALL;
import com.lr.service.Myservice;
import com.lr.service.Myservice.MyBinder;
import com.lr.service.SocketHandl;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author lenovo
 * 
 */
public class MainActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener, OnCheckedChangeListener {
	private com.util.Myviewpage viewPager;
	private RadioGroup group;// 设置所有的求换fragment

	private List<Fragment> list = new ArrayList<Fragment>();
	private MonitiorContr monitiorContr;
	private FragmentViewPagerAdapter fragmentViewPagerAdapter;
	private RadioButton dataButton;
	private RadioButton moniButton;
	private RadioButton setButton;
	private RadioButton deveButton;

	private LinearLayout ll;
	ArrayList<DataALL> listvalue;
	private long time = 0;
	private HashMap<Integer, Integer> flaghashMap2;
	public boolean isNetWork = true;
	private MyBro myBro;

	public Myservice myservice;
	private boolean isCan = true;
	private MyApplication myApplication;
	private SocketHandl socketHandl;
	private DataFragement dataFragement;
	private SetFragment setFragment;
	private TextView back;
	private TextView homeback;

	private SharedPreferences sharedPreferences2;
	private DeviConFragment deviConFragment;
	Handler handler = new Handler() {
		private AlertDialog.Builder builder;

		public void handleMessage(android.os.Message msg) {
			try {
				if (myservice != null) {

					Socket socket = myservice.getSocket();// 通过接口回调
					// 得到当前的socket对象
					if (socket != null) {
						socket.close();// 光比socket释放资源

					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (msg.what == 1) {
				Intent intent2 = new Intent(MainActivity.this, Myservice.class);
				intent2.putExtra("id", id);
				intent2.putExtras(bundle2);

				bindService(intent2, serviceConnection,
						MainActivity.this.BIND_AUTO_CREATE);// 绑定服务开启socet
				sendBrocasdMethod(true);// 发广播提醒处理网络断开的页面
			} else {
				builder = new AlertDialog.Builder(MainActivity.this);
				unbindService(serviceConnection);
				myservice.setMyinterface(null);// 制空防止出现空指针
				if (!pingIpAddr()) {
					sendBrocasdMethod(false);
					isCan = true;
					TestNetWork();
					return;
				} else {
					builder.setMessage("账号异常,请重新按确认");
					sendBrocasdMethod(true);
				}
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								Intent intent2 = new Intent(MainActivity.this,
										Myservice.class);
								intent2.putExtra("id", id);
								intent2.putExtras(bundle2);
								bindService(intent2, serviceConnection,
										MainActivity.this.BIND_AUTO_CREATE);
							}
						});
				builder.setNegativeButton("退出",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// startActivity(new
								// Intent(MainActivity.this,LogActivety.class));

								finish();

								builder.create().dismiss();
							}

						});
				AlertDialog create = builder.create();
				create.setCanceledOnTouchOutside(false);// 设置外部按钮不可用
				create.show();

			}
			;
		}

	};
	private String id;
	private SharedPreferences sharedPreferences;
	private Bundle bundle2;
	private ServiceConnection serviceConnection;

	private RadioButton weaButton;
	private WeaFragment weaFragment;
	private HashMap<String, Integer> now;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Context applicationContext = getApplicationContext();
		myApplication = (MyApplication) applicationContext;
		if (savedInstanceState != null) {
			myApplication.hashMap = (HashMap<String, String>) savedInstanceState
					.getSerializable("map");
			HashMap<String, String> hashMap1 = myApplication.hashMap;
			if (myApplication.hashMap != null
					&& myApplication.hashMap.keySet() != null) {
				Set<String> keySet = myApplication.hashMap.keySet();
				Iterator<String> iterator = keySet.iterator();
				while (iterator.hasNext()) {
					String devid = iterator.next();
					if (pingIpAddr()) {
						if (hashMap1.get(devid).equals("在线")) {
							myApplication.hashMap.put(devid, "在线");
						} else {
							myApplication.hashMap.put(devid, "离线");
						}
					} else {
						myApplication.hashMap.put(devid, "离线");
					}

				}
				id = savedInstanceState.getString("id");// 防止被系统干掉之后 依然能够返回被干的状态

			}
		}

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		myApplication = (MyApplication) getApplicationContext();
		now = myApplication.getHashMap();
		flaghashMap2 = myApplication.flaghashMap2;
		int with = getWindowManager().getDefaultDisplay().getWidth();
		int higt = getWindowManager().getDefaultDisplay().getHeight();
		myApplication.allwith = with;
		myApplication.allhigh = higt;
		// initSocket();
		Intent intent = getIntent();
		bundle2 = intent.getExtras();
		listvalue = (ArrayList<DataALL>) bundle2.getSerializable("name");
		sharedPreferences2 = getSharedPreferences("num", MODE_PRIVATE);

		for (int i = 0; i < listvalue.size(); i++) {
			String deviceID = listvalue.get(i).getDeviceID();
			SavaFra.put(deviceID);
			flaghashMap2.put(i, View.VISIBLE);
			myApplication.setTemHashmap(deviceID,
					sharedPreferences2.getInt(deviceID, 0));
			now.put(deviceID, 0);

		}
		setContentView(R.layout.activity_main);
		initView();
		group.setOnCheckedChangeListener(this);
		dataFragement = new DataFragement();
		dataFragement.setArguments(bundle2);
		monitiorContr = new MonitiorContr();
		monitiorContr.setArguments(bundle2);
		setFragment = new SetFragment();
		deviConFragment = new DeviConFragment();
		weaFragment = new WeaFragment();
		deviConFragment.setArguments(bundle2);
		setFragment.setArguments(bundle2);
		weaFragment.setArguments(bundle2);
		list.add(dataFragement);
		list.add(monitiorContr);
		list.add(setFragment);
		list.add(deviConFragment);
		list.add(weaFragment);

		viewPager.setOnPageChangeListener(this);
		fragmentViewPagerAdapter = new FragmentViewPagerAdapter(
				getSupportFragmentManager(), list);
		viewPager.setOffscreenPageLimit(5);// 预加载4次 这样可以防止视频到达另外一个页面关闭
		viewPager.setAdapter(fragmentViewPagerAdapter);

		if (myApplication != null && myApplication.getId() != null) {
			sharedPreferences = getSharedPreferences("id", MODE_PRIVATE);
			id = myApplication.getId();
			Editor edit = sharedPreferences.edit();
			edit.putString("id", id);
			edit.commit();
		}

		serviceConnection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onServiceConnected(ComponentName arg0, IBinder arg1) {
				// TODO Auto-generated method stub
				myservice = ((MyBinder) arg1).getMyservice();
				Notification foregroundNote;
				Notification.Builder mNotifyBuilder = new Notification.Builder(
						getApplicationContext());
				foregroundNote = mNotifyBuilder.setSmallIcon(R.drawable.app)
						.build();
				NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				Intent intent = new Intent(MainActivity.this,
						MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				PendingIntent activity = PendingIntent.getActivity(
						MainActivity.this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				foregroundNote.setLatestEventInfo(MainActivity.this,
						"合肥乐然物联网技术有限公司", "联系电话：18655168068", activity);
				mNotifyManager.notify(1, foregroundNote);
				myservice.startForeground(1, foregroundNote);
				myservice.setMyinterface(new Myinterface() {// 重新方法
							@Override
							public void state(HashMap<String, String> hashMap,
									String string, String devId) {
								// TODO Auto-generated method stub
								if (string.equals("-4")) {
									Log.e("mn", "离线");
									handler.sendEmptyMessage(0);
									isCan = true;
								}
								if (string.equals("19")) {
									setFragment.exchangeValue();

								}
								if (deviConFragment.getUserVisibleHint()
										&& string.equals("e")) {
									deviConFragment.ChangeViewState(hashMap,
											devId);

								}
								Intent intent = new Intent();
								intent.setAction("hu");
								myApplication.hashMap = hashMap;
								intent.putExtra("feige", string);
								sendBroadcast(intent);
								sendBroadcast(new Intent().setAction("talk"));
							}
						});
				MainActivity.this.myservice = myservice;
			}
		};
		if (!pingIpAddr()) {
			isCan = true;
			TestNetWorkDataFragment();
			return;
		} else {
			Intent intent2 = new Intent(this, Myservice.class);
			intent2.putExtra("id", id);
			intent2.putExtras(bundle2);
			bindService(intent2, serviceConnection,
					MainActivity.this.BIND_AUTO_CREATE);
		}

	}

	private void TestNetWork() {

		new Thread() {
			public void run() {
				while (isCan) {
					if (pingIpAddr()) {
						handler.sendEmptyMessage(1);
						isCan = false;
					}
				}
			};
		}.start();// 监听网络

	}

	private void TestNetWorkDataFragment() {

		new Thread() {
			public void run() {
				while (isCan) {
					if (pingIpAddr()) {
						handler.sendEmptyMessage(1);
						isCan = false;
					} else if (!pingIpAddr() && DataFragement.getResume()) {
						sendBrocasdMethod(false);
						DataFragement.setResumer(false);
					}
				}
			};
		}.start();
	}

	public void initView() {
		viewPager = (com.util.Myviewpage) findViewById(R.id.vp);
		group = (RadioGroup) findViewById(R.id.rd);
		dataButton = (RadioButton) findViewById(R.id.data);
		dataButton.setChecked(true);
		moniButton = (RadioButton) findViewById(R.id.monitoring);
		setButton = (RadioButton) findViewById(R.id.setData);
		deveButton = (RadioButton) findViewById(R.id.deviceControl);
		weaButton = (RadioButton) findViewById(R.id.wea);
		ll = (LinearLayout) findViewById(R.id.ll);
		back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(this);
		homeback = (TextView) findViewById(R.id.home_back);
		homeback.setOnClickListener(this);
	}

	public void Exchange(Myservice myservice) {
		if (viewPager.getCurrentItem() == 2) {
			SetFragment fragment = (SetFragment) list.get(2);
			fragment.setMyservice(myservice);// 回调到设置页面

		} else if (viewPager.getCurrentItem() == 3) {
			DeviConFragment deviConFragment = (DeviConFragment) list.get(3);
			deviConFragment.setMyservice(myservice);// 回调到控制页面
		}

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

		switch (arg0) {
		case 0:
			dataButton.setChecked(true);
			break;
		case 1:
			moniButton.setChecked(true);
			break;
		case 2:
			setButton.setChecked(true);
			Exchange(myservice);
			break;
		case 3:
			deveButton.setChecked(true);
			Exchange(myservice);
			break;
		case 4:
			weaButton.setChecked(true);
			break;
		default:
			break;
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.data:
			viewPager.setCurrentItem(0);
			break;
		case R.id.monitoring:
			viewPager.setCurrentItem(1);
			break;
		default:
		case R.id.setData:
			viewPager.setCurrentItem(2);
			break;
		case R.id.deviceControl:
			viewPager.setCurrentItem(3);
			break;
		case R.id.wea:
			viewPager.setCurrentItem(4);
			break;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		myApplication.hashMap = (HashMap<String, String>) savedInstanceState
				.getSerializable("map");

	}

	protected void onSaveInstanceState(Bundle outState) {
		// outState.putString("name", "liming");
		outState.putSerializable("map", myApplication.hashMap);
		outState.putString("id", myApplication.getId());
		if (myApplication.getId() == null) {
			outState.putString("id", id);
		}
		super.onSaveInstanceState(outState);
	}

	private static final boolean pingIpAddr() {
		String mPingIpAddrResult;
		try {
			String ipAddress = "60.173.247.137";
			Process p = Runtime.getRuntime().exec(
					"ping -c 1 -w 100 " + ipAddress);
			int status = p.waitFor();
			if (status == 0) {
				return true;
			} else {
				mPingIpAddrResult = "Fail: IP addr not reachable";
			}
		} catch (IOException e) {
			mPingIpAddrResult = "Fail: IOException";
		} catch (InterruptedException e) {
			mPingIpAddrResult = "Fail: InterruptedException";
		}
		return false;
	}

	@Override
	protected void onResume() {

		myBro = new MyBro();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("flag");
		registerReceiver(myBro, intentFilter);

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(myBro);

		// 扑捉异常 假如为空依然取的是上一次这样可以防止炮炸
		SharedPreferences sharedPreferences = getSharedPreferences("hu",
				MODE_PRIVATE);
		HashMap<String, Fram> hashMapFram2 = myApplication.hashMapFram;
		Editor edit = sharedPreferences.edit();
		for (int i = 0; i < listvalue.size(); i++) {
			DataALL dataALL = listvalue.get(i);
			String deviceID = dataALL.getDeviceID();
			if (null != hashMapFram2 && null != hashMapFram2.get(deviceID)) {
				Fram fram = hashMapFram2.get(deviceID);
				float o1 = fram.getO1();
				float o2 = fram.getO2o();
				float ph3 = fram.getPH3();
				float getnH4N = fram.getnH4N();
				float tem5 = fram.getTem5();
				float t6 = fram.getT6();
				String time = fram.getTime();
				Float y7 = fram.getY7();
				Float h2s = fram.getH2S8();
				float zd9 = fram.getZd9();
				float yan10 = fram.getYan10();
				float dian11 = fram.getDian11();
				float hua12 = fram.getHua12();
				float qi13 = fram.getQi13();
				float fengSu14 = fram.getFengSu14();
				float fengxiang = fram.getFengXiang15();
				float ye16 = fram.getYe16();
				float Qi17 = fram.getDQW17();
				float QiS = fram.getDQS18();

				edit.putString(deviceID, o1 + "|" + o2 + "|" + ph3 + "|"
						+ getnH4N + "|" + tem5 + "|" + t6 + "|" + time + "|"
						+ y7 + "|" + h2s + "|" + zd9 + "|" + yan10 + "|"
						+ dian11 + "|" + hua12 + "|" + qi13 + "|" + fengSu14
						+ "|" + fengxiang + "|" + ye16 + "|" + Qi17 + "|" + QiS);// 存取对象
			}
		}
		edit.commit();

		sharedPreferences2 = getSharedPreferences("num", MODE_PRIVATE);
		Editor editor = sharedPreferences2.edit();
		for (int i = 0; i < listvalue.size(); i++) {
			String id = listvalue.get(i).getDeviceID();
			Integer temHash = myApplication.getTemHash(id);
			editor.putInt(id, temHash);

		}
		editor.commit();
		try {

			if (socketHandl != null) {
				Socket socket = socketHandl.getSocket();
				if (socket != null) {
					socket.close();

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}

	class MyBro extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getStringExtra("flag").equals("min")) {
				ll.setVisibility(View.VISIBLE);
			} else if (intent.getStringExtra("flag").equals("max")) {
				ll.setVisibility(View.GONE);

			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (System.currentTimeMillis() - time > 2000) {
				Toast.makeText(this, "再按一次退出全屏", Toast.LENGTH_SHORT).show();
				time = System.currentTimeMillis();

			} else if (System.currentTimeMillis() - time < 2000) {
				finish();
			}
			return true;

		}

		return super.onKeyDown(keyCode, event);
	}

	public void setMyService(Myservice sMyservice) {
		this.myservice = sMyservice;

	}

	public Myservice getMyService() {
		return myservice;

	}

	public void sendBrocasdMethod(boolean value) {
		Intent intent = new Intent();
		intent.setAction("internet");
		intent.putExtra("isNet", value);
		sendBroadcast(intent);
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.back) {
			int currentItem = viewPager.getCurrentItem();
			switch (currentItem) {
			case 0:
				Toast.makeText(getApplicationContext(), "当前已经是第一页", 1).show();
				break;
			case 1:
				viewPager.setCurrentItem(0);
				break;
			case 2:
				viewPager.setCurrentItem(1);
				break;
			case 3:
				viewPager.setCurrentItem(2);
				break;
			case 4:
				viewPager.setCurrentItem(3);
				break;

			default:

				break;
			}
		} else if (v.getId() == R.id.home_back) {
			if (viewPager.getCurrentItem() == 0) {
				Toast.makeText(this, "当前已经是首页", Toast.LENGTH_SHORT).show();
				return;
			}
			viewPager.setCurrentItem(0);
		}

	}
}
