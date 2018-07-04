package com.ia.logistics.model.receive;

/**
 * 接收目的地变更列表
 * @author wuyang
 *
 */
public class BillDestModel extends MarkerModel{

	public static final String Node_Name = "dest";

	/**
	 * 目的地代码
	 */
	private String mdddm;

	/**
	 * 目的地名称
	 */
	private String mddmc;

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

	public String getMdddm() {
		return mdddm;
	}

	public void setMdddm(String mdddm) {
		this.mdddm = mdddm;
	}

	public String getMddmc() {
		return mddmc;
	}

	public void setMddmc(String mddmc) {
		this.mddmc = mddmc;
	}

}
