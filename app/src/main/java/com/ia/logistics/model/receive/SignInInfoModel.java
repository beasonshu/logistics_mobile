package com.ia.logistics.model.receive;

public class SignInInfoModel{
	public static final String Node_Name = "signin";

	/**
	 * 起运地代码
	 */
	private String thddm;

	/**
	 * 起运地名称
	 */
	private String thdmc;

	/**
	 * 0#失败 以0#开头+失败原因
	 *
	 * 1#成功 以1#开头+备注
	 */
	private String fhbz;

	public String getThddm() {
		return thddm;
	}

	public void setThddm(String thddm) {
		this.thddm = thddm;
	}

	public String getThdmc() {
		return thdmc;
	}

	public void setThdmc(String thdmc) {
		this.thdmc = thdmc;
	}

	public String getFhbz() {
		return fhbz;
	}

	public void setFhbz(String fhbz) {
		this.fhbz = fhbz;
	}
}
