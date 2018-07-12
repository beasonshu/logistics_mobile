package com.ia.logistics.model.receive;
/**
 * 发送装车清单
 * @author gyh
 *
 */
public class LoadListModel {

	public static final String Node_Name = "car";
	/**
	 * com.baosight.iplat4mandroid车次任务
	 */
	private String cch;
	/**
	 * 材料号
	 */
	private String clh;
	/**
	 * 合同号
	 */
	private String hth;
	/**
	 * 材料名称
	 */
	private String pzmc;
	/**
	 * 返回标志
	 */
	private String fhbz;

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
	public String getCch() {
		return cch;
	}
	public void setCch(String cch) {
		this.cch = cch;
	}
	public String getFhbz() {
		return fhbz;
	}
	public void setFhbz(String fhbz) {
		this.fhbz = fhbz;
	}

}
