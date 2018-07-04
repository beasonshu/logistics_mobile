package com.ia.logistics.model.send;

public class AchievmentsSearchModel {
	public static final String Node_Name = "ach";
	/**
	 * 司机代码
	 */
	private String sjdm;
	/**
	 * 计划开始时间
	 */
	private String jhkssj;
	/**
	 * 计划结束时间
	 */
	private String jhjssj;
	/**
	 * 计量单位
	 * 10按毛重
	 *20按件数
	 *30 按车次

	 */
	private String jldw;
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


}
