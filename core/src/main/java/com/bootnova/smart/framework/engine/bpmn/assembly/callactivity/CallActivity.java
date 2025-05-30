package com.bootnova.smart.framework.engine.bpmn.assembly.callactivity;

import javax.xml.namespace.QName;

import com.bootnova.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.bootnova.smart.framework.engine.model.assembly.impl.AbstractActivity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author 高海军 帝奇 74394
 * @date 2017 May  14:30
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class CallActivity extends AbstractActivity {

    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "callActivity");

    private String calledElement;

    private String calledElementVersion;

}
