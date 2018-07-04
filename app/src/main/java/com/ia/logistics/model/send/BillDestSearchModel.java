package com.ia.logistics.model.send;

/**
 * 接收提单变更目的地
 * @author wuyang
 *
 */
public class BillDestSearchModel {

	public static final String Node_Name = "dest";

	/**
	 * 承运商代码
	 */
	private String cysdm;

	/**
	 * 目的地代码
	 */
	private String mdddm;

	/**
	 * 目的地名称
	 */
	private String mddmc;
	/**
	 * 提单目的地名称
	 */
	private String tdmddmc;
	/**
	 * 提单目的地代码
	 */
	private String tdmdddm;

	public String getTdmddmc() {
		return tdmddmc;
	}

	public void setTdmddmc(String tdmddmc) {
		this.tdmddmc = tdmddmc;
	}

	public String getTdmdddm() {
		return tdmdddm;
	}

	public void setTdmdddm(String tdmdddm) {
		this.tdmdddm = tdmdddm;
	}

	public String getCysdm() {
		return cysdm;
	}

	public void setCysdm(String cysdm) {
		this.cysdm = cysdm;
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
