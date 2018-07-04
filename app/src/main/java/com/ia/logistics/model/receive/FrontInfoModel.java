package com.ia.logistics.model.receive;
/**
 * 司机  接收车头信息  result Model
 * @author gyh
 */

public class FrontInfoModel {

	public static final String Node_Name = "car";
	/**
	 * 司机  车头代码
	 */
	private String ctdm;
	/**
	 * 司机  车牌号
	 */
	private String cph;
	/**
	 * 0#失败 以0#开头+失败原因
	 *
	 * 1#成功 以1#开头+备注
	 */
	private String fhbz;

	public String getFhbz() {
		return fhbz;
	}

	public void setFhbz(String fhbz) {
		this.fhbz = fhbz;
	}

	public String getCtdm() {
		return ctdm;
	}
	public void setCtdm(String ctdm) {
		this.ctdm = ctdm;
	}
	public String getCph() {
		return cph;
	}
	public void setCph(String cph) {
		this.cph = cph;
	}

}
