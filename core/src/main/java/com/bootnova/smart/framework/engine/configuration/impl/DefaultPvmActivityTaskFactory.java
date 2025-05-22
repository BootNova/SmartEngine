package com.bootnova.smart.framework.engine.configuration.impl;


import com.bootnova.smart.framework.engine.configuration.PvmActivityTask;
import com.bootnova.smart.framework.engine.configuration.PvmActivityTaskFactory;

public  class DefaultPvmActivityTaskFactory implements PvmActivityTaskFactory {

    public PvmActivityTask create(Object... args){
        return new DefaultPvmActivityTask(args);
    }

}
