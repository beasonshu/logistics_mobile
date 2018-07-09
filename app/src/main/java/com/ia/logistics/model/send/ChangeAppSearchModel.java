package com.ia.logistics.model.send;
/**
 *发送申请换车指令  search  model
 *@author gyh
 */
public class ChangeAppSearchModel {

	public static final String Node_Name = "app";
	/**
	 *火车车次任务
	 */
	private String cch;
	/**
	 *车头代码
	 */
	private String ctdm;
	/**
	 *挂车代码
	 */
	private String gcdm;
	/**
	 *司机U代码
	 */
	private String sjdm;
	/**
	 *故障类型    	10 车头故障,  20 挂车故障
	 */
	private String gzlx;
	public String getCch() {
		return cch;
	}
	public void setCch(String cch) {
		this.cch = cch;
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
	public String getSjdm() {
		return sjdm;
	}
	public void setSjdm(String sjdm) {
		this.sjdm = sjdm;
	}
	public String getGzlx() {
		return gzlx;
	}
	public void setGzlx(String gzlx) {
		this.gzlx = gzlx;
	}


}
