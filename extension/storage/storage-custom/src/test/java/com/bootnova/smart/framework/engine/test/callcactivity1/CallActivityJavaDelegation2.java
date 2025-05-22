package com.bootnova.smart.framework.engine.test.callcactivity1;

import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.delegation.JavaDelegation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallActivityJavaDelegation2 implements JavaDelegation {
    Logger logger = LoggerFactory.getLogger(CallActivityJavaDelegation2.class);
    @Override
    public void execute(ExecutionContext executionContext) {
        logger.info("==> test 222");
    }
}
