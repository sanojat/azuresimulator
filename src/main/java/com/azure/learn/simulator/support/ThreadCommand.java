package com.azure.learn.simulator.support;

/**
 * @author SANOJ
 *
 */
public class ThreadCommand {

	/**
	 * 
	 */
	private String threadName;
	/**
	 * 
	 */
	private Action action;

	/**
	 * @return
	 */
	public String getThreadName() {
		return threadName;
	}

	/**
	 * @param threadName
	 */
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	/**
	 * @return
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @param action
	 */
	public void setAction(Action action) {
		this.action = action;
	}
}
