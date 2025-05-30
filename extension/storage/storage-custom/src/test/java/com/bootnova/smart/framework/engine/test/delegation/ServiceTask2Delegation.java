package com.bootnova.smart.framework.engine.test.delegation;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.delegation.TccDelegation;
import com.bootnova.smart.framework.engine.delegation.TccResult;
import com.bootnova.smart.framework.engine.model.instance.ActivityInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceTask2Delegation implements TccDelegation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTask2Delegation.class);

    private static final AtomicLong counter = new AtomicLong(1);

    public static Long getCounter() {
        return counter.get();
    }

    @Override
    public TccResult tryExecute(ExecutionContext executionContext) {
        List<ActivityInstance> activityInstances = executionContext.getProcessInstance().getActivityInstances();
        LOGGER.info("executing  RPC service " + executionContext.getRequest());
        counter.addAndGet(3);
        return TccResult.buildSucessfulResult(null);
    }

    @Override
    public TccResult confirmExecute(ExecutionContext executionContext) {
        return null;
    }

    @Override
    public TccResult cancelExecute(ExecutionContext executionContext) {
        return null;
    }

}
