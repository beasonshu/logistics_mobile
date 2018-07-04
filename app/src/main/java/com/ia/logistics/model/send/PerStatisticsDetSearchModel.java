package com.ia.logistics.model.send;
/**
 * 接受业绩统计详细
 * @author gyh
 *
 */
public class PerStatisticsDetSearchModel {
	public static final String Node_Name = "det";
	/**
	 * 司机代码
	 */
	private String sjdm;
	/**
	 * 日期跨度开始时间
	 */
	private String jhkssj;
	/**
	 * 日期跨度结束时间
	 */
	private String jhjssj;
	/**
	 * 计量单位
	 */
	private String jldw;
	/**
	 * 提单执行状态
	 * 0,未执行
	 * 1，执行中
	 * 2，已完成
	 */
	private String tdzt;
	/**
	 * 分页偏移量
	 */
	private String pyl;

	/**
	 * 每页条数
	 */
	private String myts;

	public String getPyl() {
		return pyl;
	}
	public void setPyl(String pyl) {
		this.pyl = pyl;
	}
	public String getMyts() {
		return myts;
	}
	public void setMyts(String myts) {
		this.myts = myts;
	}
	public String getSjdm() {
		return sjdm;
	}
	public void setSjdm(String sjdm) {
		this.sjdm = sjdm;
	}
	public String getJhkssj() {
		return jhkssj;
	}
	public void setJhkssj(String jhkssj) {
		this.jhkssj = jhkssj;
	}
	public String getJhjssj() {
		return jhjssj;
	}
	public void setJhjssj(String jhjssj) {
		this.jhjssj = jhjssj;
	}
	public String getJldw() {
		return jldw;
	}
	public void setJldw(String jldw) {
		this.jldw = jldw;
	}
	public String getTdzt() {
		return tdzt;
	}
	public void setTdzt(String tdzt) {
		this.tdzt = tdzt;
	}


}
