package com.bootnova.smart.framework.engine.test.parallelgateway;

import java.util.List;
import java.util.concurrent.Executors;

import com.bootnova.smart.framework.engine.configuration.LockStrategy;
import com.bootnova.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.bootnova.smart.framework.engine.helper.ExecutionInstanceHelper;
import com.bootnova.smart.framework.engine.model.assembly.ProcessDefinition;
import com.bootnova.smart.framework.engine.model.instance.ExecutionInstance;
import com.bootnova.smart.framework.engine.model.instance.InstanceStatus;
import com.bootnova.smart.framework.engine.model.instance.ProcessInstance;
import com.bootnova.smart.framework.engine.test.SimpleFileLockStrategy;
import com.bootnova.smart.framework.engine.test.cases.CustomBaseTestCase;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmbeddedParallelGatewayTest extends CustomBaseTestCase {


    protected void initProcessConfiguration() {
        processEngineConfiguration = new DefaultProcessEngineConfiguration();

        LockStrategy fileLockStrategy = new SimpleFileLockStrategy();
        processEngineConfiguration.setLockStrategy(fileLockStrategy);

        processEngineConfiguration.setExecutorService(Executors.newFixedThreadPool(10));
    }


    private List<ExecutionInstance> produceExecutionInstances() {
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("EmbeddedParallelGateway.xml").getFirstProcessDefinition();
        assertEquals(22, processDefinition.getBaseElementList().size());

        //=======1ST=========

        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion()
        );

        // 流程启动后,正确状态断言
        Assert.assertNotNull(processInstance);

        List<ExecutionInstance> firstStepExecutionInstanceList = executionQueryService.findActiveExecutionList(
                processInstance.getInstanceId());
        assertEquals(4, firstStepExecutionInstanceList.size());

        ExecutionInstance childForkReceiveTask = ExecutionInstanceHelper.findMatched(firstStepExecutionInstanceList,
                "childForkReceiveTask");

        ExecutionInstance parentReceiveTask = ExecutionInstanceHelper.findMatched(firstStepExecutionInstanceList, "parentReceiveTask");

        ExecutionInstance childJoin = ExecutionInstanceHelper.findMatched(firstStepExecutionInstanceList, "childJoin");

        ExecutionInstance parentJoin = ExecutionInstanceHelper.findMatched(firstStepExecutionInstanceList, "parentJoin");

        Assert.assertNotNull(childForkReceiveTask);
        Assert.assertNotNull(parentReceiveTask);
        Assert.assertNotNull(childJoin);
        Assert.assertNotNull(parentJoin);
        return firstStepExecutionInstanceList;
    }

    @Test
    public void testEmbedded() throws Exception {

        List<ExecutionInstance> firstStepExecutionInstanceList = produceExecutionInstances();


        //=======2ND=========
        ExecutionInstance childForkReceiveTask = ExecutionInstanceHelper.findMatched(firstStepExecutionInstanceList, "childForkReceiveTask");

        ProcessInstance  processInstance = executionCommandService.signal(childForkReceiveTask.getInstanceId());
        List<ExecutionInstance> secondStepExecutionInstanceList = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());
        assertEquals(3, secondStepExecutionInstanceList.size());


        ExecutionInstance parentReceiveTask2 = ExecutionInstanceHelper.findMatched(secondStepExecutionInstanceList, "parentReceiveTask");
        Assert.assertNotNull(parentReceiveTask2);

        //=======3RD=========

        processInstance = executionCommandService.signal(parentReceiveTask2.getInstanceId());
        List<ExecutionInstance> thirdStepExecutionInstanceList = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());
        assertEquals(0, thirdStepExecutionInstanceList.size());


        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

    }


    @Test
    public void testEmbedded2() throws Exception {


        List<ExecutionInstance> firstStepExecutionInstanceList = produceExecutionInstances();


        //=======2ND=========
        ExecutionInstance parentReceiveTask = ExecutionInstanceHelper.findMatched(firstStepExecutionInstanceList, "parentReceiveTask");

        ProcessInstance  processInstance = executionCommandService.signal(parentReceiveTask.getInstanceId());
        List<ExecutionInstance> secondStepExecutionInstanceList = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());
        assertEquals(4, secondStepExecutionInstanceList.size());


        ExecutionInstance childForkReceiveTask = ExecutionInstanceHelper.findMatched(secondStepExecutionInstanceList, "childForkReceiveTask");
        Assert.assertNotNull(childForkReceiveTask);

        //=======3RD=========

        processInstance = executionCommandService.signal(childForkReceiveTask.getInstanceId());
        List<ExecutionInstance> thirdStepExecutionInstanceList = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());
        assertEquals(0, thirdStepExecutionInstanceList.size());


        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

    }


}