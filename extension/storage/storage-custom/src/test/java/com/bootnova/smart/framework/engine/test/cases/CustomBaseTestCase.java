package com.bootnova.smart.framework.engine.test.cases;

import com.bootnova.smart.framework.engine.SmartEngine;
import com.bootnova.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.bootnova.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.bootnova.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.bootnova.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;
import com.bootnova.smart.framework.engine.persister.custom.session.PersisterSession;
import com.bootnova.smart.framework.engine.service.command.ExecutionCommandService;
import com.bootnova.smart.framework.engine.service.command.ProcessCommandService;
import com.bootnova.smart.framework.engine.service.command.RepositoryCommandService;
import com.bootnova.smart.framework.engine.service.query.ExecutionQueryService;
import com.bootnova.smart.framework.engine.service.query.ProcessQueryService;
import com.bootnova.smart.framework.engine.service.query.RepositoryQueryService;

import org.junit.After;
import org.junit.Before;

public class CustomBaseTestCase {

    protected ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();

    protected SmartEngine smartEngine = new DefaultSmartEngine();

    //3. 部署流程定义
    protected RepositoryCommandService repositoryCommandService;
    //2.获得常用服务
    protected ProcessCommandService processCommandService;
    protected ProcessQueryService processQueryService;

    protected ExecutionQueryService executionQueryService;
    protected ExecutionCommandService executionCommandService;
    protected RepositoryQueryService repositoryQueryService ;

    private   SimpleAnnotationScanner simpleAnnotationScanner ;

    @Before
    public void setUp() {
        this.simpleAnnotationScanner = new SimpleAnnotationScanner(SmartEngine.class.getPackage().getName());


        PersisterSession.create();


        initProcessConfiguration();

        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        //3. 部署流程定义
        repositoryCommandService = smartEngine
            .getRepositoryCommandService();
          repositoryQueryService = smartEngine
            .getRepositoryQueryService();

        processCommandService = smartEngine.getProcessCommandService();
        processQueryService = smartEngine.getProcessQueryService();
        executionQueryService = smartEngine.getExecutionQueryService();
        executionCommandService = smartEngine.getExecutionCommandService();


    }

    protected void initProcessConfiguration() {
        processEngineConfiguration = new DefaultProcessEngineConfiguration();
    }

    @After
    public void clear() {
        PersisterSession.destroySession();
        simpleAnnotationScanner.clear();

    }

}