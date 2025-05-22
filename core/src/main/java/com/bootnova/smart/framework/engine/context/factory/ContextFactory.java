package com.bootnova.smart.framework.engine.context.factory;

import java.util.Map;

import com.bootnova.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.model.assembly.ProcessDefinition;
import com.bootnova.smart.framework.engine.model.instance.ActivityInstance;
import com.bootnova.smart.framework.engine.model.instance.ExecutionInstance;
import com.bootnova.smart.framework.engine.model.instance.ProcessInstance;

/**
 * 实例上下文工厂 Created by ettear on 16-4-20.
 */
public interface ContextFactory {

    ExecutionContext createSignalContext(Map<String, Object> request,
                                         ProcessEngineConfiguration processEngineConfiguration,
                                         ExecutionInstance executionInstance,
                                         ActivityInstance activityInstance,
                                         ProcessInstance processInstance,
                                         ProcessDefinition processDefinition);


    ExecutionContext createGatewayContext(ExecutionContext parentContext);


    ExecutionContext createProcessContext(ProcessEngineConfiguration processEngineConfiguration,
                                          ProcessInstance processInstance, Map<String, Object> request,
                                          Map<String, Object> response, ExecutionContext mayBeNullParentContext);
}
