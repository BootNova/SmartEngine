package com.bootnova.smart.framework.engine.test.service;

import java.util.HashMap;
import java.util.Map;

import com.bootnova.smart.framework.engine.constant.DeploymentStatusConstant;
import com.bootnova.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.bootnova.smart.framework.engine.model.instance.DeploymentInstance;
import com.bootnova.smart.framework.engine.model.instance.InstanceStatus;
import com.bootnova.smart.framework.engine.model.instance.ProcessInstance;
import com.bootnova.smart.framework.engine.service.param.command.CreateDeploymentCommand;
import com.bootnova.smart.framework.engine.test.DatabaseBaseTestCase;
import com.bootnova.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.bootnova.smart.framework.engine.test.process.helper.CustomVariablePersister;
import com.bootnova.smart.framework.engine.test.process.helper.DefaultMultiInstanceCounter;
import com.bootnova.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
import com.bootnova.smart.framework.engine.test.process.helper.dispatcher.DefaultTaskAssigneeDispatcher;
import com.bootnova.smart.framework.engine.util.IOUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SimpleIntegrationTest extends DatabaseBaseTestCase{

    @Override
    protected void initProcessConfiguration() {
        super.initProcessConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
    }


    @Test
    public void test() throws Exception {


        CreateDeploymentCommand createDeploymentCommand = new CreateDeploymentCommand();
        String content = IOUtil.readResourceFileAsUTF8String("multi-instance-test.bpmn20.xml");
        createDeploymentCommand.setProcessDefinitionContent(content);
        createDeploymentCommand.setDeploymentUserId("123");
        createDeploymentCommand.setDeploymentStatus(DeploymentStatusConstant.ACTIVE);
        createDeploymentCommand.setProcessDefinitionDesc("desc");
        createDeploymentCommand.setProcessDefinitionName("name");
        createDeploymentCommand.setProcessDefinitionType("group");
        createDeploymentCommand.setProcessDefinitionCode("code");

        DeploymentInstance deploymentInstance =  deploymentCommandService.createDeployment(createDeploymentCommand);

        Assert.assertEquals("code",deploymentInstance.getProcessDefinitionCode());

        Map<String, Object> request = new HashMap();
        request.put(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_START_USER_ID,"123");
        request.put(RequestMapSpecialKeyConstant.PROCESS_DEFINITION_TYPE,"group");
        request.put("processVariable","processVariableValue");


        ProcessInstance processInstance = processCommandService.start(
            deploymentInstance.getProcessDefinitionId(), deploymentInstance.getProcessDefinitionVersion()
            ,request  );
        Assert.assertNotNull(processInstance);
        Assert.assertEquals("group",processInstance.getProcessDefinitionType());

        processInstance =   processQueryService.findById(processInstance.getInstanceId());
        Assert.assertEquals("group",processInstance.getProcessDefinitionType());
        Assert.assertEquals("multi-instance-user-task:1.0.2",processInstance.getProcessDefinitionIdAndVersion());
        Assert.assertEquals("multi-instance-user-task",processInstance.getProcessDefinitionId());
        Assert.assertEquals("1.0.2",processInstance.getProcessDefinitionVersion());

        Assert.assertEquals("123",processInstance.getStartUserId());
        Assert.assertEquals(InstanceStatus.running,processInstance.getStatus());
        Assert.assertNotNull(processInstance.getCompleteTime());
        Assert.assertNotNull(processInstance.getStartTime());
        Assert.assertNull(processInstance.getBizUniqueId());


    }



}