package com.ia.logistics.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.ia.logistics.comm.Constant.PackageState;
import com.ia.logistics.comm.Constant.Table;
import com.ia.logistics.exception.XMLException;
import com.ia.logistics.interfaces.MessageService;
import com.ia.logistics.model.receive.BillDestModel;
import com.ia.logistics.model.receive.DisPatchPlanDetLsitModel;
import com.ia.logistics.model.receive.DisPatchPlanLsitModel;
import com.ia.logistics.model.receive.DownloadHeadAndBodyModel;
import com.ia.logistics.model.receive.LoadListModel;
import com.ia.logistics.model.receive.MarkerModel;
import com.ia.logistics.model.receive.PerStatisticsDetModel;
import com.ia.logistics.model.receive.SignInInfoModel;
import com.ia.logistics.model.receive.SignsModel;
import com.ia.logistics.model.receive.TrainListChildModel;
import com.ia.logistics.model.receive.TrainListModel;
import com.ia.logistics.model.send.BillDestSearchModel;
import com.ia.logistics.model.send.CyclometerSearchMode;
import com.ia.logistics.model.send.FlowSearchModel;
import com.ia.logistics.model.send.LeaveRstSearchModel;
import com.ia.logistics.model.send.LoadListChildSearchModel;
import com.ia.logistics.model.send.LoadListSearchModel;
import com.ia.logistics.model.send.PerStatisticsDetSearchModel;
import com.ia.logistics.model.send.RequestHeadAndBodyModel;
import com.ia.logistics.model.send.RequestListModel;
import com.ia.logistics.model.send.RequestPlanAndDetailModel;
import com.ia.logistics.model.send.RequestSignAndDetailModel;
import com.ia.logistics.model.send.SignInSearchModel;
import com.ia.logistics.sql.ADVT_DBHelper;

public class InterfaceDates {
	private static InterfaceDates inteDates = null;

	public static InterfaceDates getInstance() {
		if (inteDates == null) {
			inteDates = new InterfaceDates();
		}
		return inteDates;
	}

	/**
	 * 选择车头或挂车
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DownloadHeadAndBodyModel> selectFrontOrBody(String flag) {
		ArrayList<DownloadHeadAndBodyModel> frontInfo = null;
		RequestHeadAndBodyModel search = new RequestHeadAndBodyModel();
		search.setFlag(flag);//10车头，20挂车
		search.setMyts("");
		search.setPyl("");
		search.setSjdm(MyApplications.getInstance().getUseId());
		search.setCysdm(MyApplications.getInstance().getProvider_id());
		try {
			frontInfo = (ArrayList<DownloadHeadAndBodyModel>) MessageService
					.receiveFrontInfo(search);
			return frontInfo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 获得接口数据
	public String inserBillInfo() {
		// 初始化compareMap
		Map<String, Map<String, String>> compareMap = new HashMap<String, Map<String, String>>();
		// 本地提单表
		ArrayList<Map<String, String>> localList = ADVT_DBHelper
				.getAdvtDBHelper().query(Table.TB_BILL, null, null, null, null);
		// 网络最新提单表
		List<DisPatchPlanLsitModel> resModelList = null;
		try {
			RequestPlanAndDetailModel search = new RequestPlanAndDetailModel();
			search.setSjdm(MyApplications.getInstance().getUseId());
			search.setFlag("10");
			search.setCzsm("10");
			resModelList = MessageService.receiveDisPatchPlanLsit(search);
			if (resModelList != null && !resModelList.isEmpty()) {
				ADVT_DBHelper.getAdvtDBHelper().delteTable(Table.TB_BILL);
				if (!resModelList.get(0).getFhbz().startsWith("0#")) {
					Map<String, String> subResultMap;
					for (Map<String, String> map1 : localList) {
						compareMap.put(map1.get("bill_id"), map1);
					}
					ADVT_DBHelper.getAdvtDatabase().beginTransaction();
					int i = 0;
					for (DisPatchPlanLsitModel resModel : resModelList) {
						i++;
						Map<String, String> map = new HashMap<String, String>();
						map.put("control_id", resModel.getDdjhid());
						map.put("bill_id", resModel.getTdh());
						map.put("urgency_degree", resModel.getJjcd());
						map.put("upload_time", resModel.getXfsj());
						map.put("excute_time", resModel.getYjzxrq());
						map.put("warehouse_num", resModel.getCkzyjh());
						map.put("landing_spot_name", resModel.getThdmc());
						map.put("landing_spot_num", resModel.getThddm());
						map.put("dest_spot_name", resModel.getMddmc());
						map.put("dest_spot_num", resModel.getMdddm());
						map.put("dispatcher_name", resModel.getDdyxm());
						map.put("bill_type", resModel.getCllx());
						map.put("control_status", "0");
						map.put("package_download_status", resModel.getCllx());
						map.put("tdzt", resModel.getTdzt());
						System.out.println("tdzt(提单状态)-----------"+resModel.getTdzt().trim());
						if ("1".equals(resModel.getCllx())) {
							map.put("completed_act_count", (resModel.getYsjjs()
									.length() == 0) ? "0" : resModel.getYsjjs());
							map.put("sum_act_count", resModel.getYjhjs());
							map.put("completed_gross_weight", (resModel.getYsjmz()
									.length() == 0) ? "0" : resModel.getYsjmz());
							map.put("sum_gross_weight", resModel.getYjhmz());
							map.put("completed_net_weight", (resModel.getYsjjz()
									.length() == 0) ? "0" : resModel.getYsjjz());
							map.put("sum_net_weight", resModel.getYjhjz());
						} else {
							map.put("completed_act_count", (resModel.getSjjs()
									.length() == 0) ? "0" : resModel.getSjjs());
							map.put("sum_act_count", resModel.getJs());
							map.put("completed_gross_weight", (resModel.getSjmz()
									.length() == 0) ? "0" : resModel.getSjmz());
							map.put("sum_gross_weight", resModel.getMz());
							map.put("completed_net_weight", (resModel.getSjjz()
									.length() == 0) ? "0" : resModel.getSjjz());
							map.put("sum_net_weight", resModel.getJz());
						}
						ADVT_DBHelper.getAdvtDBHelper().insert(Table.TB_BILL, map);
						subResultMap = compareMap.get(resModel.getTdh());
						if (subResultMap != null
								&& "1".equals(subResultMap
								.get("package_download_status"))) {
							Map<String, String> fileds = new HashMap<String, String>();
							fileds.put("package_download_status", "1");
							String where = "where bill_id = '" + resModel.getTdh()
									+ "'";
							ADVT_DBHelper.getAdvtDBHelper().update(Table.TB_BILL,
									fileds, where);
						}
						if (i == resModelList.size()) {
							String where = " not exists( select bill_id from Bill_info where Bill_info.bill_id ="
									+ " Package_info.bill_id group by Bill_info.bill_id ) ";
							ADVT_DBHelper.getAdvtDBHelper().delete(
									Table.TB_PACKAGE, where);
							ADVT_DBHelper.getAdvtDatabase()
									.setTransactionSuccessful();
						}
					}
					ADVT_DBHelper.getAdvtDatabase().endTransaction();
					return "2#";
				}
			}
			return resModelList.get(0).getFhbz();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0#"+e.getMessage();
		}
	}

	/**
	 * 刷新材料信息
	 */
	public String insertPackInfo(String bill_Id, Context context) {
		RequestPlanAndDetailModel search = new RequestPlanAndDetailModel();
		search.setSfzz("0");
		search.setTdh(bill_Id);
		search.setFlag("20");
		search.setCzsm("10");
		List<DisPatchPlanDetLsitModel> resModelList = null;
		try {
			resModelList = MessageService.receiveDisPatchPlanDetLsit(search,context);
			if (resModelList != null && !resModelList.isEmpty()) {
				ADVT_DBHelper.getAdvtDatabase().beginTransaction();
				String deletwhere = "bill_id = '" + bill_Id
						+ "' and local_status ='" + PackageState.PACKAGE_UNEXCUTE
						+ "'";
				ADVT_DBHelper.getAdvtDBHelper()
						.delete(Table.TB_PACKAGE, deletwhere);
				if (!resModelList.get(0).getFhbz().startsWith("0#")) {
					ArrayList<String> searchItems = new ArrayList<String>();
					searchItems.add("dest_spot_name");
					searchItems.add("landing_spot_name");
					searchItems.add("dest_spot_num");
					searchItems.add("landing_spot_num");
					searchItems.add("control_id");
					String where = "where bill_id = '" + bill_Id + "'";
					ArrayList<Map<String, String>> temArrayList = ADVT_DBHelper
							.getAdvtDBHelper().query(Table.TB_BILL, searchItems,
									where, null, null);
					int i = 0;
					for (DisPatchPlanDetLsitModel disPatchPlanDetLsitModel : resModelList) {
						i++;
						Map<String, String> fileds = new HashMap<String, String>();
						fileds.put("package_id", disPatchPlanDetLsitModel.getClh());
						fileds.put("order_num", disPatchPlanDetLsitModel.getHth());
						fileds.put("net_weight", disPatchPlanDetLsitModel.getJz());
						fileds.put("gross_weight", disPatchPlanDetLsitModel.getMz());
						fileds.put("product_name",
								disPatchPlanDetLsitModel.getPzmc());
						fileds.put("bill_id", bill_Id);
						fileds.put("control_id",
								temArrayList.get(0).get("control_id"));
						fileds.put("local_status", PackageState.PACKAGE_UNEXCUTE);
						fileds.put("remote_status", PackageState.PACKAGE_UNEXCUTE);
						fileds.put("dest_spot_num",
								temArrayList.get(0).get("dest_spot_num"));
						fileds.put("landing_spot_num",
								temArrayList.get(0).get("landing_spot_num"));
						fileds.put("dest_spot_name",
								temArrayList.get(0).get("dest_spot_name"));
						fileds.put("landing_spot_name",
								temArrayList.get(0).get("landing_spot_name"));
						fileds.put("bill_type", "0");
						fileds.put("package_count", "1");
						ADVT_DBHelper.getAdvtDBHelper().insert(Table.TB_PACKAGE,
								fileds);
						if (i == resModelList.size()) {
							ADVT_DBHelper.getAdvtDatabase()
									.setTransactionSuccessful();
						}
					}
					ADVT_DBHelper.getAdvtDatabase().endTransaction();
					return "2#";
				}
			}
			return resModelList.get(0).getFhbz();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0#"+e.getMessage();
		}
	}


	/**
	 * 装车
	 */
	public Object sendLoadingList(Context context,List<Map<String, String>>searchList,String... arg) {
		SharedPreferences preferences = context.getSharedPreferences("mybill",0);
		Object objects[][] = null;
		LoadListSearchModel main = new LoadListSearchModel();
		main.setCch(preferences.getString("cch", ""));
		objects = new Object[1][searchList.size() + 1];
		main.setCtdm(preferences.getString("lasthead", ""));
		main.setCysdm(MyApplications.getInstance().getProvider_id());
		main.setGcdm(preferences.getString("lastbody", ""));
		main.setCzbz("10");
		main.setFlag("10");
		main.setSjdm(MyApplications.getInstance().getUseId());
		main.setSjxm(MyApplications.getInstance().getUser_name());
		if ("1".equals(searchList.get(0).get("bill_type"))) {
			LoadListChildSearchModel child = new LoadListChildSearchModel();
			child.setMz(arg[0]);
			child.setJz(arg[0]);
			child.setJs(arg[1]);
			child.setCllx("1");
			child.setTdh(searchList.get(0).get("bill_id"));
			child.setDdjhid(searchList.get(0).get("control_id"));
			child.setHth(searchList.get(0).get("order_num"));
			objects[0][1] = child;
		} else {
			for (int i = 0; i < searchList.size(); i++) {
				LoadListChildSearchModel child = new LoadListChildSearchModel();
				child.setDdjhid(searchList.get(i).get("control_id"));
				child.setTdh(searchList.get(i).get("bill_id"));
				child.setMz(searchList.get(i).get("gross_weight"));
				child.setJz(searchList.get(i).get("net_weight"));
				child.setJs(searchList.get(i).get("sum_number"));
				child.setClh(searchList.get(i).get("package_id"));
				child.setCllx("0");
				child.setHth(searchList.get(i).get("order_num"));
				objects[0][i + 1] = child;
			}
		}
		objects[0][0] = main;
		try {
			LoadListModel resModel = MessageService.sendLoadingList(objects, context);// 发送装车清单
			if (resModel != null) {
				preferences.edit().putString("cch", resModel.getCch()).commit();
				if ("1".equals(searchList.get(0).get("bill_type"))) {
					return resModel;
				}else {
					if (!resModel.getFhbz().startsWith("0#")) {
						return "2#";
					}
				}
			}
			return resModel.getFhbz();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0#"+e.getMessage();
		}
	}

	/**
	 * 删除运输订单
	 */
	public String delBillId(Map<String, String> delMap,Context context){
		RequestPlanAndDetailModel planModel=new RequestPlanAndDetailModel();
		List<DisPatchPlanLsitModel> markerModel=null;
		planModel.setSjdm(MyApplications.getInstance().getUseId());
		planModel.setCzsm("20");
		planModel.setFlag("10");
		planModel.setTdh(delMap.get("bill_id"));
		try {
			markerModel= MessageService.delBill(planModel, context);
			if (markerModel != null && !markerModel.get(0).getFhbz().startsWith("0#")) {
				return "2#";
			} else {
				return markerModel.get(0).getFhbz();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "0#"+e.getMessage();
		}
	}

	/**
	 * 装车红冲捆包号（删除材料号）
	 *
	 * @param poistion
	 * @return
	 */
	public String deletePackId(Map<String, String> delteMap, Context context) {
		SharedPreferences preferences = context.getSharedPreferences("mybill",0);
		LoadListModel markerModel = null;
		LoadListSearchModel parentListSearchModel = new LoadListSearchModel();
		LoadListChildSearchModel child = new LoadListChildSearchModel();
		parentListSearchModel.setCysdm(MyApplications.getInstance().getProvider_id());
		parentListSearchModel.setSjdm(MyApplications.getInstance().getUseId());
		parentListSearchModel.setSjxm(MyApplications.getInstance().getUser_name());
		parentListSearchModel.setCtdm(preferences.getString("lasthead", ""));
		parentListSearchModel.setGcdm(preferences.getString("lastbody", ""));
		parentListSearchModel.setCch(preferences.getString("cch", ""));
		parentListSearchModel.setCzbz("00");
		parentListSearchModel.setFlag("10");
		Object objects[][] = new Object[1][2];
		objects[0][0] = parentListSearchModel;
		child.setTdh(delteMap.get("bill_id"));
		child.setJz(delteMap.get("net_weight"));
		child.setMz(delteMap.get("gross_weight"));
		child.setJs(delteMap.get("package_count"));
		child.setDdjhid(delteMap.get("control_id"));
		child.setCllx(delteMap.get("bill_type"));
		child.setClh(delteMap.get("package_id"));
		objects[0][1] = child;
		try {
			markerModel = MessageService.sendLoadingList(objects, context);
			if (markerModel != null&&!markerModel.getFhbz().startsWith("0#")) {
				return "2#";
			} else {
				return markerModel.getFhbz();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0#"+e.getMessage();
		}
	}

	/**
	 * @param context
	 * @param flag 10发车；20（预载清单）装车
	 * @return
	 */
	public String sendLeaveRst(Context context,String flag) {
		LeaveRstSearchModel search = new LeaveRstSearchModel();
		search.setCch(context.getSharedPreferences("mybill",0).getString("cch", ""));
		search.setCzbz(flag);
		search.setFksj("");
		MarkerModel result = null;
		try {
			result = MessageService.sendLeaveRst(search, context);
			if (result != null && !result.getFhbz().startsWith("0#")) {
				return "2#";
			} else {
				return result.getFhbz();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0#"+e.getMessage();
		}
	}

	/**
	 * 车次红冲
	 */
	public String returnTrip(Context context,
							 ArrayList<Map<String, String>> result_packList) {
		MarkerModel markerModel = null;
		Object[][] objects = null;
		SharedPreferences preferences = context.getSharedPreferences("mybill",0);
		LoadListSearchModel goodsSearchModel = new LoadListSearchModel();
		goodsSearchModel.setCch(preferences.getString("cch", ""));
		goodsSearchModel.setCzbz("00");
		goodsSearchModel.setFlag("20");
		goodsSearchModel.setSjdm(MyApplications.getInstance().getUseId());
		ArrayList<LoadListChildSearchModel> virt_packList = new ArrayList<LoadListChildSearchModel>();

		for (int i = 0; i < result_packList.size(); i++) {
			LoadListChildSearchModel goodcChhildSearchModel = new LoadListChildSearchModel();
			String package_type = result_packList.get(i).get("bill_type");
			if (package_type.equals("1")) {
				goodcChhildSearchModel.setClh(result_packList.get(i).get(
						"package_id"));
				goodcChhildSearchModel.setTdh(result_packList.get(i).get(
						"bill_id"));
				goodcChhildSearchModel.setCllx("1");
				goodcChhildSearchModel.setMz(result_packList.get(i).get(
						"gross_weight"));
				goodcChhildSearchModel.setJz(result_packList.get(i).get(
						"net_weight"));
				goodcChhildSearchModel.setJs(result_packList.get(i).get(
						"sum_number"));
				virt_packList.add(goodcChhildSearchModel);
			}
		}
		if (virt_packList != null && virt_packList.size() >= 1) {
			objects = new Object[1][virt_packList.size() + 1];
			objects[0][0] = goodsSearchModel;
			for (int i = 0; i < virt_packList.size(); i++) {
				objects[0][i + 1] = virt_packList.get(i);
			}
		} else {
			objects = new Object[1][2];
			objects[0][0] = goodsSearchModel;
			LoadListChildSearchModel goodcChhildSearchModel = new LoadListChildSearchModel();
			objects[0][1] = goodcChhildSearchModel;
		}

		try {
			markerModel = MessageService.sendGoods(objects, context);
			if (markerModel != null && !markerModel.getFhbz().startsWith("0#")) {
				return "2#";
			} else {
				return markerModel.getFhbz();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0#"+e.getMessage();
		}
	}

	/**
	 * 发送到货反馈
	 *
	 * @param arrival_com_List
	 * @param mddmc
	 * @param mdddm
	 * @param context
	 * @param type
	 *            0,勾选到货，1，虚捆包分步到货
	 * @return
	 * @throws XMLException
	 */
	public String sendSeclectedPack(
			ArrayList<Map<String, String>> arrival_com_List, String mddmc,
			String mdddm, Context context) {
		MarkerModel markerModel = null;
		LoadListSearchModel goodsSearchModel = new LoadListSearchModel();
		goodsSearchModel.setCch(context.getSharedPreferences("mybill",0).getString("cch", ""));
		goodsSearchModel.setCzbz("10");
		goodsSearchModel.setFlag("20");
		goodsSearchModel.setSjdm(MyApplications.getInstance().getUseId());
		goodsSearchModel.setDhdd(mddmc);
		goodsSearchModel.setDhdm(mdddm);
		Object[][] objects = new Object[1][1 + arrival_com_List.size()];
		objects[0][0] = goodsSearchModel;
		for (int i = 0; i < arrival_com_List.size(); i++) {
			LoadListChildSearchModel goodcChhildSearchModel = new LoadListChildSearchModel();
			goodcChhildSearchModel.setClh(arrival_com_List.get(i).get("package_id"));
			goodcChhildSearchModel.setTdh(arrival_com_List.get(i).get("bill_id"));
			goodcChhildSearchModel.setHth(arrival_com_List.get(i).get("order_num"));
			if ("1".equals(arrival_com_List.get(i).get("detachFlag"))) {
				goodcChhildSearchModel.setMz(arrival_com_List.get(i).get("detachWeight"));
				goodcChhildSearchModel.setJz(arrival_com_List.get(i).get("detachWeight"));
				goodcChhildSearchModel.setJs(arrival_com_List.get(i).get("detachCount"));
			} else{
				goodcChhildSearchModel.setMz(arrival_com_List.get(i).get(
						"gross_weight"));
				goodcChhildSearchModel.setJz(arrival_com_List.get(i).get(
						"net_weight"));
				goodcChhildSearchModel.setJs(arrival_com_List.get(i).get(
						"package_count"));
			}
			goodcChhildSearchModel.setCllx(arrival_com_List.get(i).get(
					"bill_type"));
			objects[0][i + 1] = goodcChhildSearchModel;
		}
		try {
			markerModel = MessageService.sendGoods(objects, context);
			if (markerModel != null && !markerModel.getFhbz().startsWith("0#")) {
				for (int i = 0; i < arrival_com_List.size(); i++) {
					if (!("1".equals(arrival_com_List.get(i).get("bill_type"))&&"1".equals(arrival_com_List.get(i).get("detachFlag")))) {
						String where = " package_id ='"+arrival_com_List.get(i).get("package_id")+"' and bill_id ='"+arrival_com_List.get(i).get("bill_id")+"'";
						ADVT_DBHelper.getAdvtDBHelper().delete(Table.TB_PACKAGE, where);
					}else {
						String where = " where bill_id ='"+arrival_com_List.get(i).get("bill_id")+"'";
						Map<String, String> fileds = new HashMap<String, String>();
						fileds.put("net_weight", " -"+arrival_com_List.get(i).get("detachWeight"));
						fileds.put("gross_weight", " -"+arrival_com_List.get(i).get("detachWeight"));
						fileds.put("package_count", " -"+arrival_com_List.get(i).get("detachCount"));
						ADVT_DBHelper.getAdvtDBHelper().updateColumnNumber(Table.TB_PACKAGE, fileds, where);
					}
				}
				return "2#";
			} else {
				return markerModel.getFhbz();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0#"+e.getMessage();
		}
	}

	/**
	 * 接受签收单号
	 *
	 * @param context
	 * @return
	 */
	public List<SignsModel> receiveSignsId(Context context) {
		RequestSignAndDetailModel signsearchmodle = new RequestSignAndDetailModel();
		SharedPreferences preferences = context.getSharedPreferences("mybill",0);
		String head = preferences.getString("lasthead", "");
		String cch = preferences.getString("cch", "");
		signsearchmodle.setCph(head);
		signsearchmodle.setFlag("10");
		signsearchmodle.setSjdm(MyApplications.getInstance().getUseId());
		signsearchmodle.setCch(cch);
		List<SignsModel> signinfo;
		try {
			signinfo = MessageService.receiveSigns(
					signsearchmodle, context);// 接受汽运签收单
			return signinfo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 更改收货地
	 *
	 * @return
	 */
	public List<BillDestModel> changeDest(Context context) {
		BillDestSearchModel search = new BillDestSearchModel();
		search.setCysdm(MyApplications.getInstance().getProvider_id());
		List<BillDestModel> dests;
		try {
			dests = MessageService.receiveAddressList(search,
					context);
			return dests;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取业绩统计详细
	 *
	 * @return
	 */
	public List<PerStatisticsDetModel> geDetaiOfAchieve(String start_time,
                                                        String end_time, String jldw, String state, String pyl,
                                                        String myts, Context context) {
		PerStatisticsDetSearchModel search = new PerStatisticsDetSearchModel();
		search.setJhkssj(start_time);
		search.setJhjssj(end_time);
		search.setJldw("10");
		search.setTdzt(state + "");
		search.setSjdm(MyApplications.getInstance().getUseId());
		search.setPyl(pyl);
		search.setMyts(myts);
		List<PerStatisticsDetModel> resList = null;
		try {
			resList = MessageService.receivePerStatisticsSDet(search, context);
			if (resList != null && !resList.isEmpty()
					&& !resList.get(0).getFhbz().startsWith("0#")) {
				return resList;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 判断是否有旧的车次
	 *
	 * @return true 表示有上次未完成的任务，否则，没有；
	 */
	public boolean HavingOldTrip(Context context) {
		RequestListModel search = new RequestListModel();
		SharedPreferences preferences = context.getSharedPreferences("mybill",0);
		String head = preferences.getString("lasthead", "");
		search.setCtdm(head);
		search.setFlag("10");
		search.setSjdm(MyApplications.getInstance().getUseId());
		search.setCczt("0#1");
		try {
			List<TrainListModel> receive = MessageService.receiveTrainList(search, context);
			if (receive != null && !receive.isEmpty()) {
				if (!receive.get(0).getFhbz().startsWith("0#")) {
					String remote_cch = receive.get(0).getCch();
					if (remote_cch!=null&&!"".equals(remote_cch)) {
						preferences.edit().clear();
						preferences.edit().putString("cch", remote_cch).
								//整个车次做完业务类型必须清空
										putString("business_type", receive.get(0).getYwlx()).commit();
						CommSet.d("旧的车次", remote_cch);
						System.err.println("是否有旧的车次号-------"+remote_cch);
						MyApplications.getInstance().setTrip_state(
								receive.get(0).getGczt());
						return true;
					}
				} else {
					context.getSharedPreferences("localPage",
							Context.MODE_PRIVATE).edit()
							.putString("localPage", "").commit();
				}
			}
			preferences.edit().putString("business_type", "").commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CommSet.e("interfacedate", e.getMessage());
		}
		return false;
	}

	/**
	 * 接收汽运车次详细
	 *
	 */
	public String LoadLastOldTrip(Context context, String packState) {
		RequestListModel search = new RequestListModel();
		search.setCch(context.getSharedPreferences("mybill", 0).getString("cch", ""));
		search.setYwlx(context.getSharedPreferences("mybill", 0).getString("business_type", ""));
		search.setFlag("20");
		try {
			List<TrainListChildModel> receive = MessageService.receiveTrainDetList(search, context);
			if (receive != null && !receive.isEmpty()
					&& !receive.get(0).getFhbz().startsWith("0#")) {
				ADVT_DBHelper.getAdvtDatabase().beginTransaction();
				String where = " local_status='"
						+ PackageState.PACKAGE_UPLOADED + "' or local_status='"
						+ PackageState.PACKAGE_DEPARTED + "'";
				ADVT_DBHelper.getAdvtDBHelper().delete(Table.TB_PACKAGE, where);
				for (int i = 0; i < receive.size(); i++) {
					Map<String, String> fileds = new HashMap<String, String>();
					fileds.put("package_id", receive.get(i).getClh());
					fileds.put("order_num", receive.get(i).getHth());
					fileds.put("net_weight", receive.get(i).getJz());
					fileds.put("gross_weight", receive.get(i).getMz());
					fileds.put("product_name", receive.get(i).getPzmc());
					fileds.put("bill_id", receive.get(i).getTdh());
					fileds.put("local_status", packState);
					fileds.put("remote_status", packState);
					fileds.put("dest_spot_num", receive.get(i).getMdddm());
					fileds.put("landing_spot_num",receive.get(i).getThddm());
					fileds.put("dest_spot_name", receive.get(i).getMddmc());
					fileds.put("landing_spot_name",receive.get(i).getThdmc());
					fileds.put("control_id", receive.get(i).getDdjhid());
					fileds.put("package_count", receive.get(i).getJs());
					fileds.put("bill_type", receive.get(i).getCllx());
					ADVT_DBHelper.getAdvtDBHelper().insert(Table.TB_PACKAGE,fileds);
					if (i == (receive.size() - 1)) {
						ADVT_DBHelper.getAdvtDatabase()
								.setTransactionSuccessful();
					}
				}
				ADVT_DBHelper.getAdvtDatabase().endTransaction();
				return "2#";
			} else {
				context.getSharedPreferences("localPage", Context.MODE_PRIVATE)
						.edit().putString("localPage", "").commit();
				// returnTrip(context);
				return receive.get(0).getFhbz();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0#"+e.getMessage();
		}
	}

	/**
	 * 发送流量统计
	 *
	 * @param gjyhsbm
	 *            国际用户识别码
	 * @param czll
	 *            车载软件使用量
	 * @param czll
	 *            总流量
	 * @param sbh
	 *            设备号
	 * @return
	 */
	public String sendFlowInfo(String gjyhsbm, long czll, String zll, String sbh) {
		FlowSearchModel search = new FlowSearchModel();
		search.setCysdm(MyApplications.getInstance().getProvider_id());
		search.setSjdm(MyApplications.getInstance().getUseId());
		search.setSjxm(MyApplications.getInstance().getUser_name());
		search.setGjyhsbm(gjyhsbm);
		search.setCzll(czll + "");
		search.setZll(zll + "");
		search.setMyts("");
		search.setPyl("");
		search.setSbh(sbh);
		MarkerModel result = null;
		try {
			result = MessageService.sendFlow(search);
			if (result != null && !result.getFhbz().startsWith("0#")) {
				return "2#";
			} else {
				return result.getFhbz();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0#"+e.getMessage();
		}
	}

	/**
	 * 路码表输入
	 *
	 * @param lm
	 * @param flag
	 * @param ctdm
	 * @return
	 */
	public String sendCyclometerInfo(String lm, String flag, String ctdm) {
		CyclometerSearchMode search = new CyclometerSearchMode();
		search.setCysdm(MyApplications.getInstance().getProvider_id());
		search.setFlag(flag);
		search.setLm(lm);
		search.setCtdm(ctdm);
		search.setSjdm(MyApplications.getInstance().getUseId());
		search.setSjxm(MyApplications.getInstance().getUser_name());
		search.setMyts("");
		search.setPyl("");
		MarkerModel result = null;
		try {
			result = MessageService.cyclometerInfo(search);
			if (result != null) {
				if (result.getFhbz() != null) {
					return result.getFhbz();
				} else {
					return "0#";
				}
			} else {
				return "0#";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0#"+e.getMessage();
		}
	}

	/**
	 *
	 * @param arg
	 *            数组 index: 0:context 1:操作标志 10 ：签到 20: 发车，取消签到30: 取得签到点（起运地）
	 *            2：起运地代码 3：起运地名称
	 */
	public String getSignForArrivalInfo(Object... arg) {
		SharedPreferences preferences = ((Context) arg[0])
				.getSharedPreferences("mybill", 0);
		SignInSearchModel searchModel = new SignInSearchModel();
		searchModel.setCtdm(preferences.getString("lasthead", ""));
		searchModel.setCysdm(MyApplications.getInstance().getProvider_id());
		searchModel.setCzbz((String) arg[1]);
		searchModel.setGcdm(preferences.getString("lastbody", ""));
		searchModel.setSjdm(MyApplications.getInstance().getUseId());
		searchModel.setSjxm(MyApplications.getInstance().getUser_name());
		if ("10".equals((String) arg[1])) {
			searchModel.setThddm((String) arg[2]);
			searchModel.setThdmc((String) arg[3]);
		}
		List<SignInInfoModel> resultList;
		try {
			resultList = MessageService
					.receiveSignForArrivalInfo(searchModel);
			if (resultList != null) {
				if (!resultList.isEmpty()
						&& (resultList.get(0).getFhbz().startsWith("1#") | resultList
						.get(0).getFhbz().startsWith("0#10"))) {
					Editor editor = preferences.edit();
					if ("30".equals((String) arg[1])) {
						editor.putString("signForArrival_name", resultList.get(0)
								.getThdmc());
						editor.putString("signForArrival_num", resultList.get(0)
								.getThddm());
					} else if ("10".equals((String) arg[1])) {
						editor.putString("signForArrival_name", (String) arg[3]);
						editor.putString("signForArrival_num", (String) arg[2]);
					} else {
						editor.putString("signForArrival_name", "");
						editor.putString("signForArrival_num", "");
					}
					editor.commit();
					return "1#";
				}
			}
			return resultList.get(0).getFhbz();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0#"+e.getMessage();
		}
	}
}
