package com.bootnova.smart.framework.engine.bpmn.assembly.common;

import javax.xml.namespace.QName;

import com.bootnova.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.bootnova.smart.framework.engine.model.assembly.NoneIdBasedElement;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  10:02.
 */
public class Documentation implements NoneIdBasedElement {

    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "documentation");

    private static final long serialVersionUID = -2660788294142169268L;

}
