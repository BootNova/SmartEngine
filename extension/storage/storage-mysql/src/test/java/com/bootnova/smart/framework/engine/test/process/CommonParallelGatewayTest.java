package com.bootnova.smart.framework.engine.test.process;

import com.bootnova.smart.framework.engine.bpmn.assembly.gateway.InclusiveGateway;
import com.bootnova.smart.framework.engine.bpmn.assembly.gateway.ParallelGateway;
import com.bootnova.smart.framework.engine.bpmn.behavior.gateway.helper.CommonGatewayHelper;
import com.bootnova.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
import com.bootnova.smart.framework.engine.model.assembly.ProcessDefinition;
import com.bootnova.smart.framework.engine.pvm.PvmProcessDefinition;
import com.bootnova.smart.framework.engine.test.DatabaseBaseTestCase;
import com.bootnova.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.bootnova.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CommonParallelGatewayTest extends DatabaseBaseTestCase {

    protected void initProcessConfiguration() {

        super.initProcessConfiguration();

        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());

        //指定线程池,多线程fork
        processEngineConfiguration.setExecutorService( Executors.newFixedThreadPool(10));

    }

    @Test
    public void testNonEmbedded()  {

        //验证场景1 ,没有嵌套

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/ParallelGatewayAllServiceTaskTest.xml").getFirstProcessDefinition();
        assertEquals(16, processDefinition.getBaseElementList().size());

        PvmProcessDefinition pvmProcessDefinition = smartEngine.getProcessEngineConfiguration()
                .getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,
                        ProcessDefinitionContainer.class).getPvmProcessDefinition(processDefinition.getId(),
                        processDefinition.getVersion());

        Map<String, String> matchedJoinGateway = CommonGatewayHelper.findMatchedJoinParallelGateway(pvmProcessDefinition, ParallelGateway.class);

        String join = matchedJoinGateway.get("fork");
        Assert.assertEquals("join",join);

    }

    @Test
    public void testSimpleEmbedded()  {

        //验证场景1 ,没有嵌套

        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("database/SimpleEmbeddedParallelGateway.xml").getFirstProcessDefinition();
        assertEquals(22, processDefinition.getBaseElementList().size());

        PvmProcessDefinition pvmProcessDefinition = smartEngine.getProcessEngineConfiguration()
                .getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,
                        ProcessDefinitionContainer.class).getPvmProcessDefinition(processDefinition.getId(),
                        processDefinition.getVersion());

        Map<String, String> matchedJoinGateway = CommonGatewayHelper.findMatchedJoinParallelGateway(pvmProcessDefinition, ParallelGateway.class);

        String join = matchedJoinGateway.get("parentFork");
        Assert.assertEquals("parentJoin",join);

        join = matchedJoinGateway.get("childFork");
        Assert.assertEquals("childJoin",join);

    }


    @Test
    public void testComplexEmbedded()  {

        //验证场景1 ,没有嵌套

        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("database/ComplexEmbeddedParallelGateway.xml").getFirstProcessDefinition();
        assertEquals(21, processDefinition.getBaseElementList().size());

        PvmProcessDefinition pvmProcessDefinition = smartEngine.getProcessEngineConfiguration()
                .getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,
                        ProcessDefinitionContainer.class).getPvmProcessDefinition(processDefinition.getId(),
                        processDefinition.getVersion());

        Map<String, String> matchedJoinGateway = CommonGatewayHelper.findMatchedJoinParallelGateway(pvmProcessDefinition, ParallelGateway.class);

        String join = matchedJoinGateway.get("parentFork");
        Assert.assertEquals("parentJoin",join);

        join = matchedJoinGateway.get("subFork");
        Assert.assertEquals("subJoin",join);

    }

    @Test
    public void testComplexEmbedded1()  {

        //验证场景1 ,没有嵌套

        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("database/InclusiveGatewayNestedTest.xml").getFirstProcessDefinition();

        PvmProcessDefinition pvmProcessDefinition = smartEngine.getProcessEngineConfiguration()
                .getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,
                        ProcessDefinitionContainer.class).getPvmProcessDefinition(processDefinition.getId(),
                        processDefinition.getVersion());

        Map<String, String> matchedJoinGateway = CommonGatewayHelper.findMatchedJoinParallelGateway(pvmProcessDefinition, InclusiveGateway.class);

        //todo fix
//        String join = matchedJoinGateway.get("mainFork");
//        Assert.assertEquals("mainJoin",join);

        String join = matchedJoinGateway.get("subFork1");
        Assert.assertEquals("subJoin1",join);

        join = matchedJoinGateway.get("subFork2");
        Assert.assertEquals("subJoin2",join);

    }




}