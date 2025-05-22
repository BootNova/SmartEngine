package com.bootnova.smart.framework.engine.test;

import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.listener.Listener;
import com.bootnova.smart.framework.engine.pvm.event.EventConstant;
import com.bootnova.smart.framework.engine.test.cases.extensions.MultiValueAndHelloListenerTest;

public class HelloListener implements Listener {
    @Override
    public void execute(EventConstant event,
                        ExecutionContext executionContext) {
        String text = (String)executionContext.getRequest().get("hello");
        MultiValueAndHelloListenerTest.trace.add(text);
    }
}
