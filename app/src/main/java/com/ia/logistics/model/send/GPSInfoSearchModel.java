package com.ia.logistics.model.send;
/**
 *发送GPS的经纬度 search model
 *@author gyh
 */
public class GPSInfoSearchModel {
	public static final String Node_Name = "gps";

	/**
	 *司机U代码
	 */
	private String sjdm;
	/**
	 PAD ID号
	 */
	private String padid;
	/**
	 *经度
	 */
	private String  jd;
	/**
	 *纬度
	 */
	private String wd;
	/**
	 *承运商代码
	 */
	private String cysdm;

	/**
	 * 车辆装载状态
	 */
	private String zzzt;
	/**
	 * 车牌号
	 */
	private String cph;
	/**
	 *司机姓名
	 */
	private String sjxm;
	/**
	 *车次号
	 */
	private String cch;


	public String getSjxm() {
		return sjxm;
	}
	public void setSjxm(String sjxm) {
		this.sjxm = sjxm;
	}
	public String getZzzt() {
		return zzzt;
	}
	public void setZzzt(String zzzt) {
		this.zzzt = zzzt;
	}
	public String getCph() {
		return cph;
	}
	public void setCph(String cph) {
		this.cph = cph;
	}
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
	public String getPadid() {
		return padid;
	}
	public void setPadid(String padid) {
		this.padid = padid;
	}
	public String getJd() {
		return jd;
	}
	public void setJd(String jd) {
		this.jd = jd;
	}
	public String getWd() {
		return wd;
	}
	public void setWd(String wd) {
		this.wd = wd;
	}
	public String getCch() {
		return cch;
	}
	public void setCch(String cch) {
		this.cch = cch;
	}


}
