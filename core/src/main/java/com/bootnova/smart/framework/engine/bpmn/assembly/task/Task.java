package com.bootnova.smart.framework.engine.bpmn.assembly.task;

import com.bootnova.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.bootnova.smart.framework.engine.model.assembly.impl.AbstractTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Task extends AbstractTask {

    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "task");
    private static final long serialVersionUID = 2900871220232200586L;



    @Override
    public String toString() {
        return super.getId();
    }

}
