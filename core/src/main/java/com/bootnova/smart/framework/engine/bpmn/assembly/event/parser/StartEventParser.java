package com.bootnova.smart.framework.engine.bpmn.assembly.event.parser;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import com.bootnova.smart.framework.engine.bpmn.assembly.event.StartEvent;
import com.bootnova.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
import com.bootnova.smart.framework.engine.xml.parser.ParseContext;
import com.bootnova.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = StartEvent.class)

public class StartEventParser extends AbstractBpmnParser<StartEvent> {

    @Override
    public Class<StartEvent> getModelType() {
        return StartEvent.class;
    }

    @Override
    public StartEvent parseModel(XMLStreamReader reader, ParseContext context) {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(XmlParseUtil.getString(reader, "id"));
        startEvent.setStartActivity(true);

        Map<String, String> properties = super.parseExtendedProperties(reader,  context);
        startEvent.setProperties(properties);
        return startEvent;
    }

}
