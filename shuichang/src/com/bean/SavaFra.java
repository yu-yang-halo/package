package com.bean;

import java.util.HashMap;

public class SavaFra {
	private static HashMap<String, Fram> hashMap = new HashMap<String, Fram>();

	public static void put(String Dviceid) {
		Fram fram = new Fram();// 增加对象为了确保唯一性

		hashMap.put(Dviceid, fram);
	}

	public static Fram getFram(String DviceId) {

		return hashMap.get(DviceId);

	}

}
