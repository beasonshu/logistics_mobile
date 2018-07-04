package com.ia.logistics.interfaces;

import java.util.List;

import android.content.Context;

import com.ia.logistics.comm.CommSet;
import com.ia.logistics.model.receive.AchievmentsModel;
import com.ia.logistics.model.receive.BillChildModel;
import com.ia.logistics.model.receive.BillDestModel;
import com.ia.logistics.model.receive.DisPatchPlanDetLsitModel;
import com.ia.logistics.model.receive.DisPatchPlanLsitModel;
import com.ia.logistics.model.receive.DownloadHeadAndBodyModel;
import com.ia.logistics.model.receive.LoadListModel;
import com.ia.logistics.model.receive.MarkerModel;
import com.ia.logistics.model.receive.PerStatisticsDetModel;
import com.ia.logistics.model.receive.SignDetilModel;
import com.ia.logistics.model.receive.SignInInfoModel;
import com.ia.logistics.model.receive.SignsModel;
import com.ia.logistics.model.receive.TrainListChildModel;
import com.ia.logistics.model.receive.TrainListModel;
import com.ia.logistics.model.send.AchievmentsSearchModel;
import com.ia.logistics.model.send.BillDestSearchModel;
import com.ia.logistics.model.send.ChangeAppSearchModel;
import com.ia.logistics.model.send.CyclometerSearchMode;
import com.ia.logistics.model.send.DisPatchPlanStaSearchModel;
import com.ia.logistics.model.send.FlowSearchModel;
import com.ia.logistics.model.send.GPSInfoSearchModel;
import com.ia.logistics.model.send.LeaveRstSearchModel;
import com.ia.logistics.model.send.LoadListChildSearchModel;
import com.ia.logistics.model.send.LoadListSearchModel;
import com.ia.logistics.model.send.PerStatisticsDetSearchModel;
import com.ia.logistics.model.send.RequestHeadAndBodyModel;
import com.ia.logistics.model.send.RequestListModel;
import com.ia.logistics.model.send.RequestPlanAndDetailModel;
import com.ia.logistics.model.send.RequestSignAndDetailModel;
import com.ia.logistics.model.send.SignInSearchModel;

/**
 * 调度终端 向服务器端发送请求得到所需的业务数据并转为相应的接口 model
 *
 */
public class MessageService {

	/**
	 * 登录 接收车头信息
	 *
	 * @throws Exception
	 */
	public static List<?> receiveFrontInfo(RequestHeadAndBodyModel search)
			throws Exception {
		List<?> lists[] = MessageUtils.SetModelData2List(search,
				RequestHeadAndBodyModel.Node_Name);
		DownloadHeadAndBodyModel resultmodel = new DownloadHeadAndBodyModel();
		return MessageUtils.receiveListsModel(lists,"receiveFrontInfo", resultmodel);
	}

	/**
	 * 接收调度计划
	 *
	 * @throws Exception
	 */
	public static List<DisPatchPlanLsitModel> receiveDisPatchPlanLsit(RequestPlanAndDetailModel search)
			throws Exception {
		List<?> lists[] = MessageUtils.SetModelData2List(search,
				RequestPlanAndDetailModel.Node_Name);
		DisPatchPlanLsitModel resultmodel = new DisPatchPlanLsitModel();
		return (List<DisPatchPlanLsitModel>) MessageUtils.receiveListsModel(lists,
				"receiveDisPatchPlanLsitR", resultmodel);
	}

	/**
	 * 接收调度计划详细
	 *
	 * @throws Exception
	 */
	public static List<DisPatchPlanDetLsitModel> receiveDisPatchPlanDetLsit(
			RequestPlanAndDetailModel search, Context context) throws Exception {
		List<?> lists[] = MessageUtils.SetModelData2List(search,
				RequestPlanAndDetailModel.Node_Name);
		DisPatchPlanDetLsitModel resultmodel = new DisPatchPlanDetLsitModel();
		return (List<DisPatchPlanDetLsitModel>) MessageUtils.receiveListsModel(lists,"receiveDisPatchPlanLsitR", resultmodel);
	}

	/**
	 * 汽运业务 发送装车清单
	 *
	 * @throws Exception
	 */
	public static LoadListModel sendLoadingList(Object objects[][],
                                                Context context) throws Exception {
		List<?> lists[] = MessageUtils.SetModels2Lists(objects,
				LoadListSearchModel.Node_Name,
				LoadListChildSearchModel.Node_Name);
		LoadListModel resultmodel = new LoadListModel();
		return (LoadListModel) MessageUtils.getResult(lists,
				"sendLoadingList", resultmodel);
	}

	/***
	 * 汽运业务 发送发车反馈
	 *
	 * @param search
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static MarkerModel sendLeaveRst(LeaveRstSearchModel search,
                                           Context context) throws Exception {
		List<?> lists[] = MessageUtils.SetModelData2List(search,
				LeaveRstSearchModel.Node_Name);
		MarkerModel resultmodel = new MarkerModel();
		return (MarkerModel) MessageUtils.getResult(lists,
				"sendLeaveRst", resultmodel);
	}

	/**
	 * 发送调度计划的执行动态(暂停用)
	 *
	 * @throws Exception
	 */
	public static MarkerModel sendDisPatchPlanStatus(
            DisPatchPlanStaSearchModel search, Context context)
			throws Exception {
		List<?> lists[] = MessageUtils.SetModelData2List(search,
				DisPatchPlanStaSearchModel.Node_Name);
		MarkerModel resultmodel = new MarkerModel();
		return (MarkerModel) MessageUtils.getResult(lists,
				"sendDisPatchPlanStatus", resultmodel);
	}

	/**
	 * 发送申请换车指令(暂停用)
	 *
	 * @throws Exception
	 */
	public static MarkerModel sendChangeApp(ChangeAppSearchModel search,
											Context context) throws Exception {
		List<?> lists[] = MessageUtils.SetModelData2List(search,
				ChangeAppSearchModel.Node_Name);
		MarkerModel resultmodel = new MarkerModel();
		resultmodel = (MarkerModel) MessageUtils.getResult(lists,
				"sendChangeApp", resultmodel);
		return resultmodel;
	}

	/**
	 * 汽运跟踪 接收汽运车次
	 *
	 * @throws Exception
	 */
	public static List<TrainListModel> receiveTrainList(RequestListModel search,
                                                        Context context) throws Exception {
		List<?> lists[] = MessageUtils.SetModelData2List(search,
				RequestListModel.Node_Name);
		TrainListModel resultmodel = new TrainListModel();
		return (List<TrainListModel>) MessageUtils.receiveListsModel(lists,
				"receiveTrainList", resultmodel);
	}

	/**
	 * 汽运跟踪 接收汽运车次详细
	 */
	public static List<TrainListChildModel> receiveTrainDetList(
			RequestListModel search, Context context) {
		List<?> lists[] = MessageUtils.SetModelData2List(
				search, RequestListModel.Node_Name);
		TrainListChildModel resultModel = new TrainListChildModel();
		List<TrainListChildModel> resList = null;
		try {
			resList = (List<TrainListChildModel>) MessageUtils.receiveListsModel(lists,
					"receiveTrainList", resultModel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return resList;
	}

	/**
	 * GPS 发送GPS的经纬度
	 *
	 * @throws Exception
	 */
	public static String sendGPSInfo(GPSInfoSearchModel search,
									 Context context) throws Exception {
		List<?> lists[] = MessageUtils.SetModelData2List(search,
				GPSInfoSearchModel.Node_Name);
		MarkerModel resultmodel = new MarkerModel();
		String retStr = (String)MessageUtils.getResult(lists,
				"sendGPSInfo", resultmodel);
		return retStr;
	}

	/**
	 * 发送到货反馈(车次红冲)
	 */
	public static MarkerModel sendGoods(Object objects[][], Context context)
			throws Exception {
		List<?> lists[] = MessageUtils.SetModels2Lists(objects,
				LoadListSearchModel.Node_Name, LoadListChildSearchModel.Node_Name);
		MarkerModel resultmodel = new MarkerModel();
		resultmodel = (MarkerModel) MessageUtils.getResult(lists, "sendLoadingList",
				resultmodel);
		return resultmodel;
	}

/*	public static List<DisPatchPlanLsitModel> receiveDisPatchPlanLsit(RequestPlanAndDetailModel search)
			throws Exception {
		List<?> lists[] = MessageUtils.SetModelData2List(search,
				RequestPlanAndDetailModel.Node_Name);
		DisPatchPlanLsitModel resultmodel = new DisPatchPlanLsitModel();
		return (List<DisPatchPlanLsitModel>) MessageUtils.receiveListsModel(lists,
				"receiveDisPatchPlanLsitR", resultmodel);
	}*/

	/**
	 * 删除提单号
	 */
	public static List<DisPatchPlanLsitModel> delBill(RequestPlanAndDetailModel detailmodle,Context context)
			throws Exception{
		List<?> lists[]=MessageUtils.SetModelData2List(detailmodle, RequestPlanAndDetailModel.Node_Name);
		//MarkerModel resultmodel=new MarkerModel();
		DisPatchPlanLsitModel resultmodel = new DisPatchPlanLsitModel();
		//resultmodel=(DisPatchPlanLsitModel)MessageUtils.receiveListsModel(lists, "receiveDisPatchPlanLsitR", resultmodel);
		return (List<DisPatchPlanLsitModel>)MessageUtils.receiveListsModel(lists, "receiveDisPatchPlanLsitR", resultmodel);
	}

	/**
	 * 接受签单
	 * @throws Exception
	 */
	public static List<SignsModel> receiveSigns(RequestSignAndDetailModel signsearchmodle,
                                                Context context) throws Exception {
		List<?> lists[] = MessageUtils.SetModelData2List(signsearchmodle,
				RequestSignAndDetailModel.Node_Name);
		SignsModel resModel = new SignsModel();
		List<SignsModel> resList = (List<SignsModel>) MessageUtils.receiveListsModel(lists,
				"receiveSigns", resModel);
		return resList;
	}

	/**
	 * 接受签单详细
	 */
	public static List<SignDetilModel> receiveSignsDet(
			RequestSignAndDetailModel search, Context context) {
		List<?> lists[] = MessageUtils.SetModelData2List(search,
				RequestSignAndDetailModel.Node_Name);
		SignDetilModel resModel = new SignDetilModel();
		List<SignDetilModel> resList = null;
		try {
			resList = (List<SignDetilModel>) MessageUtils.receiveListsModel(lists, "receiveSigns",
					resModel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resList;
	}

	/**
	 *
	 * 接收(筛选)提单变更目的地列表
	 * @throws Exception
	 */
	public static List<BillDestModel> receiveAddressList(
            BillDestSearchModel search, Context context) throws Exception {
		List<?> lists[] = MessageUtils.SetModelData2List(search,
				BillDestSearchModel.Node_Name);
		BillDestModel result = new BillDestModel();
		List<BillDestModel> resultList = (List<BillDestModel>) MessageUtils.receiveListsModel(lists,
				"receiveAddressListSJ", result);
		return resultList;
	}

	/**
	 * 车载终端 接受业绩统计
	 *
	 * @param search
	 * @param context
	 * @return
	 */
	public static List<AchievmentsModel> receiveAchievements(
            AchievmentsSearchModel search, Context context) {
		List<?> lists[] = MessageUtils.SetModelData2List(
				search, AchievmentsSearchModel.Node_Name);
		AchievmentsModel result = new AchievmentsModel();
		List<AchievmentsModel> resultList = null;
		try {
			resultList = (List<AchievmentsModel>) MessageUtils.receiveListsModel(lists,
					"receivePerStatisticsR", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 接受业绩统计详细
	 *
	 * @param search
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static List<PerStatisticsDetModel> receivePerStatisticsSDet(
            PerStatisticsDetSearchModel search, Context context) throws Exception {
		List<?> lists[] = MessageUtils
				.SetModelData2List(search, AchievmentsSearchModel.Node_Name);
		PerStatisticsDetModel result = new PerStatisticsDetModel();
		List<PerStatisticsDetModel> resultList = (List<PerStatisticsDetModel>) MessageUtils.receiveListsModel(lists,
				"receivePerStatisticsSDetR", result);
		return resultList;
	}

	/**
	 * 接受业绩统计材料明细
	 */
	public static List<BillChildModel> rceivePerPacksInfo(
			RequestPlanAndDetailModel search, Context context) {
		List<?> lists[] = MessageUtils
				.SetModelData2List(search, RequestPlanAndDetailModel.Node_Name);
		BillChildModel result = new BillChildModel();
		List<BillChildModel> resultList = null;
		try {
			resultList = (List<BillChildModel>) MessageUtils.receiveListsModel(lists,
					"rceivePerPacksInfo", result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 发送签收图片（签收反馈）
	 *
	 * @return
	 */
	public static MarkerModel signPicture(RequestSignAndDetailModel search,
										  Context context) {
		List<?> lists[] = MessageUtils
				.SetModelData2List(search, RequestSignAndDetailModel.Node_Name);
		MarkerModel result = new MarkerModel();
		try {
			result = (MarkerModel) MessageUtils.getResult(lists, "receiveSigns",
					result);
			CommSet.d("baosight","MessageSerive...." + result.getFhbz());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 流量统计
	 */
	public static MarkerModel sendFlow(FlowSearchModel search) {
		List<?> lists[] = MessageUtils.SetModelData2List(search,
				FlowSearchModel.Node_Name);
		MarkerModel result = new MarkerModel();
		try {
			result = (MarkerModel) MessageUtils.getResult(lists,
					"receiveTplTruckStatistics", result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 路码表统计
	 */
	public static MarkerModel cyclometerInfo(CyclometerSearchMode search) {
		List<?> lists[] = MessageUtils.SetModelData2List(
				search, CyclometerSearchMode.Node_Name);
		MarkerModel result = new MarkerModel();
		try {
			result = (MarkerModel) MessageUtils.getResult(lists,
					"receiceTplTruckRoadCode", result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送签到
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public static List<SignInInfoModel> receiveSignForArrivalInfo(
			SignInSearchModel search) throws Exception {
		List<?> lists[] = MessageUtils.SetModelData2List(
				search, SignInSearchModel.Node_Name);
		SignInInfoModel result = new SignInInfoModel();
		List<SignInInfoModel> resultList = (List<SignInInfoModel>) MessageUtils.receiveListsModel(lists,
				"sendSignInInfo", result);
		return resultList;
	}
}
