package com.ia.logistics.model.receive;

/**
 * 司机 接受调度计划
 *
 * @author gyh
 */
public class DisPatchPlanLsitModel {
	public static final String Node_Name = "bill";

	/**
	 * 调度下发时间
	 */
	private String xfsj;
	/**
	 * 提单号
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
	 * 件数
	 */
	private String js;
	/**
	 * 预计执行日期
	 */
	private String yjzxrq;
	/**
	 * 提单执行状态 0 未执行, 1 执行中, 2 已结案
	 */

	private String ddzt;
	/**
	 * 调度计划紧急程度 即优先级，用五角星的个数来表示，最多三颗星星 1 一颗星 2 两颗星 3 三颗星
	 */
	private String jjcd;
	/**
	 * 调度计划ID
	 */
	private String ddjhid;

	/**
	 * 0#失败 以0#开头+失败原因
	 *
	 * 1#成功 以1#开头+备注
	 */
	private String fhbz;
	/**
	 * 调度员姓名
	 */
	private String ddyxm;
	/**
	 * 司机姓名
	 */
	private String sjxm;
	/**
	 * 车头代码
	 */
	private String ctdm;
	/**
	 * 提单状态
	 */
	private String tdzt;
	/**
	 *
	 * 出库作业计划
	 */
	private String ckzyjh;
	/**
	 * 实际毛重
	 */
	private String sjmz;
	/**
	 * 实际净重
	 */
	private String sjjz;
	/**
	 * 实际件数
	 */
	private String sjjs;
	/**
	 * 提单材料类型
	 */
	private String cllx;
	/**
	 * 原计划毛重
	 */
	private String yjhmz;
	/**
	 * 原计划净重
	 */
	private String yjhjz;
	/**
	 * 原计划件数
	 */
	private String yjhjs;
	/**
	 * 虚捆包毛重
	 */
	private String ysjmz;
	/**
	 * 虚捆包实际净重
	 */
	private String ysjjz;

	/**
	 * 虚捆包实际件数
	 */
	private String ysjjs;

	public String getYjhmz() {
		return yjhmz;
	}

	public void setYjhmz(String yjhmz) {
		this.yjhmz = yjhmz;
	}

	public String getYjhjz() {
		return yjhjz;
	}

	public void setYjhjz(String yjhjz) {
		this.yjhjz = yjhjz;
	}

	public String getYjhjs() {
		return yjhjs;
	}

	public void setYjhjs(String yjhjs) {
		this.yjhjs = yjhjs;
	}

	public String getYsjmz() {
		return ysjmz;
	}

	public void setYsjmz(String ysjmz) {
		this.ysjmz = ysjmz;
	}

	public String getYsjjz() {
		return ysjjz;
	}

	public void setYsjjz(String ysjjz) {
		this.ysjjz = ysjjz;
	}

	public String getYsjjs() {
		return ysjjs;
	}

	public void setYsjjs(String ysjjs) {
		this.ysjjs = ysjjs;
	}

	public String getCllx() {
		return cllx;
	}

	public void setCllx(String cllx) {
		this.cllx = cllx;
	}

	public String getSjmz() {
		return sjmz;
	}

	public void setSjmz(String sjmz) {
		this.sjmz = sjmz;
	}

	public String getSjjz() {
		return sjjz;
	}

	public void setSjjz(String sjjz) {
		this.sjjz = sjjz;
	}

	public String getSjjs() {
		return sjjs;
	}

	public void setSjjs(String sjjs) {
		this.sjjs = sjjs;
	}

	public String getCkzyjh() {
		return ckzyjh;
	}

	public void setCkzyjh(String ckzyjh) {
		this.ckzyjh = ckzyjh;
	}

	public String getTdzt() {
		return tdzt;
	}

	public void setTdzt(String tdzt) {
		this.tdzt = tdzt;
	}

	public String getDdyxm() {
		return ddyxm;
	}

	public void setDdyxm(String ddyxm) {
		this.ddyxm = ddyxm;
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

	public String getJs() {
		return js;
	}

	public void setJs(String js) {
		this.js = js;
	}

	public String getYjzxrq() {
		return yjzxrq;
	}

	public void setYjzxrq(String yjzxrq) {
		this.yjzxrq = yjzxrq;
	}

	public String getDdzt() {
		return ddzt;
	}

	public void setDdzt(String ddzt) {
		this.ddzt = ddzt;
	}

	public String getJjcd() {
		return jjcd;
	}

	public void setJjcd(String jjcd) {
		this.jjcd = jjcd;
	}

	public String getFhbz() {
		return fhbz;
	}

	public void setFhbz(String fhbz) {
		this.fhbz = fhbz;
	}

}
