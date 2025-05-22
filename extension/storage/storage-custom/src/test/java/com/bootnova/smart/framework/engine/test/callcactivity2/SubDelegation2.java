package com.bootnova.smart.framework.engine.test.callcactivity2;

import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.delegation.JavaDelegation;


public class SubDelegation2 implements JavaDelegation {

    @Override
    public void execute(ExecutionContext executionContext) {
        System.out.println("==> SubDelegation2");
    }
}
