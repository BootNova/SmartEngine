package com.bootnova.smart.framework.engine.test.process.delegation;

import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.delegation.TccDelegation;
import com.bootnova.smart.framework.engine.delegation.TccResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ThrowExceptionTaskDelegation implements TccDelegation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThrowExceptionTaskDelegation.class);

    @Override
    public TccResult tryExecute(ExecutionContext executionContext) {

       throw new RuntimeException("test exception");

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
