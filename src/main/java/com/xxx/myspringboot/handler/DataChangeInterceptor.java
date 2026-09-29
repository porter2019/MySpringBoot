package com.xxx.myspringboot.handler;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.xxx.myspringboot.dto.AuditContext;
import com.xxx.myspringboot.dto.AuditInfo;
import com.xxx.myspringboot.dto.event.DataChangeEvent;
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

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class DataChangeInterceptor implements Interceptor {

    @Resource()
    private ApplicationEventPublisher eventPublisher;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        Executor executor = (Executor) invocation.getTarget();

        SqlCommandType commandType = ms.getSqlCommandType();

        // 1. 只处理增删改，其余直接放行
        if (commandType != SqlCommandType.INSERT
                && commandType != SqlCommandType.UPDATE
                && commandType != SqlCommandType.DELETE) {
            return invocation.proceed();
        }

        // 2. UPDATE 才需要查旧数据（执行前）
        Object oldData = null;
        if (commandType == SqlCommandType.UPDATE) {
            oldData = fetchOldData(parameter, ms, executor);
        }

        // 3. 执行原始 SQL（无论成功失败都只执行一次）
        Object result = invocation.proceed();

        // 4. 判断是否真正影响数据，安全转换，避免 ClassCastException
        if (isAffected(result)) {
            AuditInfo auditInfo = AuditContext.get(); // 从 ThreadLocal 取自定义上下文

            Class<?> entityClass = resolveEntityClass(parameter, ms);

            DataChangeEvent eventDTO = new DataChangeEvent();
            eventDTO.setChangeType(commandType);
            eventDTO.setEntityClassName(entityClass != null ? entityClass.getName() : null);
            eventDTO.setOldData(oldData);
            eventDTO.setNewData(parameter);
            eventDTO.setLocal(auditInfo != null ? auditInfo.getLocal() : "");
            eventDTO.setOperator(auditInfo != null ? auditInfo.getOperator() : "");

            eventPublisher.publishEvent(eventDTO);
        }

        return result;
    }

    private Object fetchOldData(Object parameter, MappedStatement ms, Executor executor) throws SQLException {
        // 单实体
        if (!(parameter instanceof Map)) {
            return selectOldByEntity(parameter, ms, executor);
        }
        // ParamMap：update(entity, wrapper) 或 updateById 的包装
        Map<?, ?> map = (Map<?, ?>) parameter;
        Object entity = map.get("et"); // MP 中实体通常放在 "et" key
        if (entity != null) {
            return selectOldByEntity(entity, ms, executor);
        }
        return null; // 纯 wrapper 更新、无主键，无法定位旧数据
    }

    private Object selectOldByEntity(Object entity, MappedStatement ms, Executor executor) throws SQLException {
        // 1. 拿到实体的 TableInfo（MyBatis-Plus 元数据）
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
        if (tableInfo == null || tableInfo.getKeyProperty() == null) {
            return null; // 不是 MP 实体，无法定位主键
        }

        // 2. 反射取出主键值
        Object idValue = tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty());
        if (idValue == null) {
            return null; // 没有主键，无法查旧数据
        }

        // 3. 拼接 selectById 的 MappedStatement ID
        //    ms.getId() 形如 com.xxx.mapper.UserMapper.updateById
        String statementId = ms.getId();
        String mapperNamespace = statementId.substring(0, statementId.lastIndexOf('.'));
        String selectId = mapperNamespace + ".selectById";

        MappedStatement selectMs;
        try {
            selectMs = ms.getConfiguration().getMappedStatement(selectId);
        } catch (Exception e) {
            // 该 Mapper 没有 selectById（比如自定义 Mapper），放弃查旧数据
            return null;
        }

        // 4. 用当前 Executor 直接查询，避免再次经过拦截器链
        List<Object> list = executor.query(
                selectMs,
                idValue,
                RowBounds.DEFAULT,
                Executor.NO_RESULT_HANDLER
        );
        return list.isEmpty() ? null : list.get(0);
    }

    private Class<?> resolveEntityClass(Object parameter, MappedStatement ms) {
        // 1. 单实体：直接返回它的 Class
        if (parameter != null && !(parameter instanceof Map) && !(parameter instanceof Collection)) {
            return parameter.getClass();
        }

        // 2. ParamMap：MP 通常把实体放在 "et" key
        if (parameter instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) parameter;
            Object et = map.get("et");
            if (et != null) {
                return et.getClass();
            }
            // 有些场景 key 是 "param1" 或 "entity"
            for (Object v : map.values()) {
                if (v != null && TableInfoHelper.getTableInfo(v.getClass()) != null) {
                    return v.getClass();
                }
            }
        }

        // 3. 批量：List/Collection，取第一个元素的类型
        if (parameter instanceof Collection) {
            Collection<?> coll = (Collection<?>) parameter;
            for (Object item : coll) {
                if (item != null) {
                    return item.getClass();
                }
            }
        }

        // 4. 兜底：从 MappedStatement 的 parameterType 拿
        if (ms.getParameterMap() != null) {
            Class<?> type = ms.getParameterMap().getType();
            // MyBatis 对单参数可能包装成 ParamMap，需判断是否真实实体
            if (type != null && TableInfoHelper.getTableInfo(type) != null) {
                return type;
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
