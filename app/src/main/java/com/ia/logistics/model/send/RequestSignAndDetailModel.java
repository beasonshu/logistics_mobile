package com.ia.logistics.model.send;

public class RequestSignAndDetailModel {

	public static final String Node_Name = "qs";

	private String cch;//TRUCK_NO	com.baosight.iplat4mandroid车次任务
	private String sjdm;//DRI_U_CODE	司机U代码
	private String cph;//FRONT_CODE	车牌号
	private String qszt;//SIGN_Status	签收状态	"“00”拒绝签收；“10”未签收；“20”异常签收；“30”正常签收"
	private String qsh;//SIGN_ID	com.baosight.iplat4mandroid车次号
	private String cysdm;//PROVIDER_ID	承运商代码
	private String tpsj;//SIGN_PIC	图片数据
	private String qsbz;//Memo	签收备注信息
	private String flag;//Flag	操作标志	“10”接收签收单；“20”接收签收单详细；“30”发送签收单状态及图片
	public String getCch() {
		return cch;
	}
	public void setCch(String cch) {
		this.cch = cch;
	}
	public String getSjdm() {
		return sjdm;
	}
	public void setSjdm(String sjdm) {
		this.sjdm = sjdm;
	}
	public String getCph() {
		return cph;
	}
	public void setCph(String cph) {
		this.cph = cph;
	}
	public String getQsh() {
		return qsh;
	}
	public void setQsh(String qsh) {
		this.qsh = qsh;
	}
	public String getCysdm() {
		return cysdm;
	}
	public void setCysdm(String cysdm) {
		this.cysdm = cysdm;
	}
	public String getTpsj() {
		return tpsj;
	}
	public void setTpsj(String tpsj) {
		this.tpsj = tpsj;
	}
	public String getQsbz() {
		return qsbz;
	}
	public void setQsbz(String qsbz) {
		this.qsbz = qsbz;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getQszt() {
		return qszt;
	}
	public void setQszt(String qszt) {
		this.qszt = qszt;
	}




}
/*
TRUCK_NO	com.baosight.iplat4mandroid车次任务
DRI_U_CODE	司机U代码
FRONT_CODE	车牌号
SIGN_Status	签收状态	"“00”拒绝签收；“10”未签收；“20”异常签收；
“30”正常签收
"
SIGN_ID	签收单ID
PROVIDER_ID	承运商代码
SIGN_PIC	图片数据
SIGN_ZT	签收状态	"“00”拒绝签收；“10”未签收；“20”异常签收 ；
“30”正常签收
"
Memo	签收备注信息
Flag	操作标志	“10”接收签收单；“20”接收签收单详细；“30”发送签收单状态及图片

*/