package com.ia.logistics.model.send;



import java.io.Serializable;

/**
 *根据签收单Id接受签收单详细 search model
 *
 * @author gyh
 */
public class SignsDetSearchModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final String Node_Name = "signDet";

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

	public String getQsdid() {
		return qsdid;
	}

	public void setQsdid(String qsdid) {
		this.qsdid = qsdid;
	}

	private String pyl;// 分页偏移量

	private String myts;// 每页条数

	private String qsdid;// 签收单Id

}
