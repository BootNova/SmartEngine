package com.bootnova.smart.framework.engine.bpmn.assembly.multi.instance;

import javax.xml.namespace.QName;

import com.bootnova.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.bootnova.smart.framework.engine.model.assembly.ConditionExpression;
import com.bootnova.smart.framework.engine.model.assembly.NoneIdBasedElement;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class LoopCardinality implements NoneIdBasedElement {
    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "loopCardinality");

    private ConditionExpression cardinalityExpression;

    public ConditionExpression getCardinalityExpression() {
        return cardinalityExpression;
    }

    public void setCardinalityExpression(
        ConditionExpression cardinalityExpression) {
        this.cardinalityExpression = cardinalityExpression;
    }
}
