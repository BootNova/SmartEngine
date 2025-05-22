package com.bootnova.smart.framework.engine.extendsion.parser;

import java.util.Map;

import com.bootnova.smart.framework.engine.SmartEngine;
import com.bootnova.smart.framework.engine.bpmn.assembly.task.BusinessRuleTask;
import com.bootnova.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.bootnova.smart.framework.engine.configuration.impl.DefaultIdGenerator;
import com.bootnova.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.bootnova.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.bootnova.smart.framework.engine.extendsion.parser.engine.ProcessField;
import com.bootnova.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;
import com.bootnova.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.bootnova.smart.framework.engine.model.assembly.ExtensionElements;
import com.bootnova.smart.framework.engine.model.assembly.IdBasedElement;
import com.bootnova.smart.framework.engine.model.assembly.ProcessDefinition;
import com.bootnova.smart.framework.engine.model.assembly.ProcessDefinitionSource;
import com.bootnova.smart.framework.engine.service.command.ExecutionCommandService;
import com.bootnova.smart.framework.engine.service.command.ProcessCommandService;
import com.bootnova.smart.framework.engine.service.command.RepositoryCommandService;
import com.bootnova.smart.framework.engine.service.query.ExecutionQueryService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zilong.jiangzl
 * @create 2020-07-17 2:29 下午
 */
public class ExtenstionTest {


    private SmartEngine smartEngine;

    private RepositoryCommandService repositoryCommandService;

    private ProcessCommandService processCommandService;

    private ExecutionQueryService executionQueryService;

    private ExecutionCommandService executionCommandService;

    @Before
    public void initEngine() {
        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setIdGenerator(new DefaultIdGenerator());

        processEngineConfiguration.setAnnotationScanner(
            new SimpleAnnotationScanner(
                SmartEngine.class.getPackage().getName(),
                ExtenstionTest.class.getPackage().getName()));
        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        //2.获得常用服务
        repositoryCommandService = smartEngine.getRepositoryCommandService();
        processCommandService = smartEngine.getProcessCommandService();
        executionQueryService = smartEngine.getExecutionQueryService();
        executionCommandService = smartEngine.getExecutionCommandService();
    }

    @Test
    public void testServiceTask() throws Exception {
        String tenantId = "-1";
        //1. 部署流程定义
        ProcessDefinitionSource processDefinitionSource = repositoryCommandService
                .deploy("process-def/extend/extend.bpmn20.xml",tenantId);

        ProcessDefinition processDefinition = processDefinitionSource.getProcessDefinitionList().get(0);
        Assert.assertNotNull(processDefinition);
        ExtensionElements extensionElements = processDefinition.getExtensionElements();
        Assert.assertNotNull(extensionElements);
        Assert.assertNotNull(extensionElements.getExtensionList());
        Assert.assertTrue(extensionElements.getExtensionList().size() != 0);
        for (ExtensionDecorator extensionDecorator : extensionElements.getExtensionList()) {
            if (extensionDecorator instanceof ProcessField) {
                ProcessField processField = ((ProcessField)extensionDecorator);
                if ("inputParams".equals(processField.getName())) {
                    Assert.assertEquals("[{\"key\":\"buyerOrderMessageCount\",\"type\":\"java.lang.Long\"}]", processField.getValue());
                } else if ("outputParams".equals(processField.getName())) {
                    Assert.assertEquals("[{\"key\":\"logisticsSpecialEventExtendDay\",\"type\":\"java.lang.Long\"}]", processField.getValue());
                }
            }
        }
        Assert.assertNotNull(processDefinition.getIdBasedElementMap());
        for (Map.Entry<String, IdBasedElement> entry : processDefinition.getIdBasedElementMap().entrySet()) {
            if (entry.getValue() instanceof BusinessRuleTask) {
                BusinessRuleTask businessRuleTask  = (BusinessRuleTask)entry.getValue();
                Assert.assertNotNull(businessRuleTask.getProperties().get("class"), "com.test.delegate.DecisionRuleTask");
                Assert.assertNotNull(businessRuleTask.getProperties().get("code"), "ruleTask");

                for (ExtensionDecorator extensionDecorator : extensionElements.getExtensionList()) {
                    if (extensionDecorator instanceof ProcessField) {
                        ProcessField processField = ((ProcessField)extensionDecorator);
                        if ("engineType".equals(processField.getName())) {
                            Assert.assertEquals("rule-expression", processField.getValue());
                        } if ("interval".equals(processField.getName())) {
                            Assert.assertEquals("1000", processField.getValue());
                        } else if ("outputParams".equals(processField.getName())) {
                            Assert.assertEquals("[{\"key\":\"logisticsSpecialEventExtendDay\",\"type\":\"java.lang"
                                + ".Long\"}]", processField.getValue());
                        }
                    }
                }
            }
        }
    }
}
