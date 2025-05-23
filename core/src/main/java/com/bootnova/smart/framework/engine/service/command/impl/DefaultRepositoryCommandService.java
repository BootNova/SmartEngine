package com.bootnova.smart.framework.engine.service.command.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.bootnova.smart.framework.engine.behavior.TransitionBehavior;
import com.bootnova.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.bootnova.smart.framework.engine.common.util.MapUtil;
import com.bootnova.smart.framework.engine.common.util.StringUtil;
import com.bootnova.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.bootnova.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.bootnova.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.bootnova.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.bootnova.smart.framework.engine.exception.DeployException;
import com.bootnova.smart.framework.engine.exception.EngineException;
import com.bootnova.smart.framework.engine.exception.ParseException;
import com.bootnova.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.bootnova.smart.framework.engine.extension.constant.ExtensionConstant;
import com.bootnova.smart.framework.engine.hook.LifeCycleHook;
import com.bootnova.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.bootnova.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.bootnova.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.bootnova.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.bootnova.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.bootnova.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.bootnova.smart.framework.engine.model.assembly.Activity;
import com.bootnova.smart.framework.engine.model.assembly.BaseElement;
import com.bootnova.smart.framework.engine.model.assembly.ProcessDefinition;
import com.bootnova.smart.framework.engine.model.assembly.ProcessDefinitionSource;
import com.bootnova.smart.framework.engine.model.assembly.Transition;
import com.bootnova.smart.framework.engine.pvm.PvmActivity;
import com.bootnova.smart.framework.engine.pvm.PvmProcessDefinition;
import com.bootnova.smart.framework.engine.pvm.PvmTransition;
import com.bootnova.smart.framework.engine.pvm.impl.DefaultPvmActivity;
import com.bootnova.smart.framework.engine.pvm.impl.DefaultPvmProcessDefinition;
import com.bootnova.smart.framework.engine.pvm.impl.DefaultPvmTransition;
import com.bootnova.smart.framework.engine.service.command.RepositoryCommandService;
import com.bootnova.smart.framework.engine.util.ClassUtil;
import com.bootnova.smart.framework.engine.util.IOUtil;
import com.bootnova.smart.framework.engine.xml.parser.ParseContext;
import com.bootnova.smart.framework.engine.xml.parser.XmlParserFacade;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

//import com.bootnova.smart.framework.engine.pvm.impl.DefaultPvmTransition;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = RepositoryCommandService.class)
public class DefaultRepositoryCommandService implements RepositoryCommandService, ProcessEngineConfigurationAware, LifeCycleHook {

    private ProcessEngineConfiguration processEngineConfiguration;


    private XmlParserFacade xmlParserExtensionPoint;

    private ProcessDefinitionContainer processContainer;

    private AnnotationScanner annotationScanner;


    @Override
    public ProcessDefinitionSource deploy(String classPathResource,String tenantId) throws DeployException {

        ClassLoader classLoader = ClassUtil.getContextClassLoader();

        ProcessDefinitionSource processDefinitionSource = this.parse(classLoader, classPathResource,tenantId);

        buildPvmDefinition( processDefinitionSource,tenantId);

        return processDefinitionSource;
    }
    @Override
    public ProcessDefinitionSource deploy(String classPathResource) throws DeployException {
        return deploy(classPathResource,null);
    }

    @Override
    public ProcessDefinitionSource deploy(InputStream inputStream) {
        return deploy(inputStream,null);
    }

    @Override
    public ProcessDefinitionSource deploy(InputStream inputStream,String tenantId) {
        try {
            ProcessDefinitionSource processDefinitionSource = parseStream(inputStream,tenantId);
            buildPvmDefinition( processDefinitionSource,tenantId);

            return processDefinitionSource;
        } catch (Exception e) {
            throw new DeployException("Parse process definition file failure!", e);
        } finally {
            IOUtil.closeQuietly(inputStream);
        }
    }

    @Override
    public ProcessDefinitionSource deployWithUTF8Content(String uTF8ProcessDefinitionContent) {
       return deployWithUTF8Content(uTF8ProcessDefinitionContent,null);

    }

    @Override
    public ProcessDefinitionSource deployWithUTF8Content(String uTF8ProcessDefinitionContent,String tenantId) {
        byte[] bytes ;
        try {
            bytes = uTF8ProcessDefinitionContent.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new EngineException(e);
        }
        InputStream stream = new ByteArrayInputStream(bytes);
        ProcessDefinitionSource processDefinitionSource =  this.deploy(stream,tenantId);
        return  processDefinitionSource;

    }



    private ProcessDefinitionSource parse(ClassLoader classLoader, String uri,String tenantId) throws DeployException {

        InputStream inputStream = null;
        try {
            inputStream = classLoader.getResourceAsStream(uri);

            if(null == inputStream){
                throw new IllegalArgumentException("Cant find any resources for the uri:"+uri);
            }

            ProcessDefinitionSource processDefinitionSource = parseStream(inputStream,tenantId);
            return processDefinitionSource;

        } catch (Exception e) {
            throw new DeployException("Read process definition file[" + uri + "] failure!", e);
        } finally {
            IOUtil.closeQuietly(inputStream);
        }
    }

    private ProcessDefinitionSource parseStream(InputStream inputStream,String tenantId) throws XMLStreamException, ParseException {
        XMLInputFactory factory = XMLInputFactory.newInstance();

        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

        ParseContext context = new ParseContext();

        boolean findStart = false;
        do {
            int event = reader.next();
            if (event == END_ELEMENT) {
                break;
            }
            if (event == START_ELEMENT) {
                findStart = true;
                break;
            }
        } while (reader.hasNext());

        if (findStart) {
            Object parseResult = this.xmlParserExtensionPoint.parseElement(reader, context);
            ProcessDefinitionSource processDefinitionSource =  (ProcessDefinitionSource)parseResult;
            if(processDefinitionSource.getProcessDefinitionList() != null){
                for(ProcessDefinition processDefinition : processDefinitionSource.getProcessDefinitionList()){
                    processDefinition.setTenantId(tenantId);
                }
            }
            processDefinitionSource.setTenantId(tenantId);
            return processDefinitionSource;
        } else {
            throw new DeployException("Read process definition file failure! Not found start element!");
        }
    }

    @SuppressWarnings("rawtypes")
    private void buildPvmDefinition(ProcessDefinitionSource processDefinitionSource,String tenantId) {

        List<ProcessDefinition> processDefinitionList =  processDefinitionSource.getProcessDefinitionList();

        for (ProcessDefinition processDefinition : processDefinitionList) {
            String processDefinitionSourceId = processDefinition.getId();
            String version = processDefinition.getVersion();

            if (StringUtil.isEmpty(processDefinitionSourceId) || StringUtil.isEmpty(version)) {
                throw new EngineException("empty processDefinitionSourceId or version"+processDefinitionSource);
            }

            PvmProcessDefinition pvmProcessDefinition = this.buildPvmProcessDefinition(processDefinition,tenantId);

            this.processContainer.install(pvmProcessDefinition, processDefinition);

        }


    }

    @SuppressWarnings("rawtypes")
    private PvmProcessDefinition buildPvmProcessDefinition(ProcessDefinition processDefinition,String tenantId) {


        DefaultPvmProcessDefinition pvmProcessDefinition = new DefaultPvmProcessDefinition();
        pvmProcessDefinition.setId(processDefinition.getId());
        pvmProcessDefinition.setVersion(processDefinition.getVersion());
        pvmProcessDefinition.setTenantId(tenantId);

        pvmProcessDefinition.setModel(processDefinition);

        List<BaseElement> elements = processDefinition.getBaseElementList();
        if (null != elements && !elements.isEmpty()) {

            Map<String, PvmTransition> pvmTransitionMap = MapUtil.newLinkedHashMap();
            Map<String, PvmActivity> pvmActivityMap = MapUtil.newLinkedHashMap();

            for (BaseElement element : elements) {

                if (element instanceof Transition) {
                    Transition transition = (Transition) element;

                    DefaultPvmTransition pvmTransition = new DefaultPvmTransition();
                    pvmTransition.setModel(transition);

                    String id = pvmTransition.getModel().getId();


                    PvmTransition oldValue =    pvmTransitionMap.put(id, pvmTransition);
                    if(null != oldValue){
                        throw new EngineException("duplicated id found for "+id);
                    }

                } else if (element instanceof Activity) {
                    Activity activity = (Activity) element;

                    DefaultPvmActivity pvmActivity = new DefaultPvmActivity();
                    pvmActivity.setModel(activity);

                    String id = pvmActivity.getModel().getId();


                    PvmActivity oldValue =   pvmActivityMap.put(id, pvmActivity);
                    if(null != oldValue){
                        throw new EngineException("duplicated id found for "+id);
                    }


                    if (pvmActivity.getModel().isStartActivity()) {
                        pvmProcessDefinition.setStartActivity(pvmActivity);
                    }
                }
            }



            // Process Transition Flow
            for (Map.Entry<String, PvmTransition> pvmTransitionEntry : pvmTransitionMap.entrySet()) {

                DefaultPvmTransition pvmTransition = (DefaultPvmTransition) pvmTransitionEntry.getValue();
                String sourceRef = pvmTransition.getModel().getSourceRef();
                String targetRef = pvmTransition.getModel().getTargetRef();
                DefaultPvmActivity sourcePvmActivity = (DefaultPvmActivity) pvmActivityMap.get(sourceRef);
                DefaultPvmActivity targetPvmActivity = (DefaultPvmActivity) pvmActivityMap.get(targetRef);

                if(null == sourcePvmActivity){
                    throw new EngineException("No source activity found for id "+sourceRef );
                }
                if(null == targetPvmActivity){
                    throw new EngineException("No target activity found for id "+targetRef );
                }

                pvmTransition.setSource(sourcePvmActivity);
                pvmTransition.setTarget(targetPvmActivity);

                sourcePvmActivity.addOutcomeTransition(pvmTransition.getModel().getId(), pvmTransition);
                targetPvmActivity.addIncomeTransition(pvmTransition.getModel().getId(), pvmTransition);

            }

            for (Map.Entry<String, PvmTransition> pvmTransitionEntry : pvmTransitionMap.entrySet()) {
                PvmTransition pvmTransition = pvmTransitionEntry.getValue();

                TransitionBehavior transitionBehavior = annotationScanner.getExtensionPoint(ExtensionConstant.ACTIVITY_BEHAVIOR,TransitionBehavior.class);

                pvmTransition.setBehavior(transitionBehavior);

            }

            for (Map.Entry<String, PvmActivity> pvmActivityEntry : pvmActivityMap.entrySet()) {
                PvmActivity pvmActivity = pvmActivityEntry.getValue();

                Activity activity=pvmActivity.getModel();

                AbstractActivityBehavior activityBehavior = (AbstractActivityBehavior) annotationScanner.getObject(ExtensionConstant.ACTIVITY_BEHAVIOR,activity.getClass());

                if(null == activityBehavior){
                    throw new EngineException("ActivityBehavior should be coded for activity:"+activity.getClass());
                }

                pvmActivity.setBehavior(activityBehavior);


                activityBehavior.setProcessInstanceFactory(annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ProcessInstanceFactory.class));
                activityBehavior.setExecutionInstanceFactory(annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ExecutionInstanceFactory.class));
                activityBehavior.setActivityInstanceFactory(annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ActivityInstanceFactory.class));
                activityBehavior.setTaskInstanceFactory(annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,TaskInstanceFactory.class));

                activityBehavior.setProcessEngineConfiguration( processEngineConfiguration);

                activityBehavior.setExecutionInstanceStorage(annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ExecutionInstanceStorage.class));
                activityBehavior.setVariableInstanceStorage(annotationScanner.getExtensionPoint(ExtensionConstant.COMMON, VariableInstanceStorage.class));



            }

            pvmProcessDefinition.setActivities(pvmActivityMap);
            pvmProcessDefinition.setTransitions(pvmTransitionMap);
        }
        return pvmProcessDefinition;
    }

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    @Override
    public void start() {

        this.annotationScanner = processEngineConfiguration.getAnnotationScanner();
        this.xmlParserExtensionPoint = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,
            XmlParserFacade.class);
        this.processContainer = annotationScanner.getExtensionPoint(ExtensionConstant.SERVICE,ProcessDefinitionContainer.class);
    }

    @Override
    public void stop() {

    }

}
