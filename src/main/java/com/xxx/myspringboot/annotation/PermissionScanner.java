package com.xxx.myspringboot.annotation;

import org.jspecify.annotations.NullMarked;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 权限控制器扫描器
 */
@Component
public class PermissionScanner implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * 重写setApplicationContext方法，用于注入Spring上下文
     * 该方法由ApplicationContextAware接口定义，当Spring容器初始化时自动调用
     *
     * @param applicationContext Spring上下文对象，提供对Bean的访问能力
     * @throws BeansException 如果在设置ApplicationContext过程中发生错误，将抛出BeansException
     */
    @NullMarked
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 将传入的ApplicationContext对象保存到类的成员变量中
        this.applicationContext = applicationContext;
    }

    /**
     * 获取所有带 @PermissionHandler 的 Controller 类
     * 按 orderNo 降序排列（数字越大越靠前）
     */
    public List<Class<?>> getPermissionControllerList() {
        ListableBeanFactory beanFactory = applicationContext;
        // 只取 @RestController 标注的 Bean
        Map<String, Object> controllers = beanFactory.getBeansWithAnnotation(
                org.springframework.web.bind.annotation.RestController.class);
//        controllers.putAll(beanFactory.getBeansWithAnnotation(
//                org.springframework.stereotype.Controller.class));

        List<Class<?>> result = new ArrayList<>();
        Set<Class<?>> seen = new HashSet<>();
        for (Object bean : controllers.values()) {
            Class<?> clazz = org.springframework.aop.support.AopUtils.getTargetClass(bean);
            if (!seen.add(clazz)) continue;

            PermissionHandler handler = AnnotationUtils.findAnnotation(clazz, PermissionHandler.class);
            if (handler != null) {
                result.add(clazz);
            }
        }
        // 按 orderNo 降序
        result.sort((a, b) -> {
            PermissionHandler ha = Objects.requireNonNull(AnnotationUtils.findAnnotation(a, PermissionHandler.class));
            PermissionHandler hb = Objects.requireNonNull(AnnotationUtils.findAnnotation(b, PermissionHandler.class));
            return Integer.compare(hb.orderNo(), ha.orderNo());
        });
        return result;
    }
}
