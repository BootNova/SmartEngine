package com.bootnova.smart.framework.engine.test.parallelgateway.delegation;

import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.delegation.JavaDelegation;

import java.util.concurrent.atomic.AtomicLong;

public class ParentServiceTaskDelegation implements JavaDelegation {

    @Override
    public void execute(ExecutionContext executionContext) {
        ChildServiceTaskDelegation.counter.addAndGet(3);
        System.out.println("ParentServiceTaskDelegation");

    }



}
