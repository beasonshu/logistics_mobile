package com.ia.logistics.model.send;


/**
 * 司机 发送装车清单 (子)search model
 *
 * @author gyh
 */
public class LoadListChildSearchModel {
	public static final String Node_Name = "bill";

	/**
	 * 提单号
	 */
	private String tdh;

	/**
	 * 调度计划ID
	 */
	private String ddjhid;

	/**
	 * 材料号
	 */
	private String clh;
	/**
	 * 毛重
	 */
	private String mz;
	/**
	 *净重
	 */
	private String jz;
	/**
	 * 件数
	 */
	private String js;
	/**
	 *提单材料类型
	 */
	private String cllx;
	/**
	 *合同号
	 */
	private String hth;

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

	public String getJs() {
		return js;
	}

	public void setJs(String js) {
		this.js = js;
	}

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

	public String getClh() {
		return clh;
	}

	public void setClh(String clh) {
		this.clh = clh;
	}

	public String getDdjhid() {
		return ddjhid;
	}

	public void setDdjhid(String ddjhid) {
		this.ddjhid = ddjhid;
	}

	public String getHth() {
		return hth;
	}

	public void setHth(String hth) {
		this.hth = hth;
	}
}
