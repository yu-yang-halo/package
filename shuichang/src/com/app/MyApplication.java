package com.app;

import java.io.Serializable;
import java.util.HashMap;
import com.bean.Fram;
import com.hik.mcrsdk.MCRSDK;
import com.hik.mcrsdk.rtsp.RtspClient;
import android.app.Application;

/**
 * Application 类
 * 
 * @author 胡大欢
 * 
 */
public class MyApplication extends Application {
	private static MyApplication ins;
	public HashMap<String, String> hashMap;
	public HashMap<String, Fram> hashMapFram;
	public HashMap<String, Integer> tm5 = new HashMap<String, Integer>();
	public HashMap<String, Integer> shidu;
	public int allhigh;
	public int allwith;
	public int with;
	public int hight;
	private String id;
	private boolean IsDown = true;
	public HashMap<String, Boolean> shut = new HashMap<String, Boolean>();
	public HashMap<String, Integer> now = new HashMap<String, Integer>();

	public String pingpai;// 判断小米手机
	public HashMap<Integer, Integer> flaghashMap2 = new HashMap<Integer, Integer>();

	@Override
	public void onCreate() {
		super.onCreate();
		ins = this;
		MCRSDK.init();
		RtspClient.initLib();

		MCRSDK.setPrint(1, null);
	}

	public void putHashInt(String string, Integer integer) {
		now.put(string, integer);

	}

	public HashMap<String, Integer> getHashMap() {

		return now;
	}

	public void putHashMap(String deId, boolean mark) {
		shut.put(deId, mark);
	}

	public boolean getHashMap(String DevId) {
		if (shut != null && shut.get(DevId) != null) {
			return shut.get(DevId);
		}
		return false;
	}

	public void setId(String id) {
		this.id = id;

	}

	public String getId() {
		return id;
	}

	public void setTemHashmap(String devid, Integer num) {
		tm5.put(devid, num);

	}

	public Integer getTemHash(String devid) {
		return tm5.get(devid);
	}

	public void setIsdown(boolean down) {
		IsDown = down;

	}

	public boolean getISdown() {
		return IsDown;
	}

	public static MyApplication getIns() {
		return ins;
	}
}
