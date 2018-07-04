package com.ia.logistics.model.receive;

/**
 * 司机  接收调度计划  提单详细   rsult  model
 * @author TangAijun
 *
 */
public class DisPatchPlanDetLsitModel {
	public static final String Node_Name = "pack";

	/**
	 * 材料号
	 */
	private String clh;

	/**
	 * 合同号
	 */
	private String hth;

	/**
	 * 品种名称
	 */
	private String pzmc;

	/**
	 * 毛重
	 */
	private String mz;

	/**
	 * 净重
	 */
	private String jz;

	/**
	 * 是否已装载  	0 未装载,    1 已装载
	 */
	private String sfyzz;
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

	public String getSfyzz() {
		return sfyzz;
	}

	public void setSfyzz(String sfyzz) {
		this.sfyzz = sfyzz;
	}

}
