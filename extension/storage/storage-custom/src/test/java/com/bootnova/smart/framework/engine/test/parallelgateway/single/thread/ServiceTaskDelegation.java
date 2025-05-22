package com.bootnova.smart.framework.engine.test.parallelgateway.single.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.delegation.JavaDelegation;
import com.bootnova.smart.framework.engine.delegation.TccDelegation;
import com.bootnova.smart.framework.engine.delegation.TccResult;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceTaskDelegation implements JavaDelegation {


    @Getter
    private static CopyOnWriteArrayList<String> arrayList = new CopyOnWriteArrayList<String>();

    @Override
    public void execute(ExecutionContext executionContext) {
        Map<String, Object> request = executionContext.getRequest();
        if (null != request) {
            Object o = request.get("input");
            if (o != null) {
                String input = o.toString();
                arrayList.add(input);
            }
        }

    }


}
