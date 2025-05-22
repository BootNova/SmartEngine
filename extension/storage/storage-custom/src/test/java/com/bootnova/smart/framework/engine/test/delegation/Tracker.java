package com.bootnova.smart.framework.engine.test.delegation;

import java.util.Map;

import com.bootnova.smart.framework.engine.constant.ExtensionElementsConstant;
import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.delegation.JavaDelegation;
import com.bootnova.smart.framework.engine.model.assembly.ExtensionElementContainer;
import com.bootnova.smart.framework.engine.model.assembly.ExtensionElements;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class Tracker implements JavaDelegation {
    @Override
    public void execute(ExecutionContext executionContext) {

        String processDefinitionActivityId =  executionContext.getExecutionInstance().getProcessDefinitionActivityId();

        ExtensionElementContainer idBasedElement = (ExtensionElementContainer)executionContext.getProcessDefinition().getIdBasedElementMap().get(
            processDefinitionActivityId);

        ExtensionElements extensionElements = idBasedElement.getExtensionElements();
        if(null != extensionElements){

            Map map = (Map)extensionElements.getDecorationMap().get(
                ExtensionElementsConstant.PROPERTIES);

            executionContext.getResponse().putAll(map);

        }

    }
}
