package com.ia.logistics.model.receive;

import java.io.Serializable;

/**
 * 签收单详细信息 result model
 *
 */
public class SignDetilModel extends MarkerModel implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String Node_Name = "signInfo";
	private String clh;// 材料号(捆包号)
	private String hth;// 合同号
	private String mz;// 毛重
	private String jz;// 净重
	private String pzmc;// 品种名称
	private String thdmc;// 提货点名称

	public String getThdmc() {
		return thdmc;
	}

	public void setThdmc(String thdmc) {
		this.thdmc = thdmc;
	}

	/**
	 * 0#失败 以0#开头+失败原因
	 *
	 * 1#成功 以1#开头+备注
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

	public String getPzmc() {
		return pzmc;
	}

	public void setPzmc(String pzmc) {
		this.pzmc = pzmc;
	}

	public String getFhbz() {
		return fhbz;
	}

	public void setFhbz(String fhbz) {
		this.fhbz = fhbz;
	}

}
