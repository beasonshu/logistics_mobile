package com.ia.logistics.model.receive;
/**
 * 车载终端    返回成功与否
 * @author gyh
 */
public class MarkerModel {

	public static final String Node_Name = "mark";

	/**
	 * 0#失败 以0#开头+失败原因
	 *
	 * 1#成功 以1#开头+备注
	 */
	private String fhbz;

	/**
	 * 可扩展
	 */

	public String getFhbz() {
		return fhbz;
	}

	public void setFhbz(String fhbz) {
		this.fhbz = fhbz;
	}
}
