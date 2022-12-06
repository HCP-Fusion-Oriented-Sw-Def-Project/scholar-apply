package com.example.gbdpbootcore.publicToolUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @version 1.0
 * @ClassName GyToolUtil
 * @Description //TODO
 * @Author lhf
 * @Date 2019-12-14 23:04
 **/
public class GyToolUtil {

    @SuppressWarnings("rawtypes")
    public static boolean isNull(Object obj) {
        boolean isNullFlag = true;
        if (obj != null) {
            if (obj instanceof List<?>) {
                isNullFlag = isNull((List<?>) obj);
            } else if (obj instanceof Set<?>) {
                isNullFlag = isNull((Set<?>) obj);
            } else if (obj instanceof Object[]) {
                isNullFlag = isNull((Object[]) obj);
            } else if (obj instanceof Map) {
                isNullFlag = isNull((Map) obj);
            } else if (obj instanceof String) {
                isNullFlag = isNull((String) obj);
            } else {
                isNullFlag = false;
            }
        }
        return isNullFlag;
    }

    private static boolean isNull(List<?> list) {
        return list == null || list.size() == 0;
    }

    private static boolean isNull(Set<?> set) {
        return set == null || set.size() == 0;
    }

    private static boolean isNull(Object[] objects) {
        return objects == null || objects.length == 0;
    }

    private static boolean isNull(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    private static boolean isNull(String str) {
        return str == null || "".equals(str.trim()) || "null".equalsIgnoreCase(str.trim());
    }

    public static boolean isEqual(Object o1, Object o2){
        if(o1 == null && o2 == null) {
            return true;
        } else if (o1 == null) {
            return false;
        } else if ( o1 == o2) {
            return true;
        } else if (o1.getClass() != o2.getClass()) {
            return false;
        } else {
            return o1.equals(o2);
        }
    }

    public static boolean isNotEqual(Object o1, Object o2){
        return !isEqual(o1, o2);
    }

}
