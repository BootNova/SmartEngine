package com.bootnova.smart.framework.engine.bpmn.behavior.event;

import com.bootnova.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.bootnova.smart.framework.engine.bpmn.assembly.event.StartEvent;
import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
import com.bootnova.smart.framework.engine.pvm.PvmActivity;
import com.bootnova.smart.framework.engine.pvm.event.EventConstant;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = StartEvent.class)

public class StartEventBehavior extends AbstractActivityBehavior<StartEvent> {


    public StartEventBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {

        fireEvent(context, pvmActivity, EventConstant.PROCESS_START);

        return super.enter(context, pvmActivity);
    }
}
