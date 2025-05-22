package com.bootnova.smart.framework.engine.test.delegation.event;

import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.listener.Listener;
import com.bootnova.smart.framework.engine.pvm.event.EventConstant;
import com.bootnova.smart.framework.engine.test.cases.CamundaEventTest;

public class ProcessEndListener  implements Listener {
    @Override
    public void execute(EventConstant event,
                        ExecutionContext executionContext) {
        CamundaEventTest.container.add("ProcessEndListener");

    }
}