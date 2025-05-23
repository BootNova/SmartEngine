package com.bootnova.smart.framework.engine.bpmn.assembly.artifacts;

import javax.xml.namespace.QName;

import com.bootnova.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.bootnova.smart.framework.engine.model.assembly.NoneIdBasedElement;

import lombok.Data;

/**
 * @author guoxing 2020年12月14日13:47:49
 */
@Data
public class Category implements NoneIdBasedElement {

    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE,
            "category");
    private static final long serialVersionUID = 6357539757300185621L;

}
