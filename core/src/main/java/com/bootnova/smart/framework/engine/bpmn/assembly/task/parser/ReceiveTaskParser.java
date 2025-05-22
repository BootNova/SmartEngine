package com.bootnova.smart.framework.engine.bpmn.assembly.task.parser;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import com.bootnova.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.bootnova.smart.framework.engine.bpmn.assembly.task.ReceiveTask;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
import com.bootnova.smart.framework.engine.xml.parser.ParseContext;
import com.bootnova.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ReceiveTask.class)

public class ReceiveTaskParser extends AbstractBpmnParser<ReceiveTask> {

    @Override
    public Class<ReceiveTask> getModelType() {
        return ReceiveTask.class;
    }

    @Override
    public ReceiveTask parseModel(XMLStreamReader reader, ParseContext context) {
        ReceiveTask receiveTask = new ReceiveTask();
        receiveTask.setId(XmlParseUtil.getString(reader, "id"));
        receiveTask.setName(XmlParseUtil.getString(reader, "name"));


        Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);
        receiveTask.setProperties(userTaskProperties);

        return receiveTask;
    }
}
