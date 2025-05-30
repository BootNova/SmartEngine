package com.bootnova.smart.framework.engine;

import com.bootnova.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.bootnova.smart.framework.engine.service.command.DeploymentCommandService;
import com.bootnova.smart.framework.engine.service.command.ExecutionCommandService;
import com.bootnova.smart.framework.engine.service.command.ProcessCommandService;
import com.bootnova.smart.framework.engine.service.command.RepositoryCommandService;
import com.bootnova.smart.framework.engine.service.command.TaskCommandService;
import com.bootnova.smart.framework.engine.service.command.VariableCommandService;
import com.bootnova.smart.framework.engine.service.query.ActivityQueryService;
import com.bootnova.smart.framework.engine.service.query.DeploymentQueryService;
import com.bootnova.smart.framework.engine.service.query.ExecutionQueryService;
import com.bootnova.smart.framework.engine.service.query.ProcessQueryService;
import com.bootnova.smart.framework.engine.service.query.RepositoryQueryService;
import com.bootnova.smart.framework.engine.service.query.TaskAssigneeQueryService;
import com.bootnova.smart.framework.engine.service.query.TaskQueryService;
import com.bootnova.smart.framework.engine.service.query.VariableQueryService;

/**
 * @author 高海军 帝奇
 * 业务处理引擎和服务编排引擎，此类是各种 API 的顶层入口。
 *
 * 下面的 API 方法名称比较简单，不需要额外说明 。 详细注释见各个方法的返回值对象说明。
 *
 */
public interface SmartEngine {


    ProcessEngineConfiguration getProcessEngineConfiguration();

    void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration);

    RepositoryCommandService getRepositoryCommandService();

    RepositoryQueryService getRepositoryQueryService();

    DeploymentCommandService getDeploymentCommandService();

    DeploymentQueryService getDeploymentQueryService();

    ProcessCommandService getProcessCommandService();

    ProcessQueryService getProcessQueryService();

    ActivityQueryService getActivityQueryService();

    ExecutionCommandService getExecutionCommandService();

    TaskCommandService getTaskCommandService();

    VariableCommandService getVariableCommandService();


    ExecutionQueryService getExecutionQueryService();

    TaskQueryService getTaskQueryService();

    VariableQueryService getVariableQueryService();

    TaskAssigneeQueryService getTaskAssigneeQueryService();


    void init(ProcessEngineConfiguration processEngineConfiguration);

    void destroy();


}
