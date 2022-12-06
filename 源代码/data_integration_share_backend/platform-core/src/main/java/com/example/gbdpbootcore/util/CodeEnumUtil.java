package com.example.gbdpbootcore.util;


import com.example.gbdpbootcore.enums.BaseEnum;

/**
 * @Author: kongweichang
 * @Date: 2019/6/5 16:37
 */
public class CodeEnumUtil {
    /**
     * @param enumClass
     * @param code
     * @param <E>
     * @return
     */
    public static <E extends Enum<?> & BaseEnum> E codeOf(Class<E> enumClass, int code) {
        E[] enumConstants = enumClass.getEnumConstants();
        for (E e : enumConstants) {
            if (e.code() == code) {
                return e;
            }
        }
        return null;
    }
}
