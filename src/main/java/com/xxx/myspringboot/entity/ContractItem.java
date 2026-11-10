package com.xxx.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xxx.myspringboot.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 合同明细
 */
@Getter
@Setter
@ToString
@TableName("c")
public class ContractItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 合同Id
     */
    private Long contractId;

    /**
     * 明细名称
     */
    private String name;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private BigDecimal amount;

    /**
     * 金额
     */
    private BigDecimal value;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序数字, 升序排列
     */
    private Integer sort;

    /*
    create table contract_item
(
  id           bigint                 auto_increment comment '主键自增id',
  contract_id  bigint                       not null comment '合同Id',
  name         varchar(50)                      null comment '名称',
  price        decimal(19, 4)                   null comment '单价',
  amount       decimal(19, 4)                   null comment '数量',
  value        decimal(19, 4)                   null comment '金额',
  remark       varchar(200)                     null comment '备注',
  sort         int                default 0 not null comment '排序数字',
  created_time datetime       default now() not null comment '创建时间',
  updated_time datetime       default now() not null comment '更新时间',
  is_deleted   boolean        default false not null comment '软删除',
  primary key (id)
) comment '合同明细';
     */

}
