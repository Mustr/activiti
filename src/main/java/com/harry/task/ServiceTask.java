package com.harry.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * servicetask任务节点引用的java class对象，需要实现JavaDelegate
 * @author chenxj
 *
 */
public class ServiceTask implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("ServiceTask .....");
		String addr = (String) execution.getVariable("addr");
		System.out.println(addr);
	}

}
