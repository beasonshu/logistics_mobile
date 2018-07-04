package com.ia.logistics.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ia.logistics.comm.CommSet;
import com.ia.logistics.comm.MyApplications;
import com.ia.logistics.comm.ParseXml;
import com.ia.logistics.model.send.GPSInfoSearchModel;
import com.ia.logistics.service.TelnetDemoClient;

public class MessageUtils {

	public static List<Object> listss;

	/**
	 * @title:底层发送接口
	 * @author:gyh
	 * @throws Exception
	 * @since:2011-11-18
	 * @notes:Model转化为Xml字符串并发送，获得返回的Xml字符串并转化为相应的Model
	 */
	public static Object getResult(List<?> lists[], String id, Object object)
			throws Exception {
		if(id.equals("sendGPSInfo")){
			GPSInfoSearchModel searchModel = null;
			for( int i=0;i<lists.length;i++){
				List list=(List)lists[i];
				Map map=(Map)list.get(0);
				searchModel=(GPSInfoSearchModel)map.get("gps");
			}
			StringBuffer sb=new StringBuffer();
			if(searchModel!=null){
				sb.append(""+searchModel.getCph()+"@");
				if(searchModel.getCch()== null){
					sb.append("@");
				}else{
					sb.append(""+searchModel.getCch()+"@");
				}
				sb.append(""+searchModel.getCysdm()+"@");
				sb.append(""+searchModel.getJd()+"@");
				sb.append(""+searchModel.getPadid()+"@");
				sb.append(""+searchModel.getSjdm()+"@");
				sb.append(""+searchModel.getWd()+"@");
				sb.append(""+searchModel.getSjxm()+"@");
				sb.append(""+searchModel.getZzzt()+"");
				System.out.println("-------sb.toString"+sb.toString());
			}
			return send(sb.toString(), id);
		}
		String sendXml = ParseXml.createXMLFile("CS0001", lists, "C", "S");
		CommSet.d("baosight", "sendxml=" + sendXml);
		String result = send(sendXml, id);
		if (result != null && "".equals(result.trim())) {
			return null;
		} else {
			return ParseXml.parseDocumentSBObj(result, object);
		}
	}

	/**
	 * @title:底层发送接口 //visitdetailactivity
	 * @author:gyh
	 * @throws JSONException
	 * @since:2011-11-18
	 * @notes:向服务端发起请求，并获得反馈值
	 */
	public static String send(String sendxml,String id) throws JSONException {
		CommSet.d("baosight", "id----->" + id);
		JSONObject resJson = null;
		String sendxml2 = sendxml.replaceAll("\"", "'");
		System.out.println("----------id--------" + id);
		if (id.equals("sendGPSInfo")) {
			try {
				System.out.println("--------sendXml进来了没-------" + sendxml2);
				return TelnetDemoClient.sendGpsInfo(sendxml2);
			} catch (Exception e) {
				//e.printStackTrace();
				System.out.println("-----sendGps----");
				return sendGps(sendxml2, id);
			}
		}
		StringBuffer sb = new StringBuffer("{\"msgKey\":\"\",");
		sb.append("\"attr\":{\"parameter_compressdata\":\"false\",");// 是否压缩
		sb.append("\"projectName\":\"3PL\",");// 项目名称
		sb.append("\"serviceName\":\"" + id + "\",");// 业务接口
		sb.append("\"datatype\":\"json/json\",");// 业务接口
		// sb.append("\"serviceName\":\"interfaceId\",");//业务系统 service名称
		sb.append("\"parameter_encryptdata\":\"false\"},");// 是否加密
		sb.append("\"data\":\"" + sendxml2 + "\",");// 业务数据
		CommSet.d("baosight", "处理后的xml" + sendxml2);
		sb.append("\"status\":0,");
		sb.append("\"msg\":\"\",");
		sb.append("\"detailMsg\":\"\"}");
		CommSet.d("baosight", "处理后的-------->" + sb);
		JSONObject jsonObject = new JSONObject(sb.toString());
		long fist = CommSet.getMobileBytes();
		resJson = (JSONObject) MyApplications.getInstance().getAgent().callSync(jsonObject);
		Log.d("flow", "当前流量是----->" + (CommSet.getMobileBytes() - fist));
		Log.d("MessageUtil","MessageService"+ MyApplications.getInstance().getAgent().httpAgent.getEncryptKey());
		Log.d("MessageUtil","MessageService"+ MyApplications.getInstance().getAgent().httpAgent.getEncryptVector());
		Log.d("MessageUtil","MessageService"+ MyApplications.getInstance().getAgent().httpAgent.getUserTokenID());
		CommSet.d("baosight", "ssss-------------->"+ resJson.get("data").toString());
		return resJson.get("data").toString();
	}

	/**
	 * @title:底层发送接口
	 * @author:gyh
	 * @throws JSONException
	 * @since:2011-11-18
	 * @notes:向服务端发起请求，并获得反馈值
	 */
	public static String sendGps(String sendxml, String id)
			throws JSONException {
		CommSet.d("baosight", "id----->" + id);
		JSONObject resJson = null;
		String sendxml2 = sendxml.replaceAll("\"", "'");
		System.out.println("----------id--------" + id);
		StringBuffer sb = new StringBuffer("{\"msgKey\":\"\",");
		sb.append("\"attr\":{\"parameter_compressdata\":\"false\",");// 是否压缩
		sb.append("\"projectName\":\"3PL\",");// 项目名称
		sb.append("\"serviceName\":\"" + id + "\",");// 业务接口
		sb.append("\"datatype\":\"json/json\",");// 业务接口
		// sb.append("\"serviceName\":\"interfaceId\",");//业务系统 service名称
		sb.append("\"parameter_encryptdata\":\"false\"},");// 是否加密
		sb.append("\"data\":\"" + sendxml2 + "\",");// 业务数据
		CommSet.d("baosight", "处理后的xml" + sendxml2);
		sb.append("\"status\":0,");
		sb.append("\"msg\":\"\",");
		sb.append("\"detailMsg\":\"\"}");
		CommSet.d("baosight", "处理后的-------->" + sb);
		JSONObject jsonObject = new JSONObject(sb.toString());
		long fist = CommSet.getMobileBytes();
		resJson = (JSONObject) MyApplications.getInstance().getAgent().callSync(jsonObject);
		System.out.println("resjson---------" + resJson);
		Log.d("flow", "当前流量是----->" + (CommSet.getMobileBytes() - fist));
		Log.d("MessageUtil","MessageService"+ MyApplications.getInstance().getAgent().httpAgent.getEncryptKey());
		Log.d("MessageUtil","MessageService"+ MyApplications.getInstance().getAgent().httpAgent.getEncryptVector());
		Log.d("MessageUtil","MessageService"+ MyApplications.getInstance().getAgent().httpAgent.getUserTokenID());
		CommSet.d("baosight", "ssss-------------->"+ resJson.get("data").toString());
		return resJson.get("data").toString();
	}

	/**
	 * 将Model中的数据放入List中
	 */
	public static List[] SetModelData2List(Object object, String Node_Name) {
		List[] lists = new List[1];
		List list = new ArrayList();
		Map map = new HashMap();
		map.put(Node_Name, object);
		list.add(map);
		lists[0] = list;
		return lists;
	}

	/**
	 * 将Models中的数据放入Lists中
	 */
	public static List[] SetModels2Lists(Object objects[], String Node_Name,
										 int i) {
		List[] lists = new List[i];
		for (int j = 0; j < lists.length; j++) {
			lists[j] = new ArrayList();
			Map map = new HashMap();
			map.put(Node_Name, objects[j]);
			lists[j].add(map);
		}
		return lists;
	}

	/**
	 * 将Models中的数据放入Lists中,多个主菜单中嵌套多个子菜单
	 */
	public static List[] SetModels2Lists(Object objects[][], String Node_Name,
										 String child_Node_Name) {
		int main = objects.length;
		List[] lists = new List[main];
		for (int j = 0; j < lists.length; j++) {
			lists[j] = new ArrayList();
			int child = objects[j].length;
			Map map[] = new Map[child];
			map[0] = new HashMap();
			map[0].put(Node_Name, objects[j][0]);
			lists[j].add(map[0]);
			for (int m = 1; m < child; m++) {
				map[m] = new HashMap();
				map[m].put(child_Node_Name, objects[j][m]);
				lists[j].add(map[m]);
			}
		}
		return lists;
	}

	/**
	 * 返回多条数据（联网）
	 *
	 * @throws Exception
	 */
	public static List<?> receiveListsModel(List<?> lists[], String id,
											Object object) throws Exception {
		String sendxml = ParseXml.createXMLFile("CS0001", lists, "C", "S");
		CommSet.d("baosight", "生成xml----->" + sendxml);
		String result = null;
		result = send(sendxml, id);
		if (result != null && "".equals(result.trim())) {
			return null;
		} else {
			return ParseXml.parseDocumentSBObjs(result, object);
		}
	}

	public static List<Object> getList() {

		return listss;
	}
}
