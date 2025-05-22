package com.bootnova.smart.framework.engine.pvm;

import com.bootnova.smart.framework.engine.hook.LifeCycleHook;
import com.bootnova.smart.framework.engine.model.assembly.ExtensionElementContainer;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface PvmElement<M extends ExtensionElementContainer> extends  LifeCycleHook {

    M getModel();


}
