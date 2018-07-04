package com.ia.logistics.model.receive;

/**
 * 提单详细
 *
 */
public class BillChildModel extends MarkerModel {
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
	 * 是否已装载
	 */
	private String sfyzz;
	/**
	 * 0#失败 以0#开头+失败原因
	 * 1#成功 以1#开头+备注
	 */
	private String fhbz;
	/**
	 * 提单状态
	 */
	private String tdzt;
	/**
	 * 材料过程状态
	 * @return
	 */
	private String gczt;
	/**
	 * 签收异常
	 */
	private String qsyc;
	/**
	 * 司机姓名
	 * @return
	 */
	private String sjxm;

	public String getSjxm() {
		return sjxm;
	}

	public void setSjxm(String sjxm) {
		this.sjxm = sjxm;
	}

	public String getQsyc() {
		return qsyc;
	}

	public void setQsyc(String qsyc) {
		this.qsyc = qsyc;
	}

	public String getGczt() {
		return gczt;
	}

	public void setGczt(String gczt) {
		this.gczt = gczt;
	}

	public String getTdzt() {
		return tdzt;
	}

	public void setTdzt(String tdzt) {
		this.tdzt = tdzt;
	}

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
