package com.ia.logistics.model.receive;

public class DownloadHeadAndBodyModel {

	public static final String Node_Name = "sendHeadBody";

	private String cph;//车牌号 返回车头或挂车

	private String fhbz;//返回标志	“0#”表示没有车头或挂车；“1#”表示成功

	private String userName;// 司机姓名

	private String ctzt; // #0标示 车头未使用；#1标示使用中

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCtzt() {
		return ctzt;
	}

	public void setCtzt(String ctzt) {
		this.ctzt = ctzt;
	}

	public String getCph() {
		return cph;
	}

	public void setCph(String cph) {
		this.cph = cph;
	}

	public String getFhbz() {
		return fhbz;
	}

	public void setFhbz(String fhbz) {
		this.fhbz = fhbz;
	}



}


/*
result_flag	返回标志	“0#”表示没有车头或挂车；“1#”表示成功
car_num	车牌号	返回车头或挂车

*/