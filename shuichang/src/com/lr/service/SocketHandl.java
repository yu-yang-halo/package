package com.lr.service;

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
import com.lr.javaBean.InterCallState;
import com.lr.javaBean.Utility;
import ToGet.DeviceType;
import ToGet.HSBody;
import ToGet.HSHead;
import ToGet.HSPackage;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class SocketHandl {
	int lenhead = 23;
	public static Socket socket;
	private Context context;
	private String id;
	private ArrayList<DataALL> alls;
	private boolean isfirst = true;
	private HashMap<String, String> hashMap = new HashMap<String, String>();
	private HashMap<String, Fram> hampfra = new HashMap<String, Fram>();

	public SocketHandl(Context context) {
		this.context = context;
	}

	public void initSocket(String ip, int port, String id,
			ArrayList<DataALL> alls) {
		isfirst = true;
		try {
			if (id == null) {
				SharedPreferences sharedPreferences = context
						.getSharedPreferences("id", context.MODE_PRIVATE);
				id = sharedPreferences.getString("id", "");

			}
			socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port));
			this.alls = alls;
			this.id = id;
			if (socket.isConnected()) {

				Toast.makeText(context, "连接服务器", Toast.LENGTH_SHORT).show();

			}
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

	public Socket getSocket() {
		return socket;
	}

	public void SendMessage(byte[] b) throws IOException {
		OutputStream outputStream = socket.getOutputStream();
		outputStream.write(b);
		outputStream.flush();
	}

	public static void SendMessage1(byte[] b) throws IOException {
		OutputStream outputStream = socket.getOutputStream();
		outputStream.write(b);
		outputStream.flush();
	}

	public void ReceHostData() {
		new Thread() {
			private RelData tData;

			public void run() {
				while (socket != null && socket.isConnected()
						&& !socket.isClosed() && isfirst) {

					try {
						InterCallState mainActivity = (InterCallState) context;
						InputStream inputStream = socket.getInputStream();
						byte b[] = new byte[1024];
						int read = inputStream.read(b);
						if (read == -1) {
							for (int i = 0; i < alls.size(); i++) {
								hashMap.put(alls.get(i).getDeviceID(), "离线");
							}

							mainActivity.state(hashMap, "-4");
							isfirst = false;
						}
						byte[] range = Arrays.copyOfRange(b, 0, read);
						String hexString = Utility.getHexString(range);
						Log.e("mnm", hexString);
						byte[] head = Arrays.copyOfRange(range, 0, lenhead);
						HSHead head2 = new HSHead(head);
						if ((head2.GetOrderWord() + "").equals("3")) {

							MyApplication cApplicationontext = (MyApplication) context
									.getApplicationContext();
							Log.e("biao", head2.GetDeviceID());

							byte[] copyOfRange = Arrays.copyOfRange(range,
									lenhead, lenhead + 40);
							String message = Utility.getHexString(copyOfRange);
							ToSoA toSoA = new ToSoA(message, context);
							toSoA.getWay(head2.GetDeviceID());
							Fram fram = SavaFra.getFram(head2.GetDeviceID());// 保持对象的多样
							hampfra.put(head2.GetDeviceID(), fram);
							cApplicationontext.hashMapFram = hampfra;
							Log.e("lll", head2.GetDeviceID());
							mainActivity.state(hashMap, "3");// 同样通接口回调，发广播
						}

						hashMap.put(head2.GetDeviceID(),
								(head2.GetOrderWord() + "").equals("-3") ? "离线"
										: "在线");
						Log.e("kkk", head2.GetDeviceID());
						mainActivity.state(hashMap, "-3");// 状态

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
							new byte[] { 0x12, 0x78, (byte) 0xA0, (byte) 0x9C,
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
						// SendMessage(p1.GetBytes());

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Log.e("log", "保持连接");
					try {
						Thread.sleep(30000);
					} catch (Exception e) {
						e.printStackTrace();

					}
				}

			};
		}.start();

	}

}
