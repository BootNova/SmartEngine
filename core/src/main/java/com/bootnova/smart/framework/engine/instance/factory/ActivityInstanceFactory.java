package com.bootnova.smart.framework.engine.instance.factory;

import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.model.assembly.Activity;
import com.bootnova.smart.framework.engine.model.instance.ActivityInstance;

/**
 * 活动实例工厂 Created by ettear on 16-4-20.
 */
public interface ActivityInstanceFactory {

    ActivityInstance create(Activity activity, ExecutionContext context);


}
