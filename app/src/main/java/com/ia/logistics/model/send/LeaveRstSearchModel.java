package com.ia.logistics.model.send;


public class LeaveRstSearchModel {
	/***
	 * 发送发车反馈
	 */
	public static final String Node_Name = "lev";
	/**
	 * 车次号
	 */
	private String cch;


	/**
	 *  业务操作标志 00 删除车次(装车清单)  10 发车确认 20（预载清单）装车确认

	 */
	private String czbz;
	/**
	 * 反馈时间
	 */
	private String fksj;



	public String getCzbz() {
		return czbz;
	}

	public void setCzbz(String czbz) {
		this.czbz = czbz;
	}



	public String getFksj() {
		return fksj;
	}

	public void setFksj(String fksj) {
		this.fksj = fksj;
	}

	public String getCch() {
		return cch;
	}

	public void setCch(String cch) {
		this.cch = cch;
	}

}
