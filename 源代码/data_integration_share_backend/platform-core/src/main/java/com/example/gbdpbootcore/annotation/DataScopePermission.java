package com.example.gbdpbootcore.annotation;

import java.lang.annotation.*;


/**
 * 数据权限过滤
 *
 * @author admin
 * @date 2019/12/03
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScopePermission {

    boolean filterSwitchOpen() default true;

    String value() default "";
}
