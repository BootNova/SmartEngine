package com.bootnova.smart.framework.engine.service.param.query;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInstanceQueryParam extends BaseQueryParam {

   private List<String> processInstanceIdList;

   private String activityInstanceId;

   private String processDefinitionType;

   private String processDefinitionActivityId;
   /**
    * @see com.bootnova.smart.framework.engine.constant.TaskInstanceConstant
    */
   private String status;

   private String claimUserId;

   private String tag;

   private String extension;

   private Integer priority;

   private String comment;

   private String title;

}