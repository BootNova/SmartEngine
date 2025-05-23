//package com.bootnova.smart.framework.engine.retry;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.bootnova.smart.framework.engine.SmartEngine;
//import com.bootnova.smart.framework.engine.configuration.ProcessEngineConfiguration;
//import com.bootnova.smart.framework.engine.configuration.impl.DefaultIdGenerator;
//import com.bootnova.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
//import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
//import com.bootnova.smart.framework.engine.configuration.impl.DefaultSmartEngine;
//import com.bootnova.smart.framework.engine.model.assembly.ProcessDefinition;
//import com.bootnova.smart.framework.engine.model.instance.ExecutionInstance;
//import com.bootnova.smart.framework.engine.model.instance.ProcessInstance;
//import com.bootnova.smart.framework.engine.persister.custom.RetryRecordInstance;
//import com.bootnova.smart.framework.engine.retry.model.instance.RetryRecord;
//import com.bootnova.smart.framework.engine.retry.service.command.RetryService;
//import com.bootnova.smart.framework.engine.service.command.ExecutionCommandService;
//import com.bootnova.smart.framework.engine.service.command.ProcessCommandService;
//import com.bootnova.smart.framework.engine.service.command.RepositoryCommandService;
//import com.bootnova.smart.framework.engine.service.query.ExecutionQueryService;
//
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//
//public class RetryTest {
//
//    @After
//    public void clear() {
//    }
//
//    @Test
//    public void test() throws Exception {
//
//        //1.初始化
//        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
//        processEngineConfiguration.setIdGenerator(new DefaultIdGenerator());
//
//        SmartEngine smartEngine = new DefaultSmartEngine();
//        smartEngine.init(processEngineConfiguration);
//
//        //2.获得常用服务
//        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
//        ExecutionQueryService executionQueryService = smartEngine.getExecutionQueryService();
//        ExecutionCommandService executionCommandService = smartEngine.getExecutionCommandService();
//
//        RetryService retryService = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON,RetryService.class);
//
//        //3. 部署流程定义
//        RepositoryCommandService repositoryCommandService = smartEngine.getRepositoryCommandService();
//        ProcessDefinition processDefinition = repositoryCommandService.deploy("retry-diagram.bpmn.xml").getFirstProcessDefinition();
//        Assert.assertEquals(11, processDefinition.getBaseElementList().size());
//
//        //4.启动流程实例
//        Map<String, Object> request = new HashMap<String, Object>();
//        request.put("smartEngineAction", "createActivity");
//
//        ProcessInstance processInstance = processCommandService.start(
//            processDefinition.getId(), processDefinition.getVersion(), request
//        );
//        Assert.assertNotNull(processInstance);
//
//        persisteAndUpdateThreadLocal(processInstance);
//
//        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(
//            processInstance.getInstanceId());
//        assertEquals(1, executionInstanceList.size());
//        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);
//
//        RetryRecord retryRecord = new RetryRecordInstance();
//        try {
//            processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), null);
//        } catch (Exception e) {
//            // 保存重试信息
//            retryRecord.setInstanceId(firstExecutionInstance.getInstanceId());
//            retryRecord.setRequestParams(request);
//            retryService.save(retryRecord);
//        }
//
//        // 模拟通过metaQ接收消息，执行重试
//        RetryListener retryListener = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON,RetryListener.class);
//        retryListener.onMessage(retryRecord);
//
//        // 校验流程是否正确流转到报名环节
//        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
//        assertEquals(1, executionInstanceList.size());
//        //ExecutionInstance enrollInstance = executionInstanceList.get(0);
//        //assertEquals("enroll", enrollInstance.getProcessDefinitionActivityId());
//    }
//
//    private void persisteAndUpdateThreadLocal(ProcessInstance processInstance) {
//    }
//
//}