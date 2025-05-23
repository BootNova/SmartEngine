package com.bootnova.smart.framework.engine.bpmn.assembly.multi.instance;

import javax.xml.namespace.QName;

import com.bootnova.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.bootnova.smart.framework.engine.model.assembly.ConditionExpression;
import com.bootnova.smart.framework.engine.model.assembly.NoneIdBasedElement;

import lombok.Data;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:27.
 */

@Data
public class CompletionCondition implements NoneIdBasedElement {
    public final static String ACTION_ABORT="abort";
    public final static String ACTION_CONTINUE="continue";

    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "completionCondition");
    private String action;
    private ConditionExpression expression;
}
