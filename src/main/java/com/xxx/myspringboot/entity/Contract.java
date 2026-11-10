package com.xxx.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xxx.myspringboot.entity.base.BaseEntityStandard;
import com.xxx.myspringboot.entity.enums.ContractFlagEnum;
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
    private ContractFlagEnum flag = ContractFlagEnum.Draft;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getFlagText() {
        return flag == null ? null : flag.getDesc();
    }

    /**
     * 明细列表
     */
    @TableField(exist = false)
    private List<ContractItem> ItemList = new ArrayList<>();

    /*

    create table contract
(
  id                bigint               auto_increment comment '主键自增id',
  code              varchar(30)                    null comment '合同编号',
  name              varchar(30)                    null comment '名称',
  c_type            int              default 1 not null comment '合同类型',
  sign_date         datetime                       null comment '签订时间',
  value decimal(19, 4) null comment '金额',
  region_province   varchar(50)                    null comment '省份',
  region_city       varchar(50)                    null comment '城市',
  region_district   varchar(50)                    null comment '地区',
  region_full       varchar(200)                   null comment '省市区',
  region_address    varchar(200)                   null comment '详细地址',
  flag              int              default 0 not null comment '审批状态',
  `status`          tinyint(1)       default 1 not null comment '状态',
  remark            varchar(200)                   null comment '备注',
  created_user_id   bigint                         null comment '创建者用户Id',
  created_user_name varchar(30)                    null comment '创建者用户名',
  created_time      datetime     default now() not null comment '创建时间',
  updated_user_id   bigint                         null comment '更新者用户Id',
  updated_user_name varchar(30)                    null comment '更新者用户名',
  updated_time      datetime     default now() not null comment '更新时间',
  is_deleted        boolean      default false not null comment '软删除',
  primary key (id)
) comment '合同';

     */

}

