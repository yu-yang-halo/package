package com.lr.conURL;

public class CommonServiceURL {

	// 服务器
	public static String commonIP = "http://60.173.247.137:9010";

	public static String commonIP2 = "http://60.173.247.137:9007";
	public static String videoData = commonIP2
			+ "/VideoService.svc/GetVideoData";

	// 登录
	public static String UserRegister = commonIP
			+ "/ClientService.svc/GetLogin";

	// GetDeviceInfo
	public static String GetDeviceInfo = commonIP
			+ "/ClientService.svc/GetDeviceInfo";

	// 数据分析
	public static String dataAnalysis = commonIP
			+ "/ClientService.svc/GetSensorData";
}