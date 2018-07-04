package com.ia.logistics.model.receive;
/**
 *接收汽运车次（详细）  result model
 *@author gyh
 */

public class TrainListChildModel {
	public static final String Node_Name = "pack";

	/**
	 材料号
	 */
	private String clh;
	/**
	 *合同号
	 */
	private String hth;
	/**
	 * 提单号
	 */
	private String tdh;

	/**
	 * 提货点名称
	 */
	private String thdmc;

	/**
	 * 提货点代码
	 */
	private String thddm;

	/**
	 * 调度计划id
	 */
	private String ddjhid;

	/**
	 *品种名称
	 */
	private String pzmc;
	/**
	 *毛重
	 */
	private String mz;
	/**
	 *净重
	 */
	private String jz;
	/**
	 * 0#失败 以0#开头+失败原因
	 *
	 * 1#成功 以1#开头+备注
	 */
	private String fhbz;

	/**
	 *目的名称
	 */
	private String mddmc;

	/**
	 *目的代码
	 */
	private String mdddm;
	/**
	 * 件数
	 */
	private String js;
	/**
	 * 提单材料类型
	 */
	private String cllx;

	public String getCllx() {
		return cllx;
	}
	public void setCllx(String cllx) {
		this.cllx = cllx;
	}
	public String getTdh() {
		return tdh;
	}

	public void setTdh(String tdh) {
		this.tdh = tdh;
	}

	public String getThdmc() {
		return thdmc;
	}

	public void setThdmc(String thdmc) {
		this.thdmc = thdmc;
	}

	public String getMddmc() {
		return mddmc;
	}

	public void setMddmc(String mddmc) {
		this.mddmc = mddmc;
	}

	public String getFhbz() {
		return fhbz;
	}
	public void setFhbz(String fhbz) {
		this.fhbz = fhbz;
	}
	public String getJs() {
		return js;
	}
	public void setJs(String js) {
		this.js = js;
	}

	public String getClh() {
		return clh;
	}
	public void setClh(String clh) {
		this.clh = clh;
	}
	public String getHth() {
		return hth;
	}
	public void setHth(String hth) {
		this.hth = hth;
	}
	public String getPzmc() {
		return pzmc;
	}
	public void setPzmc(String pzmc) {
		this.pzmc = pzmc;
	}
	public String getMz() {
		return mz;
	}
	public void setMz(String mz) {
		this.mz = mz;
	}
	public String getJz() {
		return jz;
	}
	public void setJz(String jz) {
		this.jz = jz;
	}
	public void setThddm(String thddm) {
		this.thddm = thddm;
	}
	public String getThddm() {
		return thddm;
	}
	public void setMdddm(String mdddm) {
		this.mdddm = mdddm;
	}
	public String getMdddm() {
		return mdddm;
	}

	public String getDdjhid() {
		return ddjhid;
	}
	public void setDdjhid(String ddjhid) {
		this.ddjhid = ddjhid;
	}



}
