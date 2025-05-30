package com.bootnova.smart.framework.engine.behavior.impl;

import com.bootnova.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.bootnova.smart.framework.engine.bpmn.assembly.task.ScriptTask;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = ScriptTask.class)
/**
 * @author zilong.jiangzl 2020-07-17
 */
public class ScriptTaskBehavior extends AbstractActivityBehavior<ScriptTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptTaskBehavior.class);

    public ScriptTaskBehavior() {
        super();
    }

}
