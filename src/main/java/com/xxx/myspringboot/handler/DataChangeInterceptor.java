package com.xxx.myspringboot.handler;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.xxx.myspringboot.dto.AuditContext;
import com.xxx.myspringboot.dto.AuditInfo;
import com.xxx.myspringboot.dto.event.DataChangeEvent;
import com.xxx.myspringboot.entity.log.LogAction;
import com.xxx.myspringboot.entity.log.LogLogin;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class DataChangeInterceptor implements Interceptor {

    @Resource
    private ApplicationEventPublisher eventPublisher;

    private static final Set<Class<?>> EXCLUDED_TABLES = Set.of(LogAction.class, LogLogin.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];

        SqlCommandType commandType = ms.getSqlCommandType();
        Class<?> entityClass = resolveEntityClass(parameter, ms);
        if (entityClass == null) {
            return invocation.proceed();
        }

        if (commandType != SqlCommandType.INSERT
                && commandType != SqlCommandType.UPDATE
                && commandType != SqlCommandType.DELETE) {
            return invocation.proceed();
        }

        if (isExcluded(parameter, ms)) {
            return invocation.proceed();
        }

        // 批量操作直接跳过审计
        if (isBatchParameter(parameter)) {
            return invocation.proceed();
        }

        Object realEntity = null;
        Object oldData = null;

        try {
            realEntity = extractEntity(parameter);

            if (commandType == SqlCommandType.UPDATE && realEntity != null) {

                Executor executor = (Executor) invocation.getTarget();
                oldData = selectOldByEntity(realEntity, ms, executor);
            }

        } catch (Exception e) {
            log.debug("获取数据变更前数据失败，跳过旧数据记录", e);
        }

        // 业务 SQL 正常执行
        Object result = invocation.proceed();

        // 审计失败不能影响业务
        try {

            if (isAffected(result)) {

                AuditInfo auditInfo = AuditContext.get();


                DataChangeEvent eventDTO = new DataChangeEvent();

                eventDTO.setChangeType(commandType);

                eventDTO.setClientType(auditInfo != null ? auditInfo.getClientType() : 0);

                eventDTO.setEntityClassName(entityClass != null ? entityClass.getName() : null);

                eventDTO.setOldData(oldData);

                // ★ 只使用真实实体
                eventDTO.setNewData(realEntity);

                eventDTO.setLocal(auditInfo != null ? auditInfo.getLocal() : "");

                eventDTO.setOperator(auditInfo != null ? auditInfo.getOperator() : "");

                eventPublisher.publishEvent(eventDTO);
            }

        } catch (Exception e) {

            log.debug("数据变更日志记录失败，忽略本次审计", e
            );
        }

        return result;
    }

    private boolean isBatchParameter(Object parameter) {
        if (parameter == null) {
            return false;
        }

        // 直接是 Collection，例如 List<Entity>
        if (parameter instanceof Collection<?>) {
            return true;
        }

        // MyBatis 参数 Map 中包含 Collection
        if (parameter instanceof Map<?, ?> map) {
            for (Object value : map.values()) {
                if (value instanceof Collection<?>) {
                    return true;
                }
            }
        }

        return false;
    }

    private Object extractEntity(Object parameter) {
        if (parameter == null) {
            return null;
        }

        // 普通实体
        if (!(parameter instanceof Map)
                && !(parameter instanceof Collection)) {

            if (TableInfoHelper.getTableInfo(parameter.getClass()) != null) {
                return parameter;
            }

            return null;
        }

        // Map 参数
        if (parameter instanceof Map<?, ?> map) {

            // 只认 et
            Object entity = map.get("et");

            if (entity != null
                    && TableInfoHelper.getTableInfo(entity.getClass()) != null) {
                return entity;
            }

            // 不再把 param1 当实体
            // Wrapper 场景 [ew, param1] 直接跳过

            return null;
        }

        // 批量 Collection
        if (parameter instanceof Collection<?> collection) {
            for (Object item : collection) {
                if (item != null
                        && TableInfoHelper.getTableInfo(item.getClass()) != null) {
                    return item;
                }
            }
        }

        return null;
    }

    private boolean isExcluded(Object parameter, MappedStatement ms) {
        Class<?> entityClass = resolveEntityClass(parameter, ms);
        if (entityClass == null) {
            return false;
        }
        for (Class<?> excluded : EXCLUDED_TABLES) {
            if (excluded.isAssignableFrom(entityClass)) {
                return true;
            }
        }
        return false;
    }

    private Object selectOldByEntity(Object entity, MappedStatement ms, Executor executor) {
        if (entity == null) {
            return null;
        }

        TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
        if (tableInfo == null || tableInfo.getKeyProperty() == null) {
            return null;
        }

        Object idValue = tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty());
        if (idValue == null) {
            return null;
        }

        String statementId = ms.getId();
        int dot = statementId.lastIndexOf('.');
        if (dot < 0) {
            return null;
        }
        String mapperNamespace = statementId.substring(0, dot);
        String selectId = mapperNamespace + ".selectById";

        MappedStatement selectMs;
        try {
            selectMs = ms.getConfiguration().getMappedStatement(selectId);
        } catch (Exception e) {
            log.debug("未找到 {}，跳过旧数据查询", selectId);
            return null;
        }

        try {
            List<Object> list = executor.query(
                    selectMs,
                    idValue,
                    RowBounds.DEFAULT,
                    Executor.NO_RESULT_HANDLER
            );
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            log.warn("查询旧数据失败, selectId={}, id={}", selectId, idValue, e);
            return null;
        }
    }

    private Class<?> resolveEntityClass(Object parameter, MappedStatement ms) {
        if (parameter == null) {
            return null;
        }

        // 普通实体
        if (!(parameter instanceof Map) && !(parameter instanceof Collection)) {
            return TableInfoHelper.getTableInfo(parameter.getClass()) != null
                    ? parameter.getClass()
                    : null;
        }

        // 批量 Collection
        if (parameter instanceof Collection<?> collection) {
            for (Object item : collection) {
                if (item != null
                        && TableInfoHelper.getTableInfo(item.getClass()) != null) {
                    return item.getClass();
                }
            }
            return null;
        }

        // Map 不再通过 get("et") / get("ew") 取参数
        // 直接遍历 values，避免触发参数绑定异常
        if (parameter instanceof Map<?, ?> map) {
            for (Object value : map.values()) {
                if (value == null) {
                    continue;
                }

                // 直接实体
                if (TableInfoHelper.getTableInfo(value.getClass()) != null) {
                    return value.getClass();
                }

                // 批量实体
                if (value instanceof Collection<?> collection) {
                    for (Object item : collection) {
                        if (item != null
                                && TableInfoHelper.getTableInfo(item.getClass()) != null) {
                            return item.getClass();
                        }
                    }
                }

                // Wrapper
                if (value instanceof AbstractWrapper<?, ?, ?> wrapper) {
                    try {
                        Class<?> entityClass = wrapper.getEntityClass();
                        if (entityClass != null
                                && TableInfoHelper.getTableInfo(entityClass) != null) {
                            return entityClass;
                        }
                    } catch (Exception e) {
                        log.debug("从 Wrapper 获取实体类型失败", e);
                    }
                }
            }
        }

        return null;
    }

    private boolean isAffected(Object result) {
        if (result instanceof Integer) return (Integer) result > 0;
        if (result instanceof Long) return (Long) result > 0;
        return false;
    }
}
