package com.xxx.myspringboot.entity.log;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志
 */
@Getter
@Setter
@ToString
@TableName("log_action")
public class LogAction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户端类型
     */
    private Integer clientType;

    /**
     * 操作类型
     */
    private Integer type;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 实体包名
     */
    private String className;

    /**
     * 用户名
     */
    private String local;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
}
