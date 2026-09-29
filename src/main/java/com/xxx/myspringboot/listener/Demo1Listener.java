package com.xxx.myspringboot.listener;

import com.xxx.myspringboot.dto.event.Demo1Event;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class Demo1Listener {

    @Async
    @EventListener
    public void handleDemo1(Demo1Event dto) {
        System.out.println("[" + Thread.currentThread().getName() + "] 监听器1：处理事件 - " + dto.getName());
        sleep(1000);
        System.out.println("[" + Thread.currentThread().getName() + "] 监听器1：事件处理完毕");
    }

    /**
     * 异步处理事务提交后的事件
     * 该方法使用@Async注解实现异步执行
     * 使用@TransactionalEventListener监听事务提交后的事件
     *
     * @param event Demo1Event类型的事件对象，包含需要处理的事件数据
     */
    @Async  // 标记该方法为异步执行方法
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)  // 监听事务提交后阶段的事件
    public void handleAfterCommit(Demo1Event event) {
        System.out.println("事务提交后处理事件: " + event.getName());  // 输出事件名称，表示事务提交后的处理逻辑
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
