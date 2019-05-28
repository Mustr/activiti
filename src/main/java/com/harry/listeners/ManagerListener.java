package com.harry.listeners;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * user task 节点监听的监听类，需要实现taskListener
 * 
 * 可以设置类似于执行者参数，通知操作
 * @author chenxj
 *
 */
public class ManagerListener implements TaskListener{
	private static final long serialVersionUID = -4140069397624131523L;

	public void notify(DelegateTask task) {
		System.out.println("ManagerListener : notify 专业负责人");
		task.setAssignee("chenxj");
	}

}
