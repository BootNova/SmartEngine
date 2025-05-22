package com.bootnova.smart.framework.engine.configuration;

import com.bootnova.smart.framework.engine.configuration.impl.option.DisabledOption;
import com.bootnova.smart.framework.engine.configuration.impl.option.ExpressionCompileResultCachedOption;
import com.bootnova.smart.framework.engine.configuration.impl.option.ProcessDefinitionMultiTenantShareOption;
import com.bootnova.smart.framework.engine.configuration.impl.option.ServiceOrchestrationEnabledOption;
import com.bootnova.smart.framework.engine.configuration.impl.option.TransferEnabledOption;
import com.bootnova.smart.framework.engine.configuration.impl.option.*;

/**
 * Created by 高海军 帝奇 74394 on  2020-02-15 21:40.
 */
public interface ConfigurationOption {

    ConfigurationOption DISABLED_OPTION = new DisabledOption();


    ConfigurationOption TRANSFER_ENABLED_OPTION = new TransferEnabledOption();

    ConfigurationOption EXPRESSION_COMPILE_RESULT_CACHED_OPTION = new ExpressionCompileResultCachedOption();

    ConfigurationOption SERVICE_ORCHESTRATION_OPTION = new ServiceOrchestrationEnabledOption();
    ConfigurationOption PROCESS_DEFINITION_MULTI_TENANT_SHARE_OPTION = new ProcessDefinitionMultiTenantShareOption(true);

    ConfigurationOption EAGER_FLUSH_ENABLED_OPTION = new EagerFlushEnabledOption();

    boolean isEnabled();

    String getId();

    Object getData();

}