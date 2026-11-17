package ${package.Entity};

<#list table.importPackages as pkg>
    import ${pkg};
</#list>
import com.baomidou.mybatisplus.annotation.TableName;
import com.xxx.myspringboot.entity.base.BaseEntityStandard;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.fesod.sheet.annotation.ExcelIgnore;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.format.DateTimeFormat;
import org.apache.fesod.sheet.annotation.format.NumberFormat;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;

/**
* ${table.comment!}
*/
@Getter
@Setter
@ToString
@ColumnWidth(12)
@TableName(autoResultMap = true)
public class ${entity} extends BaseEntityStandard {

    private static final long serialVersionUID = 1L;

<#-- 定义需要跳过的字段名（驼峰），与基类字段保持一致 -->
<#assign baseFields = ["id", "createdTime", "updatedTime", "isDeleted", "createdUserId", "createdUserName","updatedUserId", "updatedUserName","status","remark"]>

<#list table.fields as field>
    <#if !baseFields?seq_contains(field.propertyName)>
    /**
    * ${field.comment!"无注释"}
    */
    <#if field.propertyType == "LocalDateTime">
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    </#if>
    <#if field.propertyType == "BigDecimal">
    @NumberFormat("#.##")
    </#if>
    <#if field.propertyType == "Boolean">
    @ExcelIgnore
    </#if>
    @ExcelProperty("${field.comment!"无注释"}")
    private ${field.propertyType} ${field.propertyName};

    <#-- 添加额外字段-->
    <#if field.propertyType == "Boolean">
     /**
     * ${field.comment!"无注释"}（用于导出）
     */
     @ExcelProperty("${field.comment!"无注释"}")
     @TableField(exist = false)
     private String ${field.propertyName}Text;

     @JsonProperty(access = JsonProperty.Access.READ_ONLY)
     public String get${field.propertyName}Text() {
        return ${field.propertyName} ? "启用" : "禁用";
     }
    </#if>

    </#if>
</#list>

//    /**
//     * 附件列表
//     */
//    @ExcelIgnore
//    @TableField(typeHandler = JacksonTypeHandler.class)
//    public List<CommonAttach> attachList = new ArrayList<>();
//
//    /**
//     * 明细列表
//     */
//    @ExcelIgnore
//    @TableField(exist = false)
//    private List<ContractItem> ItemList = new ArrayList<>();
}