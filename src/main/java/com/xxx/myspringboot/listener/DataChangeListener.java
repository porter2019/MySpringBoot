package com.xxx.myspringboot.listener;

import com.xxx.myspringboot.dto.event.DataChangeEvent;
import com.xxx.myspringboot.service.log.ILogActionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class DataChangeListener {

    @Resource
    private ILogActionService logActionService;

    @Async("auditExecutor")
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void handleDataChangeEvent(DataChangeEvent dto) {
        log.info("[{}] 处理数据变更事件 - {}", Thread.currentThread().getName(), dto.getEntityClassName());
        try {
            logActionService.saveChangeLog(dto);

        } catch (Exception e) {
            log.error("数据变更记录失败", e);
            // 可在此处将事件发送至Redis或Kafka进行重试
        }
    }
}
