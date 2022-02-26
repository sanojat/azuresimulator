package com.azure.learn.simulator.vo;

import com.azure.learn.simulator.support.RunningStatus;

public class ThreadStatus {
	
	private String threadName;
	private RunningStatus status;
	private String exceptionMsg;

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public RunningStatus getStatus() {
		return status;
	}

	public void setStatus(RunningStatus status) {
		this.status = status;
	}

}
