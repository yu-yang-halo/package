package com.lr.service;

import interfaceDao.Myinterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.app.MyApplication;
import com.bean.Fram;
import com.bean.SavaFra;
import com.bean.ToSoA;
import com.lr.fragment.RelData;
import com.lr.javaBean.DataALL;
import com.lr.javaBean.Utility;
import ToGet.DeviceType;
import ToGet.HSBody;
import ToGet.HSHead;
import ToGet.HSPackage;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

public class Myservice extends Service {
	int lenhead = 23;
	public static Socket socket;
	private Context context;
	private String id;
	private boolean isFirst = true;
	private Myinterface myinterface;
	private final String ip = "60.173.247.137";
	// 服务器端口
	private int port = 9001;
	private ArrayList<DataALL> alls;
	private boolean isfirst = true;
	HashMap<String, String> filterState = new HashMap<String, String>();
	private HashMap<String, String> hashMap = new HashMap<String, String>();
	private HashMap<String, Fram> hampfra = new HashMap<String, Fram>();
	HashMap<String, String> stateDev = new HashMap<String, String>();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = getApplicationContext();
	}

	public void initSocket(String ip, int port, String id,
			ArrayList<DataALL> alls) {
		initSocket();
		isfirst = true;
		try {
			if (id == null) {
				SharedPreferences sharedPreferences = context
						.getSharedPreferences("id", context.MODE_PRIVATE);
				id = sharedPreferences.getString("id", "");
			}
			socket = new Socket();
			try {

				socket.connect(new InetSocketAddress(ip, port));

			} catch (Exception exception) {
				return;

			}
			this.alls = alls;
			this.id = id;

			Toast.makeText(context, "连接服务器", Toast.LENGTH_SHORT).show();

			HSHead head = new HSHead(
					(byte) 3,
					DeviceType.Android,
					id,
					new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 },
					(short) 01, (byte) 0);
			HSBody body = new HSBody(new byte[0]);
			HSPackage p = new HSPackage(head, body);
			ReceHostData();
			SendMessage(p.GetBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Looper.loop();
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Bundle bundle2 = arg0.getExtras();
		alls = (ArrayList<DataALL>) bundle2.getSerializable("name");
		String id1 = arg0.getStringExtra("id");
		id = id1;
		initSocket(ip, port, id, alls);
		return new MyBinder();
	}

	public void setMyinterface(Myinterface l) {
		myinterface = l;

	}

	public class MyBinder extends Binder {
		public Myservice getMyservice() {
			return Myservice.this;
		}
	}

	public void SendMessage(byte[] b) throws IOException {
		OutputStream outputStream = socket.getOutputStream();
		outputStream.write(b);
		outputStream.flush();
	}

	public void ReceHostData() {
		new Thread() {
			private RelData tData;
			private boolean isNotnull;
			private InputStream inputStream;

			public void run() {
				while (socket != null && socket.isConnected()
						&& !socket.isClosed() && isfirst) {
					try {
						inputStream = socket.getInputStream();
						byte b[] = new byte[1024];
						int read = inputStream.read(b);
						if (read == -1) {
							for (int i = 0; i < alls.size(); i++) {
								hashMap.put(alls.get(i).getDeviceID(), "离线");
							}
							isfirst = false;
							if (myinterface != null) {
								myinterface.state(hashMap, "-4", null);
								isfirst = false;
							}

						}
						byte[] range = Arrays.copyOfRange(b, 0, read);
						String hexString = Utility.getHexString(range);
						Log.e("hex", hexString);
						if (read % 26 == 0 && (read / 26) > 0) {// 可能会发多条
							int start = 0;
							int end = 23;
							for (int i = 0; i < read / 26; i++) {
								byte[] head1 = Arrays.copyOfRange(range, start,
										end);
								start = start + 26;
								end = end + 26;
								HSHead head3 = new HSHead(head1);
								if (head3.GetBodyLength() == 0) {
									hashMap.put(head3.GetDeviceID(),
											(head3.GetOrderWord() + "")
											.equals("-3") ? "离线" : "在线");
								}
							}
						}
						byte[] head = Arrays.copyOfRange(range, 0, lenhead);// 只有一条时
						HSHead head2 = new HSHead(head);// 解析

						if ((head2.GetOrderWord() + "").equals("3")) {// 如果是类容的时候
							MyApplication cApplicationontext = (MyApplication) context
									.getApplicationContext();
							byte[] copyOfRange = Arrays.copyOfRange(range,
									lenhead, lenhead + 40);
							String message = Utility.getHexString(copyOfRange);
							ToSoA toSoA = new ToSoA(message, context);
							toSoA.getWay(head2.GetDeviceID());
							Fram fram = SavaFra.getFram(head2.GetDeviceID());// 保持对象的多样
							hampfra.put(head2.GetDeviceID(), fram);
							cApplicationontext.hashMapFram = hampfra;
							System.out.println("FRAM_DATA  MYService"+hampfra);
							if (myinterface != null) {
								myinterface.state(hashMap, "3", null);// 同样通接口回调，发广播
								String d = Utility.getHexString((Arrays
										.copyOfRange(range, lenhead + 40,
												lenhead + 41)));
								HashMap<String, Integer> map = cApplicationontext
										.getHashMap();

								if (map.get(head2.GetDeviceID()) == 0) {
									if ("1e".equals(d)) {
										String state = Utility
												.getHexString((Arrays
														.copyOfRange(range,
																lenhead + 41,
																lenhead + 45)));
										map.put(head2.GetDeviceID(), 2);
										stateDev.put(head2.GetDeviceID(), state);
										myinterface.state(stateDev, "e",
												head2.GetDeviceID());

									}
								}

							}
						}
						if ((head2.GetOrderWord() + "").equals("19")) {
							filterState.put(head2.GetDeviceID(), "19");
							myinterface.state(filterState, 19 + "", null);

						}
						//
						if ((head2.GetOrderWord() + "").equals("15")) {// 电机控制
							int getBodyLength = head2.GetBodyLength();

							byte[] copyOfRange = Arrays.copyOfRange(range,
									lenhead, lenhead + getBodyLength);
							String relBody = Utility.getHexString(copyOfRange);
							Log.e("ju", relBody);
							String sta = "";
							for (int i = 0; i < relBody.length(); i++) {
								if ((i + 1) % 4 == 0) {
									sta = sta + relBody.charAt(i);
								}

							}
							stateDev.put(head2.GetDeviceID(), sta);
							myinterface.state(stateDev, "e", null);
						}
						hashMap.put(head2.GetDeviceID(),
								(head2.GetOrderWord() + "").equals("-3") ? "离线"
										: "在线");
						Log.e("sta", hashMap.toString());
						if (myinterface == null) {// 监听是否为空 为空的时候等待不为空 然后回调
							isNotnull = true;
							new Thread() {
								public void run() {
									while (isNotnull) {
										if (myinterface != null) {
											myinterface.state(hashMap, "-3",
													null);// 状态
											isNotnull = false;
										}

									}

								};

							}.start();

						}
						if (myinterface != null) {
							myinterface.state(hashMap, "-3", null);// 状态
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			};

		}.start();
		keepLive();
	}

	public void keepLive() {
		new Thread() {
			public void run() {
				while (socket.isConnected() && !socket.isClosed()) {
					HSHead head = new HSHead((byte) 2, DeviceType.Water, id,
							new byte[] { 0x12, 0x78, (byte) 0xA0, (byte) 0x9C,//协议
							0x00, 0x00, 0x00, 0x00 }, (short) 02,
							(byte) 0);
					HSBody body = new HSBody(new byte[] { 0 });
					HSPackage p = new HSPackage(head, body);
					HSHead head1 = new HSHead((byte) 3, DeviceType.Android, id,
							new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
							0x00, 0x00 }, (short) 01, (byte) 0);
					HSBody body1 = new HSBody(new byte[0]);
					HSPackage p1 = new HSPackage(head1, body1);
					try {
						SendMessage(p.GetBytes());
						SendMessage(p1.GetBytes());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Log.e("log", "保持连接");
					try {
						Thread.sleep(15000);
					} catch (Exception e) {
						e.printStackTrace();

					}
				}

			};
		}.start();

	}

	private void initSocket() {//联网机制
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads().detectDiskWrites().detectAll().penaltyLog()
		.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
				.penaltyLog().penaltyDeath().build());
	}

	public Socket getSocket() {
		return socket;
	}

}
