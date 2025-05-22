package com.bootnova.smart.framework.engine.persister.database.dao;

import com.bootnova.smart.framework.engine.common.util.DateUtil;
import com.bootnova.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ActivityInstanceDAOWithTenantIdTest extends BaseElementTest {

    @Setter(onMethod=@__({@Autowired}))
    ActivityInstanceDAO dao;

    String tenantId = "-1";
    ActivityInstanceEntity entity = null;

    @Before
    public void before() {
        entity = new ActivityInstanceEntity();
        entity.setId(1L);
        entity.setTenantId(tenantId);

        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());

        entity.setProcessDefinitionIdAndVersion("processDefinitionId");
//        entity.setExecutionInstanceId(11L);
        entity.setProcessDefinitionActivityId("ProcessDefinitionActivityId");
        //entity.setProcessDefinitionId("processDefinitionId");
        entity.setProcessInstanceId(22L);
        entity.setTenantId(tenantId);
//        entity.setTaskInstanceId(33L);
    }

    @Test
    public void testInsert() {

        dao.insert(entity);
        Assert.assertNotNull(entity);
        Assert.assertTrue(entity.getTenantId().equals(tenantId));
    }

    @Test
    public void testFindOne() {
        dao.insert(entity);

        ActivityInstanceEntity result = dao.findOne(entity.getId(),tenantId);
        Assert.assertNotNull(result);
    }

    @Test
    public void testDelete() {
        dao.insert(entity);

        ActivityInstanceEntity result = dao.findOne(entity.getId(),tenantId);
        Assert.assertNotNull(result);

        dao.delete(entity.getId(),tenantId);

        result = dao.findOne(entity.getId(),tenantId);
        Assert.assertNull(result);


    }

}
