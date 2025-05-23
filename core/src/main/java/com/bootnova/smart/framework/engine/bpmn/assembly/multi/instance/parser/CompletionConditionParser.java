package com.bootnova.smart.framework.engine.bpmn.assembly.multi.instance.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.bootnova.smart.framework.engine.bpmn.assembly.expression.ConditionExpressionImpl;
import com.bootnova.smart.framework.engine.bpmn.assembly.multi.instance.CompletionCondition;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
import com.bootnova.smart.framework.engine.model.assembly.ConditionExpression;
import com.bootnova.smart.framework.engine.xml.parser.AbstractElementParser;
import com.bootnova.smart.framework.engine.xml.parser.ParseContext;
import com.bootnova.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:26.
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = CompletionCondition.class)
public class CompletionConditionParser extends AbstractElementParser<CompletionCondition>
{


    @Override
    public Class<CompletionCondition> getModelType() {
        return CompletionCondition.class;
    }

    @Override
    public CompletionCondition parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException {
        CompletionCondition completionCondition = new CompletionCondition();

        String expressionType = XmlParseUtil.getString(reader, "group");
        String action = XmlParseUtil.getString(reader, "action");

        completionCondition.setAction(action);
        String content = reader.getElementText();
        if (null != content) {
            ConditionExpression expression = new ConditionExpressionImpl();
            expression.setExpressionContent(content);
            expression.setExpressionType(expressionType);
            completionCondition.setExpression(expression);
        }
        return completionCondition;
    }
}
