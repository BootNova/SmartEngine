package com.bootnova.smart.framework.engine.instance.factory;

import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.model.assembly.Activity;
import com.bootnova.smart.framework.engine.model.instance.ExecutionInstance;
import com.bootnova.smart.framework.engine.model.instance.TaskInstance;

/**
 * 任务实例工厂 Created by ettear on 16-4-20.
 */
public interface TaskInstanceFactory {

    /**
     * 创建任务实例
     *
     * @return 任务实例
     */
    TaskInstance create(Activity activity, ExecutionInstance executionInstance, ExecutionContext context);
}
