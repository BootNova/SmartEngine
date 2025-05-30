package com.bootnova.smart.framework.engine.instance.storage;

import java.util.List;

import com.bootnova.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.bootnova.smart.framework.engine.configuration.VariablePersister;
import com.bootnova.smart.framework.engine.model.instance.VariableInstance;

public interface VariableInstanceStorage {

    void insert(VariablePersister variablePersister, VariableInstance variableInstance,
                ProcessEngineConfiguration processEngineConfiguration);

    List<VariableInstance> findList(String processInstanceId, String executionInstanceId,
                                    VariablePersister variablePersister,String tenantId,
                                    ProcessEngineConfiguration processEngineConfiguration);

//    List<VariableInstance> findAll(String processInstanceId,VariablePersister variablePersister,
//                                    ProcessEngineConfiguration processEngineConfiguration);


}
