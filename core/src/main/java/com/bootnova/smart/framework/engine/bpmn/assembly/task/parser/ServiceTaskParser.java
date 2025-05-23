package com.bootnova.smart.framework.engine.bpmn.assembly.task.parser;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import com.bootnova.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.bootnova.smart.framework.engine.bpmn.assembly.task.ServiceTask;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
import com.bootnova.smart.framework.engine.model.assembly.BaseElement;
import com.bootnova.smart.framework.engine.xml.parser.ParseContext;
import com.bootnova.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ServiceTask.class)

public class ServiceTaskParser extends AbstractBpmnParser<ServiceTask> {

    @Override
    public Class<ServiceTask> getModelType() {
        return ServiceTask.class;
    }

    @Override
    public ServiceTask parseModel(XMLStreamReader reader, ParseContext context) {
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(XmlParseUtil.getString(reader, "id"));
        serviceTask.setName(XmlParseUtil.getString(reader, "name"));


        Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);
        serviceTask.setProperties(userTaskProperties);

        return serviceTask;
    }

    @Override
    protected boolean parseModelChild(ServiceTask model, BaseElement child) {

        return false;
    }

}
