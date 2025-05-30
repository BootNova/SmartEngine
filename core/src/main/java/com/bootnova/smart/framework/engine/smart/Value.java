package com.bootnova.smart.framework.engine.smart;

import java.util.Map;

import com.bootnova.smart.framework.engine.common.util.MapUtil;
import com.bootnova.smart.framework.engine.constant.ExtensionElementsConstant;
import com.bootnova.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.bootnova.smart.framework.engine.model.assembly.ExtensionElements;

import com.bootnova.smart.framework.engine.xml.parser.ParseContext;
import lombok.Data;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 * please use @com.bootnova.smart.framework.engine.smart.Property, Example: com.bootnova.smart.framework.engine.test.cases.extensions.CompositePropertiesTest
 */
@Data
@Deprecated
public class Value  implements PropertiesElementMarker, ExtensionDecorator,CustomExtensionElement {

    public final static String xmlLocalPart = "value";

    private String name;
    private String value;

    @Override
    public String getDecoratorType() {
        return ExtensionElementsConstant.PROPERTIES;
    }

    @Override
    public void decorate(ExtensionElements extensionElements, ParseContext context) {

        Map map =  (Map)extensionElements.getDecorationMap().get(getDecoratorType());

        if(null == map){
            map = MapUtil.newHashMap();
            extensionElements.getDecorationMap().put(this.getDecoratorType(),map);
        }

        map.put(this.getName(),this.getValue());

    }

}
