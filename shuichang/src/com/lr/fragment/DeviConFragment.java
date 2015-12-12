package com.lr.fragment;

import interfaceDao.GetService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.app.MyApplication;
import com.example.huoyuyunshu.R;
import com.lr.javaBean.DataALL;
import com.lr.javaBean.Utility;
import com.lr.service.Myservice;
import ToGet.DeviceType;
import ToGet.HSBody;
import ToGet.HSHead;
import ToGet.HSPackage;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DeviConFragment extends Fragment implements GetService {
	private ListView listView;
	private ArrayList<DataALL> list;
	LayoutInflater layoutInflater;
	private Myservice socketHandl;
	private final int RES1 = 2;
	private final int RES2 = 3;
	private final int RES3 = 4;
	private final int Fail = 5;
	private final int allFail = 6;
	private boolean isCheckWhich;
	private int IsBigbutton = 0;
	private String num;
	private int type;
	private String deviId;
	private int nowDownState;
	private String nowDowDeviId;
	private String allnum;
	private int alltype;
	private String allId;
	private byte[] nums;
	private byte co;
	private String onedevid;

	private HashMap<Integer, Integer> hashMapvisOrgone = new HashMap<Integer, Integer>();
	private HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> hashMapmax = new HashMap<String, Integer>();
	private HashMap<String, ArrayList<View>> oneAll = new HashMap<String, ArrayList<View>>();
	private MyDerCon myDerCon;
	private int ISFrom = 0;
	private HashMap<String, Integer> preHashMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> premaxhMap = new HashMap<String, Integer>();
	private HashMap<String, Boolean> preMaxdev = new HashMap<String, Boolean>();

	private ProgressDialog progressDialog;

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				String devid = null;

				try {

					devid = msg.obj.toString();
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (devid != null && progressDialog != null
						&& progressDialog.isShowing()) {
					return;

				}
				if (devid == null) {

					Toast.makeText(getActivity(), "操作成功", Toast.LENGTH_SHORT)
							.show();

				}

				if (progressDialog != null && progressDialog.isShowing()) {

					progressDialog.dismiss();

				}
				myDerCon.notifyDataSetChanged();

			} else if (msg.what == RES1) {
				if (progressDialog.isShowing()) {
					try {
						Cont(Integer.parseInt(num), type, deviId);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message message = Message.obtain();
					message.what = RES2;
					handler.sendMessageDelayed(message, 4000);
				}

			} else if (msg.what == RES2) {
				if (progressDialog.isShowing()) {
					try {
						Cont(Integer.parseInt(num), type, deviId);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Message message = Message.obtain();
					message.what = RES3;
					handler.sendMessageDelayed(message, 4000);
				}

			} else if (msg.what == RES3) {
				if (progressDialog.isShowing()) {
					try {
						Cont(Integer.parseInt(num), type, deviId);

					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message message = Message.obtain();
					message.what = Fail;
					handler.sendMessageDelayed(message, 3000);
				}

			} else if (msg.what == Fail) {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					Toast.makeText(getActivity(), "通信异常", Toast.LENGTH_SHORT)
							.show();
					hashMap.put(nowDowDeviId, nowDownState);
					myDerCon.notifyDataSetChanged();
				}

			} else if (msg.what == allFail) {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					Toast.makeText(getActivity(), "通信异常", Toast.LENGTH_SHORT)
							.show();

					Set<String> set = premaxhMap.keySet();
					for (String string : set) {
						hashMapmax.put(string, premaxhMap.get(string));
					}
					Set<String> set2 = preHashMap.keySet();
					for (String string2 : set2) {
						hashMap.put(string2, preHashMap.get(string2));
					}

					myDerCon.notifyDataSetChanged();

				}
			} else if (msg.what == 7) {
				if (progressDialog.isShowing()) {
					Message message = Message.obtain();
					message.what = 8;
					allOffOrON(allId, nums, co);
					handler.sendMessageDelayed(message, 3000);

				}

			} else if (msg.what == 8) {
				if (progressDialog.isShowing()) {
					Message message = Message.obtain();
					message.what = allFail;
					allOffOrON(allId, nums, co);
					handler.sendMessageDelayed(message, 3000);

				}
			}

		};
	};
	private MyApplication myApplication;
	private HashMap<String, Integer> now;

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Bundle bundle = getArguments();
		this.layoutInflater = inflater;
		myApplication = (MyApplication) getActivity().getApplicationContext();

		list = (ArrayList<DataALL>) bundle.getSerializable("name");
		now = myApplication.getHashMap();
		for (int i = 0; i < list.size(); i++) {
			String d = "";
			String electrics = list.get(i).getElectrics();
			ArrayList<View> arrayList = new ArrayList<View>();
			String[] split = electrics.split(",");
			for (int j = 0; j < split.length; j++) {
				View view = inflater.inflate(R.layout.dvc, null);
				TextView feng = (TextView) view.findViewById(R.id.feng);
				feng.setText(split[j]);
				String num = split[j].split("-")[0];
				view.setTag(num);
				d = d + num;
				hashMap.put(list.get(i).getDeviceID() + num, 0);
				arrayList.add(view);

			}

			hashMapvisOrgone.put(i, View.VISIBLE);
			hashMapmax.put(list.get(i).getDeviceID() + d, 0);
			preMaxdev.put(list.get(i).getDeviceID() + d, false);
			Log.e("has", hashMapmax.get(list.get(i).getDeviceID() + d) + "||"
					+ list.get(i).getDeviceID());
			oneAll.put(list.get(i).getDeviceID(), arrayList);
			// now.put(list.get(i).getDeviceID(),0);//设备观察

		}
		View view = inflater.inflate(R.layout.devcontr, null);
		listView = (ListView) view.findViewById(R.id.lv);
		myDerCon = new MyDerCon();
		listView.setAdapter(myDerCon);
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	class MyDerCon extends BaseAdapter {

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
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			View view = layoutInflater.inflate(R.layout.dcy, null);
			final LinearLayout ll = (LinearLayout) view.findViewById(R.id.dj);
			LinearLayout stateLin = (LinearLayout) view
					.findViewById(R.id.devstate);
			final ImageView fl = (ImageView) view.findViewById(R.id.kai);
			final ImageView flag = (ImageView) view.findViewById(R.id.fl);
			ll.setVisibility(hashMapvisOrgone.get(position));
			if (hashMapvisOrgone.get(position) == View.GONE) {
				stateLin.setBackgroundResource(R.drawable.wcf2);
			} else if (hashMapvisOrgone.get(position) == View.VISIBLE) {
				stateLin.setBackgroundColor(Color.rgb(246, 246, 246));

			}
			flag.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					LinearLayout devstate = (LinearLayout) listView.getChildAt(
							position - listView.getFirstVisiblePosition())
							.findViewById(R.id.devstate);
					if (ll.getVisibility() == View.GONE) {
						ll.setVisibility(View.VISIBLE);
						flag.setImageResource(R.drawable.top);
						hashMapvisOrgone.put(position, View.VISIBLE);
						devstate.setBackgroundColor(Color.rgb(246, 246, 246));
					} else {
						ll.setVisibility(View.GONE);
						flag.setImageResource(R.drawable.blo);
						devstate.setBackgroundResource(R.drawable.wcf2);
						hashMapvisOrgone.put(position, View.GONE);
					}
				}
			});

			fl.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// myApplication.setIsdown(false);
					String d = "";

					ArrayList<View> arrayList = oneAll.get(list.get(position)
							.getDeviceID());
					byte[] b = new byte[arrayList.size()];
					String deviceID = list.get(position).getDeviceID();// 设备ID
					for (int i = 0; i < arrayList.size(); i++) {
						View view2 = arrayList.get(i);
						String string = view2.getTag().toString();// 得到相对应的保存在view里面的值
						d = d + string;
						b[i] = (byte) Integer.parseInt(string);
					}

					Set<String> set = hashMap.keySet();
					for (String string : set) {
						preHashMap.put(string, hashMap.get(string));

					}
					Set<String> set2 = hashMapmax.keySet();
					for (String string : set2) {
						premaxhMap.put(string, hashMapmax.get(string));
					}

					if (hashMapmax.get(deviceID + d) == 0) {
						// Toast.makeText(getActivity(),"ss",1).show();
						preMaxdev.put(deviceID + d, true);
						// --------------------
						for (int i = 0; i < arrayList.size(); i++) {
							View view2 = arrayList.get(i);
							ImageView imageView = (ImageView) view2
									.findViewById(R.id.fengoff);
							imageView.setImageResource(R.drawable.on);
							hashMap.put(list.get(position).getDeviceID()
									+ view2.getTag().toString(), 1);

						}
						allOffOrON(list.get(position).getDeviceID(), b,
								(byte) 1);
						hashMapmax.put(deviceID + d, 1);
						fl.setImageResource(R.drawable.on);
					} else if (hashMapmax.get(deviceID + d) == 1) {
						// /--------------------------------------
						// isCheckWhich = false;
						preMaxdev.put(deviceID + d, false);
						// ----------------
						for (int i = 0; i < arrayList.size(); i++) {
							View view2 = arrayList.get(i);
							ImageView imageView = (ImageView) view2
									.findViewById(R.id.fengoff);
							imageView.setImageResource(R.drawable.off);
							hashMap.put(list.get(position).getDeviceID()
									+ view2.getTag().toString(), 0);
						}
						allOffOrON(list.get(position).getDeviceID(), b,
								(byte) 0);

						hashMapmax.put(deviceID + d, 0);
						fl.setImageResource(R.drawable.off);
					}

					progressDialog = new ProgressDialog(getActivity());
					progressDialog = new ProgressDialog(getActivity(),
							ProgressDialog.THEME_HOLO_DARK);
					progressDialog.setMessage("正在等待回复...");
					progressDialog.setCanceledOnTouchOutside(false);
					progressDialog.setCancelable(false);
					progressDialog.show();
					Display defaultDisplay = getActivity().getWindow()
							.getWindowManager().getDefaultDisplay();
					int width = defaultDisplay.getWidth();
					int high = defaultDisplay.getHeight();
					WindowManager.LayoutParams params = progressDialog
							.getWindow().getAttributes();
					params.alpha = 0.8f;
					params.width = (int) (width * 0.8);
					progressDialog.getWindow().setAttributes(params);
					Message msg = new Message();
					msg.what = 7;
					handler.sendMessageDelayed(msg, 4000);

				}
			});

			TextView textView = (TextView) view.findViewById(R.id.devname);
			textView.setText(list.get(position).getPondName());
			ArrayList<View> arrayList = oneAll.get(list.get(position)
					.getDeviceID());
			String flage = "";
			String biaozhi = "";
			for (int i = 0; i < arrayList.size(); i++) {
				final View view2 = arrayList.get(i);
				final ImageView ima2 = (ImageView) view2
						.findViewById(R.id.fengoff);
				ViewGroup viewGroup = (ViewGroup) view2.getParent();
				if (viewGroup != null) {
					viewGroup.removeAllViews();
				}
				// //----------华丽的分割线
				String string1 = view2.getTag().toString();
				String deviceID = list.get(position).getDeviceID();
				String key1 = deviceID + string1;
				biaozhi = biaozhi + string1;
				if (hashMap.get(key1) == 0) {

					ima2.setImageResource(R.drawable.off);
					flage = "0" + flage;

				} else if (hashMap.get(key1) == 1) {
					ima2.setImageResource(R.drawable.on);
					flage = "1" + flage;

				}

				// -----------------------------------------------------------------------

				ll.addView(view2);
				ima2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						myApplication.setIsdown(false);
						initprogress();
						String string = view2.getTag().toString();
						String deviceID = list.get(position).getDeviceID();
						String key = deviceID + string;
						if (hashMap.get(key) == 0) {
							ima2.setImageResource(R.drawable.on);
							hashMap.put(key, 1);
							nowDowDeviId = key;
							nowDownState = 0;
							// ///////////////////////////
							if (getInt(hashMap, deviceID, 1)) {
								String d = "";
								ArrayList<View> arrayList = oneAll.get(list
										.get(position).getDeviceID());
								String deviceIDone = list.get(position)
										.getDeviceID();
								for (int i = 0; i < arrayList.size(); i++) {
									View view2 = arrayList.get(i);
									String stringone = view2.getTag()
											.toString();
									d = d + stringone;
								}
								hashMapmax.put(deviceIDone + d, 1);
								fl.setImageResource(R.drawable.on);
								preMaxdev.put(deviceID + d, true);
							}
							try {
								Cont(Integer.parseInt(string), 1, deviceID);
								num = string;
								type = 1;
								deviId = deviceID;
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else if (hashMap.get(key) == 1) {
							ima2.setImageResource(R.drawable.off);
							hashMap.put(key, 0);
							nowDowDeviId = key;
							nowDownState = 1;
							String d = "";
							ArrayList<View> arrayList = oneAll.get(list.get(
									position).getDeviceID());
							String deviceIDone = list.get(position)
									.getDeviceID();
							for (int i = 0; i < arrayList.size(); i++) {
								View view2 = arrayList.get(i);
								String stringone = view2.getTag().toString();
								d = d + stringone;
							}
							if (getInt(hashMap, deviceID, 0)) {
								fl.setImageResource(R.drawable.off);
								preMaxdev.put(deviceID + d, false);
								hashMapmax.put(deviceIDone + d, 0);
							}
							try {
								Cont(Integer.parseInt(string), 0, deviceID);
								num = string;
								type = 0;
								deviId = deviceID;

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}

					private void initprogress() {
						progressDialog = new ProgressDialog(getActivity());
						progressDialog = new ProgressDialog(getActivity(),
								ProgressDialog.THEME_HOLO_DARK);
						progressDialog.setMessage("正在等待回复...");
						progressDialog.setCanceledOnTouchOutside(false);
						progressDialog.setCancelable(false);
						progressDialog.show();
						Display defaultDisplay = getActivity().getWindow()
								.getWindowManager().getDefaultDisplay();
						int width = defaultDisplay.getWidth();
						int high = defaultDisplay.getHeight();
						WindowManager.LayoutParams params = progressDialog
								.getWindow().getAttributes();
						params.alpha = 0.8f;
						params.width = (int) (width * 0.8);
						progressDialog.getWindow().setAttributes(params);
						Message msg = new Message();
						msg.what = RES1;
						handler.sendMessageDelayed(msg, 3000);

					}
				});
			}
			// -----------------------------
			if (!flage.contains("1")) {

				fl.setImageResource(R.drawable.off);
				// Toast.makeText(getActivity(),""+(hashMapmax.get(list.get(position).getDeviceID()+biaozhi)),1).show();
				hashMapmax.put(list.get(position).getDeviceID() + biaozhi, 0);
				preMaxdev
						.put(list.get(position).getDeviceID() + biaozhi, false);
			} else if (!flage.contains("0")) {
				fl.setImageResource(R.drawable.on);
				// Toast.makeText(getActivity(),""+(hashMapmax.get(list.get(position).getDeviceID()+biaozhi)),1).show();
				hashMapmax.put(list.get(position).getDeviceID() + biaozhi, 1);
				preMaxdev.put(list.get(position).getDeviceID() + biaozhi, true);
			} else if (flage.contains("0") && flage.contains("1")) {
				if (preMaxdev.get(list.get(position).getDeviceID() + biaozhi)) {

					hashMapmax.put(list.get(position).getDeviceID() + biaozhi,
							1);
					fl.setImageResource(R.drawable.on);
				} else {
					hashMapmax.put(list.get(position).getDeviceID() + biaozhi,
							0);
					fl.setImageResource(R.drawable.off);
				}

			}
			// ----------------------------------------------------------
			return view;
		}

		public boolean getInt(HashMap<String, Integer> hashMap, String dev,
				int flg) {
			Set<String> keySet = hashMap.keySet();
			for (String string : keySet) {
				if (!string.contains(dev)) {
					continue;
				}
				Integer integer = hashMap.get(string);

				if (integer != flg) {
					return false;
				}
			}
			return true;
		}

	}

	public void ChangeViewState(HashMap<String, String> hashMap1,
			String devidone) {
		ISFrom = 1;

		Set<String> keySet = hashMap.keySet();
		for (String string : keySet) {
			String devId = string.substring(0, string.length() - 1);
			String mun = string.substring(string.length() - 1, string.length());
			if (hashMap1.get(devId) != null) {
				String string2 = hashMap1.get(devId);
				String substring = string2.substring(Integer.parseInt(mun) - 1,
						Integer.parseInt(mun));
				hashMap.put(devId + mun, Integer.parseInt(substring));
			}
		}
		Message message = Message.obtain();
		message.obj = devidone;
		message.what = 1;

		handler.sendMessage(message);

	}

	@Override
	public void setMyservice(Myservice myservice) {
		// TODO Auto-generated method stub
		socketHandl = myservice;

	}

	public void allOffOrON(String id, byte num[], byte contr) {

		allId = id;
		nums = num;
		co = contr;
		if (now.get(id) == 0) {
			now.put(id, 1);
		}

		HSHead head = new HSHead((byte) 2, DeviceType.Water, id, new byte[] {
				0x12, 0x78, (byte) 0xA0, (byte) 0x9C, 0x00, 0x00, 0x00, 0x00 },
				(short) 16, (byte) 2);
		byte[] b = new byte[num.length * 2];
		for (int i = 0; i < num.length; i++) {
			b[2 * i] = num[i];
			b[2 * i + 1] = contr;
		}
		HSBody body = new HSBody(b);
		HSPackage p = new HSPackage(head, body);
		try {
			if (socketHandl != null && socketHandl.getSocket() != null) {
				if (socketHandl.getSocket().isConnected()
						&& socketHandl.getSocket() != null) {
					socketHandl.SendMessage(p.GetBytes());
					try {
						Log.e("ll", Utility.getHexString(p.GetBytes()));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void Cont(int num, int type, String id) throws IOException {
		if (now.get(id) == 0) {
			now.put(id, 1);

		}
		HSHead head = new HSHead((byte) 2, DeviceType.Water, id, new byte[] {
				0x12, 0x78, (byte) 0xA0, (byte) 0x9C, 0x00, 0x00, 0x00, 0x00 },
				(short) 16, (byte) 2);
		HSBody body = new HSBody(new byte[] { (byte) num, (byte) type });
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
}
