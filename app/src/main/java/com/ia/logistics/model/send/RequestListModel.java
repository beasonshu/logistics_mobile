package com.ia.logistics.model.send;

/**
 * 车次与详细请求
 */
public class RequestListModel {

	public static final String Node_Name = "tra";
	/**
	 * 司机U代码
	 */
	private String sjdm;

	/**
	 * 火车车次任务
	 */
	private String cch;

	/**
	 * 车头代码(或车牌号)
	 */
	private String ctdm;

	/**
	 * 0 未执行, 1 运输中, 2 运输完毕
	 */
	private String cczt;

	/**
	 * 10车次，20接车车次详细
	 */
	private String flag;
	/**
	 * 分页偏移量 所要获取数据的页码
	 */
	private String pyl;
	/**
	 * 每页条数
	 */
	private String myts;
	/**
	 * 业务类型,默认为空:表示有调度指令装车生成的车次 如果是10,表示没有调度(如钢联预载清单模式)
	 *
	 */
	private String ywlx;

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getSjdm() {
		return sjdm;
	}

	public void setSjdm(String sjdm) {
		this.sjdm = sjdm;
	}

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

	public String getCtdm() {
		return ctdm;
	}

	public void setCtdm(String ctdm) {
		this.ctdm = ctdm;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getCczt() {
		return cczt;
	}

	public void setCczt(String cczt) {
		this.cczt = cczt;
	}

}
/*
 * DRI_U_CODE 司机U代码 FRONT_CODE 车头代码 STATUS 车次状态 "“0” 未执行；“1” 运输中；“2” 运输完毕；
 * 格式：“0#1#2”,表示0或1或2 " Off_set 分页偏移量 Page_size 每页条数
 */