package com.ia.logistics.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.Constant.PackageState;
import com.ia.logistics.comm.Constant.Table;

@TargetApi(16)
public class ADVT_DBHelper {

	private static SQLiteDatabase advt_Database = null;

	private static ADVT_DBHelper advt_DBHelper = null;

	/**
	 * 取得ADVTDBHelper
	 *
	 * @return
	 */
	public static ADVT_DBHelper getAdvtDBHelper() {
		return advt_DBHelper;
	}

	/**
	 * 取得SQLiteDatabase
	 *
	 * @return
	 */
	public static SQLiteDatabase getAdvtDatabase() {
		return advt_Database;
	}

	/**
	 * 判断数据库是否存在
	 *
	 * @param dbName
	 * @param con
	 * @return
	 */
	public static boolean exist(Context con,String db_name) {
		String[] dataBaseList = con.databaseList();
		if (dataBaseList != null && dataBaseList.length > 0) {
			for (int i = 0; i < dataBaseList.length; i++) {
				if (db_name.equals(dataBaseList[i])) {
					return true;
				}
			}
			for (int i = 0; i < dataBaseList.length; i++) {
				con.deleteDatabase(dataBaseList[i]);
			}
		}
		return false;
	}

	/**
	 * 判断表是否存在
	 *
	 * @param tabName 表名
	 * @return
	 */
	public boolean tabIsExist(String tabName) {
		if (tabName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from table and name ='"
					+ tabName.trim() + "' ";
			cursor = advt_Database.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					return true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			CommSet.e("baosight", e.getMessage());
		}finally{
			if (cursor !=null ) {
				if (!cursor.isClosed()) {
					cursor.close();
				}
				cursor = null;
			}
		}
		return false;
	}

	/**
	 * 判断未装车材料是否存在
	 *
	 * @param package_id 表名
	 * @return
	 */
	public boolean packageIsExist(String package_id) {
		if (package_id == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from "+Table.TB_PACKAGE+" where package_id ='"+package_id.trim()+"' and local_status = '"+PackageState.PACKAGE_UNEXCUTE+"'";
			cursor = advt_Database.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					return true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if (cursor !=null ) {
				if (!cursor.isClosed()) {
					cursor.close();
				}
				cursor = null;
			}
		}
		return false;
	}

	/**
	 * 打开或创建数据库
	 *
	 * @param dbName
	 * @param con
	 * @return
	 */
	public static void openDatabase(Context con,String db_name) {
		if (advt_Database!=null) {
			if (advt_Database.isOpen()) {
				advt_Database.close();
			}
			advt_Database=null;
		}
		if (advt_Database==null) {
			advt_Database = con.openOrCreateDatabase(db_name,
					Context.MODE_PRIVATE, null);
		}
		if (advt_DBHelper ==null) {
			advt_DBHelper = new ADVT_DBHelper();
		}
	}

	/**
	 * 关闭数据库
	 */
	public void closeDB() {
		if (advt_Database != null && advt_Database.isOpen()) {
			advt_Database.close();
			advt_Database = null;
		}
	}

	/**
	 * 创建表
	 *
	 * @param tblName
	 *            表名
	 * @param keyName
	 *            主键字段名
	 * @param fileds
	 *            字段及 类型表Map<String:字段名, String：字段类型>
	 */
	public boolean creadTable(String tblName, String keyName,
							  Map<String, String> fileds) {
		StringBuffer sql = new StringBuffer();
		StringBuffer cretable = new StringBuffer();
		for (Entry<String, String> entry : fileds.entrySet()) {
			cretable.append(entry.getKey());
			cretable.append(" ");
			cretable.append(entry.getValue());
			if (keyName.equalsIgnoreCase(entry.getKey())) {
				cretable.append(" primary key");
			}
			cretable.append(", ");
		}
		String buff = cretable.toString();
		buff = buff.substring(0, buff.length() - 2);
		sql.append("create table ");
		sql.append(tblName);
		sql.append("(");
		sql.append(buff);
		sql.append(")");
		CommSet.d("baosight", sql.toString());
		try {
			advt_Database.execSQL(sql.toString());
		} catch (Exception ex) {
			CommSet.i("baosight", ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 创建带默认自定义主键表
	 *
	 * @param tblName
	 *            表名
	 * @param fileds
	 *            字段及 类型表Map<String:字段名, String：字段类型>
	 */
	public boolean creadTableAutoincrementKey(String tblName,
											  Map<String, String> fileds) {
		StringBuffer sql = new StringBuffer();
		StringBuffer cretable = new StringBuffer();
		cretable.append("recodeId");
		cretable.append(" INTEGER primary key autoincrement,");
		for (Entry<String, String> entry : fileds.entrySet()) {
			cretable.append(entry.getKey());
			cretable.append(" ");
			cretable.append(entry.getValue());
			cretable.append(", ");
		}
		String buff = cretable.toString();
		buff = buff.substring(0, buff.length() - 2);
		sql.append("create table ");
		sql.append(tblName);
		sql.append("(");
		sql.append(buff);
		sql.append(")");
		CommSet.d("baosight", sql.toString());
		try {
			advt_Database.execSQL(sql.toString());
		} catch (Exception ex) {
			CommSet.i(tblName, ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 插入表记录
	 * @param tblName
	 * @param fileds 	字段表Map<String:字段名， String：字段值>
	 * @return
	 */
	public boolean insert(String tblName, Map<String, String> fileds) {
		StringBuffer sql = new StringBuffer();
		StringBuffer filedBuffer = new StringBuffer();
		StringBuffer valueBuffer = new StringBuffer();
		for (Entry<String, String> entry : fileds.entrySet()) {
			filedBuffer.append(entry.getKey());
			filedBuffer.append(", ");
			valueBuffer.append("'");
			valueBuffer.append(entry.getValue());
			valueBuffer.append("', ");
		}
		String filed = filedBuffer.toString();
		filed = filed.substring(0, filed.length() - 2);
		String value = valueBuffer.toString();
		value = value.substring(0, value.length() - 2);
		sql.append("insert into ");
		sql.append(tblName);
		sql.append("(");
		sql.append(filed);
		sql.append(")");
		sql.append(" values " );
		sql.append(" (" );
		sql.append(value);
		sql.append(")");
		CommSet.d("baosight",sql.toString());
		try {
			advt_Database.execSQL(sql.toString());
		} catch (Exception ex) {
			CommSet.i(tblName, ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 删除表记录
	 *
	 * @param tblName
	 *            表名
	 * @param where
	 *            条件
	 */
	public boolean delete(String tblName, String where) {
		StringBuffer sql = new StringBuffer();
		sql.append("delete ");
		sql.append(" from ");
		sql.append(tblName);
		sql.append(" where ");
		sql.append(where);
		try {
			advt_Database.execSQL(sql.toString());
		} catch (Exception ex) {
			CommSet.i(tblName, ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 更新表数据
	 *
	 * @param tblName
	 *            表名
	 * @param fileds
	 *            字段表Map<String:字段名， String：字段值>
	 * @param where
	 *            条件
	 * @return
	 */
	public boolean update(String tblName, Map<String, String> fileds,
						  String where) {

		// 遍历字段表的Key和Value+sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("update ");
		sql.append(tblName);
		sql.append(" set ");
		StringBuffer fieldSf = new StringBuffer();
		for (Entry<String, String> entry : fileds.entrySet()) {
			fieldSf.append(entry.getKey());
			fieldSf.append(" = '");
			fieldSf.append(entry.getValue());
			fieldSf.append("', ");
		}
		String buff = fieldSf.toString();
		buff = buff.substring(0, buff.length() - 2);
		sql.append(buff);
		if (where != null) {
			sql.append(where);
		}
		CommSet.d("baosight",sql.toString());
		try {
			advt_Database.execSQL(sql.toString());
		} catch (Exception ex) {
			CommSet.i(tblName, ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 更新表数据 数值型字段
	 *
	 * @param tblName
	 *            表名
	 * @param fileds
	 *            字段表Map<String:字段名， String：字段值>
	 * @param where
	 *            条件
	 * @return
	 */
	public boolean updateColumnNumber(String tblName, Map<String, String> fileds,
									  String where) {

		// 遍历字段表的Key和Value+sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("update ");
		sql.append(tblName);
		sql.append(" set ");
		StringBuffer fieldSf = new StringBuffer();
		for (Entry<String, String> entry : fileds.entrySet()) {
			fieldSf.append(entry.getKey());
			fieldSf.append(" = ");
			fieldSf.append(entry.getKey()+entry.getValue());
			fieldSf.append(", ");
		}
		String buff = fieldSf.toString();
		buff = buff.substring(0, buff.length() - 2);
		sql.append(buff);
		if (where != null) {
			sql.append(where);
		}
		CommSet.d("baosight",sql.toString());
		try {
			advt_Database.execSQL(sql.toString());
		} catch (Exception ex) {
			CommSet.i(tblName, ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 查询记录
	 *
	 * @param tblName
	 *            表名
	 * @param columns
	 *            要查询的字段
	 * @param where
	 *            条件语句
	 * @param groupBy
	 *            分组语句
	 * @param orderBy
	 *            排序语句
	 * @return ArrayList<Map<String, String>> 查询结果列表<字段名， 字段值>
	 */
	public ArrayList<Map<String, String>> query(String tblName,
												ArrayList<String> columns, String where, String groupBy,
												String orderBy) {
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		StringBuffer column = new StringBuffer();
		if (columns != null && columns.size() > 0) {
			for (int i = 0; i < columns.size(); i++) {
				column.append(columns.get(i));
				if (i < columns.size() - 1) {
					column.append(", ");
				}
			}
			sql.append(column.toString());
		} else {
			sql.append(" * ");
		}
		sql.append(" from ");
		sql.append(tblName);

		if (where != null) {
			sql.append(" ");
			sql.append(where);
		}
		if (groupBy != null) {
			sql.append(" ");
			sql.append(groupBy);
		}
		if (orderBy != null) {
			sql.append(" ");
			sql.append(orderBy);
		}
		CommSet.d("baosight",sql.toString());
		Cursor cursor = null;
		try {
			cursor = advt_Database.rawQuery(sql.toString(), null);
			int columnC = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				HashMap<String, String> buff = new HashMap<String, String>();
				for (int j = 0; j < columnC; j++) {
					String keyName = cursor.getColumnName(j);
					String keyValue = cursor.getString(j);
					buff.put(keyName, keyValue);
				}
				result.add(buff);
			}
		} catch (Exception ex) {
			CommSet.i(tblName, ex.getMessage());
		}finally{
			if (cursor != null) {
				if (!cursor.isClosed()) {
					cursor.close();
				}
				cursor = null;
			}
		}
		return result;
	}

	/**
	 * 清空数据表
	 *
	 * @param tblName
	 *          表名
	 * @return
	 */
	public boolean delteTable(String tblName) {
		String sql = "delete from "+tblName+"";
		try {
			advt_Database.execSQL(sql);
		} catch (Exception e) {
			// TODO: handle exception
			CommSet.i(tblName, e.getMessage());
			return false;
		}
		return true;
	}

	/**筛选材料号
	 * author 
	 * @param where
	 * @return
	 */
	public ArrayList<Map<String, String>> caiLiao(String where) {
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		StringBuffer sql = new StringBuffer();
//		sql.append("select ");
//		StringBuffer column = new StringBuffer();		
		/*
		 * select * from  Package_info t inner join Bill_info b on t.bill_id =b.bill_id where t.package_id =  0;
		 * */
		sql.append("select * from  Package_info t inner join Bill_info b on t.bill_id =b.bill_id where t.package_id =\""+where+"\"");
//		sql.append(where);

		CommSet.d("baosight",sql.toString());
		Cursor cursor = null;
		try {
			cursor = advt_Database.rawQuery(sql.toString(), null);

			int columnC = cursor.getColumnCount();
			while (cursor.moveToNext()) {

				HashMap<String, String> buff = new HashMap<String, String>();
				for (int j = 0; j < columnC; j++) {
					System.out.println("----------------j----"+j);
					String keyName = cursor.getColumnName(j);
					String keyValue = cursor.getString(j);
					buff.put(keyName, keyValue);

				}
				result.add(buff);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			if (cursor != null) {
				if (!cursor.isClosed()) {
					cursor.close();
				}
				cursor = null;
			}
		}
		return result;
	}
}
