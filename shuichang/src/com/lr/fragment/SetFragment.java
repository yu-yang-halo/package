package com.lr.fragment;

import interfaceDao.GetService;
import interfaceDao.Myinterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.shuichang.R;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.lr.javaBean.DataALL;
import com.lr.javaBean.Utility;
import com.lr.service.Myservice;
import com.lr.service.SocketHandl;

import ToGet.DeviceType;
import ToGet.HSBody;
import ToGet.HSHead;
import ToGet.HSPackage;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SetFragment extends Fragment implements GetService {
	private ArrayList<DataALL> list;
	private ListView listView;
	private Myservice socketHandl;
	SharedPreferences ssharedPreferences;
	SharedPreferences sharedPreferences;
	SharedPreferences Co2sharedPreferences;
	SharedPreferences hSharedPreferences;
	ProgressDialog progressDialog;
	private int num;
	private float max;
	private float min;
	private final int RES2 = 2;
	private final int RES3 = 3;
	private final int RES4 = 4;
	private final int Fail = -1;
	private String devid;
	HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					if (min == 0 && max == 0) {
						Toast.makeText(getActivity(), "取消成功",
								Toast.LENGTH_SHORT).show();
						sharedPreferences = getActivity().getSharedPreferences(
								"wendu", getActivity().MODE_PRIVATE);
						Editor edit = sharedPreferences.edit();

						edit.putString(devid, 0 + "|" + 0);
						edit.commit();
						mySetAdapter.notifyDataSetChanged();

					} else {

						Toast.makeText(getActivity(), "设置成功",
								Toast.LENGTH_SHORT).show();
					}
				}
			}

			else if (msg.what == 2) {
				if (progressDialog.isShowing()) {
					Message message = Message.obtain();
					message.what = RES3;
					try {
						Cont(num, min, max, devid);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.sendMessageDelayed(message, 3000);

				}

			} else if (msg.what == RES3) {
				if (progressDialog.isShowing()) {
					Message message = Message.obtain();
					message.what = Fail;
					try {
						Cont(num, min, max, devid);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.sendMessageDelayed(message, 4000);

				}

			} else if (msg.what == Fail) {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					if (max == 0 && min == 0) {

						Toast.makeText(getActivity(), "取消失败,通讯异常",
								Toast.LENGTH_SHORT).show();

					} else {
						Toast.makeText(getActivity(), "设置失败,通讯异常",
								Toast.LENGTH_SHORT).show();
					}

				}

			}
		};

	};
	private MySetAdapter mySetAdapter;

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		list = (ArrayList<DataALL>) getArguments().getSerializable("name");
		for (int i = 0; i < list.size(); i++) {// 初始化隐藏和展开状态
			hashMap.put(i, View.VISIBLE);
		}
		View view = inflater.inflate(R.layout.listset, null);
		listView = (ListView) view.findViewById(R.id.listset);
		mySetAdapter = new MySetAdapter();
		listView.setAdapter(mySetAdapter);
		return view;
	}

	class MySetAdapter extends BaseAdapter {
		private EditText ws;
		private EditText wx;
		private TextView wset;
		private TextView title;
		private LinearLayout contr;
		private LinearLayout env;
		private TextView cancleset;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub

			final View view = getActivity().getLayoutInflater().inflate(
					R.layout.setvalue, null);
			title = (TextView) view.findViewById(R.id.reltitle);
			env = (LinearLayout) view.findViewById(R.id.environment);
			ws = (EditText) view.findViewById(R.id.ws);
			wx = (EditText) view.findViewById(R.id.wx);
			wset = (TextView) view.findViewById(R.id.wset);
			contr = (LinearLayout) view.findViewById(R.id.contr);
			cancleset = (TextView) view.findViewById(R.id.canceset);
			cancleset.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					progressDialog = new ProgressDialog(getActivity(),
							ProgressDialog.THEME_HOLO_DARK);
					progressDialog.setMessage("正在取消...");
					progressDialog.setCanceledOnTouchOutside(false);
					progressDialog.show();
					Display defaultDisplay = getActivity().getWindow()
							.getWindowManager().getDefaultDisplay();
					int width = defaultDisplay.getWidth();
					int high = defaultDisplay.getHeight();
					WindowManager.LayoutParams params = progressDialog
							.getWindow().getAttributes();
					params.alpha = 0.8f;
					params.width = (int) (width * 0.6);
					progressDialog.getWindow().setAttributes(params);
					min = 0;
					max = 0;
					devid = list.get(position).getDeviceID();
					num = 1;
					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Message msg = Message.obtain();
							msg.what = 2;
							try {
								Cont(1, 0, 0, devid);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							handler.sendMessageDelayed(msg, 3000);
						}
					}.start();

				}
			});
			title.setText(list.get(position).getPondName());
			Integer integer = hashMap.get(position);
			if (integer == View.VISIBLE) {
				env.setVisibility(View.VISIBLE);
				contr.setBackgroundColor(Color.rgb(246, 246, 246));
			} else if (integer == View.GONE) {
				env.setVisibility(View.GONE);
				contr.setBackgroundResource(R.drawable.wcf2);
			}
			sharedPreferences = getActivity().getSharedPreferences("wendu",
					getActivity().MODE_PRIVATE);
			String string = sharedPreferences.getString(list.get(position)
					.getDeviceID(), "");

			if (!"".equals(string)) {
				ws.setText(string.split("\\|")[0]);
				wx.setText(string.split("\\|")[1]);
			}

			wset.setOnClickListener(new OnClickListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					EditText ws = (EditText) view.findViewById(R.id.ws);
					EditText wx = (EditText) view.findViewById(R.id.wx);
					String whigh = ws.getText().toString();
					String wlow = wx.getText().toString();
					try {
						float wh = Float.parseFloat(whigh);
						float wl = Float.parseFloat(wlow);
						if (wh - wl <= 0) {
							Toast.makeText(getActivity(), "上限不能低于下限",
									Toast.LENGTH_SHORT).show();
							return;

						} else {
							sharedPreferences = getActivity()
									.getSharedPreferences("wendu",
											getActivity().MODE_PRIVATE);
							Editor edit = sharedPreferences.edit();
							String deviceID = list.get(position).getDeviceID();
							edit.putString(deviceID, ws.getText().toString()
									+ "|" + wx.getText().toString());
							edit.commit();
							Cont(1, wl, wh, deviceID);
							min = wl;
							max = wh;
							num = 1;
							devid = deviceID;
							progressDialog = new ProgressDialog(getActivity(),
									ProgressDialog.THEME_HOLO_DARK);
							progressDialog.setMessage("正在设置...");
							progressDialog.setCanceledOnTouchOutside(false);
							progressDialog.show();
							Display defaultDisplay = getActivity().getWindow()
									.getWindowManager().getDefaultDisplay();
							int width = defaultDisplay.getWidth();
							int high = defaultDisplay.getHeight();
							WindowManager.LayoutParams params = progressDialog
									.getWindow().getAttributes();
							params.alpha = 0.8f;
							params.width = (int) (width * 0.6);
							progressDialog.getWindow().setAttributes(params);
							new Thread() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Message msg = Message.obtain();
									msg.what = 2;
									handler.sendMessageDelayed(msg, 3000);
								}
							}.start();

						}
					} catch (Exception exception) {
						Toast.makeText(getActivity(), "格式异常",
								Toast.LENGTH_SHORT).show();
						return;
					}
				}
			});

			contr.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					View view2 = listView.getChildAt(
							position - listView.getFirstVisiblePosition())
							.findViewById(R.id.environment);
					View view = listView.getChildAt(
							position - listView.getFirstVisiblePosition())
							.findViewById(R.id.contr);
					ImageView tg = (ImageView) listView.getChildAt(
							position - listView.getFirstVisiblePosition())
							.findViewById(R.id.tagone);
					if (view2.getVisibility() == View.GONE) {
						view2.setVisibility(View.VISIBLE);
						hashMap.put(position, View.VISIBLE);
						tg.setImageResource(R.drawable.blo);
						view.setBackgroundColor(Color.rgb(246, 246, 246));

					} else if (view2.getVisibility() == View.VISIBLE) {
						view2.setVisibility(View.GONE);
						hashMap.put(position, View.GONE);
						tg.setImageResource(R.drawable.top);
						view.setBackgroundResource(R.drawable.wcf2);
					}
				}
			});

			return view;
		}

	}

	public void Cont(int num, float min, float max, String id)
			throws IOException {
		// SocketHandl socketHandl = new SocketHandl(getActivity());

		HSHead head = new HSHead((byte) 2, DeviceType.Water, id, new byte[] {
				0x12, 0x78, (byte) 0xA0, (byte) 0x9C, 0x00, 0x00, 0x00, 0x00 },
				(short) 19, (byte) 2);
		HSBody body = new HSBody(new byte[] { (byte) num, 85,
				(byte) (min * 10), (byte) (max * 10) });
		HSPackage p = new HSPackage(head, body);
		if (socketHandl != null && socketHandl.getSocket() != null) {
			if (socketHandl.getSocket().isConnected()
					&& socketHandl.getSocket() != null) {
				socketHandl.SendMessage(p.GetBytes());
				try {
					Log.e("lk", Utility.getHexString(p.GetBytes()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			Toast.makeText(getActivity(), "服务端未打开", 1).show();
		}
	}

	@Override
	public void setMyservice(Myservice myservice) {
		// TODO Auto-generated method stub
		socketHandl = myservice;

	}

	public void exchangeValue() {
		handler.sendEmptyMessage(0);
	}

}
