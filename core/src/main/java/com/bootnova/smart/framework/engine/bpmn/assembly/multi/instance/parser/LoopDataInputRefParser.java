package com.bootnova.smart.framework.engine.bpmn.assembly.multi.instance.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.bootnova.smart.framework.engine.bpmn.assembly.multi.instance.LoopDataInputRef;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
import com.bootnova.smart.framework.engine.xml.parser.AbstractElementParser;
import com.bootnova.smart.framework.engine.xml.parser.ParseContext;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = LoopDataInputRef.class)

public class LoopDataInputRefParser extends AbstractElementParser<LoopDataInputRef>
      {


    @Override
    public Class<LoopDataInputRef> getModelType() {
        return LoopDataInputRef.class;
    }

    @Override
    public LoopDataInputRef parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException {
        LoopDataInputRef loopDataInputRef = new LoopDataInputRef();
        loopDataInputRef.setReference(reader.getElementText());
        return loopDataInputRef;
    }
}
