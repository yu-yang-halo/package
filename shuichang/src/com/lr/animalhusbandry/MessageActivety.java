package com.lr.animalhusbandry;

import java.util.ArrayList;
import java.util.List;
import com.DB.DBUtils;
import com.DB.Message;
import com.app.MyApplication;
import com.example.huoyuyunshu.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MessageActivety extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	private ListView listView;
	private List<Message> list;
	MyApplication myApplication;
	private String devId;
	private Button clear;
	private MyDb myDb;
	private MyCustomAdapter myCustom;
	private boolean isResum;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				myCustom.setDate(null);// 制空
			} else if (msg.what == 1) {
				Bundle data = msg.getData();
				@SuppressWarnings("unchecked")
				ArrayList<Message> list = (ArrayList<Message>) data
						.getSerializable("db");
				myCustom.setDate(list);
			}
		};
	};
	private RadioGroup radioGroup;
	private RadioButton all;
	private RadioButton wd;
	private RadioButton sd;
	private RadioButton cd;
	private RadioButton nh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		myApplication = (MyApplication) getApplicationContext();
		Intent intent = getIntent();
		devId = intent.getStringExtra("devId");
		setContentView(R.layout.listmessage);
		list = DBUtils.getList(this, devId);
		radioGroup = (RadioGroup) findViewById(R.id.allcat);
		initView();
		clear = (Button) findViewById(R.id.clear);
		myCustom = new MyCustomAdapter();
		listView.setAdapter(myCustom);
		radioGroup.setOnCheckedChangeListener(this);
		clear.setOnClickListener(this);

	}

	private void initView() {
		all = (RadioButton) findViewById(R.id.all);
		wd = (RadioButton) findViewById(R.id.wd);
		sd = (RadioButton) findViewById(R.id.sd);
		cd = (RadioButton) findViewById(R.id.cd);
		nh = (RadioButton) findViewById(R.id.Nd);
		listView = (ListView) findViewById(R.id.messlist);
	}

	class MyCustomAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list == null ? 0 : list.size();
		}

		public void setDate(List<Message> list1) {
			list = list1;
			notifyDataSetChanged();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			myApplication.setTemHashmap(devId, 0);
			View view = getLayoutInflater().inflate(R.layout.dbmessage, null);
			TextView com = (TextView) view.findViewById(R.id.com);
			TextView time = (TextView) view.findViewById(R.id.time);
			Message message = list.get(position);
			String timemessgae = message.getTime();
			String messagestr = message.getMessage();
			com.setText(messagestr);
			com.setTextColor(Color.BLUE);
			time.setText(timemessgae);
			return view;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				DBUtils.Delete(list, MessageActivety.this);// 防止出现阻塞
				handler.sendEmptyMessage(0);
			};

		}.start();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		isResum = true;
		myDb = new MyDb();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("talk");
		registerReceiver(myDb, intentFilter);// 处理广播 不然会报错 在程序退出之后
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myDb);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isResum = false;
	}

	class MyDb extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			if (isResum) {
				if (all.isChecked()) {
					new Thread() {
						public void run() {

							List<Message> list = DBUtils.getList(
									MessageActivety.this, devId);
							ArrayList<Message> list2 = (ArrayList<Message>) list;
							Bundle bundle = new Bundle();
							bundle.putSerializable("db", list2);
							android.os.Message message = android.os.Message
									.obtain();
							message.what = 1;
							message.setData(bundle);
							handler.sendMessage(message);
						};

					}.start();
				} else if (wd.isChecked()) {
					new Thread() {
						public void run() {

							getCatoary("wendu");
						};

					}.start();
				} else if (cd.isChecked()) {
					new Thread() {
						public void run() {

							getCatoary("co2");
						};

					}.start();
				} else if (nh.isChecked()) {
					new Thread() {
						public void run() {

							getCatoary("NH4");
						};

					}.start();
				} else if (sd.isChecked()) {
					new Thread() {
						public void run() {
							getCatoary("shidu");
						};

					}.start();
				}

			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (checkedId == R.id.all) {
			List<Message> list = DBUtils.getList(MessageActivety.this, devId);
			ArrayList<Message> list2 = (ArrayList<Message>) list;
			Bundle bundle = new Bundle();
			bundle.putSerializable("db", list2);
			android.os.Message message = android.os.Message.obtain();
			message.what = 1;
			message.setData(bundle);
			handler.sendMessage(message);

		} else if (checkedId == R.id.wd) {
			getCatoary("wendu");
		} else if (checkedId == R.id.cd) {
			getCatoary("co2");
		} else if (checkedId == R.id.Nd) {
			getCatoary("NH4");

		} else if (checkedId == R.id.sd) {
			getCatoary("shidu");
		}
	}

	private void getCatoary(String wendu) {
		List<Message> list = DBUtils.findcatotary(devId, wendu,
				MessageActivety.this);
		ArrayList<Message> list2 = (ArrayList<Message>) list;
		Bundle bundle = new Bundle();
		bundle.putSerializable("db", list2);
		android.os.Message message = android.os.Message.obtain();
		message.what = 1;
		message.setData(bundle);
		handler.sendMessage(message);
	}

}
