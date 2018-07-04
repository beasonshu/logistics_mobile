package com.ia.logistics.model.receive;
/**
 * 	接收业绩统计 Result Model
 * @author gyh
 */
public class PerStatisticsModel {
	public static final String Node_Name = "ach";
	/**
	 * 毛重
	 */
	private String mc;
	/**
	 * 吨位未完成
	 */
	private String dwwwc;
	/**
	 * 吨位已派单
	 */
	private String dwypd;
	/**
	 * 吨位未派单
	 */
	private String dwwpd;
	/**
	 * 吨位执行中
	 */
	private String dwzxz;
	/**
	 * 吨位已完成
	 */
	private String dwywc;

	/**
	 * 吨位正常签收
	 */
	private String dwzcqs;
	/**
	 * 吨位异常签收
	 */
	private String dwycqs;
	/**
	 * 件数未完成
	 */
	private String jswwc;
	/**
	 * 件数已派单
	 */
	private String jsypd;
	/**
	 * 件数未派单
	 */
	private String jswpd;
	/**
	 * 件数执行中
	 */
	private String jszxz;
	/**
	 * 件数已完成
	 */
	private String jsywc;
	/**
	 * 件数正常签收
	 */
	private String jszcqs;
	/**
	 * 件数异常签收
	 */
	private String jsycqs;
	/**
	 * 车次未完成
	 */
	private String ccwwc;
	/**
	 * 车次执行中
	 */
	private String cczxz;
	/**
	 * 车次已完成
	 */
	private String ccywc;
	/**
	 * 司机姓名
	 */
	private String sjxm;
	/**
	 * 货主姓名
	 */
	private String hzxm;
	/**
	 * 日期
	 */
	private String rq;

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public String getDwwwc() {
		return dwwwc;
	}

	public void setDwwwc(String dwwwc) {
		this.dwwwc = dwwwc;
	}

	public String getSjxm() {
		return sjxm;
	}

	public void setSjxm(String sjxm) {
		this.sjxm = sjxm;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getDwzxz() {
		return dwzxz;
	}

	public void setDwzxz(String dwzxz) {
		this.dwzxz = dwzxz;
	}

	public String getDwywc() {
		return dwywc;
	}

	public void setDwywc(String dwywc) {
		this.dwywc = dwywc;
	}

	public String getJswwc() {
		return jswwc;
	}

	public void setJswwc(String jswwc) {
		this.jswwc = jswwc;
	}

	public String getJszxz() {
		return jszxz;
	}

	public void setJszxz(String jszxz) {
		this.jszxz = jszxz;
	}

	public String getJsywc() {
		return jsywc;
	}

	public void setJsywc(String jsywc) {
		this.jsywc = jsywc;
	}

	public String getCcwwc() {
		return ccwwc;
	}

	public void setCcwwc(String ccwwc) {
		this.ccwwc = ccwwc;
	}

	public String getCczxz() {
		return cczxz;
	}

	public void setCczxz(String cczxz) {
		this.cczxz = cczxz;
	}

	public String getCcywc() {
		return ccywc;
	}

	public void setCcywc(String ccywc) {
		this.ccywc = ccywc;
	}
	public String getDwypd() {
		return dwypd;
	}

	public void setDwypd(String dwypd) {
		this.dwypd = dwypd;
	}

	public String getDwwpd() {
		return dwwpd;
	}

	public void setDwwpd(String dwwpd) {
		this.dwwpd = dwwpd;
	}
	public String getDwzcqs() {
		return dwzcqs;
	}

	public void setDwzcqs(String dwzcqs) {
		this.dwzcqs = dwzcqs;
	}

	public String getDwycqs() {
		return dwycqs;
	}

	public void setDwycqs(String dwycqs) {
		this.dwycqs = dwycqs;
	}
	public String getJsypd() {
		return jsypd;
	}

	public void setJsypd(String jsypd) {
		this.jsypd = jsypd;
	}

	public String getJswpd() {
		return jswpd;
	}

	public void setJswpd(String jswpd) {
		this.jswpd = jswpd;
	}
	public String getJszcqs() {
		return jszcqs;
	}

	public void setJszcqs(String jszcqs) {
		this.jszcqs = jszcqs;
	}

	public String getJsycqs() {
		return jsycqs;
	}

	public void setJsycqs(String jsycqs) {
		this.jsycqs = jsycqs;
	}

}
