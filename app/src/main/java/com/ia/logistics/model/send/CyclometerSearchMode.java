package com.ia.logistics.model.send;

public class CyclometerSearchMode {

	public static final String Node_Name = "lmb";
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
	 * 承运商代码
	 */
	private String cysdm;
	/**
	 * 路码
	 */
	private String lm;
	/**
	 * 登录注销标志
	 */
	private String flag;
	/**
	 * 分页偏移量
	 */
	private String pyl;
	/**
	 * 每页条数
	 */
	private String myts;
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
	public String getCysdm() {
		return cysdm;
	}
	public void setCysdm(String cysdm) {
		this.cysdm = cysdm;
	}
	public String getLm() {
		return lm;
	}
	public void setLm(String lm) {
		this.lm = lm;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getPyl() {
		return pyl;
	}
	public void setPyl(String pyl) {
		this.pyl = pyl;
	}
	public String getMyts() {
		return myts;
	}
	public void setMyts(String myts) {
		this.myts = myts;
	}

}
