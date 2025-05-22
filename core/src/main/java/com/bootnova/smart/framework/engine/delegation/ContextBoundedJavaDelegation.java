package com.bootnova.smart.framework.engine.delegation;

import com.bootnova.smart.framework.engine.model.assembly.Activity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 高海军 帝奇
 */
public abstract class ContextBoundedJavaDelegation implements JavaDelegation {

    @Getter
    @Setter
    protected String className;
    @Getter
    @Setter
    protected Activity activity;


}
