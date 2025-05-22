package com.bootnova.smart.framework.engine.test.process.delegation;

import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.delegation.JavaDelegation;
import com.bootnova.smart.framework.engine.test.process.VariableInstanceAndMultiInstanceTest;
import org.springframework.stereotype.Service;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@Service
public class UserTaskTestDelegation implements JavaDelegation {


    @Override
    public void execute(ExecutionContext executionContext) {
        String text= (String)executionContext.getRequest().get("text");

        VariableInstanceAndMultiInstanceTest.trace.add(text);
    }
}
