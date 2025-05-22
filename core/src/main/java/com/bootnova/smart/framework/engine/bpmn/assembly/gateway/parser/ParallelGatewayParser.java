package com.bootnova.smart.framework.engine.bpmn.assembly.gateway.parser;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import com.bootnova.smart.framework.engine.bpmn.assembly.gateway.ParallelGateway;
import com.bootnova.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
import com.bootnova.smart.framework.engine.xml.parser.ParseContext;
import com.bootnova.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ParallelGateway.class)

public class ParallelGatewayParser extends AbstractBpmnParser<ParallelGateway> {

    @Override
    public Class<ParallelGateway> getModelType() {
        return ParallelGateway.class;
    }

    @Override
    public ParallelGateway parseModel(XMLStreamReader reader, ParseContext context) {

        ParallelGateway parallelGateway = new ParallelGateway();
        parallelGateway.setId(XmlParseUtil.getString(reader, "id"));
        parallelGateway.setName(XmlParseUtil.getString(reader, "name"));


        Map<String, String> properties = super.parseExtendedProperties(reader,  context);
        parallelGateway.setProperties(properties);

        return parallelGateway;
    }

}
