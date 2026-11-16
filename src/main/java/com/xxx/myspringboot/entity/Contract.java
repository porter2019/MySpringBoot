package com.xxx.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xxx.myspringboot.dto.CommonAttach;
import com.xxx.myspringboot.entity.base.BaseEntityStandard;
import com.xxx.myspringboot.entity.enums.ContractFlagEnum;
import com.xxx.myspringboot.entity.enums.ContractTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.fesod.sheet.annotation.ExcelIgnore;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.format.DateTimeFormat;
import org.apache.fesod.sheet.annotation.format.NumberFormat;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;

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
@ColumnWidth(12)
@TableName(autoResultMap = true)
public class Contract extends BaseEntityStandard {

    private static final long serialVersionUID = 1L;

    /**
     * 合同编号
     */
    @ExcelProperty("合同编号")
    @ColumnWidth(15)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String code;

    /**
     * 名称
     */
    @ExcelProperty("合同名称")
    @ColumnWidth(20)
    private String name;

    /**
     * 合同类型
     */
    @ExcelIgnore
    @TableField("c_type")
    private ContractTypeEnum cType = ContractTypeEnum.Buy;

    /**
     * 合同类型（用于导出）
     */
    @ExcelProperty("合同类型")
    @TableField(exist = false)
    private String cTypeText;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getCTypeText() {
        return cType == null ? null : cType.getDesc();
    }

    /**
     * 签订时间
     */
    @ExcelProperty("签订时间")
    @DateTimeFormat("yyyy-MM-dd")
    private LocalDateTime signDate;

    /**
     * 金额
     */
    @ExcelProperty("金额")
    @NumberFormat("#.##")
    private BigDecimal value;

    /**
     * 省份
     */
    @ExcelProperty("省份")
    private String regionProvince;

    /**
     * 市
     */
    @ExcelProperty("城市")
    private String regionCity;

    /**
     * 区
     */
    @ExcelProperty("地区")
    private String regionDistrict;

    /**
     * 省市区聚合
     */
    @ExcelIgnore
    private String regionFull;

    /**
     * 详细地址
     */
    @ExcelProperty("地址")
    @ColumnWidth(20)
    private String regionAddress;

    /**
     * 审批状态
     */
    @ExcelIgnore
    private ContractFlagEnum flag = ContractFlagEnum.Draft;

    /**
     * 审批状态（用于导出）
     */
    @TableField(exist = false)
    @ExcelProperty("审批状态")
    private String flagText;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getFlagText() {
        return flag == null ? null : flag.getDesc();
    }

    /**
     * 附件列表
     */
    @ExcelIgnore
    @TableField(typeHandler = JacksonTypeHandler.class)
    public List<CommonAttach> attachList = new ArrayList<>();

    /**
     * 明细列表
     */
    @ExcelIgnore
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
  `attachList`      JSON             default null       comment '附件列表',
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

