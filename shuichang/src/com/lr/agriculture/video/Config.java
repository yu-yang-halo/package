package com.lr.agriculture.video;

import com.app.MyApplication;

import android.content.Context;
import android.content.SharedPreferences;

public final class Config {
	private static Config ins = new Config();
	private SharedPreferences sp;
	/**
	 * 配置文件文件名
	 */
	private static final String CONFIG_FILE_NAME = "demo_conf";
	/**
	 * 服务器地址
	 */
	private static final String SERVER_ADDR = "server_addr";
	public static String commonIP = "http://60.173.247.137:9008";

	public static String urlString = "http://60.173.247.137:9008/VideoService.svc/GetVideoXml?cameraName=huhuan";

	public static String getNum = commonIP + "/VideoService.svc/GetVideoXml";
	/**
	 * 自动登录
	 */
	private static final String AUTO_LOGIN = "auto_login";

	/**
	 * 默认登录地址，这个地址是我们公司测试地址，开发者可以根据实际情况进行修改
	 */
	public static final String DEF_SERVER = "http://60.173.247.137:81";

	private Config() {
		sp = MyApplication.getIns().getSharedPreferences(CONFIG_FILE_NAME,
				Context.MODE_PRIVATE);
	}

	public static Config getIns() {
		return ins;
	}

	/**
	 * 设置服务器地址
	 * 
	 * @param serverAdrr
	 * @since V1.0
	 */
	public void setServerAddr(String serverAdrr) {
		sp.edit().putString(SERVER_ADDR, serverAdrr).commit();
	}

	/**
	 * 获取服务器地址
	 * 
	 * @return
	 * @since V1.0
	 */
	public String getServerAddr() {
		return sp.getString(SERVER_ADDR, DEF_SERVER);
	}

	/**
	 * 设置自动登录
	 * 
	 * @param autologin
	 * @since V1.0
	 */
	public void setAutoLogin(boolean autologin) {
		sp.edit().putBoolean(AUTO_LOGIN, autologin).commit();
	}

	/**
	 * 是否自动登录
	 * 
	 * @return
	 * @since V1.0
	 */
	public boolean isAutoLogin() {
		return sp.getBoolean(AUTO_LOGIN, false);
	}
}
