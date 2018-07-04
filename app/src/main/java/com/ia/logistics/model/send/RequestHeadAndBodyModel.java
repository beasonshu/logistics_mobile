package com.ia.logistics.model.send;
/**
 * 接收车头或挂车
 *
 */
public class RequestHeadAndBodyModel {

	public static final String Node_Name = "hb";

	private String sjdm;//司机U代码

	private String flag;//是否下载车头或挂车	“10”表示车头，“20”表示挂车

	private String cysdm;//承运商代码

	private String pyl;//分页偏移量

	private String myts;//每页条数

	public String getSjdm() {
		return sjdm;
	}

	public void setSjdm(String sjdm) {
		this.sjdm = sjdm;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getCysdm() {
		return cysdm;
	}

	public void setCysdm(String cysdm) {
		this.cysdm = cysdm;
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



}

/*
DRI_U_CODE	司机U代码
FLAG	是否下载车头或挂车	“10”表示车头，“20”表示挂车
PROVIDER_ID	承运商代码
Off_set	分页偏移量
Page_size	每页条数

*/
