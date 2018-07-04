package com.ia.logistics.model.receive;


/**
 *接受签收单result model
 *@author gyh
 */
public class SignsModel extends MarkerModel {

	public static final String Node_Name = "signSend";
	private String qsdid;//签收单Id
	private String cch;//车次号
	private String ctdm;//车牌号(车头代码)
	private String qssj;//签收时间
	private String qsbz;//签收备注
	private String qszt;//签收状态
	private String mddmc;//目的地名称
	private String mz;//毛重
	private String jz;//净重
	private String js;//件数

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
	public String getQsdid() {
		return qsdid;
	}
	public void setQsdid(String qsdid) {
		this.qsdid = qsdid;
	}
	public String getCch() {
		return cch;
	}
	public void setCch(String cch) {
		this.cch = cch;
	}
	public String getCtdm() {
		return ctdm;
	}
	public void setCtdm(String ctdm) {
		this.ctdm = ctdm;
	}
	public String getQssj() {
		return qssj;
	}
	public void setQssj(String qssj) {
		this.qssj = qssj;
	}
	public String getQsbz() {
		return qsbz;
	}
	public void setQsbz(String qsbz) {
		this.qsbz = qsbz;
	}
	public String getQszt() {
		return qszt;
	}
	public void setQszt(String qszt) {
		this.qszt = qszt;
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
	public String getJs() {
		return js;
	}
	public void setJs(String js) {
		this.js = js;
	}

}
