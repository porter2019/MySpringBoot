package com.xxx.myspringboot.listener;

import cn.hutool.json.JSONUtil;
import com.xxx.myspringboot.dto.event.DataChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataChangeListener {
    @Async("auditExecutor")   // 指定专用线程池，避免占用默认线程池
    @EventListener
    public void handleDataChangeEvent(DataChangeEvent dto) {
        // 打印当前线程名和处理的事件信息，用于调试和监控
        System.out.println("[" + Thread.currentThread().getName() + "] 处理事件 - " + dto.getEntityClassName());
        try {
            // 使用JSON工具类将事件对象转换为JSON字符串并记录日志
            log.info(JSONUtil.toJsonStr(dto));
//            // 1. 构建变更记录实体
//            ChangeLog changeLog = new ChangeLog();
//            changeLog.setEntityClass(event.getEntityClassName());
//            changeLog.setChangeType(event.getChangeType().name());
//            changeLog.setOldData(event.getOldDataJson());
//            changeLog.setNewData(event.getNewDataJson());
//            changeLog.setOperator(event.getOperator());
//
//            // 2. 计算具体变更的字段（精细化记录）
//            if (event.getChangeType() == SqlCommandType.UPDATE) {
//                Map<String, Object> fieldChanges = DiffUtil.diff(
//                        event.getOldDataJson(),
//                        event.getNewDataJson()
//                );
//                changeLog.setChangedFields(JSON.toJSONString(fieldChanges));
//            }
//
//            // 3. 持久化到数据库（或发送到消息队列）
//            changeLogMapper.insert(changeLog);

        } catch (Exception e) {
            // 4. 降级策略：记录失败时，至少打印日志或存入死信队列
            log.error("数据变更记录失败", e);
            // 可在此处将事件发送至Redis或Kafka进行重试
        }
    }
}
