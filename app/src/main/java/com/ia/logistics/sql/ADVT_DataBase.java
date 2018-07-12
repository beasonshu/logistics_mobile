package com.ia.logistics.sql;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.Constant.ADVT_DB;
import com.ia.logistics.comm.Constant.Table;

public class ADVT_DataBase{
	/**
	 * 检查本地数据库是否创建
	 * @param con
	 */
	public static void checkADVTDB(Context con,String db_name){
		CommSet.d("baosight", "检查本地数据库是否创建");
		if(!ADVT_DBHelper.exist(con,db_name)){
			CommSet.d("baosight", "初始化本地数据库");
			//应用首次运行本地数据库没有的场合，创建本地数据库
			ADVT_DBHelper.openDatabase(con,db_name);
			//初始化本地数据库各个表
			initADVTDB(con);
		} 
		else {
			//打开数据库
			ADVT_DBHelper.openDatabase(con,db_name);
		}
	}
	
	 /**
	  * 初始化
	  * 创建本地数据表
	  * @param con
	  */
	 public static void initADVTDB(Context con){
		//创建调度计划表
		 creatBillInfo();
		//创建材料表
		 creatPackageInfo();
		 //创建虚捆包表
//		 createVirtPackInfo();
		//创建车次表
//		 creatCarTripInfo();
	}
	
	 /**
	  * 应用退出时清理已和服务器同步完成的数据
	  */
	public static void clearDB(){
		//清理任务表
	}
	
	/**
	 * 创建调度计划表
	 */
	private static void creatBillInfo(){
		Map<String, String> fileds = new HashMap<String, String>();
		//任务主表字段
		String[] BILL_FILEDS = {
				"control_id",		        //调度计划号			
				"bill_id",				    //运输订单
//				"net_weight",			    //净重			
//				"gross_weight",			    //毛重			
//				"act_count",			    //件数		
				"warehouse_num",			//出库作业计划号		
				"dest_spot_num",		    //收货地代码
				"dest_spot_name",	        //收货地名称
				"landing_spot_num",		    //起运地代码
				"landing_spot_name",	    //起运地名称
//				"excute_time",		        //预计执行时间			
				"control_status",		    //调度计划状态			
				"urgency_degree",	        //提单紧急程度			
				"dispatcher_name",		    //调度员姓名			
				"package_download_status",	//提单下载状态
				"bill_type",                 //提单类型，0:实捆包提单，1：虚捆包提单	
				"tdzt"                      //提单状态  
				};
		for(int i = 0; i < BILL_FILEDS.length; i++){
			fileds.put(BILL_FILEDS[i], ADVT_DB.FILED_TYPE_TEXT);
		}
		fileds.put("upload_time", ADVT_DB.FILED_TYPE_INTEGER);//调度下发时间		
		fileds.put("excute_time", ADVT_DB.FILED_TYPE_INTEGER);//预计执行时间	
		fileds.put("sum_net_weight", ADVT_DB.FILED_TYPE_REAL);//总净重		
		fileds.put("sum_gross_weight", ADVT_DB.FILED_TYPE_REAL);//总毛重	
		fileds.put("completed_net_weight", ADVT_DB.FILED_TYPE_REAL);//已完成净重		
		fileds.put("completed_gross_weight", ADVT_DB.FILED_TYPE_REAL);//已完成毛重
		fileds.put("sum_act_count", ADVT_DB.FILED_TYPE_INTEGER);//总件数	
		fileds.put("completed_act_count", ADVT_DB.FILED_TYPE_INTEGER);//已完成件数
		ADVT_DBHelper.getAdvtDBHelper().creadTableAutoincrementKey(Table.TB_BILL, fileds);
	}
	
	/**
	 * 创建材料信息表
	 */
	private static void creatPackageInfo(){
		Map<String, String> fileds = new HashMap<String, String>();
		//任务主表字段
		String[] PACKAGE_FILEDS = {
				"control_id",		        //调度计划号	
				"package_id",		        //材料号			
				"order_num",				//合同号			
				"bill_id",				    //运输订单
				"product_name",			    //品种名称	
				"landing_spot_num",		    //起运地代码
				"landing_spot_name",	    //起运地名称
				"dest_spot_num",		    //收货地代码
				"dest_spot_name",	        //收货地名称
				"local_status",		       	//本地材料状态			
				"remote_status",	     	//服务端材料状态	
				"bill_type"	     		//材料类型
				};
		for(int i = 0; i < PACKAGE_FILEDS.length; i++){
			fileds.put(PACKAGE_FILEDS[i], ADVT_DB.FILED_TYPE_TEXT);
		}
		fileds.put("net_weight", ADVT_DB.FILED_TYPE_REAL);//净重	
		fileds.put("gross_weight", ADVT_DB.FILED_TYPE_REAL);//毛重	
		fileds.put("package_count", ADVT_DB.FILED_TYPE_INTEGER);//材料件数	
		ADVT_DBHelper.getAdvtDBHelper().creadTableAutoincrementKey(Table.TB_PACKAGE, fileds);
	}
	
	/**
	 * 创建车次信息表
	 */
	/*private static void creatCarTripInfo(){
		Map<String, String> fileds = new HashMap<String, String>();
		//任务主表字段
		String[] PACKAGE_FILEDS = {
				"car_trip_num",	     		//com.baosight.iplat4mandroid车次任务
				"user_id",					//司机代码			
				"user_name",				//司机姓名			
				"provider_id",			    //承运商代码	
				"provider_name",			//承运商名称	
				"entrucking_time",	        //装车时间			
				"depart_time",		    	//发车时间		
				"arrival_time",            	//到货时间		
				"front_code",			    //车头号			
				"trailer_code"		    	//挂车号				
				};
		for(int i = 0; i < PACKAGE_FILEDS.length; i++){
			fileds.put(PACKAGE_FILEDS[i], ADVT_DB.FILED_TYPE_TEXT);
		}
		ADVT_DBHelper.getAdvtDBHelper().creadTableAutoincrementKey(Table.TB_BILL, fileds);
	}*/
	
}
