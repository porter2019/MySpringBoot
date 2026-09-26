package com.xxx.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统模块下的功能
 */
@Getter
@Setter
@ToString
@TableName("sys_handler")
public class SysHandler extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 所属模块Id
     */
    private Long moduleId;

    /**
     * 模块名称
     */
    private String handlerName;

    /**
     * 功能别名
     */
    private String aliasName;

    /**
     * 关联控制器
     */
    private String refController;

    /**
     * 排序数字
     */
    private Integer orderNo;
}
