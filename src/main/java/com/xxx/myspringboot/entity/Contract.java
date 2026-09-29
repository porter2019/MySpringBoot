package com.xxx.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xxx.myspringboot.entity.base.BaseEntityStandard;
import com.xxx.myspringboot.entity.enums.ContractTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 合同
 */
@Getter
@Setter
@ToString
public class Contract extends BaseEntityStandard {

    private static final long serialVersionUID = 1L;

    /**
     * 合同编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 合同类型
     */
    @TableField("c_type")
    private ContractTypeEnum cType = ContractTypeEnum.Buy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getCTypeText() {
        return cType == null ? null : cType.getDesc();
    }

    /**
     * 签订时间
     */
    private LocalDateTime signDate;

    /**
     * 金额
     */
    private BigDecimal value;

    /**
     * 省份
     */
    private String regionProvince;

    /**
     * 市
     */
    private String regionCity;

    /**
     * 区
     */
    private String regionDistrict;

    /**
     * 省市区聚合
     */
    private String regionFull;

    /**
     * 详细地址
     */
    private String regionAddress;

    /**
     * 审批状态
     */
    private Integer flag;

    /**
     * 状态
     */
    private Boolean status = true;

    /**
     * 备注
     */
    private String remark;

    /**
     * 明细列表
     */
    @TableField(exist = false)
    private List<ContractItem> ItemList = new ArrayList<>();

}

