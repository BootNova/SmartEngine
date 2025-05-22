package com.bootnova.smart.framework.engine.configuration;


import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.pvm.PvmActivity;

import java.util.concurrent.Callable;

public  interface PvmActivityTask extends Callable<ExecutionContext> {


}
