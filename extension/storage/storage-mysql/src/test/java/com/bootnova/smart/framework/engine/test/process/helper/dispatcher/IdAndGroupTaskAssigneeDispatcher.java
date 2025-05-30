package com.bootnova.smart.framework.engine.test.process.helper.dispatcher;

import java.util.ArrayList;
import java.util.List;

import com.bootnova.smart.framework.engine.configuration.TaskAssigneeDispatcher;
import com.bootnova.smart.framework.engine.constant.AssigneeTypeConstant;
import com.bootnova.smart.framework.engine.context.ExecutionContext;
import com.bootnova.smart.framework.engine.model.assembly.Activity;
import com.bootnova.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 January  18:03.
 */
public class IdAndGroupTaskAssigneeDispatcher implements TaskAssigneeDispatcher {

    @Override
    public List<TaskAssigneeCandidateInstance> getTaskAssigneeCandidateInstance(Activity activity, ExecutionContext context) {
        List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList= new ArrayList();

        TaskAssigneeCandidateInstance taskAssigneeCandidateInstance = new TaskAssigneeCandidateInstance();
        taskAssigneeCandidateInstance.setAssigneeId("testuser1");
        taskAssigneeCandidateInstance.setAssigneeType(AssigneeTypeConstant.USER);
        taskAssigneeCandidateInstance.setPriority(200);
        taskAssigneeCandidateInstanceList.add(taskAssigneeCandidateInstance);

        TaskAssigneeCandidateInstance taskAssigneeCandidateInstance1 = new TaskAssigneeCandidateInstance();
        taskAssigneeCandidateInstance1.setAssigneeId("testuser3");
        taskAssigneeCandidateInstance1.setAssigneeType(AssigneeTypeConstant.USER);
        taskAssigneeCandidateInstance.setPriority(300);
        taskAssigneeCandidateInstanceList.add(taskAssigneeCandidateInstance1);


        TaskAssigneeCandidateInstance taskAssigneeCandidateInstance2 = new TaskAssigneeCandidateInstance();
        taskAssigneeCandidateInstance2.setAssigneeId("testuser5");
        taskAssigneeCandidateInstance2.setAssigneeType(AssigneeTypeConstant.USER);
        taskAssigneeCandidateInstanceList.add(taskAssigneeCandidateInstance2);


        taskAssigneeCandidateInstance2 = new TaskAssigneeCandidateInstance();
        taskAssigneeCandidateInstance2.setAssigneeId("testgroup11");
        taskAssigneeCandidateInstance2.setAssigneeType(AssigneeTypeConstant.GROUP);
        taskAssigneeCandidateInstanceList.add(taskAssigneeCandidateInstance2);

        taskAssigneeCandidateInstance2 = new TaskAssigneeCandidateInstance();
        taskAssigneeCandidateInstance2.setAssigneeId("testgroup22");
        taskAssigneeCandidateInstance2.setAssigneeType(AssigneeTypeConstant.GROUP);
        taskAssigneeCandidateInstanceList.add(taskAssigneeCandidateInstance2);




        return taskAssigneeCandidateInstanceList;
    }



}
