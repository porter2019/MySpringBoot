package com.xxx.myspringboot.annotation;

import java.lang.annotation.*;

/**
 * 类上的权限 注解
 */
@Target(ElementType.TYPE) // 注解可用在类和方法上 @Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时可通过反射读取
@Documented // 注解会出现在JavaDoc中   // 子类可以继承父类的注解@Inherited
public @interface PermissionHandler {

    /**
     * 模块名称 例如：系统管理
     */
    String module();

    /**
     * 功能名称 例如：用户管理
     */
    String handler();

    /**
     * 功能别名 例如：SysUser
     */
    String alias();

    /**
     * 排序数字
     */
    int orderNo() default 0;
}