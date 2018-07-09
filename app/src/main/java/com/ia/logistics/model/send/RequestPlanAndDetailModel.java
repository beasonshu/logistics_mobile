package com.ia.logistics.model.send;
/**
 * 接收调度计划和详细
 *
 */
public class RequestPlanAndDetailModel {

	public static final String Node_Name = "pd";

	private String sjdm;// 司机U代码

	private String pyl;// 分页偏移量

	private String myts;// 每页条数

	private String tdh;// 运输订单

	private String flag;// 下载调度计划或捆包 “10” 调度计划；“20” 材料明细，Bill_id必要条件

	private String tdzt;// 通用状态 “00” 未执行；“10” 执行中；“20”执行完毕

	private String jhkssj;// 计划开始时间

	private String jhjssj;// 计划结束时间

	private String px;//排序

	private String thdmc;//起运地名称

	private String ckzyjh;//出库作业计划号

	private String sfzz;//是否装载

	private String czsm; //操作说明  10代表查询，20代表删除

	public String getCzsm() {
		return czsm;
	}

	public void setCzsm(String czsm) {
		this.czsm = czsm;
	}

	public String getSjdm() {
		return sjdm;
	}

	public void setSjdm(String sjdm) {
		this.sjdm = sjdm;
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

	public String getTdh() {
		return tdh;
	}

	public void setTdh(String tdh) {
		this.tdh = tdh;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

	public String getPx() {
		return px;
	}

	public void setPx(String px) {
		this.px = px;
	}

	public String getThdmc() {
		return thdmc;
	}

	public void setThdmc(String thdmc) {
		this.thdmc = thdmc;
	}

	public String getCkzyjh() {
		return ckzyjh;
	}

	public void setCkzyjh(String ckzyjh) {
		this.ckzyjh = ckzyjh;
	}

	public String getSfzz() {
		return sfzz;
	}

	public void setSfzz(String sfzz) {
		this.sfzz = sfzz;
	}

	public String getTdzt() {
		return tdzt;
	}

	public void setTdzt(String tdzt) {
		this.tdzt = tdzt;
	}

}

/*
 * DRI_U_CODE 司机U代码 Off_set 分页偏移量 Page_size 每页条数 Bill_id 运输订单 Flag 下载调度计划或捆包 “10”
 * 调度计划；“20” 捆包，Bill_id必要条件 Status 状态 “00” 未执行；“10” 执行中；“20”执行完毕
 */