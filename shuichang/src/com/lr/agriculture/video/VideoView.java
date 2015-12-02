package com.lr.agriculture.video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.app.MyApplication;
import com.example.shuichang.R;
import com.hikvision.vmsnetsdk.LineInfo;
import com.hikvision.vmsnetsdk.RealPlayURL;
import com.hikvision.vmsnetsdk.ServInfo;
import com.hikvision.vmsnetsdk.VMSNetSDK;

public class VideoView implements OnClickListener, OnCheckedChangeListener,
		Callback, LiveCallBack {
	private Button mStartBtn;
	private ImageView statue;
	private LinearLayout mlayout;
	private RelativeLayout mRelativeLayout;
	private AlphaAnimation mAlphaAnimation;
	private Button maButton;
	/**
	 * 停止播放按钮
	 */
	private Button mStopBtn;
	private boolean targer = true;
	private long exitTime = 0;
	private LiveControl mLiveControl;
	/**
	 * 播放视频的控件对象
	 */
	private SurfaceView mSurfaceView;
	/**
	 * 创建取流等待bar
	 */
	private ProgressBar mProgressBar;
	/**
	 * 创建消息对象
	 */
	private Handler mMessageHandler = new MyHandler();
	private boolean mIsRecord;
	/**
	 * 播放流量
	 */
	private long mStreamRate = 0;
	private AlertDialog.Builder maBuilder;
	private SurfaceHolder holder;
	private android.view.ViewGroup.LayoutParams layoutParams;
	public boolean isfull;

	private Context context;
	private View view;
	private String videoName;
	private MyApplication myApplication;

	public VideoView(Context context, String videoName) {
		this.context = context;
		this.videoName = videoName;
		myApplication = (MyApplication) context.getApplicationContext();
		view = LayoutInflater.from(context).inflate(R.layout.live, null);

		initUI();
	}

	/**
	 * 初始化网络库和控制层对象
	 * 
	 * @since V1.0
	 */

	/**
	 * 初始化控件
	 * 
	 * @since V1.0
	 */
	private void initUI() {

		mlayout = (LinearLayout) view.findViewById(R.id.ControlLayout);
		mlayout.setVisibility(View.GONE);
		mRelativeLayout = (RelativeLayout) view
				.findViewById(R.id.SurfaceViewLayout);
		mRelativeLayout.setOnClickListener(this);
		statue = (ImageView) view.findViewById(R.id.ima);
		statue.setOnClickListener(this);
		maButton = (Button) view.findViewById(R.id.max);
		maButton.setOnClickListener(this);
		mLiveControl = new LiveControl();
		mLiveControl.setLiveCallBack(this);
		mStartBtn = (Button) view.findViewById(R.id.liveStartBtn);
		mStartBtn.setOnClickListener(this);
		mStopBtn = (Button) view.findViewById(R.id.liveStopBtn);
		mStopBtn.setOnClickListener(this);
		mSurfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
		// mSurfaceView.getHolder().addCallback(this);
		layoutParams = mSurfaceView.getLayoutParams();
		mSurfaceView.setOnClickListener(this);
		holder = mSurfaceView.getHolder();
		holder.addCallback(this);
		mProgressBar = (ProgressBar) view.findViewById(R.id.liveProgressBar);
		mProgressBar.setVisibility(View.INVISIBLE);
	}

	public View getView() {
		return view;
	}

	public boolean getPlay() {
		if (mLiveControl.getLiveState() == mLiveControl.LIVE_PLAY) {
			stopBtnOnClick();
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ima:

		case R.id.surfaceView:
			targer = true;

			if (mLiveControl.getLiveState() != mLiveControl.LIVE_PLAY) {
				startBtnOnClick();
			} else if (isfull) {

				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(context, "再按一次退出全屏", Toast.LENGTH_SHORT)
							.show();
					exitTime = System.currentTimeMillis();
				} else {
					min();
					isfull = false;
					if (mlayout.getVisibility() == View.VISIBLE) {
						mlayout.setVisibility(View.GONE);
					}

				}
			} else if (mLiveControl.getLiveState() == mLiveControl.LIVE_PLAY) {
				mlayout.setVisibility(View.VISIBLE);
				mAlphaAnimation = new AlphaAnimation(1.0f, 0.0f);
				mAlphaAnimation.setDuration(2000);
				mAlphaAnimation.setStartOffset(3000);
				mAlphaAnimation.setFillAfter(true);
				mlayout.startAnimation(mAlphaAnimation);
			}
			break;
		case R.id.liveStartBtn:
			startBtnOnClick();
			break;
		case R.id.liveStopBtn:
			stopBtnOnClick();
			statue.setVisibility(View.VISIBLE);
			break;
		case R.id.max:
			max();
			break;

		default:
			break;
		}
	}

	/**
	 * 开始云台控制，弹出控制界面
	 */

	/**
	 * 发送云台控制命令
	 * 
	 * @param gestureID
	 *            1-云台转上 、2-云台转下 、3-云台转左 、4-云台转右、 11-云台左上 、12-云台右上 13-云台左下
	 *            、14-云台右下、7-镜头拉近、8-镜头拉远、9-镜头近焦、10-镜头远焦
	 */

	private void startBtnOnClick() {

		final ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo mobNetInfoActivity = connectivityManager
				.getActiveNetworkInfo();
		if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
			statue.setVisibility(View.VISIBLE);
			Toast.makeText(context, "当前网络异常，检查网络", Toast.LENGTH_LONG).show();
			targer = false;
			statue.setVisibility(View.VISIBLE);
			return;
		} else if (mobNetInfoActivity.getType() != ConnectivityManager.TYPE_WIFI) {
			maBuilder = new AlertDialog.Builder(context);
			targer = false;
			maBuilder.setTitle("乐然温馨提示");
			View view = ((Activity) context).getLayoutInflater().inflate(
					R.layout.message, null);
			maBuilder.setView(view);
			maBuilder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							targer = true;
							maBuilder.create().dismiss();
							mProgressBar.setVisibility(View.VISIBLE);
							getInstream();
						}
					});
			maBuilder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							targer = false;
							statue.setVisibility(View.VISIBLE);
							if (mProgressBar.isShown()) {
								statue.setVisibility(View.GONE);

							}

							maBuilder.create().dismiss();
						}
					});
			maBuilder.show();
		}

		if (targer) {

			mProgressBar.setVisibility(View.VISIBLE);

			getInstream();
		}
	}

	private void getInstream() {
		statue.setVisibility(View.GONE);
		new Thread() {
			@Override
			public void run() {
				super.run();
				mLiveControl.setLiveParams(getURl(videoName), "admin", "12345");
				if (mLiveControl.LIVE_PLAY == mLiveControl.getLiveState()) {
					mLiveControl.stop();
				}

				if (mLiveControl.LIVE_INIT == mLiveControl.getLiveState()) {
					mLiveControl.startLive(mSurfaceView);
				}
			}
		}.start();
	}

	/**
	 * 该方法是获取播放地址的，当mStreamType=2时，获取的是MAG，当mStreamType =1时获取的子码流，当mStreamType =
	 * 0时获取的是主码流 由于该方法中部分参数是监控点的属性，所以需要先获取监控点信息，具体获取监控点信息的方法见resourceActivity。
	 * 
	 * @param streamType
	 *            2、表示MAG取流方式；1、表示子码流取流方式；0、表示主码流取流方式；
	 * @return String 播放地址 ：2、表示返回的是MAG的播放地址;1、表示返回的是子码流的播放地址；0、表示返回的是主码流的播放地址。
	 * @since V1.0
	 */
	private String getURl(String name) {
		String dataString = null;
		final RealPlayURL realPlayURL = new RealPlayURL();
		String urlString = "http://60.173.247.137:9007/VideoService.svc/GetVideoData";
		HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
		hashMap2.put("cameraName", name);
		ArrayList<LineInfo> lineInfoList = new ArrayList<LineInfo>();
		ServInfo servInfo = new ServInfo();
		VMSNetSDK.getInstance().getLineList(Config.DEF_SERVER, lineInfoList);
		VMSNetSDK.getInstance()
				.login(Config.DEF_SERVER, "admin", "hefeileran802",
						lineInfoList.get(1).lineID, getMac(), servInfo);

		String data = CommonDataService.getJsonData(urlString, hashMap2);
		try {
			JSONObject jsonObject = new JSONObject(data);
			dataString = jsonObject.getString("GetVideoDataResult");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		VMSNetSDK.getInstance().getRealPlayURL(Config.DEF_SERVER,
				servInfo.sessionID, dataString.split("\\|")[0], 1, realPlayURL);

		return realPlayURL.url1;

	}

	protected String getMac() {
		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		String mac = wm.getConnectionInfo().getMacAddress();
		return mac == null ? "" : mac;
	}

	public void stopBtnOnClick() {
		if (null != mLiveControl) {
			mLiveControl.stop();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (null != mLiveControl) {
			if (mIsRecord) {

				mLiveControl.stopRecord();
				mIsRecord = false;
			}
			mLiveControl.stop();
		}
	}

	@Override
	public void onMessageCallback(int messageID) {
		sendMessageCase(messageID);
	}

	/**
	 * 返回已经播放的流量 void
	 * 
	 * @return long
	 * @since V1.0
	 */
	public long getStreamRate() {
		return mStreamRate;
	}

	/**
	 * 发送消息
	 * 
	 * @param i
	 *            void
	 * @since V1.0
	 */
	private void sendMessageCase(int i) {
		if (null != mMessageHandler) {
			Message msg = Message.obtain();
			msg.arg1 = i;
			mMessageHandler.sendMessage(msg);
		}
	}

	/**
	 * 消息类
	 * 
	 * @author huangweifeng
	 * @Data 2013-10-23
	 */
	@SuppressLint("HandlerLeak")
	private final class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case ConstantLive.RTSP_SUCCESS:
				UIUtil.showToast(context, "启动取流成功");
				break;

			case ConstantLive.STOP_SUCCESS:
				UIUtil.showToast(context, "停止成功");
				break;

			case ConstantLive.START_OPEN_FAILED:
				UIUtil.showToast(context, "开启播放库失败");
				if (null != mProgressBar) {
					mProgressBar.setVisibility(View.GONE);
				}
				break;

			case ConstantLive.PLAY_DISPLAY_SUCCESS:
				UIUtil.showToast(context, "播放成功");

				if (null != mProgressBar) {
					mProgressBar.setVisibility(View.GONE);
				}

				break;

			case ConstantLive.RTSP_FAIL:
				statue.setVisibility(View.VISIBLE);
				UIUtil.showToast(context, "RTSP链接失败");
				if (null != mProgressBar) {
					mProgressBar.setVisibility(View.GONE);
				}
				if (null != mLiveControl) {
					mLiveControl.stop();
				}
				break;

			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
	}

	public void getStatu() {
		if (mLiveControl.LIVE_PLAY == mLiveControl.getLiveState()) {
			stopBtnOnClick();
		}
	}

	public void max() {
		Intent intent = new Intent();
		intent.putExtra("flag", "max");
		intent.setAction("flag");
		context.sendBroadcast(intent);// 发广播通知视频下面的东西隐藏
		// ((Activity) context).getWindow().setFlags(
		// WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);

		((Activity) context)
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		mSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(
				myApplication.allhigh, myApplication.allwith));

		isfull = true;

	}

	public void min() {
		Intent intent = new Intent();
		intent.putExtra("flag", "min");
		intent.setAction("flag");
		context.sendBroadcast(intent);
		((Activity) context)
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mSurfaceView.setLayoutParams(layoutParams);

	}

	public void NosendBromin() {
		((Activity) context)
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mSurfaceView.setLayoutParams(layoutParams);
		isfull = false;
	}

	public void NoSendBroMax() {
		((Activity) context)
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		mSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(
				myApplication.allhigh, myApplication.allwith));

		isfull = true;
	}

}
