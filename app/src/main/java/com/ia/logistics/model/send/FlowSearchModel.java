package com.ia.logistics.model.send;

public class FlowSearchModel {
	public static final String Node_Name = "flow";
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
	 * 设备号
	 */
	private String sbh;
	/**
	 * 国际用户识别码
	 */
	private String gjyhsbm;
	/**
	 * 总流量
	 */
	private String zll;
	/**
	 * 车载软件使用量
	 */
	private String czll;
	/**
	 * 分页偏移量
	 */
	private String pyl;
	/**
	 * 每页条数
	 */
	private String myts;
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
	public String getSbh() {
		return sbh;
	}
	public void setSbh(String sbh) {
		this.sbh = sbh;
	}
	public String getGjyhsbm() {
		return gjyhsbm;
	}
	public void setGjyhsbm(String gjyhsbm) {
		this.gjyhsbm = gjyhsbm;
	}
	public String getZll() {
		return zll;
	}
	public void setZll(String zll) {
		this.zll = zll;
	}
	public String getCzll() {
		return czll;
	}
	public void setCzll(String czll) {
		this.czll = czll;
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
