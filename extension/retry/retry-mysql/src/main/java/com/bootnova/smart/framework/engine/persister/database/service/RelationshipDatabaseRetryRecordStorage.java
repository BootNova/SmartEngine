package com.bootnova.smart.framework.engine.persister.database.service;

import com.bootnova.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
import com.bootnova.smart.framework.engine.persister.database.dao.RetryRecordDAO;
import com.bootnova.smart.framework.engine.persister.database.entity.RetryRecordEntity;
import com.bootnova.smart.framework.engine.retry.instance.storage.RetryRecordStorage;
import com.bootnova.smart.framework.engine.retry.model.instance.DefaultRetryRecordInstance;
import com.bootnova.smart.framework.engine.retry.model.instance.RetryRecord;
import com.bootnova.smart.framework.engine.retry.service.command.RetryPersistence;

/**
 * @author zhenhong.tzh
 * @date 2019-04-27
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = RetryRecordStorage.class)
public class RelationshipDatabaseRetryRecordStorage implements RetryRecordStorage {

    @Override
    public RetryRecord find(String instanceId, RetryPersistence retryPersistence, ProcessEngineConfiguration processEngineConfiguration) {
        RetryRecordDAO retryRecordDAO = (RetryRecordDAO)processEngineConfiguration.getInstanceAccessor().access("retryRecordDAO");
        RetryRecordEntity retryRecordEntity = retryRecordDAO.queryByInstanceId(instanceId);
        if (retryRecordEntity == null) {
            return null;
        }
        RetryRecord retryRecord = new DefaultRetryRecordInstance();
        retryRecord.setInstanceId(instanceId);
        retryRecord.setRetrySuccess(retryRecordEntity.isRetrySuccess());
        retryRecord.setRetryTimes(retryRecordEntity.getRetryTimes());
        retryRecord.setRequestParams(retryPersistence.deserialize(retryRecordEntity.getRequestParams()));
        return retryRecord;
    }

    @Override
    public boolean insert(RetryRecord retryRecord, RetryPersistence retryPersistence, ProcessEngineConfiguration processEngineConfiguration) {
        RetryRecordDAO retryRecordDAO = (RetryRecordDAO)processEngineConfiguration.getInstanceAccessor().access("retryRecordDAO");

        RetryRecordEntity retryRecordEntity = getRetryRecordEntity(retryRecord, retryPersistence);
        long count = retryRecordDAO.insert(retryRecordEntity);
        return count > 0;
    }

    @Override
    public boolean update(RetryRecord retryRecord, RetryPersistence retryPersistence, ProcessEngineConfiguration processEngineConfiguration) {
        RetryRecordDAO retryRecordDAO = (RetryRecordDAO)processEngineConfiguration.getInstanceAccessor().access("retryRecordDAO");

        RetryRecordEntity retryRecordEntity = getRetryRecordEntity(retryRecord, retryPersistence);
        long count = retryRecordDAO.update(retryRecordEntity);
        return count > 0;
    }

    private RetryRecordEntity getRetryRecordEntity(RetryRecord retryRecord, RetryPersistence retryPersistence) {
        RetryRecordEntity retryRecordEntity = new RetryRecordEntity();
        retryRecordEntity.setInstanceId(retryRecord.getInstanceId());
        retryRecordEntity.setRetryTimes(retryRecord.getRetryTimes());
        retryRecordEntity.setRetrySuccess(retryRecord.isRetrySuccess());
        retryRecordEntity.setRequestParams(retryPersistence.serialize(retryRecord.getRequestParams()));
        return retryRecordEntity;
    }
}
