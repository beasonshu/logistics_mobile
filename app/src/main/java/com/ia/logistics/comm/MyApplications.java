package com.ia.logistics.comm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;

import com.baosight.iplat4mandroid.core.ei.agent.EiServiceAgent;
import com.ia.logistics.model.receive.SignDetilModel;

public class MyApplications extends Application {

	private static MyApplications applications;
	/**
	 * ???????
	 */
	private String useId;

	/**
	 * ???????
	 */
	private String user_name;

	/**
	 * ?????????
	 */
	private String provider_id;

	/**
	 * ??????????
	 */
	private String provide_name;

	/**
	 * ??????
	 */
	private String trip_state;

	/**
	 * ????????,???agent?????м??????
	 */
	private EiServiceAgent agent;
	
	private ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

	private List<SignDetilModel> cacheList = new ArrayList<SignDetilModel>();

	public List<SignDetilModel> getCacheList() {
		return cacheList;
	}

	public void setCacheList(List<SignDetilModel> cacheList) {
		this.cacheList = cacheList;
	}

	public static MyApplications getInstance() {
		return applications;
	}

	public String getUseId() {
		return useId;
	}

	public void setUseId(String useId) {
		this.useId = useId;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getProvider_id() {
		return provider_id;
	}

	public void setProvider_id(String provider_id) {
		this.provider_id = provider_id;
	}

	public String getProvide_name() {
		return provide_name;
	}

	public void setProvide_name(String provide_name) {
		this.provide_name = provide_name;
	}

	public EiServiceAgent getAgent() {
		CommSet.e("agent", ""+(agent==null));
		return agent;
	}

	public void setAgent(EiServiceAgent agent) {
		this.agent = agent;
	}

	public ArrayList<Map<String, String>> getList() {
		return list;
	}

	// ???????
	public boolean addItem(Map<String, String> map) {
		for (Map<String, String> iterable_element : list) {
			if (map.get("package_id")
					.equals(iterable_element.get("package_id"))) {
				return false;
			}
		}
		list.add(map);
		sort();
		return true;
	}

	// ????
	private void sort() {
		// TODO Auto-generated method stub
		if (!list.isEmpty()) {
			Collections.sort(list, new Comparator<Map<String, String>>() {

				@Override
				public int compare(Map<String, String> lhs,
						Map<String, String> rhs) {
					// TODO Auto-generated method stub
					return lhs.get("package_id")
							.compareTo(rhs.get("package_id"));
				}
			});
		}
	}

	public void setList(List<HashMap<String, String>> list) {
		this.list.addAll(list);
	}


	boolean m_bKeyRight = true; // ???Key???????????

	

	//@TargetApi(9)
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		applications = this;
		//CustomException customException = CustomException.getInstance();
		//customException.init(getApplicationContext());
	}

	/**
	 * @param trip_state
	 *            the trip_state to set
	 */
	public void setTrip_state(String trip_state) {
		this.trip_state = trip_state;
	}

	/**
	 * *@param trip_state
	 * 
	 * @return the trip_state
	 */
	public String getTrip_state() {
		return trip_state;
	}

}
