package com.bootnova.smart.framework.engine.bpmn.assembly.multi.instance.parser;

import javax.xml.stream.XMLStreamReader;

import com.bootnova.smart.framework.engine.bpmn.assembly.multi.instance.LoopCardinality;
import com.bootnova.smart.framework.engine.exception.EngineException;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
import com.bootnova.smart.framework.engine.xml.parser.AbstractElementParser;
import com.bootnova.smart.framework.engine.xml.parser.ParseContext;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = LoopCardinality.class)

public class LoopCardinalityParser extends AbstractElementParser<LoopCardinality>
      {


    @Override
    public Class<LoopCardinality> getModelType() {
        return LoopCardinality.class;
    }

    @Override
    public LoopCardinality parseElement(XMLStreamReader reader, ParseContext context) {

        throw new EngineException("Not supported");

    }
}
