package com.xxx.myspringboot.dto.event;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.apache.ibatis.mapping.SqlCommandType;

import java.time.LocalDateTime;

@Data
public class DataChangeEvent {
//    private DataChangeEvent() {
//    }

    private SqlCommandType changeType; // 操作类型
    private Integer clientType;            // 客户端类型
    private Object oldData;            // 变更前数据（JSON字符串或实体）
    private Object newData;            // 变更后数据
    private String operator;           // 操作人（从ThreadLocal或安全上下文获取）
    private String local;              //业务路径（从ThreadLocal或安全上下文获取）
    private LocalDateTime changeTime = LocalDateTime.now();
    private String entityClassName;    // 实体类名

    // 关键：将数据转换为JSON，避免后续序列化问题
    public String getOldDataJson() {
        return JSONUtil.toJsonStr(oldData);
    }

    public String getNewDataJson() {
        return JSONUtil.toJsonStr(newData);
    }
}
