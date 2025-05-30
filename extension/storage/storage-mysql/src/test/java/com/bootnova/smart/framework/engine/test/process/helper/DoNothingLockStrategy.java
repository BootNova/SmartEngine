package com.bootnova.smart.framework.engine.test.process.helper;

import com.bootnova.smart.framework.engine.configuration.LockStrategy;
import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.exception.LockException;

import org.springframework.stereotype.Service;

/**
 * Created by 高海军 帝奇 74394 on 2017 January  18:03.
 */
@Service
public class DoNothingLockStrategy implements LockStrategy {


    @Override
    public void tryLock(String processInstanceId, ExecutionContext context) throws LockException {
        //ExtensionPointRegistry extensionPointRegistry = smartEngine.getProcessEngineConfiguration()
        //    .getExtensionPointRegistry();
        //PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        //
        //ProcessInstanceStorage processInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);
        //
        //processInstanceStorage.update()

        //ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("processInstanceDAO");
        //
        //processInstanceDAO.tryLock(Long.valueOf(processInstanceId));

        //可以是设置 db uniqueKey 唯一索引； 或者在插入后直接再锁上； 或者使用其他中间件。

        //String processDefinitionActivityId = context.getExecutionInstance().getProcessDefinitionActivityId();
        //
        //ProcessDefinition processDefinition = context.getProcessDefinition();
        //
        //IdBasedElement idBasedElement = processDefinition.getIdBasedElementMap().get(processDefinitionActivityId);



    }

    @Override
    public void unLock(String processInstanceId, ExecutionContext context) throws LockException {
        //do nothing
    }


}
