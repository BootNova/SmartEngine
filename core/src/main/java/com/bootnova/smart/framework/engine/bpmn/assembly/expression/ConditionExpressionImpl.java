package com.bootnova.smart.framework.engine.bpmn.assembly.expression;

import javax.xml.namespace.QName;

import com.bootnova.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.bootnova.smart.framework.engine.model.assembly.ConditionExpression;

import lombok.Data;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@Data
public class ConditionExpressionImpl implements ConditionExpression {


    private static final long serialVersionUID = -6152070683207905381L;
    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "conditionExpression");

    private String expressionType;
    private String expressionContent;
}
