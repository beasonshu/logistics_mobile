package com.ia.logistics.model.receive;
/**
 *接收汽运车次  result model
 *@author gyh
 */
public class TrainListModel {

	public static final String Node_Name = "tra";
	/**
	 *com.baosight.iplat4mandroid车次任务
	 */
	private String cch;

	/**
	 *司机姓名
	 */
	private String sjxm;
	/**
	 *车牌号
	 */
	private String cph;
	/**
	 *挂车号
	 */
	private String gch;

	/**
	 *调度下发时间
	 */
	private String xfsj;
	/**
	 *毛重
	 */
	private String mz;
	/**
	 *净重
	 */
	private String jz;
	/**
	 *件数
	 */
	private String js;
	/**
	 *车次状态
	 */
	private String cczt;
	/**
	 * 0#失败 以0#开头+失败原因
	 *
	 * 1#成功 以1#开头+备注
	 */
	private String fhbz;

	/**
	 * 汽运车次过程状态
	 * 00已装车 10已发车 20部分到货21全部到货 30部分签收 31全部签收
	 */
	private String gczt;
	/**
	 * 材料类型
	 */
	private String cllx;
	/**
	 * 业务类型
	 * 默认为空:表示正常流程(有调度有司机);10表示只有司机模式
	 */
	private String ywlx ;
	/**
	 * 预载清单号
	 */
	private String yzqdh ;

	public String getCllx() {
		return cllx;
	}

	public void setCllx(String cllx) {
		this.cllx = cllx;
	}

	public String getFhbz() {
		return fhbz;
	}

	public void setFhbz(String fhbz) {
		this.fhbz = fhbz;
	}

	public String getCch() {
		return cch;
	}
	public void setCch(String cch) {
		this.cch = cch;
	}
	public String getSjxm() {
		return sjxm;
	}
	public void setSjxm(String sjxm) {
		this.sjxm = sjxm;
	}
	public String getCph() {
		return cph;
	}
	public void setCph(String cph) {
		this.cph = cph;
	}
	public String getGch() {
		return gch;
	}
	public void setGch(String gch) {
		this.gch = gch;
	}
	public String getXfsj() {
		return xfsj;
	}
	public void setXfsj(String xfsj) {
		this.xfsj = xfsj;
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
	public String getCczt() {
		return cczt;
	}
	public void setCczt(String cczt) {
		this.cczt = cczt;
	}

	/**
	 * @param gczt the gczt to set
	 */
	public void setGczt(String gczt) {
		this.gczt = gczt;
	}

	/** *@param gczt
	 * @return the gczt
	 */
	public String getGczt() {
		return gczt;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getYzqdh() {
		return yzqdh;
	}

	public void setYzqdh(String yzqdh) {
		this.yzqdh = yzqdh;
	}

}
