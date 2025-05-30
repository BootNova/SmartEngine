package com.bootnova.smart.framework.engine.persister.custom;

import java.util.Collection;
import java.util.List;

import com.bootnova.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.bootnova.smart.framework.engine.exception.EngineException;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
import com.bootnova.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.bootnova.smart.framework.engine.model.instance.ActivityInstance;
import com.bootnova.smart.framework.engine.model.instance.ProcessInstance;
import com.bootnova.smart.framework.engine.persister.custom.session.PersisterSession;

import static com.bootnova.smart.framework.engine.persister.common.constant.StorageConstant.NOT_IMPLEMENT_INTENTIONALLY;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  11:54.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ActivityInstanceStorage.class)
public class CustomActivityInstanceStorage implements ActivityInstanceStorage {


    @Override
    public void insert(ActivityInstance instance,
                       ProcessEngineConfiguration processEngineConfiguration) {
    }

    @Override
    public ActivityInstance update(ActivityInstance instance,
                                   ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public ActivityInstance find(String activityInstanceId,String tenantId,
                                 ProcessEngineConfiguration processEngineConfiguration) {
        Collection<ProcessInstance> processInstances = PersisterSession.currentSession().getProcessInstances().values();

        boolean matched = false;
        ActivityInstance matchedActivityInstance = null;

        for (ProcessInstance processInstance : processInstances) {
            List<ActivityInstance> activityInstances = processInstance.getActivityInstances();

            for (ActivityInstance activityInstance : activityInstances) {
                if (activityInstance.getInstanceId().equals(activityInstanceId)) {
                    matched= true;
                    matchedActivityInstance = activityInstance;
                    break;
                }
            }
            if(matched){
                break;
            }
        }


        return matchedActivityInstance;
    }

    @Override
    public ActivityInstance findWithShading(String processInstanceId, String activityInstanceId,String tenantId,
            ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }


    @Override
    public void remove(String instanceId,String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public List<ActivityInstance> findAll(String processInstanceId,String tenantId,
                                          ProcessEngineConfiguration processEngineConfiguration) {
        ProcessInstance processInstance= PersisterSession.currentSession().getProcessInstance(processInstanceId);
        return null == processInstance ? null : processInstance.getActivityInstances();
    }
}
