package com.bootnova.smart.framework.engine.instance.storage;

import java.util.List;

import com.bootnova.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.bootnova.smart.framework.engine.model.instance.DeploymentInstance;
import com.bootnova.smart.framework.engine.service.param.query.DeploymentInstanceQueryParam;

/**
 * Created by yueyu.yr on 2017/9/22.
 *
 * @author yueyu.yr
 * @date 2017/09/22
 */
public interface DeploymentInstanceStorage {

    DeploymentInstance insert(DeploymentInstance deploymentInstance,
                              ProcessEngineConfiguration processEngineConfiguration);

    DeploymentInstance update(DeploymentInstance deploymentInstance,
                              ProcessEngineConfiguration processEngineConfiguration);

    DeploymentInstance findById(String id,String tenantId,
                                ProcessEngineConfiguration processEngineConfiguration);

    List<DeploymentInstance> findByPage(DeploymentInstanceQueryParam deploymentInstanceQueryParam,
                                        ProcessEngineConfiguration processEngineConfiguration);

    int count(DeploymentInstanceQueryParam deploymentInstanceQueryParam,
              ProcessEngineConfiguration processEngineConfiguration);

    void remove(String id,String tenantId,
                ProcessEngineConfiguration processEngineConfiguration);

}
