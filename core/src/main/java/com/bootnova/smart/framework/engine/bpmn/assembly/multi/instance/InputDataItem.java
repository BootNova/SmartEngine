package com.bootnova.smart.framework.engine.bpmn.assembly.multi.instance;

import javax.xml.namespace.QName;

import com.bootnova.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.bootnova.smart.framework.engine.model.assembly.NoneIdBasedElement;

/**
 * @author ettear
 * Created by ettear on 16/10/2017.
 */
public class InputDataItem implements NoneIdBasedElement {
    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "inputDataItem");

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
