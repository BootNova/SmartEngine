package com.bootnova.smart.framework.engine.test.process;

import com.bootnova.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.bootnova.smart.framework.engine.exception.EngineException;
import com.bootnova.smart.framework.engine.model.assembly.BaseElement;
import com.bootnova.smart.framework.engine.model.assembly.ProcessDefinition;
import com.bootnova.smart.framework.engine.model.instance.ExecutionInstance;
import com.bootnova.smart.framework.engine.model.instance.InstanceStatus;
import com.bootnova.smart.framework.engine.model.instance.ProcessInstance;
import com.bootnova.smart.framework.engine.model.instance.TaskInstance;
import com.bootnova.smart.framework.engine.test.DatabaseBaseTestCase;
import com.bootnova.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.bootnova.smart.framework.engine.test.process.helper.CustomVariablePersister;
import com.bootnova.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration("/spring/application-test.xml")
@Transactional
@RunWith(SpringRunner.class)

public class InclusiveGatewayThreadMultiWithTenantIdTest extends DatabaseBaseTestCase {

    protected void initProcessConfiguration() {
        super.initProcessConfiguration();

        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());

        // 指定线程池，多线程fork
        CustomThreadFactory threadFactory = new CustomThreadFactory("smart-engine");
        processEngineConfiguration.setExecutorService(Executors.newFixedThreadPool(30, threadFactory));
    }

    // 自定义线程工厂
    static class CustomThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        CustomThreadFactory(String poolName) {
            namePrefix = poolName + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            thread.setDaemon(false); // 设置为非守护线程
            return thread;
        }
    }

    @Test
    public void testScenario01_AllBranchesTriggered() {
        String tenantId = "-1";
        // 本case验证场景1：所有分支都触发
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayAllBranchesTest.xml",tenantId).getFirstProcessDefinition();
        List<BaseElement> baseElementList = processDefinition.getBaseElementList();
        assertEquals(15, baseElementList.size());

        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，使所有分支都被触发
        request.put("condition1", true);
        request.put("condition2", true);
        request.put("condition3", true);
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 流程启动后，正确状态断言
        Assert.assertNotNull(processInstance);
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        // 验证所有分支都被执行
        List<ExecutionInstance> executionInstanceList = executionQueryService.findAll(processInstance.getInstanceId(),processInstance.getTenantId());
        
        // 验证执行实例中包含所有分支的活动
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
                
        assertTrue(actualActivityIds.contains("service1"));
        assertTrue(actualActivityIds.contains("service2"));
        assertTrue(actualActivityIds.contains("service3"));
    }

    @Test
    public void testScenario02_NoBranchesTriggered() {
        String tenantId = "-1";

        // 本case验证场景2：所有分支都不触发，应走默认分支
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayAllBranchesTest.xml",tenantId).getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，使所有分支都不被触发
        request.put("condition1", false);
        request.put("condition2", false);
        request.put("condition3", false);
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);


        // 当所有分支条件都不满足时，应该抛出EngineException异常
        EngineException exception = Assert.assertThrows(EngineException.class, () -> {
            processCommandService.start(
                    processDefinition.getId(), processDefinition.getVersion(),
                    request);
        });

        // 验证异常消息包含预期的内容
        assertTrue(exception.getMessage().contains("No Transitions matched"));
        assertTrue(exception.getMessage().contains("inclusiveFork"));


    }

    @Test
    public void testScenario03_NoBranches_ButDefaultBranchTriggered() {
        String tenantId = "-1";
        // 本case验证场景2：所有分支都不触发，应走默认分支
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("database/InclusiveGatewayDefaultBranchesTest.xml",tenantId).getFirstProcessDefinition();

        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，使所有分支都不被触发
        request.put("condition1", false);
        request.put("condition2", false);
        request.put("condition3", false);
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);


        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                request);

        // 流程启动后，正确状态断言
        Assert.assertNotNull(processInstance);
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        // 验证所有分支都被执行
        List<ExecutionInstance> executionInstanceList = executionQueryService.findAll(processInstance.getInstanceId(),processInstance.getTenantId());

        // 验证执行实例中包含所有分支的活动
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());

        assertTrue(actualActivityIds.contains("service1"));
        assertFalse(actualActivityIds.contains("service2"));
        assertFalse(actualActivityIds.contains("service3"));


    }

    @Test
    public void testScenario04_OneBranchTriggered() {
        String tenantId = "-1";
        // 本case验证场景3：只触发一个分支
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayAllBranchesTest.xml",tenantId).getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，只触发一个分支
        request.put("condition1", true);
        request.put("condition2", false);
        request.put("condition3", false);
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);

        long service1SleepTime = 400L;
        String service1ActivityId = "service1";

        long service2SleepTime = 500L;
        String service2ActivityId = "service2";

        long service3SleepTime = 300L;
        String service3ActivityId = "service3";

        request.put(service1ActivityId, service1SleepTime);
        request.put(service2ActivityId, service2SleepTime);
        request.put(service3ActivityId, service3SleepTime);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 流程启动后，正确状态断言
        Assert.assertNotNull(processInstance);
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        // 验证只有一个分支被执行
        List<ExecutionInstance> executionInstanceList = executionQueryService.findAll(processInstance.getInstanceId(),processInstance.getTenantId());
        
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
                
        assertTrue(actualActivityIds.contains("service1"));
        assertFalse(actualActivityIds.contains("service2"));
        assertFalse(actualActivityIds.contains("service3"));
    }

    @Test
    public void testScenario05_TwoBranchesTriggered() {
        String tenantId = "-1";
        // 本case验证场景4：触发两个分支
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayAllBranchesTest.xml",tenantId).getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，触发两个分支
        request.put("condition1", true);
        request.put("condition2", true);
        request.put("condition3", false);
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 流程启动后，正确状态断言
        Assert.assertNotNull(processInstance);
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        // 验证两个分支被执行
        List<ExecutionInstance> executionInstanceList = executionQueryService.findAll(processInstance.getInstanceId(),processInstance.getTenantId());
        
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
                
        assertTrue(actualActivityIds.contains("service1"));
        assertTrue(actualActivityIds.contains("service2"));
        assertFalse(actualActivityIds.contains("service3"));
        assertFalse(actualActivityIds.contains("defaultService"));
    }

    @Test
    public void testScenario06_AllServiceTasks() {
        String tenantId="-1";
        // 本case验证场景5：包容网关后接服务任务
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayAllServiceTaskTest.xml",tenantId).getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，触发两个分支
        request.put("condition1", true);
        request.put("condition2", true);
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 流程启动后，正确状态断言
        Assert.assertNotNull(processInstance);
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        // 验证服务任务被执行
        List<ExecutionInstance> executionInstanceList = executionQueryService.findAll(processInstance.getInstanceId(),processInstance.getTenantId());
        
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
                
        assertTrue(actualActivityIds.contains("service1"));
        assertTrue(actualActivityIds.contains("service2"));
        assertTrue(actualActivityIds.contains("inclusiveJoin"));
        assertTrue(actualActivityIds.contains("endService"));
    }

    @Test
    public void testScenario07_ServiceAndReceiveTasks() {
        String tenantId="-1";
        // 本case验证场景6：包容网关后接服务任务和接收任务
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayServiceAndReceiveTest.xml",tenantId).getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，触发两个分支
        request.put("condition1", true);
        request.put("condition2", true);
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 验证执行轨迹
        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(2, executionInstanceList.size());
        
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());

        // 一个分支到达join，一个分支在receiveTask
        assertTrue(actualActivityIds.contains("inclusiveJoin"));
        assertTrue(actualActivityIds.contains("receiveTask"));

        // 驱动receiveTask继续执行
        Optional<ExecutionInstance> receiveTask = executionInstanceList.stream()
                .filter(e -> e.getProcessDefinitionActivityId().equals("receiveTask"))
                .findFirst();
        assertTrue(receiveTask.isPresent());
        
        processInstance = executionCommandService.signal(receiveTask.get().getInstanceId(), request);

        // 验证流程完成
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
        
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(0, executionInstanceList.size());
    }

    @Test
    public void testScenario08_ServiceAndUserTasks() {
        String tenantId="-1";
        // 本case验证场景7：包容网关后接服务任务和用户任务
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayServiceAndUserTaskTest.xml",tenantId).getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，触发两个分支
        request.put("condition1", true);
        request.put("condition2", true);
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 验证用户任务创建
        List<TaskInstance> taskInstanceList = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(1, taskInstanceList.size());
        assertEquals("userTask", taskInstanceList.get(0).getProcessDefinitionActivityId());

        // 验证执行轨迹
        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(2, executionInstanceList.size());
        
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());

        // 一个分支到达join，一个分支在userTask
        assertTrue(actualActivityIds.contains("inclusiveJoin"));
        assertTrue(actualActivityIds.contains("userTask"));

        // 完成用户任务
        taskCommandService.complete(taskInstanceList.get(0).getInstanceId(), request);

        // 验证流程完成
        processInstance = processQueryService.findById(processInstance.getInstanceId(), processInstance.getTenantId());
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
        
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(0, executionInstanceList.size());
    }

    @Test
    public void testScenario09_UnbalancedGateway() {
        String tenantId = "-1";
        // 本case验证场景8：不平衡的包容网关（1个fork 2个join）
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayUnbalancedTest.xml",tenantId).getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，触发所有分支
        request.put("condition1", true);
        request.put("condition2", true);
        request.put("condition3", true);
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);
    
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);
    
        // 验证执行轨迹
        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        
        // 验证分支1和分支2已经到达第一个join，分支3继续执行
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
                
//        assertTrue(actualActivityIds.contains("firstJoin"));
//        assertTrue(actualActivityIds.contains("service3"));
//
//        // 新增：验证service1和service2已经执行完成
//        List<ExecutionInstance> allExecutionInstances = executionQueryService.findAll(processInstance.getInstanceId());
//        Set<String> allActivityIds = allExecutionInstances.stream()
//                .map(ExecutionInstance::getProcessDefinitionActivityId)
//                .collect(Collectors.toSet());
//
//        assertTrue(allActivityIds.contains("service1"));
//        assertTrue(allActivityIds.contains("service2"));
//
//        // 等待分支3执行完成
//        Optional<ExecutionInstance> service3 = executionInstanceList.stream()
//                .filter(e -> e.getProcessDefinitionActivityId().equals("service3"))
//                .findFirst();
//        assertTrue(service3.isPresent());
//
//        processInstance = executionCommandService.signal(service3.get().getInstanceId(), request);
//
//        // 新增：验证中间服务任务已执行
//        allExecutionInstances = executionQueryService.findAll(processInstance.getInstanceId());
//        allActivityIds = allExecutionInstances.stream()
//                .map(ExecutionInstance::getProcessDefinitionActivityId)
//                .collect(Collectors.toSet());
//
//        assertTrue(allActivityIds.contains("middleService"));
//        assertTrue(allActivityIds.contains("secondJoin"));
    
        // 验证流程完成
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
        
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(0, executionInstanceList.size());
    }

    @Test
    public void testScenario10_NestedGateways() {
        String tenantId = "-1";
        // 本case验证场景9：嵌套的包容网关
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayAllServiceNestedTest.xml",tenantId).getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，触发主包容网关的所有分支
        request.put("mainCondition1", true);
        request.put("mainCondition2", true);
        // 设置条件变量，触发子包容网关1的部分分支
        request.put("subCondition1_1", true);
        request.put("subCondition1_2", false);
        // 设置条件变量，触发子包容网关2的部分分支
        request.put("subCondition2_1", false);
        request.put("subCondition2_2", true);
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 验证执行轨迹
        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        
        // 验证子包容网关的分支执行情况
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());

        Assert.assertTrue(actualActivityIds.isEmpty());

        // 验证流程完成
        processInstance = processQueryService.findById(processInstance.getInstanceId(),processInstance.getTenantId());
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
        
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(0, executionInstanceList.size());
    }


    @Test
    public void testScenario11_NestedGateways_PausedInstance() {
        String tenantId="-1";
        // 本case验证场景9：嵌套的包容网关
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("database/InclusiveGatewayNestedTest.xml",tenantId).getFirstProcessDefinition();

        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，触发主包容网关的所有分支
        request.put("mainCondition1", true);
        request.put("mainCondition2", true);
        // 设置条件变量，触发子包容网关1的部分分支
        request.put("subCondition1_1", true);
        request.put("subCondition1_2", false);
        // 设置条件变量，触发子包容网关2的部分分支
        request.put("subCondition2_1", false);
        request.put("subCondition2_2", true);
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);

        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                request);

        // 验证执行轨迹
        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());

        // 验证子包容网关的分支执行情况
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());

        // 验证只有满足条件的分支被执行
        assertTrue(actualActivityIds.contains("subService1_1"));
        assertFalse(actualActivityIds.contains("subService1_2"));
        assertFalse(actualActivityIds.contains("subService2_1"));
        assertTrue(actualActivityIds.contains("subService2_2"));
        
        // 验证流程实例状态为运行中（因为有receiveTask暂停）
        processInstance = processQueryService.findById(processInstance.getInstanceId(), processInstance.getTenantId());
        assertEquals(InstanceStatus.running, processInstance.getStatus());
        
        // 找到所有活动的执行实例
        Map<String, ExecutionInstance> activeExecutionMap = new HashMap<>();
        for (ExecutionInstance executionInstance : executionInstanceList) {
            if (executionInstance.isActive()) {
                activeExecutionMap.put(executionInstance.getProcessDefinitionActivityId(), executionInstance);
            }
        }

        // 验证receiveTask确实暂停了流程
        assertTrue(activeExecutionMap.containsKey("subService1_1"));
        assertTrue(activeExecutionMap.containsKey("subService2_2"));
        
        // 完成第一个receiveTask (subService1_1)
        ExecutionInstance subService1_1Instance = activeExecutionMap.get("subService1_1");
        processInstance = executionCommandService.signal(subService1_1Instance.getInstanceId(), request);
        
        // 验证第一个子网关分支已完成，但第二个子网关分支仍在等待
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(), processInstance.getTenantId());
        actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
                
        // 第一个子网关分支已完成，应该到达subJoin1
        assertTrue(actualActivityIds.contains("subJoin1") || actualActivityIds.contains("mainJoin"));
        // 第二个子网关分支仍在等待
        assertTrue(actualActivityIds.contains("subService2_2"));
        
        // 完成第二个receiveTask (subService2_2)
        activeExecutionMap.clear();
        for (ExecutionInstance executionInstance : executionInstanceList) {
            if (executionInstance.isActive()) {
                activeExecutionMap.put(executionInstance.getProcessDefinitionActivityId(), executionInstance);
            }
        }
        
        ExecutionInstance subService2_2Instance = activeExecutionMap.get("subService2_2");
        processInstance = executionCommandService.signal(subService2_2Instance.getInstanceId(), request);
        
        // 验证所有子网关分支已完成，流程应该到达主join或结束
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        
        // 验证流程完成
        processInstance = processQueryService.findById(processInstance.getInstanceId(),processInstance.getTenantId());
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(), processInstance.getTenantId());
        assertEquals(0, executionInstanceList.size());
    }
}