/**
 * 
 */
package com.ia.logistics.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.ia.logistics.comm.Constant.PackageState;
import com.ia.logistics.comm.Constant.Table;
import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.MyApplications;

/**
 * @author BeasonShu
 * @project baosteel_3pl_advt_run
 * @since 2012-4-6����02:25:37
 */
public class SQLTransaction {
	private static SQLTransaction sqlTransaction = null;

	public static SQLTransaction getInstance() {
		if (sqlTransaction == null) {
			sqlTransaction = new SQLTransaction();
		}
		return sqlTransaction;
	}

	/**
	 * ��ѯҳ����ʾ�����б�
	 * 
	 * @param local_status
	 * @param bill_id
	 */
	public ArrayList<Map<String, String>> getPackList(String local_status,
			String bill_id) {
		String where = null;
		if (bill_id != null) {
			where = "where bill_id='" + bill_id + "'  and local_status ='"
					+ local_status + "'";
		} else {
			where = "where local_status ='" + local_status + "'";
		}
		return ADVT_DBHelper.getAdvtDBHelper().query(Table.TB_PACKAGE, null,
				where, null, null);
	}

	/**
	 * ��ѯ���ȼƻ�
	 */

	public ArrayList<Map<String, String>> getBillList(String where,
			String groupBy, String orderBy) {
		return ADVT_DBHelper.getAdvtDBHelper().query(Table.TB_BILL, null,
				where, groupBy, orderBy);
	}

	/**
	 * ��ѯ���Ϻ�
	 * @param where
	 * @param groupBy
	 * @param orderBy
	 * @return
	 */
	public ArrayList<Map<String, String>> getBillList1(String where,
			String groupBy, String orderBy) {
		return ADVT_DBHelper.getAdvtDBHelper().caiLiao(where);
	}
	
	/**
	 * ����ָ������״̬
	 * 
	 * @param package_id
	 * @param old_state
	 * @param flag
	 *            true:����״̬,false:Զ��״̬
	 * @return true : �ɹ� ,false:ʧ��
	 */
	public boolean updatePackState(String package_id, String new_state,
			String old_state, boolean flag) {
		Map<String, String> fileds = new HashMap<String, String>();
		String status = null;
		String where = null;
		if (!flag) {
			status = "remote_status";
			if (package_id != null) {
				where = "where package_id ='" + package_id
						+ "'and remote_status ='" + old_state
						+ "' and local_status ='" + new_state + "'";
			} else {
				where = "where remote_status ='" + old_state
						+ "' and local_status ='" + new_state + "'";
			}
		} else {
			status = "local_status";
			if (package_id != null) {
				where = "where package_id ='" + package_id
						+ "'and local_status ='" + old_state + "'";
			} else {
				where = "where local_status ='" + old_state + "'";
			}
		}
		fileds.put(status, new_state);
		return ADVT_DBHelper.getAdvtDBHelper().update(Table.TB_PACKAGE, fileds,
				where);

	}
	
	/**
	 * ���������
	 * @return
	 */
	public void deleteImaginary() {
		ADVT_DBHelper.getAdvtDatabase().beginTransaction();
		String where = " local_status ='"+PackageState.PACKAGE_DEPARTED+"' and bill_type = '1'";
		ArrayList<Map<String, String>> maps = ADVT_DBHelper.getAdvtDBHelper().query(Table.TB_PACKAGE, null, where, null, null);
		if (maps.size()>0) {
			Map<String, String> fileds = new HashMap<String, String>();
			for (Map<String, String> map : maps) {
				where = " where bill_id = '"+map.get("bill_id")+"'";
				fileds.put("completed_net_weight", " -"+map.get("net_weight"));
				fileds.put("completed_gross_weight", " -"+map.get("gross_weight"));
				fileds.put("completed_act_count", " -"+map.get("package_count"));
				ADVT_DBHelper.getAdvtDBHelper().updateColumnNumber(Table.TB_BILL, fileds, where);
				fileds.clear();
			}
		}
		where = " local_status ='"+PackageState.PACKAGE_DEPARTED+"'";
		boolean result = ADVT_DBHelper.getAdvtDBHelper().delete(Table.TB_PACKAGE, where);
		if (result) {
			ADVT_DBHelper.getAdvtDatabase().setTransactionSuccessful();
			ADVT_DBHelper.getAdvtDatabase().endTransaction();
		}
	}
	
	/**
	 * ɾ��������
	 * @param imaginaryMap
	 * @return
	 */
	public void deletePackageByBillid(Map<String, String> imaginaryMap) {
		String where = " where bill_id = '"+imaginaryMap.get("bill_id")+"'";
		Map<String, String> fileds = new HashMap<String, String>();
		fileds.put("completed_gross_weight", " - "+imaginaryMap.get("gross_weight"));
		fileds.put("completed_net_weight", " - "+imaginaryMap.get("net_weight"));
		fileds.put("completed_act_count", " - "+imaginaryMap.get("package_count"));
		ADVT_DBHelper.getAdvtDBHelper().updateColumnNumber(Table.TB_BILL, fileds, where);
		where = " bill_id = '"+imaginaryMap.get("bill_id")+"'";
		ADVT_DBHelper.getAdvtDBHelper().delete(Table.TB_PACKAGE, where);
	}
	
	/**
	 * �����ᵥ��
	 * @param map
	 */
	public void delPackageByTDH(Map<String, String> map){
		String where ="bill_id =' "+map.get("bill_id")+"'";
		ADVT_DBHelper.getAdvtDBHelper().delete(Table.TB_BILL, where);
	}

	/**
	 * ɨ�������Ϣ������id���ᵥ���б�
	 * 
	 * @param package_id
	 */
	public ArrayList<Map<String, String>> ScanByPackageId(String package_id) {
		String where = "where package_id = '" + package_id
				+ "' and local_status ='" + PackageState.PACKAGE_UNEXCUTE + "'";
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("package_id");
		columns.add("bill_id");
		columns.add("control_id");
		return ADVT_DBHelper.getAdvtDBHelper().query(Table.TB_PACKAGE, columns,
				where, null, null);
	}

	/**
	 * ɾ���������ݿ�
	 */
	public boolean delteLocalDB(Context con) {
		CommSet.d("baosight","ɾ�����ݿ�");
		ADVT_DBHelper advt_DBHelper = ADVT_DBHelper.getAdvtDBHelper();
		if (advt_DBHelper!=null) {
			advt_DBHelper.closeDB();
		}
		return con.deleteDatabase(MyApplications.getInstance().getUseId());
	}
}
