package com.zeafen.LocNetMonitoring.data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassToMapConverter {
    public static Map<String, Object> convertToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }
}
