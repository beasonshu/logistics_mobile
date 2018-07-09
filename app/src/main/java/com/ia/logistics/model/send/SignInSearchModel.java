package com.ia.logistics.model.send;

public class SignInSearchModel {
	public static final String Node_Name = "sign";
	/**
	 * 承运商代码
	 */
	private String cysdm;
	/**
	 * 司机代码
	 */
	private String sjdm;
	/**
	 * 司机姓名
	 */
	private String sjxm;
	/**
	 * 车头代码
	 */
	private String ctdm;
	/**
	 * 挂车代码
	 */
	private String gcdm;
	/**
	 * 起运地代码
	 */
	private String thddm;
	/**
	 * 起运地名称
	 */
	private String thdmc;
	/**
	 * 业务操作标志
	 */
	private String czbz;

	public String getCysdm() {
		return cysdm;
	}

	public void setCysdm(String cysdm) {
		this.cysdm = cysdm;
	}

	public String getSjdm() {
		return sjdm;
	}

	public void setSjdm(String sjdm) {
		this.sjdm = sjdm;
	}

	public String getSjxm() {
		return sjxm;
	}

	public void setSjxm(String sjxm) {
		this.sjxm = sjxm;
	}

	public String getCtdm() {
		return ctdm;
	}

	public void setCtdm(String ctdm) {
		this.ctdm = ctdm;
	}

	public String getGcdm() {
		return gcdm;
	}

	public void setGcdm(String gcdm) {
		this.gcdm = gcdm;
	}

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

	public String getCzbz() {
		return czbz;
	}

	public void setCzbz(String czbz) {
		this.czbz = czbz;
	}

}
