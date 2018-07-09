package com.ia.logistics.model.send;

/**
 * 接收汽运车次（详细）
 */
public class TrainListDetSearchModel {
	public static final String Node_Name = "pack";
	/**
	 * 偏移量
	 */
	private String pyl;
	/**
	 * 每页条数
	 */
	private String myts;
	/**
	 * 火车车次任务
	 */
	private String cch;
	/**
	 * 业务类型
	 */
	private String ywlx;

	public String getCch() {
		return cch;
	}

	public void setCch(String cch) {
		this.cch = cch;
	}

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

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}
}
