package com.ia.logistics.model.receive;
/**
 * 接受业绩统计详细
 * @author gyh
 *
 */
public class PerStatisticsDetModel {
	public static final String Node_Name = "det";

	/**
	 * 调度计划id
	 */
	private String ddjhid;
	/**
	 * 下发时间
	 */
	private String xfsj;
	/**
	 * 运输订单
	 */
	private String tdh;
	/**
	 * 提货点代码
	 */
	private String thddm;
	/**
	 * 提货点名称
	 */
	private String thdmc;
	/**
	 * 目的地代码
	 */
	private String mdddm;
	/**
	 * 目的地名称
	 */
	private String mddmc;
	/**
	 * 毛重
	 */
	private String mz;
	/**
	 * 净重
	 */
	private String jz;
	/**
	 * 优先级
	 */
	private String jjcd;
	/**
	 * 调度员姓名
	 */
	private String ddyxm;
	/**
	 * 车头代码
	 */
	private String ctdm;
	/**
	 * 件数
	 */
	private String js;
	/**
	 * 签收异常
	 */
	private String qsyc;
	/**
	 * 返回标志
	 */
	private String fhbz;
	/**
	 * 制造单元号
	 * @return
	 */
	private String ckzyjh;

	public String getCkzyjh() {
		return ckzyjh;
	}
	public void setCkzyjh(String ckzyjh) {
		this.ckzyjh = ckzyjh;
	}
	public String getFhbz() {
		return fhbz;
	}
	public void setFhbz(String fhbz) {
		this.fhbz = fhbz;
	}
	public String getQsyc() {
		return qsyc;
	}
	public void setQsyc(String qsyc) {
		this.qsyc = qsyc;
	}
	public String getDdjhid() {
		return ddjhid;
	}
	public void setDdjhid(String ddjhid) {
		this.ddjhid = ddjhid;
	}
	public String getXfsj() {
		return xfsj;
	}
	public void setXfsj(String xfsj) {
		this.xfsj = xfsj;
	}
	public String getTdh() {
		return tdh;
	}
	public void setTdh(String tdh) {
		this.tdh = tdh;
	}
	public String getThddm() {
		return thddm;
	}
	public void setThddm(String thddm) {
		this.thddm = thddm;
	}
	public String getThdmc() {
		return thdmc;
	}
	public void setThdmc(String thdmc) {
		this.thdmc = thdmc;
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
	public String getJjcd() {
		return jjcd;
	}
	public void setJjcd(String jjcd) {
		this.jjcd = jjcd;
	}
	public String getDdyxm() {
		return ddyxm;
	}
	public void setDdyxm(String ddyxm) {
		this.ddyxm = ddyxm;
	}
	public String getCtdm() {
		return ctdm;
	}
	public void setCtdm(String ctdm) {
		this.ctdm = ctdm;
	}
	public String getJs() {
		return js;
	}
	public void setJs(String js) {
		this.js = js;
	}

}
