/**
 *
 */
package com.ia.logistics.service;

import java.util.Map;

/**
 * @author BeasonShu
 * @project baosteel_3pl_advt_run
 * @since 2012-2-9上午10:26:44
 */
public class Task {
	private int task_id;

	@SuppressWarnings("rawtypes")
	private Map params;

	public int getTask_id() {
		return task_id;
	}

	public Task() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public Task(int task_id, Map params) {
		super();
		this.task_id = task_id;
		this.params = params;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	@SuppressWarnings("rawtypes")
	public Map getParams() {
		return params;
	}

	@SuppressWarnings("rawtypes")
	public void setParams(Map params) {
		this.params = params;
	}


}
