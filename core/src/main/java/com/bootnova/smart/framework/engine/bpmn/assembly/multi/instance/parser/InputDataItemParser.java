package com.bootnova.smart.framework.engine.bpmn.assembly.multi.instance.parser;

import javax.xml.stream.XMLStreamReader;

import com.bootnova.smart.framework.engine.bpmn.assembly.multi.instance.InputDataItem;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
import com.bootnova.smart.framework.engine.xml.parser.AbstractElementParser;
import com.bootnova.smart.framework.engine.xml.parser.ParseContext;
import com.bootnova.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = InputDataItem.class)

public class InputDataItemParser extends AbstractElementParser<InputDataItem>
      {


    @Override
    public Class<InputDataItem> getModelType() {
        return InputDataItem.class;
    }

    @Override
    protected InputDataItem parseModel(XMLStreamReader reader, ParseContext context) {
        InputDataItem inputDataItem = new InputDataItem();
        inputDataItem.setName(XmlParseUtil.getString(reader, "name"));
        return inputDataItem;
    }
}
