package com.harry.test.activiti;

import java.util.Collection;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.harry.test.TestBase;
import com.harry.utils.SpringUtils;

/**
 * 工作流的基本测试类
 * @author Administrator
 *
 */
public class ActivitiTest extends TestBase{

	//管理流程定义接口
    protected RepositoryService repositoryService;
	
    //执行管理，包括启动、推进、删除流程实例等操作
    protected RuntimeService runtimeService;
	
    //任务管理接口
    protected TaskService taskService;
	
    //表单管理
    protected FormService formService;
	
    //历史管理接口
    protected HistoryService historyService;
  
    //在Activiti中最核心的类，其他的类都是由他而来。
    @Autowired
    protected ProcessEngine processEngine;
	
	@Before
	public void test(){
		
		repositoryService = SpringUtils.getBean("repositoryService");
		
		runtimeService = SpringUtils.getBean("runtimeService");
		
		taskService = SpringUtils.getBean("taskService");
		
		formService = SpringUtils.getBean("formService");
		
		historyService = SpringUtils.getBean("historyService");
	}
	
	@Test
	public void test1() {
		System.out.println(repositoryService);
		System.out.println(runtimeService);
		System.out.println(taskService);
		System.out.println(formService);
		System.out.println(historyService);
		System.out.println(processEngine);
	}
	
	protected void printCollection(Collection<?> coll) {
		if (coll != null) {
			for (Object col : coll) {
				System.out.println(col);
			}
		} else {
			System.out.println("is null");
		}
	}
}
