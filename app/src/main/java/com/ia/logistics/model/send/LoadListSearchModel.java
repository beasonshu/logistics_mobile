package com.ia.logistics.model.send;

/**
 * 司机 发送装车清单 (主 ) search model
 */
public class LoadListSearchModel {

	public static final String Node_Name = "car";
	/**
	 * 承运商代码
	 */
	private String cysdm;
	/**
	 * com.baosight.iplat4mandroid车次任务
	 */
	private String cch;

	/**
	 * 司机U代码
	 */
	private String sjdm;

	/**
	 * 司机姓名
	 */
	private String sjxm;

	/**
	 * 车头代码
	 */
	private String ctdm;

	/**
	 * 挂车代码
	 */
	private String gcdm;
	/**
	 * 操作标志 00 删除车次材料(或到货车次红冲) ；10 新增车次材料（或到货确认）
	 */
	private String czbz;

	/**
	 * 10装车；20到货
	 */
	private String flag;

	/**
	 * 到货地点
	 *
	 */
	private String dhdd;
	/**
	 * 到货代码
	 */
	private String dhdm;

	public String getSjdm() {
		return sjdm;
	}

	public void setSjdm(String sjdm) {
		this.sjdm = sjdm;
	}

	public String getCzbz() {
		return czbz;
	}

	public String getCch() {
		return cch;
	}

	public void setCch(String cch) {
		this.cch = cch;
	}

	public void setCzbz(String czbz) {
		this.czbz = czbz;
	}

	public String getSjxm() {
		return sjxm;
	}

	public void setSjxm(String sjxm) {
		this.sjxm = sjxm;
	}

	public String getCtdm() {
		return ctdm;
	}

	public void setCtdm(String ctdm) {
		this.ctdm = ctdm;
	}

	public String getGcdm() {
		return gcdm;
	}

	public void setGcdm(String gcdm) {
		this.gcdm = gcdm;
	}

	public String getCysdm() {
		return cysdm;
	}

	public void setCysdm(String cysdm) {
		this.cysdm = cysdm;
	}

	public String getDhdd() {
		return dhdd;
	}

	public void setDhdd(String dhdd) {
		this.dhdd = dhdd;
	}

	public String getDhdm() {
		return dhdm;
	}

	public void setDhdm(String dhdm) {
		this.dhdm = dhdm;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
