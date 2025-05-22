package com.bootnova.smart.framework.engine.test.service;

import com.bootnova.smart.framework.engine.model.assembly.ProcessDefinition;
import com.bootnova.smart.framework.engine.model.instance.ProcessInstance;
import com.bootnova.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.bootnova.smart.framework.engine.model.instance.TaskInstance;
import com.bootnova.smart.framework.engine.service.param.query.PaginateQueryParam;
import com.bootnova.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.bootnova.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.bootnova.smart.framework.engine.test.DatabaseBaseTestCase;
import com.bootnova.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.bootnova.smart.framework.engine.test.process.helper.CustomVariablePersister;
import com.bootnova.smart.framework.engine.test.process.helper.DefaultMultiInstanceCounter;
import com.bootnova.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
import com.bootnova.smart.framework.engine.test.process.helper.dispatcher.IdAndGroupTaskAssigneeDispatcher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TaskServiceWithTenantTest extends DatabaseBaseTestCase {

    @Override
    protected void initProcessConfiguration() {
        super.initProcessConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new IdAndGroupTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
    }


    @Test
    public void test() throws Exception {
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("user-task-id-and-group-test.bpmn20.xml",null).getFirstProcessDefinition();

        String tenantId = null;
        //4.启动流程实例
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),tenantId
        );
        Assert.assertNotNull(processInstance);


        PendingTaskQueryParam pendingTaskQueryParam = new PendingTaskQueryParam();
        pendingTaskQueryParam.setAssigneeUserId("testuser1");
        pendingTaskQueryParam.setAssigneeGroupIdList(Arrays.asList("testgroup11"));
        pendingTaskQueryParam.setTenantId(null);

        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());
        List<TaskAssigneeInstance> taskAssigneeInstancelist =taskAssigneeQueryService.findList(submitTaskInstanceList.get(0).getInstanceId(),null);
        Assert.assertEquals(5,taskAssigneeInstancelist.size());

         submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId(),null);
        Assert.assertEquals(1,submitTaskInstanceList.size());
        TaskInstance submitTaskInstance = submitTaskInstanceList.get(0);

        TaskInstance taskInstance =  taskQueryService.findOne(submitTaskInstance.getInstanceId(),null);
        Assert.assertEquals("userTask1",taskInstance.getProcessDefinitionActivityId());



        submitTaskInstanceList=  taskQueryService.findPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());

        PaginateQueryParam paginateQueryParam = new PaginateQueryParam();
        paginateQueryParam.setPageOffset(0);
        paginateQueryParam.setPageSize(10);
        submitTaskInstanceList=  taskQueryService.findPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());

        Long count =  taskQueryService.countPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1L,count.longValue());

        PendingTaskQueryParam  pendingTaskQueryParam1 = new PendingTaskQueryParam();
        pendingTaskQueryParam1.setPageOffset(0);
        pendingTaskQueryParam1.setPageSize(10);
        List<String> processInstanceIdList = new ArrayList<String>(2);
        processInstanceIdList.add(processInstance.getInstanceId());
        pendingTaskQueryParam1.setProcessInstanceIdList(processInstanceIdList);
        submitTaskInstanceList=  taskQueryService.findPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());


        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam ();
        taskInstanceQueryParam.setPageOffset(0);
        taskInstanceQueryParam.setPageSize(10);
        List<String> processInstanceIdList111 = new ArrayList<String>(2);
        processInstanceIdList111.add(processInstance.getInstanceId());
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList111);
        submitTaskInstanceList =    taskQueryService.findList(taskInstanceQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());

        List<TaskAssigneeInstance> taskAssigneeInstanceList =   taskAssigneeQueryService.findList(submitTaskInstanceList.get(0).getInstanceId(),null);
        Assert.assertEquals(5,taskAssigneeInstanceList.size());


        ////taskQueryService.findList()
        //
        //
        //
        //
        ////5.流程流转:构造提交申请参数
        //Map<String, Object> submitFormRequest = new HashMap<String, Object>();
        //submitFormRequest.put("title", "new_title");
        //submitFormRequest.put("qps", "300");
        //submitFormRequest.put("capacity","10g");
        //submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,"1");
        //submitFormRequest.put("action", "agree");
        //submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG, FullMultiInstanceTest.AGREE);
        //
        ////6.流程流转:处理 submitTask,完成任务申请.
        //taskCommandService.complete(submitTaskInstance.getInstanceId(),submitFormRequest);
        //
        //submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        //Assert.assertEquals(2,submitTaskInstanceList.size());
        //taskCommandService.complete(submitTaskInstanceList.get(0).getInstanceId(),submitFormRequest);
        //
        //
        //submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        //Assert.assertEquals(1,submitTaskInstanceList.size());
        //taskCommandService.complete(submitTaskInstanceList.get(0).getInstanceId(),submitFormRequest);
        //
        //submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        //Assert.assertEquals(0,submitTaskInstanceList.size());
        //
        //
        //
        ////10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
        //ProcessInstance finalProcessInstance = processQueryService.findById(submitTaskInstance.getProcessInstanceId());
        //Assert.assertEquals(InstanceStatus.completed,finalProcessInstance.getStatus());


    }

    @Test
    public void testWithTenantId() throws Exception {
        String tenantId = "-100";
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("user-task-id-and-group-test.bpmn20.xml",tenantId).getFirstProcessDefinition();

        //4.启动流程实例
        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),tenantId
        );
        Assert.assertNotNull(processInstance);


        PendingTaskQueryParam pendingTaskQueryParam = new PendingTaskQueryParam();
        pendingTaskQueryParam.setAssigneeUserId("testuser1");
        pendingTaskQueryParam.setAssigneeGroupIdList(Arrays.asList("testgroup11"));
        pendingTaskQueryParam.setTenantId(tenantId);

        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());
        List<TaskAssigneeInstance> taskAssigneeInstancelist =taskAssigneeQueryService.findList(submitTaskInstanceList.get(0).getInstanceId(),tenantId);
        Assert.assertEquals(5,taskAssigneeInstancelist.size());

        submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId(),tenantId);
        Assert.assertEquals(1,submitTaskInstanceList.size());
        TaskInstance submitTaskInstance = submitTaskInstanceList.get(0);

        TaskInstance taskInstance =  taskQueryService.findOne(submitTaskInstance.getInstanceId(),tenantId);
        Assert.assertEquals("userTask1",taskInstance.getProcessDefinitionActivityId());



        submitTaskInstanceList=  taskQueryService.findPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());

        PaginateQueryParam paginateQueryParam = new PaginateQueryParam();
        paginateQueryParam.setPageOffset(0);
        paginateQueryParam.setPageSize(10);
        submitTaskInstanceList=  taskQueryService.findPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());

        Long count =  taskQueryService.countPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1L,count.longValue());

        PendingTaskQueryParam  pendingTaskQueryParam1 = new PendingTaskQueryParam();
        pendingTaskQueryParam1.setPageOffset(0);
        pendingTaskQueryParam1.setPageSize(10);
        List<String> processInstanceIdList = new ArrayList<String>(2);
        processInstanceIdList.add(processInstance.getInstanceId());
        pendingTaskQueryParam1.setProcessInstanceIdList(processInstanceIdList);
        submitTaskInstanceList=  taskQueryService.findPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());


        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam ();
        taskInstanceQueryParam.setTenantId(tenantId);
        taskInstanceQueryParam.setPageOffset(0);
        taskInstanceQueryParam.setPageSize(10);
        List<String> processInstanceIdList111 = new ArrayList<String>(2);
        processInstanceIdList111.add(processInstance.getInstanceId());
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList111);
        submitTaskInstanceList =    taskQueryService.findList(taskInstanceQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());

        List<TaskAssigneeInstance> taskAssigneeInstanceList =   taskAssigneeQueryService.findList(submitTaskInstanceList.get(0).getInstanceId(),tenantId);
        Assert.assertEquals(5,taskAssigneeInstanceList.size());


        ////taskQueryService.findList()
        //
        //
        //
        //
        ////5.流程流转:构造提交申请参数
        //Map<String, Object> submitFormRequest = new HashMap<String, Object>();
        //submitFormRequest.put("title", "new_title");
        //submitFormRequest.put("qps", "300");
        //submitFormRequest.put("capacity","10g");
        //submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,"1");
        //submitFormRequest.put("action", "agree");
        //submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG, FullMultiInstanceTest.AGREE);
        //
        ////6.流程流转:处理 submitTask,完成任务申请.
        //taskCommandService.complete(submitTaskInstance.getInstanceId(),submitFormRequest);
        //
        //submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        //Assert.assertEquals(2,submitTaskInstanceList.size());
        //taskCommandService.complete(submitTaskInstanceList.get(0).getInstanceId(),submitFormRequest);
        //
        //
        //submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        //Assert.assertEquals(1,submitTaskInstanceList.size());
        //taskCommandService.complete(submitTaskInstanceList.get(0).getInstanceId(),submitFormRequest);
        //
        //submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        //Assert.assertEquals(0,submitTaskInstanceList.size());
        //
        //
        //
        ////10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
        //ProcessInstance finalProcessInstance = processQueryService.findById(submitTaskInstance.getProcessInstanceId());
        //Assert.assertEquals(InstanceStatus.completed,finalProcessInstance.getStatus());


    }



}