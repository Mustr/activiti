package com.harry.listeners;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * start event节点的监听对象。需要实现ExecutionListener接口
 * @author chenxj
 *
 */
public class StartListerens implements ExecutionListener{
	private static final long serialVersionUID = 2002368934415613421L;

	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("任务开始了！");
		System.out.println("businessKey : " + execution.getProcessBusinessKey());
		System.out.println("eventName : " + execution.getEventName());
	}

}
