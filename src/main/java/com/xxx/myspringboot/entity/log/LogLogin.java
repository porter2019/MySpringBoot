package com.xxx.myspringboot.entity.log;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录日志
 */
@Getter
@Setter
@ToString
@TableName("log_login")
public class LogLogin implements Serializable {

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

    public String getClientTypeName() {
        return switch (clientType) {
            case 1 -> "后台";
            case 2 -> "移动端";
            default -> "未知";
        };
    }

    /**
     * 用户id
     */
    private long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String cellPhone;

    /**
     * 登录平台
     */
    private String platform;

    /**
     * ip
     */
    private String ip;

    /**
     * UA
     */
    private String userAgent;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime = LocalDateTime.now();
}
