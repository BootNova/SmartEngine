package com.bootnova.smart.framework.engine.configuration.impl.option;

import com.bootnova.smart.framework.engine.configuration.ConfigurationOption;


public class EagerFlushEnabledOption implements ConfigurationOption {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getId() {
        return "eagerFlushEnabledOption";
    }

    @Override
    public Object getData() {
        return null;
    }
}