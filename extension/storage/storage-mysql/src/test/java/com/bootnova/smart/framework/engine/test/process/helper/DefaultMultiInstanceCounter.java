package com.bootnova.smart.framework.engine.test.process.helper;

import java.util.ArrayList;
import java.util.List;

import com.bootnova.smart.framework.engine.SmartEngine;
import com.bootnova.smart.framework.engine.configuration.MultiInstanceCounter;
import com.bootnova.smart.framework.engine.constant.AdHocConstant;
import com.bootnova.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.bootnova.smart.framework.engine.service.query.TaskQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2017 January  18:03.
 */
public class DefaultMultiInstanceCounter implements MultiInstanceCounter {


    @Override
    public Integer countPassedTaskInstanceNumber(String processInstanceId, String activityInstanceId,
                                                 SmartEngine smartEngine) {
      TaskQueryService taskQueryService = smartEngine.getTaskQueryService();

        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        List<String> processInstanceIdList = new ArrayList<String>(2);
        processInstanceIdList.add(processInstanceId);
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
        taskInstanceQueryParam.setActivityInstanceId(activityInstanceId);

        taskInstanceQueryParam.setTag(AdHocConstant.AGREE);
        Integer count  = taskQueryService.count(taskInstanceQueryParam).intValue();
        return  count;

    }

    @Override
    public Integer countRejectedTaskInstanceNumber(String processInstanceId, String activityInstanceId,
                                                   SmartEngine smartEngine) {
        TaskQueryService taskQueryService = smartEngine.getTaskQueryService();

        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        List<String> processInstanceIdList = new ArrayList<String>(2);
        processInstanceIdList.add(processInstanceId);
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
        taskInstanceQueryParam.setActivityInstanceId(activityInstanceId);
        taskInstanceQueryParam.setTag(AdHocConstant.DISAGREE);
        Integer count  = taskQueryService.count(taskInstanceQueryParam).intValue();
        return  count;

    }

}
