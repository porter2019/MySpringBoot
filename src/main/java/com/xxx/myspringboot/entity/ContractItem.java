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
@TableName("contract_item")
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
}
