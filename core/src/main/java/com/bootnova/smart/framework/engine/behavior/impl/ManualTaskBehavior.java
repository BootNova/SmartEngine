package com.bootnova.smart.framework.engine.behavior.impl;

import com.bootnova.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.bootnova.smart.framework.engine.bpmn.assembly.task.ManualTask;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = ManualTask.class)
/**
 * @author zilong.jiangzl 2020-07-17
 */
public class ManualTaskBehavior extends AbstractActivityBehavior<ManualTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManualTaskBehavior.class);

    public ManualTaskBehavior() {
        super();
    }

}
