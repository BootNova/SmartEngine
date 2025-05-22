package com.bootnova.smart.framework.engine.test.service;

import java.util.Collection;

import com.bootnova.smart.framework.engine.common.util.IdAndVersionUtil;
import com.bootnova.smart.framework.engine.model.assembly.ProcessDefinition;
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
public class RepositoryServiceTest extends DatabaseBaseTestCase {

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
    public void testSimple() throws Exception {

        String processDefinitionId="exclusiveTest";
        String version = "1.0.0";

        String content = IOUtil.readResourceFileAsUTF8String("multi-instance-test.bpmn20.xml");
        repositoryCommandService.deployWithUTF8Content(content);


        repositoryCommandService.deploy("test-usertask-and-servicetask-exclusive.bpmn20.xml");

        ProcessDefinition processDefinition = repositoryQueryService.getCachedProcessDefinition(IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId,version,null));

        Assert.assertNotNull(processDefinition);

        processDefinition = repositoryQueryService.getCachedProcessDefinition(IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId,version,null));

        Assert.assertNotNull(processDefinition);

        Collection<ProcessDefinition> processDefinitionCollection = repositoryQueryService.getAllCachedProcessDefinition();

        Assert.assertEquals(2,processDefinitionCollection.size());



    }

}