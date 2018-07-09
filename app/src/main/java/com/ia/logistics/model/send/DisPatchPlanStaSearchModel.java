package com.ia.logistics.model.send;

/**
 * 司机  发送调度计划的执行动态
 * @author gyh
 */
public class DisPatchPlanStaSearchModel {

	public static final String Node_Name = "state";
	/**
	 * 火车车次任务
	 */
	private String cch;
	/**
	 * 业务操作类型  10 发车确认  , 20 到货确认  , 30 签收确认
	 *00 到货退回（即装车状态）,  01 不签收（即红冲）
	 */
	private String ywczlx;
	/**
	 * 反馈时间
	 */
	private String fksj;
	public String getCch() {
		return cch;
	}
	public void setCch(String cch) {
		this.cch = cch;
	}
	public String getYwczlx() {
		return ywczlx;
	}
	public void setYwczlx(String ywczlx) {
		this.ywczlx = ywczlx;
	}
	public String getFksj() {
		return fksj;
	}
	public void setFksj(String fksj) {
		this.fksj = fksj;
	}
}
