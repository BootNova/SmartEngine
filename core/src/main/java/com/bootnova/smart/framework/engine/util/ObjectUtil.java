package com.bootnova.smart.framework.engine.util;

import java.util.Date;

/**
 * @author JannLim
 */
public class ObjectUtil {

    public static String obj2Str(Object obj) {
        return obj == null ? null : obj.toString();
    }

    public static Date obj2Date(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Date) {
            return (Date) obj;
        } else if (obj instanceof Long) {
            return new Date((Long) obj);
        } else {
            return null;
        }
    }

    public static Integer obj2Integer(Object obj) {
        if (obj == null) {
            return null;
        }
        return (Integer) obj;
    }

}
