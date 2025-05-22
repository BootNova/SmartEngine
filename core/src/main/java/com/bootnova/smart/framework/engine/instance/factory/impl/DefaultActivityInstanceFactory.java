package com.bootnova.smart.framework.engine.instance.factory.impl;

import com.bootnova.smart.framework.engine.common.util.DateUtil;
import com.bootnova.smart.framework.engine.configuration.IdGenerator;
import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
import com.bootnova.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.bootnova.smart.framework.engine.instance.impl.DefaultActivityInstance;
import com.bootnova.smart.framework.engine.model.assembly.Activity;
import com.bootnova.smart.framework.engine.model.instance.ActivityInstance;

/**
 * 默认活动实例工厂实现 Created by ettear on 16-4-20.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ActivityInstanceFactory.class)

public class DefaultActivityInstanceFactory implements ActivityInstanceFactory {


    @Override
    public ActivityInstance create(Activity activity, ExecutionContext context) {
        DefaultActivityInstance activityInstance = new DefaultActivityInstance();

        IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();
        idGenerator.generate(activityInstance);
        activityInstance.setStartTime(DateUtil.getCurrentDate());
        activityInstance.setProcessInstanceId(context.getProcessInstance().getInstanceId());
        activityInstance.setProcessDefinitionIdAndVersion(context.getProcessInstance().getProcessDefinitionIdAndVersion());
        String activityId = activity.getId();
        activityInstance.setProcessDefinitionActivityId(activityId);
        activityInstance.setTenantId(context.getTenantId());
        return activityInstance;
    }
}