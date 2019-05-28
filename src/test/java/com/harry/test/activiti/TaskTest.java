package com.harry.test.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * 业务流程测试
 * @author Administrator
 *
 */
public class TaskTest extends ActivitiTest{

	//启动业务
	@Test
	public void testStartTask() {
		String processDefinitionKey = "leave";//流程定义信息key 会获取流程定义信息的最新版本
		String businessKey = "12345678";//业务对象的key
		//定义一些变量，这些变量可以替换bpmn中的el表达式的占位符中，如：${creator} 会把下面的变量中的creator的值替换
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("var1", "value1");
		variables.put("creator", "李逵");
		variables.put("instructor", "辅导员的key11223344");
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
		System.out.println(processInstance);
	}
	
	//获取当前任务完成之后的连线名称
	@Test
	public void taskOutcomeList() {
		String taskId = "62508";
		
		//返回存放连线的名称集合
		List<String> list = new ArrayList<String>();
		//1:使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		//2：获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		
		//3：查询ProcessDefinitionEntity对象
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		
		//使用任务对象Task获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		
		//使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		
		//获取当前活动的id
		String activityId = processInstance.getActivityId();
		
		//4：获取当前的活动
		ActivityImpl activity = processDefinitionEntity.findActivity(activityId);
		
		//5：获取当前活动完成之后连线的名称
		List<PvmTransition> pvmList = activity.getOutgoingTransitions();
		
		if (pvmList != null) {
			for (PvmTransition pvm : pvmList) {
				System.out.println(pvm);
				
				String name = (String) pvm.getProperty("name");
				if(StringUtils.isNotBlank(name)){
					list.add(name);
				}
				else{
					list.add("默认提交");
				}
			}
		}
		System.out.println("===============");
		printCollection(list);
	}
	
	//执行任务
	@Test
	public void taskComplete() {
		String taskId = "105007";
		
		Task task = taskService.createTaskQuery()//
				.taskId(taskId)//使用任务ID查询
				.singleResult();
		
		String processInstanceId = task.getProcessInstanceId();
		String message = "消息消息44556677！";
		
		/**
		 * 注意：添加批注的时候，由于Activiti底层代码是使用：
		 * 		String userId = Authentication.getAuthenticatedUserId();
			    CommentEntity comment = new CommentEntity();
			    comment.setUserId(userId);
			  所有需要从Session中获取当前登录人，作为该任务的办理人（审核人），对应act_hi_comment表中的User_ID的字段，不过不添加审核人，该字段为null
			 所以要求，添加配置执行使用Authentication.setAuthenticatedUserId();添加当前任务的审核人
		 * */
		Authentication.setAuthenticatedUserId("辅导员重新审核11");
		taskService.addComment(taskId, processInstanceId, message);
		
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("pass", true);//流程中连线网关（Exclusive Gateway）的判断条件，决定走怎么走接下来的哪条路线

		//3：使用任务ID，完成当前人的个人任务，同时流程变量，执行到某个节点的时候，会启动节点的监听方法等
		taskService.complete(taskId, variables);
	}
	
	//获取流程实例的所有批注，还有其他过滤条件的接口
	@Test
	public void getProcessInstance() {
		String processInstanceId = "95001";
		
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		System.out.println(pi);
		
		List<Comment> list = taskService.getProcessInstanceComments(processInstanceId);
		for (Comment comment : list) {
			System.out.println(comment.getUserId() + " : " +comment.getFullMessage());
		}
	}
	
	//jobs： 某个节点设置了一步的（asynchronous），那么执行到该节点的时候会把改节点加入到job中。
	//在任务中查询不到。需要手动，或者其他手段来触发执行execute方法才会继续走下去
	@Test
	public void jobs() {
		List<Job> list = processEngine.getManagementService().createJobQuery().list();
		
		printCollection(list);
		
		processEngine.getManagementService().executeJob("80005");
	}
	
	//查看当前活动，获取当期活动对应的坐标x,y,width,height，将4个值存放到Map<String,Object>中
	@Test
	public void coording() {
		String taskId = "";
		// 存放坐标
		Map<String, Object> map = new HashMap<String, Object>();
		// 使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery()//
				.taskId(taskId)// 使用任务ID查询
				.singleResult();
		// 获取流程定义的ID
		String processDefinitionId = task.getProcessDefinitionId();
		// 获取流程定义的实体对象（对应.bpmn文件中的数据）
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);
		// 流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		// 使用流程实例ID，查询正在执行的执行对象表，获取当前活动对应的流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()// 创建流程实例查询
				.processInstanceId(processInstanceId)// 使用流程实例ID查询
				.singleResult();
		// 获取当前活动的ID
		String activityId = pi.getActivityId();
		// 获取当前活动对象
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);// 活动ID
		// 获取坐标
		map.put("x", activityImpl.getX());
		map.put("y", activityImpl.getY());
		map.put("width", activityImpl.getWidth());
		map.put("height", activityImpl.getHeight());
		System.out.println(map);
	}
}
