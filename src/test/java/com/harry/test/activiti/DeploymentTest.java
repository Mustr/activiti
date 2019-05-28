package com.harry.test.activiti;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;

public class DeploymentTest extends ActivitiTest{

	//测试部署流程定义
	@Test
	public void testDeploymentBuilder() {
		Deployment deploy = repositoryService.createDeployment()
				.name("请假流程v7")//流程的名字
				//此处使用的是把bpmn文件和流程图直接部署，还可以采用其他方式 如：zip，把两个文件放在一个zip包中，使用zip流部署
				.addClasspathResource("diagrams/leave.bpmn")
				.addClasspathResource("diagrams/leave.png")
				.deploy();
		System.out.println(deploy);
		/*
		 * 部署的流程定义的数据信息在
		 * act_re_deployment和 act_re_procdef表中可以查看
		 */
	}
	
	//测试查询流程定义
	@Test
	public void testQueryDeployment() {
		//流程定义act_re_deployment
		List<Deployment> list = repositoryService.createDeploymentQuery()
		.orderByDeploymenTime().asc()
		.list();
		
		printCollection(list);
		
		//流程定义信息act_re_procdef
		List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
		.orderByProcessDefinitionVersion().asc()
		.list();
		printCollection(processDefinitions);
	}
	
	//删除流程定义
	@Test
	public void testDeleteDeployment() {
		//删除给定的部署和级联删除到流程实例，历史流程实例和作业。
		repositoryService.deleteDeployment("2501",true);
	}
	
	
	@Test//获取流程图
	public void testDeploymentStream() {
		//act_ge_bytearray表中可以查看流程图，bpmn图
		String deploymentId = "7501";
		String resourceName = "diagrams/process.png";
		InputStream in = repositoryService.getResourceAsStream(deploymentId, resourceName);
		String file = "F:\\porcess.png";
		OutputStream os = null;
		byte[] b = new byte[1024];
		try {
			os = new FileOutputStream(file);
			while (in.read(b) > 0) {
				os.write(b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
