package com.ia.logistics.comm;

/**
 * 常量定义
 *
 */
public class Constant{
	//调度计划状态
	/**
	 * 0 未执行
	 */
	public static final int BILL_STATUS_NOSTART = 0;
	/**
	 * 1 执行中
	 */
	public static final int BILL_STATUS_DOING = 1;
	/**
	 * 2 已结案
	 */
	public static final int BILL_STATUS_END = 2;
	/**
	 * 3 已取消
	 */
	public static final int BILL_STATUS_CANCEL = 3;
	/**
	 * 4 未下载
	 */
	public static final int BILL_STATUS_NODOWLOAD = 4;
	/**
	 * 5 已下载
	 */
	public static final int BILL_STATUS_DOWLOADED = 5;

	//捆包状态
	/**
	 * 0 未装载
	 */
	public static final int PACK_STATUS_NOLOAD = 0;
	/**
	 * 1 已装载
	 */
	public static final int PACK_STATUS_LOADED = 1;

	//业务操作类型
	/**
	 * 1 发车确认
	 */
	public static final int OPER_TYPE_SEND = 1;
	/**
	 * 2 到货确认
	 */
	public static final int OPER_TYPE_ARRIVE = 2;
	/**
	 * 3 签收确认
	 */
	public static final int OPER_TYPE_SING = 3;
	/**
	 * 4 到货退回（即装车状态）
	 */
	public static final int OPER_TYPE_RETURN = 4;
	/**
	 * 5不签收（即红冲）
	 */
	public static final int OPER_TYPE_NOSING = 5;

	//日期过滤维度
	/**
	 * 1 今日
	 */
	public static final int CALENDAR_TYPE_DAY = 1;
	/**
	 * 2 本周
	 */
	public static final int CALENDAR_TYPE_WEEK = 2;
	/**
	 * 3 本月
	 */
	public static final int CALENDAR_TYPE_MONTH = 3;


	///排序维度
	/**
	 * 1 车次排序
	 */
	public static final int ORDER_TYPE_CAR = 1;
	/**
	 * 2 载重排序
	 */
	public static final int ORDER_TYPE_WEIGHT = 2;
	/**
	 * 3 公里数排序
	 */
	public static final int ORDER_TYPE_MILEAGE = 3;
	//设置更新Gps的时间
	public static final int GPS_TIME_MILISECOND=6000;

	public class ADVT_DB {
		/**
		 * 数据库字段类型
		 */
		public static final String FILED_TYPE_TEXT = "text";
		public static final String FILED_TYPE_INTEGER = "integer";
		public static final String FILED_TYPE_REAL = "real";
	}
	/**
	 * 所有的表名
	 */
	public class Table {
		/**
		 * 提单表
		 */
		public static final String TB_BILL= "Bill_info";
		/**
		 * 材料表
		 */
		public static final String TB_PACKAGE = "Package_info";
	}

	/**
	 * activity 的任务号
	 */
	public class ActivityTaskId {
		/**
		 * 任务
		 */
		public static final int TASK_BILL_ACTIVITY= 0;
		/**
		 * 提单详情（装车）
		 */
		public static final int TASK_ENNTRUCKING_ACTIVITY = 1;
		/**
		 * 待发车
		 */
		public static final int TASK_DEPART_ACTIVITY= 2;

		/**
		 * 删除材料号
		 */
		public static final int TASK_DELETE_PACKAGE= 3;

		/**
		 * 删除运输订单
		 */
		public static final int TASK_DELETE_BILL=7;

		/**
		 * 待收货
		 */
		public static final int TASK_ARRIVAL_ACTIVITY = 4;
		/**
		 * 待到货退回
		 */
		public static final int TASK_ARRIVAL_BACK = 5;
		/**
		 * 签收确认
		 */
		public static final int TASK_SIGN_ACITIVIYT = 6;
	}
	/**
	 * 材料所有经历状态
	 * PACKAGE_UNEXCUTE:未装车（装车页面），PACKAGE_UPLOADED:已装车（待发车页面），
	 * PACKAGE_DEPARTED:已发车（待收货页面），PACKAGE_ARRIVALED:已到货（待签收页面 )。
	 */
	public class PackageState {
		/**
		 * 未装车（装车页面）
		 */
		public static final String PACKAGE_UNEXCUTE = "-1";
		/**
		 * 已装车（待发车页面）
		 */
		public static final String PACKAGE_UPLOADED = "0";

		/**
		 * 已发车（待收货页面）
		 */
		public static final String PACKAGE_DEPARTED = "1";

		/**
		 * 已到货（待签收页面）
		 */
//        public static final String PACKAGE_ARRIVALED = "2";
	}

}