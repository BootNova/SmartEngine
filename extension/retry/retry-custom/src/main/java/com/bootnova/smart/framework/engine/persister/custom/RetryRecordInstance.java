package com.bootnova.smart.framework.engine.persister.custom;

import java.util.Map;

import com.bootnova.smart.framework.engine.retry.model.instance.RetryRecord;

import lombok.Data;

/**
 * @author zhenhong.tzh
 * @date 2019-04-30
 */
@Data
public class RetryRecordInstance implements RetryRecord {
    private String instanceId;
    private String tenantId;
    private int retryTimes;
    private boolean retrySuccess;
    private Map<String, Object> requestParams;
}
