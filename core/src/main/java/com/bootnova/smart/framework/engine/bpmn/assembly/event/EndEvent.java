package com.bootnova.smart.framework.engine.bpmn.assembly.event;

import javax.xml.namespace.QName;

import com.bootnova.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:51:07 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EndEvent extends AbstractEvent {

    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "endEvent");

    private static final long serialVersionUID = 3529827842581828898L;

    private String className;
}
