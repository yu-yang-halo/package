package com.lr.javaBean;

import java.io.Serializable;

public class DataALL implements Serializable {
	private String CustomerNo;
	private String DeviceID;
	private String ProvinceName;
	private String OrgName;
	private String PondName;
	private String VedioName;
	private String Electrics;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	private String cityName;

	public String getCustomerNo() {
		return CustomerNo;
	}

	public void setCustomerNo(String customerNo) {
		CustomerNo = customerNo;
	}

	public String getDeviceID() {
		return DeviceID;
	}

	public void setDeviceID(String deviceID) {
		DeviceID = deviceID;
	}

	public String getProvinceName() {
		return ProvinceName;
	}

	public void setProvinceName(String provinceName) {
		ProvinceName = provinceName;
	}

	public String getOrgName() {
		return OrgName;
	}

	public void setOrgName(String orgName) {
		OrgName = orgName;
	}

	public String getPondName() {
		return PondName;
	}

	public void setPondName(String pondName) {
		PondName = pondName;
	}

	public String getVedioName() {
		return VedioName;
	}

	public void setVedioName(String vedioName) {
		VedioName = vedioName;
	}

	public String getElectrics() {
		return Electrics;
	}

	public void setElectrics(String electrics) {
		Electrics = electrics;
	}

}
