package com.bootnova.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.bootnova.smart.framework.engine.common.util.DateUtil;
import com.bootnova.smart.framework.engine.persister.database.entity.VariableInstanceEntity;

import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class VariableInstanceDAOTest extends BaseElementTest {

    @Setter(onMethod=@__({@Autowired}))
    VariableInstanceDAO dao;

    VariableInstanceEntity entity = null;

    @Before
    public void before() {
        entity = new VariableInstanceEntity();
        long id = System.currentTimeMillis();
        entity.setId(id);

        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());

        entity.setProcessInstanceId(222L);
        entity.setExecutionInstanceId(333L);
        entity.setFieldKey("testKey");
        entity.setFieldType("java.lang.String");
        entity.setFieldStringValue("testValue");
    }

    @Test
    public void testInsert() {
        dao.insert(entity);
        Assert.assertNotNull(entity);
    }

    @Test
    public void testFindOne() {
        dao.insert(entity);

        VariableInstanceEntity result = dao.findOne(entity.getId(),null);
        Assert.assertNotNull(result);
        Assert.assertEquals("testKey", result.getFieldKey());
        Assert.assertEquals("testValue", result.getFieldStringValue());
    }

    @Test
    public void testFindList() {
        dao.insert(entity);

        List<VariableInstanceEntity> result = dao.findList(entity.getProcessInstanceId(), entity.getExecutionInstanceId(),null);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("testKey", result.get(0).getFieldKey());
    }

    @Test
    public void testDelete() {
        dao.insert(entity);

        VariableInstanceEntity result = dao.findOne(entity.getId(),null);
        Assert.assertNotNull(result);

        dao.delete(entity.getId(),null);

        result = dao.findOne(entity.getId(),null);
        Assert.assertNull(result);
    }

    @Test
    public void testUpdate() {
        dao.insert(entity);

        entity.setFieldStringValue("newValue");

        dao.update(entity);

        VariableInstanceEntity result = dao.findOne(entity.getId(),null);
        Assert.assertNotNull(result);
        Assert.assertEquals("newValue", result.getFieldStringValue());
    }

    @Test
    public void testDifferentTypes() {
        // Test Long value
        entity.setFieldType("java.lang.Long");
        entity.setFieldLongValue(123L);
        entity.setFieldStringValue(null);
        dao.insert(entity);
        
        VariableInstanceEntity result = dao.findOne(entity.getId(),null);
        Assert.assertEquals(123L, result.getFieldLongValue().longValue());

        // Test Double value
        entity = new VariableInstanceEntity();
        entity.setId(System.currentTimeMillis() + (int) (Math.random() * 1000) + (int) (Math.random() * 1000));
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setProcessInstanceId(222L);
        entity.setExecutionInstanceId(333L);
        entity.setFieldKey("doubleKey");
        entity.setFieldType("java.lang.Double");
        entity.setFieldDoubleValue(123.45);
        
        dao.insert(entity);
        
        result = dao.findOne(entity.getId(),null);
        Assert.assertEquals(123.45, result.getFieldDoubleValue(), 0.001);
    }
}