package com.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.DB.DBUtils;
import com.app.MyApplication;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.lr.javaBean.Utility;

public class ToSoA {// 协议类
	String message;
	private Context context;
	private SharedPreferences ndsharedPreferences;//
	private SharedPreferences shidusharedPreferences;//湿度
	private SharedPreferences co2SharedPreferences;//Co2 保存
	private SharedPreferences hsSharedPreferences;//硫化氢
	// Fram fram=SinFram.getSinFram();//保存类
	private MyApplication myApplication;
	private String messgaewendu;
	private String catotpey;

	public ToSoA(String message, Context context) {
		ndsharedPreferences = context.getSharedPreferences("wendu",
				context.MODE_PRIVATE);
		shidusharedPreferences = context.getSharedPreferences("shidu",
				context.MODE_PRIVATE);
		co2SharedPreferences = context.getSharedPreferences("co2",
				context.MODE_PRIVATE);
		hsSharedPreferences = context.getSharedPreferences("hs",
				context.MODE_PRIVATE);
		this.message = message;
		this.context = context;
		this.myApplication = (MyApplication) context.getApplicationContext();

	}

	public void getWay(String dviId) {
		int length = message.length();
		int num = length / 10;// 多少组
		int count = 0;// 目前的位置
		for (int i = 0; i < num; i++) {
			if (count < length) {
				String mes = message.substring(count, count + 10);
				// String string = mes.substring(2,10);
				EveryMessgae(mes, dviId);// 根据每个字来判断相应的东西
				count = count + 10;
				num++;
			} else {
				return;// 返回之后
			}
		}
	}

	public void EveryMessgae(String message, String dveID) {
		Fram fram = SavaFra.getFram(dveID);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		fram.setTime(simpleDateFormat.format(new Date()));
		String head = message.substring(0, 2);

		float number = Utility.HexString2Float(message.substring(2, 10));
		Log.e("head", head + "|" + number);
		if (head.equals("01")) {// 溶氧
			// 光照
			fram.setO1(number);
			String value = ndsharedPreferences.getString(dveID, "");
			setMaxandMIn(dveID, number, value, "NH4");
		} else if (head.equals("02")) {// 溶氧饱和度
			fram.setO2o(number);
			String value = shidusharedPreferences.getString(dveID, "");
			setMaxandMIn(dveID, number, value, "shidu");
		} else if (head.equals("03")) {// ph 值
			fram.setPH3(number);
			String value = hsSharedPreferences.getString(dveID, "");
			setMaxandMIn(dveID, number, value, "wendu");

		} else if (head.equals("04")) {// 氨氮
			fram.setnH4N(number);
			String value = co2SharedPreferences.getString(dveID, "");
			setMaxandMIn(dveID, number, value, "co2");
		} else if (head.equals("05")) {// 温度
			fram.setTem5(number);
		} else if (head.equals("06")) {// 亚硝酸盐
			fram.setT6(number);
		} else if (head.equals("07")) {
			fram.setY7(number);// 液位
		} else if (head.equals("08")) {
			fram.setH2S8(number);// 硫化氢
		} else if (head.equals("09")) {
			fram.setZd9(number);// 浊度
		} else if (head.equals("0a")) {
			fram.setYan10(number);// 盐度
		} else if (head.equals("0b")) {
			fram.setDian11(number);// 电导率
		} else if (head.equals("0c")) {
			fram.setHua12(number);// 化学需量
		} else if (head.equals("0d")) {
			fram.setQi13(number);// 大气压
		} else if (head.equals("oe")) {
			fram.setFengSu14(number);// 风速
		} else if (head.equals("of")) {
			fram.setFengXiang15(number);// 风向
		} else if (head.equals("10")) {
			fram.setYe16(number);// 叶绿色
		} else if (head.equals("11")) {
			fram.setDQW17(number);// 大气温度
		} else if (head.equals("12")) {
			fram.setDQS18(number);// 大气湿度
		} else if (head.equals("13")) {
			fram.setLat19(number);// 纬度
		} else if (head.equals("14")) {
			fram.setLgt20(number);// 经度
		}else if (head.equals("15")) {
			fram.setSpd21(number);// 速度
		}
		System.out.println("FRAM_DATA head  "+head+"  : number "+number);

	}

	private void setMaxandMIn(String dveID, float number, String value,
			String type) {
		if (!"".equals(value)) {

			float high = Float.parseFloat(value.split("\\|")[0]);//取出保存的值
			float low = Float.parseFloat(value.split("\\|")[1]);//最低值
			if (high != low) {
				if (number < low || number > high) {
					Integer integer = myApplication.getTemHash(dveID);// 存取已经保存的值
					if (null == integer) {
						integer = 0;
					}
					Date date = new Date();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String time = simpleDateFormat.format(date);
					String getmessage = getmessage(number, type, high, low);
					boolean hashMap = myApplication.getHashMap(dveID);
					if (hashMap) {
						shut(getmessage);
					}
					long add = DBUtils.add(context, getmessage, type, dveID,
							time);
					Log.e("add", add + "");
					Integer num = integer + 1;// 增长数
					myApplication.setTemHashmap(dveID, num);// 保存相应的数字，便于下次处理
				}
			}
		}
	}

	public void shut(String messageShut) {
		SpeechUtility.createUtility(context, SpeechConstant.APPID
				+ "= 5594b30f");
		SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context,
				null);
		mTts.startSpeaking(messageShut, null);

	}

	private String getmessage(float number, String type, float high, float low) {
		if ("shidu".equals(type)) {
			catotpey = "湿度";
		}
		if ("wendu".equals(type)) {
			catotpey = "温度";

		}
		if ("NH4".equals(type)) {
			catotpey = "溶氧值";
		}
		if ("co2".equals(type)) {
			catotpey = "co2";
		}

		if (number < low) {
			messgaewendu = "当前" + catotpey + "为" + number + "小于设定的下限请处理";

		} else if (number > high) {
			messgaewendu = "当前" + catotpey + "为" + number + "大于设定的上限请处理";
		}
		return messgaewendu;

	}

}
