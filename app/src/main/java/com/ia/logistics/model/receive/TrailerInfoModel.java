package com.ia.logistics.model.receive;
/**
 * 司机  接收挂车信息 result
 * @author gyh
 *
 */
public class TrailerInfoModel {

	public static final String Node_Name = "trai";
	/**
	 * 挂车代码  1 常用, 0 所有
	 */
	private String gcdm;
	/**
	 *挂车号
	 */
	private String gch;
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

	public String getGcdm() {
		return gcdm;
	}
	public void setGcdm(String gcdm) {
		this.gcdm = gcdm;
	}
	public String getGch() {
		return gch;
	}
	public void setGch(String gch) {
		this.gch = gch;
	}
}
