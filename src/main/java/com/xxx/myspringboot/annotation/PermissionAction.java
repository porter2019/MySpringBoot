package com.xxx.myspringboot.annotation;

import java.lang.annotation.*;

/**
 * 方法上的权限 注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionAction {
    /**
     * 操作名称 例如：查看
     */
    String name();

    /**
     * 别名 例如：show
     */
    String alias();

    /*
      是否自动校验（如需权限拦截器使用）
     */
//    boolean autoCheck() default true;
}