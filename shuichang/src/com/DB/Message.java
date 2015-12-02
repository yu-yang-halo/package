package com.DB;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable {
	public String message;// 信息
	public String typeNo;//设备类型

	public String getTypeNo() {
		return typeNo;
	}

	public void setTypeNo(String typeNo) {
		this.typeNo = typeNo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {

		this.time = time;
	}

	public String time;
	public String category;// 类别
	public int id;//

}
