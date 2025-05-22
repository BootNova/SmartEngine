package com.bootnova.smart.framework.engine.instance.storage;

import java.util.List;
import java.util.Map;

import com.bootnova.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.bootnova.smart.framework.engine.model.instance.ExecutionInstance;
import com.bootnova.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.bootnova.smart.framework.engine.service.param.query.PendingTaskQueryParam;

public interface TaskAssigneeStorage {

    List<TaskAssigneeInstance> findList(String taskInstanceId,String tenantId,
                                        ProcessEngineConfiguration processEngineConfiguration);

    Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList,String tenantId,
                                                                       ProcessEngineConfiguration processEngineConfiguration) ;


    List<TaskAssigneeInstance> findPendingTaskAssigneeList(PendingTaskQueryParam pendingTaskQueryParam,
                                                           ProcessEngineConfiguration processEngineConfiguration);

    Long countPendingTaskAssigneeList(PendingTaskQueryParam pendingTaskQueryParam,
                                      ProcessEngineConfiguration processEngineConfiguration);

    TaskAssigneeInstance insert(TaskAssigneeInstance taskAssigneeInstance,
                                ProcessEngineConfiguration processEngineConfiguration);

    TaskAssigneeInstance update(String taskAssigneeInstanceId, String assigneeId,String tenantId,
                                ProcessEngineConfiguration processEngineConfiguration);

    TaskAssigneeInstance findOne(String taskAssigneeId,String tenantId,
                                 ProcessEngineConfiguration processEngineConfiguration);

    void remove(String taskAssigneeInstanceId,String tenantId,
                ProcessEngineConfiguration processEngineConfiguration);

    void removeAll(String taskInstanceId,String tenantId,
                   ProcessEngineConfiguration processEngineConfiguration);

}
